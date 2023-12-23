package com.example.purplebox.util.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var firstName:String,
    var lastName:String,
    var email:String,
    var imagePath:String=""
):Parcelable{

    constructor() : this("","","")
}
