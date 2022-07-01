package com.mine.chat.dataandmodels.firebasedb.remotedb

import com.mine.chat.dataandmodels.modelclasses.ModelCreateUser
import com.mine.chat.dataandmodels.modelclasses.ModelLogin
import com.mine.chat.dataandmodels.ModelResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthStateObserver {

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var instance: FirebaseAuth? = null

    fun start(valueEventListener: FirebaseAuth.AuthStateListener, instance: FirebaseAuth) {
        this.authListener = valueEventListener
        this.instance = instance
        this.instance!!.addAuthStateListener(authListener!!)
    }

    fun clear() {
        authListener?.let { instance?.removeAuthStateListener(it) }
    }
}

class FirebaseAuthSource {

    companion object {
        val authInstance = FirebaseAuth.getInstance()
    }

    private fun attachAuthObserver(b: ((ModelResult<FirebaseUser>) -> Unit)): FirebaseAuth.AuthStateListener {
        return FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                b.invoke(ModelResult.Error("No user"))
            } else { b.invoke(ModelResult.Success(it.currentUser)) }
        }
    }

    fun loginWithEmailAndPassword(modelLogin: ModelLogin): Task<AuthResult> {
        return authInstance.signInWithEmailAndPassword(modelLogin.email, modelLogin.password)
    }

    fun createUser(modelCreateUser: ModelCreateUser): Task<AuthResult> {
        return authInstance.createUserWithEmailAndPassword(modelCreateUser.email, modelCreateUser.password)
    }

    fun logout() {
        authInstance.signOut()
    }

    fun attachAuthStateObserver(firebaseAuthStateObserver: FirebaseAuthStateObserver, b: ((ModelResult<FirebaseUser>) -> Unit)) {
        val listener = attachAuthObserver(b)
        firebaseAuthStateObserver.start(listener, authInstance)
    }
}

