package com.example.sns

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sns.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SignUpActivity :AppCompatActivity(){
    val db=Firebase.firestore
    val storage=Firebase.storage

//    Uri.parse("android.resource://com.example.sns/R.drawable.default_profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        var imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.default_profile))
            .appendPath(resources.getResourceTypeName(R.drawable.default_profile))
            .appendPath(resources.getResourceEntryName(R.drawable.default_profile))
            .build()

        val getImage=registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    imageUri = it.data?.data
                    binding.profilePicture.setImageURI(imageUri)
                } else println(imageUri)
        }

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        binding.profilePicture.setOnClickListener {
            getImage.launch(photoPickerIntent)
        }

        println(imageUri)
        binding.signup.setOnClickListener{

            var fine=true
            if(imageUri==null){
                Toast.makeText(this, "Select Profile Image", Toast.LENGTH_SHORT).show()
                fine=false
            }

            val id=binding.id.text.toString()
            if(id.isEmpty()&&fine) {
                Toast.makeText(this, "Need Id", Toast.LENGTH_SHORT).show()
                fine = false
            }
//            if(!isValidId(id)) {
//                Toast.makeText(this, "Invalid Id", Toast.LENGTH_SHORT).show()
//                fine=false
//            }

            val pw=binding.pw.text.toString()
            if(!isValidPw(pw)&&fine){
                Toast.makeText(this, "Invalid Pw", Toast.LENGTH_SHORT).show()
                fine=false
            }
            val pwc=binding.pwcheck.text.toString()
            if(!(pwc.equals(pw))&&fine){
                Toast.makeText(this, "Pw Difference", Toast.LENGTH_SHORT).show()
                fine=false
            }

            val email=binding.email.text.toString()
            if(fine) {
                Firebase.auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Sign Up Success", Toast.LENGTH_SHORT).show()
                        val fileName=Firebase.auth.uid.toString()+".png"
                        uploadProfileImg(fileName,imageUri)
                        addUser(Firebase.auth.uid.toString(),id,email)
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                        finish()
                    } else {
                        Log.w("SignUpActivity", "signup failed", it.exception)
                        Toast.makeText(this, "Email Already Exist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

//    private fun isValidId(id: String): Boolean {
//        val idConstraint="^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]$"
//        val regex=idConstraint.toRegex()
//        if(regex.matches(id))return true
//        return false
//    }

    fun isValidPw(pw:String):Boolean{
        val pwConstraint="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,20}$"
        val regex=Regex(pwConstraint)
        if(!regex.matches(pw))return false
        return true
    }

    private fun uploadProfileImg(fileName: String, imageUri:Uri) {
        val imageRef = storage.reference.child("Profile_picture/${fileName}")
        imageRef.putFile(imageUri).addOnCompleteListener {
            if (it.isSuccessful) {
            }
        }
    }

    private fun addUser(uid:String,Id:String,Email:String){
        val ref=db.collection("User").document(uid)
        db.runTransaction{
            var user=User(Email,Id,0,uid,0,0)
            it.set(ref,user)
        }.addOnCompleteListener {
        }
    }

}