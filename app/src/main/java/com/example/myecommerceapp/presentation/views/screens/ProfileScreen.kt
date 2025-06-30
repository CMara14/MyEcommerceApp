package com.example.myecommerceapp.presentation.views.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.PinkPastel
import com.example.myecommerceapp.ui.theme.White
import com.example.myecommerceapp.presentation.viewmodel.ProfileViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.myecommerceapp.ui.theme.ActiveButton
import com.example.myecommerceapp.ui.theme.DisabledButton
import com.example.myecommerceapp.ui.theme.DisabledTextButton
import android.net.Uri
import androidx.compose.material.icons.filled.CameraAlt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val confirmNewPassword by viewModel.confirmNewPassword.collectAsState()
    val nationality by viewModel.nationality.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSaveButtonEnabled by viewModel.isSaveButtonEnabled.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val lastNameError by viewModel.lastNameError.collectAsState()
    val newPasswordError by viewModel.newPasswordError.collectAsState()
    val confirmNewPasswordError by viewModel.confirmNewPasswordError.collectAsState()
    val nationalityError by viewModel.nationalityError.collectAsState()

    val profileImageUriString by viewModel.profileImageUri.collectAsState()
    val selectedImageUri: Uri? = remember(profileImageUriString) {
        if (profileImageUriString != null) Uri.parse(profileImageUriString) else null
    }

    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onProfileImageSelected(uri?.toString())
    }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
            viewModel.resetSaveSuccess()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Log Out", tint = White)
                    }
                }
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = PinkPastel)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        shape = RoundedCornerShape(percent = 50),
                        colors = CardDefaults.cardColors(containerColor = InputFieldColor)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Profile Picture",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(percent = 50))
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Upload Profile Picture",
                                    tint = LightGrayText,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.onNameChanged(it) },
                        label = { Text("Name", color = LightGrayText) },
                        isError = nameError != null,
                        supportingText = { if (nameError != null) Text(nameError!!) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldColor,
                            unfocusedContainerColor = InputFieldColor,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = PinkPastel,
                            unfocusedBorderColor = InputFieldColor,
                            focusedLabelColor = PinkPastel,
                            unfocusedLabelColor = LightGrayText,
                            cursorColor = PinkPastel,
                            errorCursorColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    viewModel.onNameChanged(name)
                                }
                            }
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { viewModel.onLastNameChanged(it) },
                        label = { Text("Last Name", color = LightGrayText) },
                        isError = lastNameError != null,
                        supportingText = { if (lastNameError != null) Text(lastNameError!!) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldColor,
                            unfocusedContainerColor = InputFieldColor,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = PinkPastel,
                            unfocusedBorderColor = InputFieldColor,
                            focusedLabelColor = PinkPastel,
                            unfocusedLabelColor = LightGrayText,
                            cursorColor = PinkPastel,
                            errorCursorColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    viewModel.onLastNameChanged(lastName)
                                }
                            }
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {  },
                        label = { Text("Email (not editable)", color = LightGrayText) },
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldColor,
                            unfocusedContainerColor = InputFieldColor,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = PinkPastel,
                            unfocusedBorderColor = InputFieldColor,
                            focusedLabelColor = PinkPastel,
                            unfocusedLabelColor = LightGrayText,
                            cursorColor = PinkPastel,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { viewModel.onNewPasswordChanged(it) },
                        label = { Text("New Password", color = LightGrayText) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (passwordVisible) "Hide password" else "Show password"
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description, tint = LightGrayText)
                            }
                        },
                        isError = newPasswordError != null,
                        supportingText = { if (newPasswordError != null) Text(newPasswordError!!) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldColor,
                            unfocusedContainerColor = InputFieldColor,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = PinkPastel,
                            unfocusedBorderColor = InputFieldColor,
                            focusedLabelColor = PinkPastel,
                            unfocusedLabelColor = LightGrayText,
                            cursorColor = PinkPastel,
                            errorCursorColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    viewModel.onNewPasswordChanged(newPassword)
                                }
                            }
                    )

                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = { viewModel.onConfirmNewPasswordChanged(it) },
                        label = { Text("Confirm New Password", color = LightGrayText) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = confirmNewPasswordError != null,
                        supportingText = { if (confirmNewPasswordError != null) Text(confirmNewPasswordError!!) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldColor,
                            unfocusedContainerColor = InputFieldColor,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = PinkPastel,
                            unfocusedBorderColor = InputFieldColor,
                            focusedLabelColor = PinkPastel,
                            unfocusedLabelColor = LightGrayText,
                            cursorColor = PinkPastel,
                            errorCursorColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    viewModel.onConfirmNewPasswordChanged(confirmNewPassword)
                                }
                            }
                    )

                    OutlinedTextField(
                        value = nationality,
                        onValueChange = { viewModel.onNationalityChanged(it) },
                        label = { Text("Nationality", color = LightGrayText) },
                        isError = nationalityError != null,
                        supportingText = { if (nationalityError != null) Text(nationalityError!!) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldColor,
                            unfocusedContainerColor = InputFieldColor,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = PinkPastel,
                            unfocusedBorderColor = InputFieldColor,
                            focusedLabelColor = PinkPastel,
                            unfocusedLabelColor = LightGrayText,
                            cursorColor = PinkPastel,
                            errorCursorColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    viewModel.onNationalityChanged(nationality)
                                }
                            }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.saveUserProfile() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ActiveButton,
                            contentColor = White,
                            disabledContainerColor = DisabledButton,
                            disabledContentColor = DisabledTextButton
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = isSaveButtonEnabled
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Save Changes")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

