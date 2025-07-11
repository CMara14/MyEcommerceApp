package com.example.myecommerceapp.ui.views.screens.profile

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.material.icons.filled.CameraAlt
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
import coil.compose.AsyncImage
import com.example.myecommerceapp.ui.theme.ActiveButton
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.DisabledButton
import com.example.myecommerceapp.ui.theme.DisabledTextButton
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.PinkPastel
import com.example.myecommerceapp.ui.theme.White
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

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
    val showProfileSummary by viewModel.showProfileSummary.collectAsState()

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

    var showPermissionRationaleDialog by remember { mutableStateOf(false) }
    var showImageSourceSelectionDialog by remember { mutableStateOf(false) }
    var permissionToRequest: String? by remember { mutableStateOf(null) }

    var tempCameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.uploadImageAndSaveProfile(uri)
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            tempCameraImageUri?.let { uri ->
                viewModel.uploadImageAndSaveProfile(uri)
                tempCameraImageUri = null
            }
        } else {
            viewModel.onProfileImageSelected(null)
            Toast.makeText(context, "No picture", Toast.LENGTH_SHORT).show()
            tempCameraImageUri = null
        }
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = viewModel.createImageUri(context)
            if (uri != null) {
                tempCameraImageUri = uri
                takePictureLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val requestGalleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        }
    }

    fun onProfileImageClick() {
        showImageSourceSelectionDialog = true
    }

    fun handleGallerySelection() {
        val permission =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickImageLauncher.launch("image/*")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                permission
            ) -> {
                permissionToRequest = permission
                showPermissionRationaleDialog = true
            }

            else -> {
                requestGalleryPermissionLauncher.launch(permission)
            }
        }
    }

    fun handleCameraSelection() {
        val permission = Manifest.permission.CAMERA
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                val uri = viewModel.createImageUri(context)
                if (uri != null) {
                    tempCameraImageUri = uri
                    takePictureLauncher.launch(uri)
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                permission
            ) -> {
                permissionToRequest = permission
                showPermissionRationaleDialog = true
            }

            else -> {
                requestCameraPermissionLauncher.launch(permission)
            }
        }
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

    if (showPermissionRationaleDialog) {
        AlertDialog(
            onDismissRequest = {
                showPermissionRationaleDialog = false
                permissionToRequest = null
            },
            title = { Text("Permission Required", color = White) },
            text = {
                Text(
                    "To be able to ${if (permissionToRequest == Manifest.permission.CAMERA) "take photos with the camera" else "select images from your gallery"}, we need your permission.",
                    color = LightGrayText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionRationaleDialog = false
                        permissionToRequest?.let {
                            if (it == Manifest.permission.CAMERA) {
                                requestCameraPermissionLauncher.launch(it)
                            } else {
                                requestGalleryPermissionLauncher.launch(it)
                            }
                        }
                        permissionToRequest = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPastel)
                ) {
                    Text("Accept", color = White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionRationaleDialog = false
                        permissionToRequest = null
                    }
                ) {
                    Text("Cancel", color = LightGrayText)
                }
            },
            containerColor = DarkBackground,
            titleContentColor = White,
            textContentColor = LightGrayText
        )
    }

    if (showImageSourceSelectionDialog) {
        AlertDialog(
            onDismissRequest = {
                showImageSourceSelectionDialog = false
            },
            title = { Text("Select Image Source", color = White) },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            showImageSourceSelectionDialog = false
                            handleGallerySelection()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPastel)
                    ) {
                        Text("Gallery", color = White)
                    }
                    Button(
                        onClick = {
                            showImageSourceSelectionDialog = false
                            handleCameraSelection()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPastel)
                    ) {
                        Text("Camera", color = White)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceSelectionDialog = false
                }) {
                    Text("Cancel", color = LightGrayText)
                }
            },
            containerColor = DarkBackground,
            titleContentColor = White,
            textContentColor = LightGrayText
        )
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
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Log Out",
                            tint = White
                        )
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
            } else if (showProfileSummary) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = InputFieldColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    ProfileSummaryCardContent(
                        name = name,
                        lastName = lastName,
                        email = email,
                        nationality = nationality,
                        profileImageUri = selectedImageUri,
                        onContinueEditing = { viewModel.hideProfileSummary() },
                        onUpdate = { viewModel.saveUserProfile() },
                        isLoading = isLoading
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { onProfileImageClick() },
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
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(percent = 50))
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
                        onValueChange = { },
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
                   Spacer(modifier = Modifier.height(15.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { viewModel.onNewPasswordChanged(it) },
                        label = { Text("New Password", color = LightGrayText) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description =
                                if (passwordVisible) "Hide password" else "Show password"
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = description,
                                    tint = LightGrayText
                                )
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
                        supportingText = {
                            if (confirmNewPasswordError != null) Text(
                                confirmNewPasswordError!!
                            )
                        },
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
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.showProfileSummary() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
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
                            CircularProgressIndicator(
                                color = White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Save Changes", fontSize = 20.sp)
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun shouldShowRequestPermissionRationale(
    context: android.content.Context,
    permission: String
): Boolean {
    val activity = context as? Activity
    return activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, permission) }
        ?: false
}

@Composable
fun ProfileSummaryCardContent(
    name: String,
    lastName: String,
    email: String,
    nationality: String,
    profileImageUri: Uri?,
    onContinueEditing: () -> Unit,
    onUpdate: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .size(150.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = CardDefaults.cardColors(containerColor = InputFieldColor)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(percent = 50))
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "No Profile Picture",
                        tint = LightGrayText,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }

        Text(
            text = "$name $lastName",
            style = MaterialTheme.typography.headlineMedium.copy(color = White, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )

        ProfileDetailRow(label = "First Name", value = name)
        ProfileDetailRow(label = "Last Name", value = lastName)
        ProfileDetailRow(label = "Email", value = email)
        ProfileDetailRow(label = "Nationality", value = nationality)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onContinueEditing,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = PinkPastel
                ),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = SolidColor(PinkPastel),
                    width = 1.dp
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Return", fontSize = 20.sp, textAlign = TextAlign.Center)
            }

            Button(
                onClick = onUpdate,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ActiveButton,
                    contentColor = White,
                    disabledContainerColor = DisabledButton,
                    disabledContentColor = DisabledTextButton
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Update", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(color = LightGrayText),
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(color = White, fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.6f)
        )
    }
    HorizontalDivider(thickness = 1.dp, color = LightGrayText.copy(alpha = 0.2f))
}