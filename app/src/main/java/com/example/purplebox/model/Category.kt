package com.example.purplebox.model

data class Category(
    val name:String,
    val products:Int?,
    val rank:Int,
    val image:String
){
    constructor() : this("",0,0,"")
}
