package com.example.sns.posting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sns.databinding.AcivityPostingBinding
import com.example.sns.login.SignInActivity
import com.example.sns.login.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Date

class PostingActivity: AppCompatActivity() {
    val db= Firebase.firestore
    val storage=Firebase.storage
    var imageUri:Uri?=null
    lateinit var binding:AcivityPostingBinding
    val uid=Firebase.auth.uid.toString()

    //유저네임 받아오기
    val username="eee"
    val isedit=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AcivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logincheck()


        val getImage=registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data!!
                binding.image.setImageURI(imageUri)
            } else {
                println("@@@@imageUri"+imageUri)
            }
        }

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        getProfileImg()



        binding.image.setOnClickListener {
            logincheck()
            getImage.launch(photoPickerIntent)
        }

        binding.upload.setOnClickListener{
            binding.upload.isEnabled=false
            logincheck()
            uploadpost()

        }

    }


    private fun uploadpost(){
        Toast.makeText(this,"업로드 중",Toast.LENGTH_SHORT).show()
        var post= Post(Date(),uid,username,binding.description.text.toString(), imageUri.toString(), Firebase.auth.uid.toString())
        val imgref=storage.reference.child("Post/${Date()}")
        db.runTransaction {
            val userref=db.collection("User").document(uid)
            var user=it.get(userref).toObject<User>()
            post.userID=user!!.id
            user.numOfPost+=1
            it.update(userref, "numOfPost",user.numOfPost)
        }.addOnSuccessListener {
            if (imageUri != null) {
                imgref.putFile(imageUri!!).addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {
                        post.imageUrl = it.toString()
                        db.collection("item").add(post).addOnSuccessListener {
                            Toast.makeText(this,"성공",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            } else {
                db.collection("item").add(post).addOnSuccessListener {
                    Toast.makeText(this,"성공",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun getProfileImg(){
        val n="Profile_picture/${Firebase.auth.uid}.png"
        val profileRef=storage.reference.child(n)
        profileRef.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful){
                Glide.with(this).load(it.result).into(binding.profile)
            }
        }
    }

    private fun logincheck(){
        if (Firebase.auth.currentUser == null) {
            Toast.makeText(this,"로그인이 필요합니다", Toast.LENGTH_SHORT)
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
            finish()
        }
    }
}
