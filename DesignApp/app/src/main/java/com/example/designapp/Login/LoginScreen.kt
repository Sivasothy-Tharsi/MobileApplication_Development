package com.example.designapp.Login

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import android.widget.Toast
import androidx.compose.foundation.Image
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.designapp.R
import com.example.designapp.presentation.Dimens
import com.example.designapp.presentation.onboarding.OnBoardingScreen
import com.example.designapp.ui.theme.DesignAppTheme
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavHostController,
                viewModel: LoginViewModel = hiltViewModel() ) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.loginState.collectAsState(initial = null)
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberPassword by rememberSaveable { mutableStateOf(false) }
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    // Retrieve saved email, password, and rememberPassword when the screen is first created
    LaunchedEffect(true) {
        email = sharedPreferences.getString("email", "") ?: ""
        password = sharedPreferences.getString("password", "") ?: ""
        rememberPassword = sharedPreferences.getBoolean("rememberPassword", false)
    }

    fun saveCredentials() {
        // Save the email, password, and rememberPassword
        sharedPreferences.edit()
            .putString("email", email)
            .putString("password", password)
            .putBoolean("rememberPassword", rememberPassword)
            .apply()
    }

    Box(
        modifier = Modifier


    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .offset(y = 50.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Text("Login", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
//        var emailValue by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,

                onValueChange = {
                    email = it
                },
                label = { Text("Email", color = colorResource(id = R.color.text_medium)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.text_medium), // Set the focused border color
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = colorResource(id = R.color.text_medium)// Set the unfocused border color
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

//        var passwordValue by remember { mutableStateOf("") }
            OutlinedTextField(

                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text("Password", color = colorResource(id = R.color.text_medium)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        PasswordVisibilityToggleIcon(isPasswordVisible, iconSize = 22.dp)
                    }
                },
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
//                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        saveCredentials()
                        scope.launch {
                            viewModel.loginUser(email, password)
                        }
                        softwareKeyboardController?.hide()
                    }
                ),
                modifier = Modifier.fillMaxWidth(),

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.text_medium), // Set the focused border color
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = colorResource(id = R.color.text_medium)
                    // Set the unfocused border color
                )


            )
            Spacer(modifier = Modifier.height(16.dp))

            // Remember Password checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberPassword,
                    onCheckedChange = { newRememberPassword ->
                        rememberPassword = newRememberPassword
                        if (!newRememberPassword) {
                            // Clear password if "Remember Password" is unchecked
                            password = ""
                            email = ""
                        }
                        saveCredentials()
                    },
                    modifier = Modifier
                        .padding(end = 8.dp),
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = colorResource(id = R.color.white),
                        checkedColor = colorResource(id = R.color.text_medium),
                        uncheckedColor = Color.Gray
                )
                )


                Text("Remember Me", color = colorResource(id = R.color.text_medium))
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Forgot Password link
            Text(
                text = "Forgot Password?",
                color = colorResource(id = R.color.text_medium),
                modifier = Modifier.clickable {
                    navController.navigate("password_recovery")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),


                onClick = {

                    scope.launch {
                        viewModel.loginUser(email, password)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.text_medium),
                    contentColor = Color.White
                ),


                ) {
                Text("Login", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Link to Signup Screen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.MediumPadding2),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ")
                Text(
                    text = "SignUp",
                    color = colorResource(id = R.color.text_medium),

                    modifier = Modifier.clickable {

                        navController.navigate("signup")
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))


            LaunchedEffect(key1 = state.value, key2 = "loginEffect") {
                scope.launch {
                    when {
                        state.value?.isSuccess?.isNotEmpty() == true -> {
                            val success = state.value?.isSuccess
                            Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()

                            // Navigate to home screen
                            navController.navigate("home") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        }

                        state.value?.isError?.isNotEmpty() == true -> {
                            val error = state.value?.isError
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                            // Handle other actions for login failure if needed
                        }
                    }
                }
            }

        }

    }
}


@Composable
fun PasswordVisibilityToggleIcon(isPasswordVisible: Boolean, iconSize: Dp = 24.dp) {
    val icon = if (isPasswordVisible) R.drawable.ic_eye_open else R.drawable.ic_eye_closed

    Icon(
        painter = painterResource(id = icon),
        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
        modifier = Modifier.size(iconSize)
    )
}



