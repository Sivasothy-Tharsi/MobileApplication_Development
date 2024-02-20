package com.example.designapp.presentation.onboarding

import androidx.annotation.DrawableRes
import com.example.designapp.R


data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,

)

val pages = listOf(
    Page(
        title = "Welcome to ARtistryDesign! ",
        description = "Where your creativity meets augmented reality. Let's turn your imagination into virtual decoration!",
        image = R.drawable.logo3

    ),
    Page(
        title = "AR Rangoli Designs",
        description = "ArtistryDesign is not just an app; " +
                "it's your passport to a world of creative possibilities.\n " +
                "Our app redefines the way you decorate and envision your " +
                "living spaces by seamlessly blending the physical and the virtual",
        image = R.drawable.logo3,
    ),
    Page(
        title = "Virtual Floor Patterns",
        description = "ArtistryDesign is designed to empower you with the ability to visualize and place virtual objects " +
                "in augmented reality, effortlessly transforming any space into a personalized masterpiece.",
        image = R.drawable.logo3,


    )
)