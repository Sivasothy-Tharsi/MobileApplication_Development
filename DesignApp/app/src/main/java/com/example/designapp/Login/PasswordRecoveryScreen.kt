package com.example.designapp.Login

// PasswordRecoveryScreen.kt

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.designapp.R
import com.example.designapp.presentation.Dimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryScreen(
    navController: NavController,
    onBack: () -> Unit,
    viewModel: PasswordRecoveryViewModel
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<PasswordRecoveryResult?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // App Bar
        TopAppBar(
            title = { Text("Password Recovery") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Password Button
        Button(
            onClick = {
                if (!isLoading) {
                    scope.launch {
                        isLoading = true
                        result = viewModel.performPasswordRecovery(email)
                        isLoading = false

                        // Check if the password recovery was successful
                        if (result is PasswordRecoveryResult.Success) {
                            // Navigate to the login screen after successful password recovery
                            navController.navigate("login")
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(4.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.text_medium),
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Reset Password", fontWeight = FontWeight.Bold)
            }
        }

        // Display success or error message
        result?.let { passwordRecoveryResult ->
            when (passwordRecoveryResult) {
                is PasswordRecoveryResult.Success -> {
                    Toast.makeText(context, "Password reset email sent successfully", Toast.LENGTH_SHORT).show()
                }
                is PasswordRecoveryResult.Error -> {
                    Toast.makeText(context, passwordRecoveryResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

