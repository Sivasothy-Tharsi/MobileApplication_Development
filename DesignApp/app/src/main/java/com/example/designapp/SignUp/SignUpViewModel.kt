package com.example.designapp.SignUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designapp.Login.LoginState
import com.example.designapp.data.AuthRepository
import com.example.designapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun registerUser(email:String, password:String) = viewModelScope.launch {
        Log.d("auth-reg",email);
        Log.d("auth-reg",password);

        repository.registerUser(email.toString().trim(),password).collect{result ->
            Log.d("auth-reg",result.message.toString());
            when(result)
            {
//
                is Resource.Success->
                {
                    _signUpState.send(SignUpState(isSuccess = "SignUp Successful"))

                }
                is Resource.Loading ->
                {
                    _signUpState.send(SignUpState(isLoading = true))

                }
                is Resource.Error->{
                    _signUpState.send(SignUpState(isError = result.message))

                }
            }
        }
    }

}