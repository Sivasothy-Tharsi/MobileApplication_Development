package com.example.designapp.presentation.onboarding.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designapp.R
import com.example.designapp.presentation.Dimens.MediumPadding1
import com.example.designapp.presentation.Dimens.MediumPadding2
import com.example.designapp.presentation.onboarding.Page
import com.example.designapp.presentation.onboarding.pages
import com.example.designapp.ui.theme.DesignAppTheme


@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    page: Page,
) {
    Column(
        modifier = Modifier,
    ) {
        Image(modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.5f)
            .scale(scaleX = 0.96f, scaleY = 0.95f),

            painter = painterResource(id = page.image),


            contentDescription = null,
            contentScale = ContentScale.Crop
        )


        Spacer(modifier = Modifier.height(MediumPadding1))
        Text(
            text = page.title,
            modifier = Modifier
//                .fillMaxHeight(fraction = 0.1f)
                .offset( y=-50.dp)
                .padding(horizontal = MediumPadding2)
                .align(Alignment.CenterHorizontally),

            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),

            color = colorResource(id = R.color.display_small)
        )
        Text(
            text = page.description,
            modifier = Modifier
                .fillMaxHeight(fraction = 0.7f)
                .offset(y=-20.dp)
                .padding(horizontal = MediumPadding2)
                .align(Alignment.CenterHorizontally),


            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 32.sp),
            color = colorResource(id = R.color.text_medium),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Preview( uiMode = UI_MODE_NIGHT_YES,showBackground = false)
@Composable
fun OnBoardingPagePreview() {
    DesignAppTheme {
        OnBoardingPage(
            page = pages[0]
        )
    }
}