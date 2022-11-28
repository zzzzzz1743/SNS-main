package com.example.sns.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.posting.Post
import com.example.sns.databinding.FragmentPostBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class AllUsersFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private var adapter: PostAdapter? = null
    private var db: FirebaseFirestore = Firebase.firestore
    private var itemsCollectionRef = db.collection("item")
    private var snapshotListenr: ListenerRegistration? = null
    val posts=ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(layoutInflater,)

        val recyclerview = binding.homefragmentRecyclerview

        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(requireContext(), emptyList())

        recyclerview.adapter = adapter

        updateList()

        return binding.root
    }


    private fun updateList(){

        itemsCollectionRef.orderBy("date",Query.Direction.DESCENDING).get().addOnSuccessListener {
            for(item in it) {
                posts.add(item.toObject<Post>())
            }
            adapter?.updateList(posts)

        }
    }
}