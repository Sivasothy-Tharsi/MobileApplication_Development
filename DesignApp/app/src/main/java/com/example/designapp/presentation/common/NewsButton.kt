package com.example.designapp.presentation.common

import android.text.Layout.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.designapp.R
import java.lang.reflect.Modifier

@Composable
fun NewsButton(

    text: String,
    onClick:() -> Unit
) {
    Button(

        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.text_medium),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 16.dp),



    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )


    }
}

@Composable
fun NewsTextButton(
    text: String,
    onClick:() -> Unit,

) {
    TextButton(onClick = onClick,

        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        )) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(id = R.color.text_medium)
        )
        
    }
}