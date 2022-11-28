package com.example.sns

data class User(
    var email:String="",
    var id:String="",
    var numOfPost:Int=0,
    var uid:String="",
    var from: ArrayList<String> =ArrayList(),
    var to: ArrayList<String> =ArrayList()
)
