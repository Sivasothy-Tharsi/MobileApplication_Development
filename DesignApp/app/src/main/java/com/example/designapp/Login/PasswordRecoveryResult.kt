package com.example.designapp.Login

sealed class PasswordRecoveryResult {
    object Success : PasswordRecoveryResult()
    data class Error(val message: String) : PasswordRecoveryResult()
}