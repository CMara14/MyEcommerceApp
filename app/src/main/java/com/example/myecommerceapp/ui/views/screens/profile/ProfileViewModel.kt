package com.example.myecommerceapp.ui.views.screens.profile
import com.example.myecommerceapp.BuildConfig

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.domain.model.User
import com.example.myecommerceapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val myApplication: Application,
    private val authRepository: AuthRepository
) : AndroidViewModel(myApplication) {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmNewPassword = MutableStateFlow("")
    val confirmNewPassword: StateFlow<String> = _confirmNewPassword.asStateFlow()

    private val _nationality = MutableStateFlow("")
    val nationality: StateFlow<String> = _nationality.asStateFlow()

    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri: StateFlow<String?> = _profileImageUri.asStateFlow()

    private val _originalProfileImageUri = MutableStateFlow<String?>(null)

    private val _isImageUploading = MutableStateFlow(false)
    val isImageUploading: StateFlow<Boolean> = _isImageUploading.asStateFlow()

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to BuildConfig.CLOUD_NAME,
            "api_key" to BuildConfig.CLOUD_API_KEY,
            "api_secret" to BuildConfig.CLOUD_API_SECRET
        )
    )

    fun uploadImageAndSaveProfile(imageUri: Uri?) {
        if (imageUri == null) {
            _errorMessage.value = "No picture selected"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isImageUploading.value = true
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val inputStream = myApplication.contentResolver.openInputStream(imageUri)
                val uploadResult =
                    cloudinary.uploader()
                        .upload(inputStream, ObjectUtils.asMap("upload_preset", "profile_image"))

                val imageUrl = uploadResult["secure_url"] as String

                _profileImageUri.value = imageUrl
                saveUserProfile()

            } catch (e: Exception) {
                _errorMessage.value = "Error uploading image"
                _profileImageUri.value =
                    _originalProfileImageUri.value
            } finally {
                _isImageUploading.value = false
                _isLoading.value = false
            }
        }
    }


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _lastNameError = MutableStateFlow<String?>(null)
    val lastNameError: StateFlow<String?> = _lastNameError.asStateFlow()

    private val _newPasswordError = MutableStateFlow<String?>(null)
    val newPasswordError: StateFlow<String?> = _newPasswordError.asStateFlow()

    private val _confirmNewPasswordError = MutableStateFlow<String?>(null)
    val confirmNewPasswordError: StateFlow<String?> = _confirmNewPasswordError.asStateFlow()

    private val _nationalityError = MutableStateFlow<String?>(null)
    val nationalityError: StateFlow<String?> = _nationalityError.asStateFlow()

    private val _showProfileSummary = MutableStateFlow(false)
    val showProfileSummary: StateFlow<Boolean> = _showProfileSummary.asStateFlow()

    val isSaveButtonEnabled: StateFlow<Boolean> = combine(
        name,
        lastName,
        newPassword,
        confirmNewPassword,
        nationality,
        profileImageUri,
    ) { args ->
        val currentName = args[0] as String
        val currentLastName = args[1] as String
        val currentNewPassword = args[2] as String
        val currentConfirmNewPassword = args[3] as String
        val currentNationality = args[4] as String
        val currentProfileImageUri = args[5] as String?

        val isAnyFieldChanged =
            currentName != (_name.value) ||
                    currentLastName != (_lastName.value) ||
                    currentNationality != (_nationality.value) ||
                    currentNewPassword.isNotBlank() ||
                    currentConfirmNewPassword.isNotBlank() ||
                    currentProfileImageUri != (_originalProfileImageUri.value)

        val areRequiredFieldsValid =
            currentName.isNotBlank() &&
                    currentLastName.isNotBlank() &&
                    currentNationality.isNotBlank()

        val isPasswordValid =
            currentNewPassword.isBlank() ||
                    (currentNewPassword.isNotBlank() && currentConfirmNewPassword.isNotBlank() && currentNewPassword == currentConfirmNewPassword)

        val hasErrors = nameError.value != null || lastNameError.value != null ||
                newPasswordError.value != null || confirmNewPasswordError.value != null ||
                nationalityError.value != null

        areRequiredFieldsValid && isPasswordValid && isAnyFieldChanged && !hasErrors
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val user = authRepository.getCurrentUserProfile()
                if (user != null) {
                    _name.value = user.name
                    _lastName.value = user.lastName
                    _email.value = user.email
                    _password.value = user.password
                    _nationality.value = user.nationality
                    _profileImageUri.value = user.profileImage
                    _originalProfileImageUri.value = user.profileImage
                } else {
                    _errorMessage.value = "The user profile could not be loaded."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createImageUri(context: Context): Uri? {
        val cacheDir = File(context.cacheDir, "images")
        cacheDir.mkdirs()

        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(cacheDir, "JPEG_${timeStamp}_.jpg")

        return try {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
        } catch (e: IllegalArgumentException) {
            _errorMessage.value = "Error creating URI for image: ${e.message}"
            null
        }
    }

    fun onProfileImageSelected(uri: String?) {
        _profileImageUri.value = uri
    }

    fun onNameChanged(newName: String) {
        _name.value = newName
        _nameError.value = null
        _errorMessage.value = null
    }

    fun onLastNameChanged(newLastName: String) {
        _lastName.value = newLastName
        _lastNameError.value = null
        _errorMessage.value = null
    }

    fun onNewPasswordChanged(newPass: String) {
        _newPassword.value = newPass
        _errorMessage.value = null
        _newPasswordError.value = null
        _confirmNewPasswordError.value = null
    }

    fun onConfirmNewPasswordChanged(confirmPass: String) {
        _confirmNewPassword.value = confirmPass
        _errorMessage.value = null
        _confirmNewPasswordError.value = null
    }

    fun onNationalityChanged(newNationality: String) {
        _nationality.value = newNationality
        _nationalityError.value = null
        _errorMessage.value = null
    }

    fun showProfileSummary() {
        onNameChanged(_name.value)
        onLastNameChanged(_lastName.value)
        onNewPasswordChanged(_newPassword.value)
        onConfirmNewPasswordChanged(_confirmNewPassword.value)
        onNationalityChanged(_nationality.value)

        if (nameError.value != null || lastNameError.value != null ||
            newPasswordError.value != null || confirmNewPasswordError.value != null ||
            nationalityError.value != null
        ) {
            _errorMessage.value = "Please correct the errors in the form."
            return
        }

        if (!isSaveButtonEnabled.value) {
            _errorMessage.value = "No valid changes to save."
            return
        }
        _showProfileSummary.value = true
    }

    fun hideProfileSummary() {
        _showProfileSummary.value = false
    }

    fun saveUserProfile() {
        _showProfileSummary.value = false
        _errorMessage.value = null
        _nameError.value = null
        _lastNameError.value = null
        _newPasswordError.value = null
        _confirmNewPasswordError.value = null
        _nationalityError.value = null

        val currentName = _name.value.trim()
        val currentLastName = _lastName.value.trim()
        val currentNewPassword = _newPassword.value.trim()
        val currentConfirmNewPassword = _confirmNewPassword.value.trim()
        val currentNationality = _nationality.value.trim()
        val currentProfileImageUri = _profileImageUri.value

        var formValid = true

        if (currentName.isBlank()) {
            _nameError.value = "The name cannot be empty"
            formValid = false
        }

        if (currentLastName.isBlank()) {
            _lastNameError.value = "The last name cannot be empty"
            formValid = false
        }

        if (currentNewPassword.isNotEmpty() && currentNewPassword.length < 8) {
            _newPasswordError.value = "The password must be at least 8 characters long."
            formValid = false
        }

        if (currentNewPassword.isNotEmpty() && currentConfirmNewPassword.isNotEmpty() && currentNewPassword != currentConfirmNewPassword) {
            _confirmNewPasswordError.value = "Passwords do not match"
            formValid = false
        }

        if (currentNationality.isBlank()) {
            _lastNameError.value = "Nationality cannot be empty"
            formValid = false
        }

        if (!formValid) {
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val updatedPassword =
                    currentNewPassword.ifBlank { password.value }

                val updatedUser = User(
                    email = email.value,
                    name = currentName,
                    lastName = currentLastName,
                    password = updatedPassword,
                    nationality = currentNationality,
                    profileImage = currentProfileImageUri
                )

                val success = authRepository.updateUserProfile(updatedUser)
                if (success) {
                    _saveSuccess.value = true
                    _newPassword.value = ""
                    _confirmNewPassword.value = ""
                    _originalProfileImageUri.value = currentProfileImageUri
                } else {
                    _errorMessage.value = "The profile could not be updated."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error saving profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
