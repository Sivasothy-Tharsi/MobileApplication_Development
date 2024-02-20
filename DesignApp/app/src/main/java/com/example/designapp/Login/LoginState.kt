package com.example.designapp.Login

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = "",
//    val userRole: UserRole?=null
)