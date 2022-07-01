package com.mine.chat.dataandmodels.firebasedb.managerepository

import com.mine.chat.dataandmodels.modelclasses.ModelCreateUser
import com.mine.chat.dataandmodels.modelclasses.ModelLogin
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseAuthSource
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseAuthStateObserver
import com.mine.chat.dataandmodels.ModelResult
import com.google.firebase.auth.FirebaseUser

class AuthenticationRepository{
    private val firebaseAuthService = FirebaseAuthSource()

    fun observeAuthState(stateObserver: FirebaseAuthStateObserver, b: ((ModelResult<FirebaseUser>) -> Unit)){
        firebaseAuthService.attachAuthStateObserver(stateObserver,b)
    }

    fun loginUser(modelLogin: ModelLogin, b: ((ModelResult<FirebaseUser>) -> Unit)) {
        b.invoke(ModelResult.Loading)
        firebaseAuthService.loginWithEmailAndPassword(modelLogin).addOnSuccessListener {
            b.invoke(ModelResult.Success(it.user))
        }.addOnFailureListener {
            b.invoke(ModelResult.Error(msg = it.message))
        }
    }

    fun createUser(modelCreateUser: ModelCreateUser, b: ((ModelResult<FirebaseUser>) -> Unit)) {
        b.invoke(ModelResult.Loading)
        firebaseAuthService.createUser(modelCreateUser).addOnSuccessListener {
            b.invoke(ModelResult.Success(it.user))
        }.addOnFailureListener {
            b.invoke(ModelResult.Error(msg = it.message))
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }
}