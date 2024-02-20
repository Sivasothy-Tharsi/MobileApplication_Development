package com.example.designapp.help

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.designapp.R
import com.example.designapp.ui.theme.DesignAppTheme



@Composable
fun HelpScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(36.dp)

            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "How to Use the AR Application",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = colorResource(id = R.color.text_medium),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        InstructionCard("1. Open the AR app and grant necessary permissions.")
        InstructionCard("2. Point your device's camera towards a flat surface.", )
        InstructionCard("3. Tap on the screen to place AR objects.")

        // Additional instructions...

        Row(
            modifier = Modifier
                .fillMaxWidth()

                .padding(top = 52.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = colorResource(id = R.color.text_medium),
                        shape = RoundedCornerShape(28.dp),
                        //elevation = 4.dp
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun InstructionCard(instruction: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            ,
        //elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)

        ) {
            Text(
                text = instruction,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}