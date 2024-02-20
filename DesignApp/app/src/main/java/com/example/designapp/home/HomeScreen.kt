package com.example.designapp.home

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.designapp.R
import com.example.designapp.ui.theme.DesignAppTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val context = LocalContext.current
    var exitPressedOnce by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (exitPressedOnce) {
                    // If the user pressed the back button twice, exit the app
                    dispatcher?.onBackPressed()
                } else {
                    // If it's the first press, show a message or perform any desired action
                    // Here, I'm showing a Toast message
                    Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()

                    // Set the flag to true, indicating the first press
                    exitPressedOnce = true

                    // Reset the flag after a delay (e.g., 2 seconds)
                    android.os.Handler().postDelayed({ exitPressedOnce = false }, 2000)
                }
            }
        }

        dispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)

            .padding(16.dp)
        ,
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)


        ) {



            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "info",
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .align(Alignment.TopEnd)
                    .clickable(onClick = {navController.navigate("helpScreen")})
            )
            Spacer(modifier = Modifier.height(16.dp)
            )
            Column(modifier = Modifier
                .padding(0.dp,10.dp,0.dp,0.dp)
                .align(alignment = Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally


            ) {

                Spacer(modifier = Modifier.height(16.dp)
                )
                Text("ARtistry Design", fontWeight = FontWeight.Bold, fontSize = 34.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)

                    )
                Image(modifier= Modifier
                    .fillMaxWidth()
//                    .alpha(0.2f)

                    .height(300.dp),
                    painter = painterResource(id = R.drawable.ar_logo),
                    contentDescription = "logo"
                    ,

                    )
            }



        }

//        Spacer(modifier = Modifier.height(16.dp))
//        Text("You have successfully logged in!", fontSize = 16.sp,textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        GradientButton("Floor Design",navController)
        Spacer(modifier = Modifier.height(32.dp))
        GradientButton("Wall Design",navController)
        Spacer(modifier = Modifier.height(212.dp))

        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Logout",
            modifier = Modifier
                .size(50.dp)
                .padding(0.dp)
                .align(alignment = Alignment.End)
                .clickable {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                    navController.enableOnBackPressed(false)
                }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
//
        }



    }
}

@Composable
fun GradientButton(text: String, navController: NavHostController) {
    val startColor = Color(0xFFDAA520) // Dark Goldenrod
    val endColor = Color(0xFFFFD700)   // Gold

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(startColor, endColor)
    )

    Button(
        onClick = {
            if(text == "Floor Design") {
                navController.navigate("floor")
            }
            if(text == "Wall Design") {
                navController.navigate("wall")
            }
        },

        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.text_medium),
            contentColor = Color.White
        ),


        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(20.dp, 0.dp, 20.dp, 0.dp)
        ,

        shape = RoundedCornerShape(size= 16.dp),
        elevation = ButtonDefaults.buttonElevation()

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}