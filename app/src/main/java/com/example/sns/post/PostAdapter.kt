package com.example.sns.post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns.posting.Post
import com.example.sns.profile.ProfileActivity
import com.example.sns.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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
        val profileimage = itemView.findViewById<ImageView>(R.id.home_profile)

        fun bind(uid:String){
            profileimage.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context, ProfileActivity::class.java).putExtra("uid",uid))
            }
            userID.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context, ProfileActivity::class.java).putExtra("uid",uid))
            }
            explainID.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context, ProfileActivity::class.java).putExtra("uid",uid))
            }
        }
    }

    fun updateList(newList : List<Post>){
        postlist = newList
        notifyDataSetChanged()
    }
}

