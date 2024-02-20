package com.example.designapp.SignUp

import android.widget.Toast
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.designapp.R
import com.example.designapp.presentation.Dimens.MediumPadding2
import com.example.designapp.ui.theme.DesignAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignupScreen(navController: NavController,
                 viewModel: SignUpViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signUpState.collectAsState(initial = null)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .offset(y = 50.dp)


    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Sign Up", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
//        var email by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = email,
            onValueChange = {
                            email=it
            },
            label = { Text("Email",
                color = colorResource(id = R.color.text_medium))
                    },
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
//        var password by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = password,
            onValueChange = {
                            password=it
            },
            label = { Text("Password", color = colorResource(id = R.color.text_medium)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Handle next action
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
//        Spacer(modifier = Modifier.height(16.dp))
//        var conpasswordValue by remember { mutableStateOf("") }
//        OutlinedTextField(
//            value = conpasswordValue,
//            onValueChange = {
//                            conpasswordValue=it
//            },
//            label = { Text("Confirm Password") },
//            leadingIcon = {
//                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
//            },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = {
//                    // Handle signup action
//                    // LocalSoftwareKeyboardController.current?.hide();
//                }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                      scope.launch {
                          viewModel.registerUser(email, password)
                      }
                // Handle signup action
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.text_medium),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)

        ) {
            Text("Sign Up"
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MediumPadding2),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Already have an account? ")
            Text(
                text = "Log in",
                color = colorResource(id = R.color.text_medium),

                modifier = Modifier.clickable {
                    // Handle navigation to login screen
                    navController.navigate("login")
                }
            )
        }
    }
    LaunchedEffect(key1 = state.value?.isSuccess) {
        scope.launch {
            if (state.value?.isSuccess?.isNotEmpty() == true) {
                val success = state.value?.isSuccess
                Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
            }
        }
    }
    LaunchedEffect(key1 = state.value?.isError) {
        scope.launch {
            if (state.value?.isError?.isNotBlank() == true) {
                val error = state.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SignupScreenPreview() {
    DesignAppTheme {
        val navController = rememberNavController()
        SignupScreen(navController)
    }
}