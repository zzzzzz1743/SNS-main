package com.example.sns

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProfileAdapter (private val posts:ArrayList<Post>):RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val img:ImageView=itemView.findViewById(R.id.item_img)
        private val exp:TextView=itemView.findViewById(R.id.item_text)

        fun bind(post:Post){
            exp.text=post.explain
            Glide.with(itemView).load(post.imageUrl).into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView=LayoutInflater.from(parent.context).inflate(R.layout.profile_item,parent,false)
        return ProfileAdapter.ViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {
        val post=posts[position]
        holder.apply {
            bind(post)
            itemView.tag = post
        }
    }

    override fun getItemCount(): Int=posts.size
}