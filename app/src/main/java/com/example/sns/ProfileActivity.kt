package com.example.sns

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sns.databinding.ProfilePageBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfilePageBinding
    lateinit var storage: FirebaseStorage

    var post: Long?= 0  //포스트 개수
    var follower: Long?= 0  //팔로워 수
    var following: Long?= 0  //팔로잉 수
    var introduction: String?= "자기소개"  //자기소개 텍스트
    var userId: String?= "qwer12345"  //유저 아이디
    var is_myProfile: Boolean? = null  //프로필 창이 자신/타인의 것인지 구분
    var is_following: Boolean? = false  //타인의 프로필일시 팔로우 되어있는지 구분

    val db: FirebaseFirestore = Firebase.firestore
    val userCollectionRef = db.collection("users")
    val itemsCollectionRef = db.collection("item")


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        db.collection("item").whereEqualTo("Uid", "Bye").get().addOnSuccessListener {
            for(doc in it) {
                val p1 = doc.toObject<Post>()
                //println("@@@@@@@qq" + p1)
            }
        }

        val layoutArray = arrayListOf<LinearLayout>()

        var auth : FirebaseAuth? = null
        var firestore : FirebaseFirestore? = null
        var uid : String? = null

        auth = Firebase.auth
        uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore = FirebaseFirestore.getInstance()

        storage = Firebase.storage
        val storageRef = storage.reference

        val profile_mainButton = findViewById<Button>(R.id.profile_mainButton)

        //프로필창에 띄울 프로필이 누구의 것인지 이것으로 확인
        val profile_id : String = "tpbfKr5lNPTafrIhQv3wSTOZZ5v2"

        //프로필 이미지 이미지뷰
        val profile_image = findViewById<ImageView>(R.id.profile_image)

        //본인/타인 프로필인지 구분해서 프로필 메인 버튼을 수정
        if (uid.equals(profile_id)) {
            profile_mainButton.setText("프로필 수정")
            is_myProfile = true
        } else {
            checkFollow()
            is_myProfile = false
        }

        //프로필 정보를 아이디에 따라 표시해야함
        //아래 메뉴 > 홈을 눌렀다면 본인 아이디
        //타인 프로필 누를 시 타인 아이디
        fillUserData(profile_id)
        getProfileImg(profile_id, profile_image)
        postsInProfile(profile_id, layoutArray)



        //본인 프로필일시 버튼이 정보 수정으로 넘어감
        //타인 프로필일시 버튼이 팔로/언팔로 넘어감
        profile_mainButton.setOnClickListener {
            if (is_myProfile == true) {
                startActivity(Intent(this@ProfileActivity,UpdateProfileActivity::class.java))
            } else {
                if (is_following == true) {
                    userUnfollow()
                } else {
                    userFollow()
                }
            }
        }
    }

    //프로필 이미지 외의 정보 받아오기
    private fun fillUserData(itemID: String) {
        userCollectionRef.document(itemID).get().addOnSuccessListener {
            val user=it.toObject<User>()
            if(user!=null) {
                //여기부터
                binding.userId.text = user!!.id


                val user_id = findViewById<TextView>(R.id.user_id)
                val post_count = findViewById<TextView>(R.id.post_count)
                val follower_count = findViewById<TextView>(R.id.follower_count)
                val following_count = findViewById<TextView>(R.id.following_count)
                val intro_text = findViewById<TextView>(R.id.intro_text)

                user_id.text = userId
                post_count.text = post.toString()
                follower_count.text = follower.toString()
                following_count.text = following.toString()
                intro_text.text = introduction
            }
        }
    }

    private fun checkFollow() {
        //상대가 팔로우 되어있는지 확인
    }

    private fun userFollow() {
        //팔로우 기능
    }

    private fun userUnfollow() {
        //언팔로우 기능
    }

    //프로필 이미지 받아오기
    private fun getProfileImg(rogin: String, profile_image: ImageView){
        val n="Profile_picture/${rogin}.png"
        val profileRef=storage.reference.child(n)
        profileRef.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful){
                Glide.with(this).load(it.result).into(profile_image)

                val items = arrayListOf<String>()
                itemsCollectionRef.get()
                    .addOnCompleteListener {
                    }
            }
        }
    }

    private fun getPostImg(rute: String, image: ImageView){
        Glide.with(this).load(rute).into(image)
    }

    private fun getPostInProfile (itemID: String) {
        userCollectionRef.document(itemID).get().addOnSuccessListener {

        }
    }

    //프로필에 맞는 포스트 출력하기
    private fun postsInProfile(Uid: String, array: ArrayList<LinearLayout>) {
        val items = arrayListOf<String>()
        var items_count = 0
        /*
        itemsCollectionRef.whereEqualTo("Uid", Uid).get()
            .addOnSuccessListener {
                for (doc in it) {
                    items.add("${doc["imageUrl"]}")
                }
            }.addOnFailureListener {
            }

         */

        db.collection("item").whereEqualTo("Uid", Uid).get().addOnSuccessListener {
            for(doc in it) {
                items.add("${doc["imageUrl"]}")
                items_count++
            }
            println("@@@@@" + items)
        }


        var f : Int = 1
        var z : Int = 1
        val array_size : Int = items_count/3
        while(f <= array_size) {
            makeLayout(array)
            println("@@@@@@@@@@@@@@@@@@2")
            for(i: Int in 1..3) {
                makeImage(array[f], items.get(z))
                z++
                println("@@@@" + z)
            }
            println("@@@@@" + f)
            f++
        }
    }

    //라이너 레이아웃 동적 생성
    private fun makeLayout(array: ArrayList<LinearLayout>) {
        val layout :LinearLayout = findViewById<LinearLayout>(R.id.posts_inprofile)
        val layoutParams :LayoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT,
            1
        )

        val iv = LinearLayout(this)

        iv.layoutParams = layoutParams // imageView layout 설정

        array.add(iv)
        layout.addView(iv) // 기존 linearLayout에 imageView 추가
    }

    //이미지뷰 동적생성
    private fun makeImage(layout : LinearLayout, rute: String) {
        val layoutParams :LayoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT,
            1
        )

        val iv = ImageView(this)

        getPostImg(rute, iv)

        iv.layoutParams = layoutParams // imageView layout 설정

        layout.addView(iv) // 기존 linearLayout에 imageView 추가
    }


}