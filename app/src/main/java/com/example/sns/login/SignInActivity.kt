package com.example.sns.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sns.MainActivity
import com.example.sns.databinding.ActivitySigninBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SignInActivity : AppCompatActivity() {

    val db=Firebase.firestore
    val storage=Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup.setOnClickListener {
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
            finish()
        }

        binding.signin.setOnClickListener {
            var fine=true
            val userEmail=binding.email.text.toString()
            val password=binding.pw.text.toString()
            if(userEmail.isEmpty()){
                Toast.makeText(this, "Need Email", Toast.LENGTH_SHORT).show()
                fine=false
            }
            if(password.isEmpty()&&fine){
                Toast.makeText(this, "Need Password", Toast.LENGTH_SHORT).show()
                fine=false
            }
            if(fine)doLogin(userEmail,password)
        }
    }
    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Sign in succeeded", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Can't sign in", Toast.LENGTH_SHORT).show()
                }
            }
    }
}