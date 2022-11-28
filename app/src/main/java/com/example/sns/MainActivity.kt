package com.example.sns

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sns.post.AllUsersFragment
import com.example.sns.post.HomeFragment
import com.example.sns.databinding.ActivityMainBinding
import com.example.sns.login.SignInActivity
import com.example.sns.posting.PostingActivity
import com.example.sns.profile.ProfileActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.action_home ->{
                var homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,homeFragment).commit()
                return true
            }
            R.id.action_alluser ->{
                var allUsersFragment = AllUsersFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,allUsersFragment).commit()
                return true
            }
            R.id.action_newpost ->{
                logincheck()
                startActivity(Intent(this, PostingActivity::class.java))

            }
            R.id.action_profile ->{
                logincheck()
                startActivity(Intent(this, ProfileActivity::class.java).putExtra("uid",Firebase.auth.uid))
            }
        }
        return false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logincheck()

        binding.logout.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            dialog
                .setTitle("로그아웃 하시겠습니까?")
                .setMessage("로그인 화면으로 돌아갑니다")
                .setPositiveButton("예", DialogInterface.OnClickListener{ dialogInterface, id ->
                    Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    Firebase.auth.signOut()
                    logincheck()
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener{ dialogInterface, id ->
                    Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show()
                })
                .create()
                .show()
        }

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