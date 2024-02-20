package com.example.designapp.categorey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designapp.Login.LoginViewModel
import com.example.designapp.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FloorDesignVM @Inject constructor(
    private val loginViewModel: LoginViewModel
): ViewModel() {

//    private val _isButtonVisible = MutableStateFlow(false)
//    private val _isAdminLoggedIn: StateFlow<Boolean> = loginViewModel.isAdminLoggedIn
//    val isAdminLoggedIn: Boolean get() = _isAdminLoggedIn.value
//    val isButtonVisible: StateFlow<Boolean> get() = _isButtonVisible
//
//    init {
//        observeAdminLoginStatus()
//    }
//
//    private fun observeAdminLoginStatus() {
//        viewModelScope.launch {
//            loginViewModel.isAdminLoggedIn.collect { isAdmin ->
//                // Update the visibility state of the button
//                _isButtonVisible.value = isAdmin
//
//                Log.d("fhf",_isAdminLoggedIn.value.toString())
//            }
//        }
//    }




}