package com.example.sns

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

data class Post(
    var date: Date = Date(),
    var Uid:String?=Firebase.auth.uid,
    var userID:String = "",
    var explain:String = "",
    var like:Int = 0,
    var imageUrl: String = "",
    var profileimage : String ="",
    //var likes : MutableMap<String, Boolean> = HashMap()
)
