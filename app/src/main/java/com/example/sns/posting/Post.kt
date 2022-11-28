package com.example.sns.posting

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

data class Post(
    var date: Date = Date(),
    var uid:String?=Firebase.auth.uid,
    var userID:String = "",
    var explain:String = "",
    var imageUrl: String = "",
    var profileimage : String ="",
)
