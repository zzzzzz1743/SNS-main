package com.example.sns

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sns.databinding.ActivityProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    val posts= ArrayList<Post>()

    val db=Firebase.firestore
    val storage=Firebase.storage
    lateinit var uid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uid=intent.getStringExtra("uid")!!


        getProfileImg(uid)
        getUserinfo(uid)

        val fllwbtn=binding.followBtn

        fllwbtn.setOnClickListener{
            if(Firebase.auth.uid==uid)Toast.makeText(this,"자신을 팔로우할 수 없습니다",Toast.LENGTH_SHORT).show()
            else if(fllwbtn.text.toString()=="팔로우")follow(Firebase.auth.uid!!,uid)
            else if(fllwbtn.text.toString()=="언팔로우")unfollow(Firebase.auth.uid!!,uid)
        }

        db.collection("item").orderBy("date",Query.Direction.DESCENDING).get().addOnSuccessListener {
            for(doc in it) {
                val post=doc.toObject<Post>()
                if(post.uid==uid)
                    posts.add(post)
                println("@@@@@@@qq" + posts)
            }
            val adapter=ProfileAdapter(posts)
            binding.profileRecyclerview.adapter=adapter
            println("success")
        }.addOnFailureListener {
            println("failure")
        }
    }

    private fun follow(from:String,to:String){
        //정보 가져오고 수정하고 보내기
         var touser=User()
        db.runTransaction {
            //상대의 팔로워 목록에 나를 추가
            val ref = db.collection("User").document(to)

            touser = it.get(ref).toObject<User>()!!

            if(touser==null){
                touser= User()
            }

            if(!(touser.from.contains(from)))
                touser?.from?.add(from)

            it.set(ref,touser!!)
        }.addOnSuccessListener {
            binding.from.text=touser.from.size.toString()
            binding.to.text=touser.to.size.toString()
        }

        db.runTransaction{
            //내가 팔로우 하는 사람을 추가
            val ref1=db.collection("User").document(from)

            var fromuser = it.get(ref1).toObject<User>()

            if(fromuser==null){
                fromuser=User()
            }

            if(!(fromuser!!.to.contains(to)))
                fromuser?.to?.add(to)

            it.set(ref1,fromuser!!)
        }.addOnCompleteListener {
            if(it.isSuccessful){
                binding.followBtn.text="언팔로우"
            }
        }
    }

    private fun unfollow(from: String, to: String){
        //정보 가져오고 수정하고 보내기
         var touser:User=User()
        db.runTransaction {
            //상대의 팔로워 목록에서 나를 제거
            val ref = db.collection("User").document(to)

            touser = it.get(ref).toObject<User>()!!

            if(touser==null){
                touser= User()
            }

            for(i in 0 until touser.from.size){
                if(touser.from.contains(from))touser.from.remove(from)
            }

            it.set(ref,touser!!)
        }.addOnSuccessListener {
            binding.from.text=touser.from.size.toString()
            binding.to.text=touser.to.size.toString()
        }

        var fromuser:User?=User()
        db.runTransaction{
            //내가 팔로우 하는 사람을 추가
            val ref1=db.collection("User").document(from)

             fromuser= it.get(ref1).toObject<User>()

            if(fromuser==null){
                fromuser=User()
            }

            for(i in 0 until fromuser!!.to.size){
                if(fromuser!!.to.contains(to))fromuser!!.to.remove(to)
            }

            it.set(ref1,fromuser!!)
        }.addOnCompleteListener {
            if(it.isSuccessful){
                binding.followBtn.text="팔로우"
            }
        }
    }

    private fun getUserinfo(uid:String){
        db.collection("User").document(uid).get().addOnSuccessListener {
            val user=it.toObject<User>()
            binding.userId.text=user!!.id
            binding.from.text=user!!.from.size.toString()
            binding.to.text=user!!.to.size.toString()
            binding.postCount.text=user!!.numOfPost.toString()
        }
    }

    //프로필 이미지 받아오기
    private fun getProfileImg(Uid: String){
        val n="Profile_picture/${Uid}.png"
        val profileRef=storage.reference.child(n)
        profileRef.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful){
                Glide.with(this).load(it.result).into(binding.profileImage)
            }
        }
    }

}