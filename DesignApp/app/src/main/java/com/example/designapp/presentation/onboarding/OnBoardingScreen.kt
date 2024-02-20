package com.example.designapp.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.designapp.R
import com.example.designapp.presentation.Dimens.MediumPadding2
import com.example.designapp.presentation.Dimens.pageIndicatorWidth
import com.example.designapp.presentation.common.NewsButton
import com.example.designapp.presentation.common.NewsTextButton
import com.example.designapp.presentation.onboarding.components.OnBoardingPage
import com.example.designapp.presentation.onboarding.components.PageIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(navController: NavController) {
   Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 0)
        val buttonState = remember {
            derivedStateOf {
                when(pagerState.currentPage){
                    0-> listOf("","Next")
                    1-> listOf("Back","Next")
                    2-> listOf("Back","GetStarted")
                    else-> listOf("","")
                }
            }
        }
       
       HorizontalPager(pageCount = pages.size, state = pagerState) {index->
           OnBoardingPage(page = pages[index])
           
       }

       Spacer(modifier = Modifier.weight(1f))
       Row(modifier = Modifier
           .fillMaxWidth()
           //.fillMaxHeight(0.4f)
           .offset(y = -25.dp)
           .align(Alignment.CenterHorizontally)
           .padding(horizontal = MediumPadding2)
           .navigationBarsPadding(),

           horizontalArrangement = Arrangement.Center,
           verticalAlignment = Alignment.CenterVertically,


           ) {
           PageIndicator(modifier = Modifier.width(pageIndicatorWidth) ,pageSize = pages.size, selectedPage = pagerState.currentPage)
       }
//Code for button
       Row(modifier = Modifier
           .fillMaxWidth()
           .offset(y = -20.dp)
           .align(Alignment.CenterHorizontally)
           .padding(horizontal = MediumPadding2)
           .navigationBarsPadding(),
           horizontalArrangement = Arrangement.End,
           verticalAlignment = Alignment.CenterVertically) {
           val scope = rememberCoroutineScope()

            if(buttonState.value[0].isNotEmpty()) {
                NewsTextButton(text = buttonState.value[0],
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.currentPage-1)
                        }
                    })
            }
           
           NewsButton(text = buttonState.value[1],
               onClick = {
                   scope.launch {
                      if(pagerState.currentPage==2){
                          navController.navigate("login")
                          //TODO:Navigate to Home Screen
                      }else{
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage+1
                        )
                      }
                   }
               })
       }
//       Spacer(modifier = Modifier.weight(0.5f))
   }
}