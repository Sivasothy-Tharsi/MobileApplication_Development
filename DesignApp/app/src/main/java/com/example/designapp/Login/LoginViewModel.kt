

package com.example.designapp.Login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designapp.data.AuthRepository
import com.example.designapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
):ViewModel() {

    val _loginState = Channel<LoginState>()
    val loginState = _loginState.receiveAsFlow()
//    private var isAdminLoggedIn = false



    fun loginUser(email:String, password:String) = viewModelScope.launch {
        Log.d("auth-log", email);
        Log.d("auth-log", password)

        repository.loginUser(email.toString().trim(), password).collect { result ->
            Log.d("auth-log", result.message.toString());

            when (result) {
                is Resource.Success -> {
                    if (email.trim() == "mmadhunicka7@gmail.com") {
                        _loginState.send(LoginState(isSuccess = "Admin Sign-In Successful"))
                    } else {
                        _loginState.send(LoginState(isSuccess = "SignIn Successful"))
                    }
                }


                is Resource.Loading -> {
                    _loginState.send(LoginState(isLoading = true))

                }

                is Resource.Error -> {
                    _loginState.send(LoginState(isError = result.message))

                }
            }
        }
    }
}



