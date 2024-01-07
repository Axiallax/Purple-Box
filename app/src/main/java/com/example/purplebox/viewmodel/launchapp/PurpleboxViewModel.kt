package com.example.purplebox.viewmodel.launchapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.purplebox.firebaseDatabase.FirebaseDb
import com.example.purplebox.model.User
import com.example.purplebox.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PurpleboxViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {


    val register = MutableLiveData<Resource<User>>()

    val admin = MutableLiveData<Boolean>()
    val loginError = MutableLiveData<String>()

    val resetPassword = MutableLiveData<Resource<String>>()

    /*val resetPassword = MutableLiveData<Resource<String>>()*/

    fun registerNewUser(
        user: User,
        password: String
    ) {
        register.postValue(Resource.Loading())
        firebaseDatabase.createNewUser(user.email, password).addOnCompleteListener {
            if (it.isSuccessful)
                firebaseDatabase.saveUserInformation(Firebase.auth.currentUser!!.uid, user)
                    .addOnCompleteListener { it2 ->
                        if (it2.isSuccessful) {
                            register.postValue(Resource.Success(user))
                        }
                        else
                            register.postValue(Resource.Error(it2.exception.toString()))
                    }
            else
                register.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun loginUser(
        email: String,
        password: String
    ) = firebaseDatabase.loginUser(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            checkUserLevelAccess()
        }
        else
            loginError.postValue(it.exception.toString())
    }

    /*fun resetPassword(email: String) {
        resetPassword.postValue(Resource.Loading())
        firebaseDatabase.resetPassword(email).addOnCompleteListener {
            if (it.isSuccessful)
                resetPassword.postValue(Resource.Success(email))
            else
                resetPassword.postValue(Resource.Error(it.exception.toString()))

        }
    }*/

    fun checkUserLevelAccess() {
        firebaseDatabase.getUser().get().addOnSuccessListener{
            if (it.getString("admin") != null)
                admin.postValue(true)
            else
                admin.postValue(false)
        }
    }

    fun logOut() {
        firebaseDatabase.logout()
    }

    fun isUserSignedIn() : Boolean {
        if (FirebaseAuth.getInstance().currentUser?.uid != null)
            return true
        return false
    }

}