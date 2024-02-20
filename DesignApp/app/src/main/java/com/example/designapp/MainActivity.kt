package com.example.designapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.designapp.presentation.onboarding.OnBoardingScreen
import com.example.designapp.Login.LoginScreen
import com.example.designapp.Login.PasswordRecoveryScreen
import com.example.designapp.Login.PasswordRecoveryViewModel
import com.example.designapp.SignUp.SignupScreen
import com.example.designapp.categorey.FloorDesign
import com.example.designapp.categorey.WallDesign
import com.example.designapp.database.RealTimeDatabase
import com.example.designapp.help.HelpScreen
import com.example.designapp.home.HomeScreen
import com.example.designapp.ui.theme.DesignAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
//        window.statusBarColor=ContextCompat.getColor(R.color.black)
        setContent {

            DesignAppTheme {

                window.statusBarColor=ContextCompat.getColor(this,R.color.black)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}


@Composable
fun MyApp() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val isLoggedIn = auth.currentUser!=null
    NavHost(navController, startDestination = if(isLoggedIn) "home" else "onboarding") {
        composable("onboarding") {
            OnBoardingScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }


        composable("signup") {
            SignupScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("floor") {
            FloorDesign(navController= navController)
        }
        composable("wall") {
            WallDesign(navController= navController)
        }
        composable("insertModel") {
            RealTimeDatabase()
        }
        composable("password_recovery") {
            PasswordRecoveryScreen(
                navController = navController,
                onBack = {
                    navController.popBackStack()
                },
                viewModel = PasswordRecoveryViewModel()
            )
        }
        composable("helpScreen") {
            HelpScreen(navController = navController)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    DesignAppTheme {
        MyApp()
    }
}