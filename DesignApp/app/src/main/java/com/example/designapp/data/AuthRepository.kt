package com.example.designapp.data

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import com.example.designapp.util.Resource


interface AuthRepository {

    fun loginUser(email:String, password: String) : Flow<Resource<AuthResult>>
    fun registerUser(email:String, password: String) : Flow<Resource<AuthResult>>

}