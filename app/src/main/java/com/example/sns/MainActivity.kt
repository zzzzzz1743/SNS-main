package com.example.sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.sns.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.action_home ->{
                var friendFragment = FriendFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,friendFragment).commit()
                return true

            }
            R.id.action_search ->{
                var homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,homeFragment).commit()
                return true
            }
            R.id.action_newpost ->{
                logincheck()
                startActivity(Intent(this,PostingActivity::class.java))

            }
            R.id.action_notification ->{
                Firebase.auth.signOut()
                logincheck()
            }
            R.id.action_profile ->{
                logincheck()
                startActivity(Intent(this,ProfileActivity::class.java).putExtra("uid",Firebase.auth.uid))
            }
        }
        return false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logincheck()

        bottom_navigation.setOnItemSelectedListener(this)
        //Set default screen
        bottom_navigation.selectedItemId = R.id.action_home

    }

    private fun logincheck(){
        if (Firebase.auth.currentUser == null) {
            Toast.makeText(this,"로그인이 필요합니다",Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
            finish()
        }
    }
}