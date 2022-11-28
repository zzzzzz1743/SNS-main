package com.example.sns

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class PostAdapter (context : Context, private var postlist : List<Post>) : RecyclerView.Adapter<PostAdapter.CustomViewHolder>(){

    private var db: FirebaseFirestore = Firebase.firestore

    override fun getItemCount(): Int {
        return postlist.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.userID.text = postlist[position].userID
        holder.explainID.text = postlist[position].userID
        holder.explain.text = postlist[position].explain
        Glide.with(holder.itemView.context).load(postlist[position].imageUrl).into(holder.image_posted)
        holder.like_count.text = "좋아요  ${postlist[position].like}개"

        holder.bind(postlist[position].uid!!)


        val n="Profile_picture/${postlist[position].profileimage}.png"
        val profileRef=Firebase.storage.reference.child(n)
        profileRef.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful){
                Glide.with(holder.itemView.context).load(it.result).into(holder.profileimage)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val userID = itemView.findViewById<TextView>(R.id.home_userid) //유저 아이디
        val image_posted = itemView.findViewById<ImageView>(R.id.home_image) //이미지
        val explainID = itemView.findViewById<TextView>(R.id.home_explain_id) //내용 옆 아이디
        val explain = itemView.findViewById<TextView>(R.id.home_explain) //내용
        val like_count = itemView.findViewById<TextView>(R.id.home_like_text) //좋아요 수
        val profileimage = itemView.findViewById<ImageView>(R.id.home_profile)

        fun bind(uid:String){
            profileimage.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context,ProfileActivity::class.java).putExtra("uid",uid))
            }
            userID.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context,ProfileActivity::class.java).putExtra("uid",uid))
            }
            explainID.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context,ProfileActivity::class.java).putExtra("uid",uid))
            }
        }
    }

    fun updateList(newList : List<Post>){
        postlist = newList
        notifyDataSetChanged()
    }
/*
    fun like_event(position: String){
        var tsDoc = db.collection("item").document(position)
        db.runTransaction{ transaction ->

            val uid = Firebase.auth.currentUser?.uid
            val cDTO = transaction.get(tsDoc).toObject<Post>()

            if(cDTO!!.likes.containsKey(uid)){
                cDTO.like = cDTO.like - 1
                cDTO.likes.remove(uid)
            }
            else{
                cDTO.like = cDTO.like + 1
                cDTO.likes.remove(uid)
            }

        }
    }*/
}

