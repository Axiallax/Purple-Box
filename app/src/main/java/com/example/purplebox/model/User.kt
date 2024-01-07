package com.example.purplebox.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userName:String,
    var contactNumber:String,
    var email:String,
    var imagePath:String=""
):Parcelable{

    constructor() : this("","","")
}
