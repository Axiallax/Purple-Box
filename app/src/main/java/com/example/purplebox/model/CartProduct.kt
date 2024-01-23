package com.example.purplebox.model

data class CartProduct (
    val id:Int,
    val name:String,
    val image:String,
    val price:String,
    val newPrice:String?,
    val quantity:Int,
    val size:String
        ) {
    constructor() : this(0,"","","","",0,"")
}
