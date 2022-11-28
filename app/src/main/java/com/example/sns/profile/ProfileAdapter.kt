package com.example.sns.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns.R
import com.example.sns.posting.Post

class ProfileAdapter (private val posts:ArrayList<Post>):RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val img:ImageView=view.findViewById(R.id.item_img)
        private val exp:TextView=view.findViewById(R.id.item_text)

        fun bind(post: Post){
            this.exp.text = post.explain
            Glide.with(this.itemView.context).load(post.imageUrl).into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView=LayoutInflater.from(parent.context).inflate(R.layout.profile_item,parent,false)
        return ViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=posts[position]
        holder.apply {
            bind(post)
            itemView.tag = post
        }
    }

    override fun getItemCount(): Int=posts.size
}