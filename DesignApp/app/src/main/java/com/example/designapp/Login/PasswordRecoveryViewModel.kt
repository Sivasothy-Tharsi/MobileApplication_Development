package com.example.designapp.Login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class PasswordRecoveryViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    suspend fun performPasswordRecovery(email: String): PasswordRecoveryResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            PasswordRecoveryResult.Success
        } catch (e: Exception) {
            PasswordRecoveryResult.Error(e.message ?: "Failed to send password reset email")
        }
    }
}