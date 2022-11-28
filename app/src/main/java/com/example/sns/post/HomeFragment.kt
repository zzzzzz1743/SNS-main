package com.example.sns.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.posting.Post
import com.example.sns.login.User
import com.example.sns.databinding.FragmentPostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

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
        val myref=db.collection("User").document(Firebase.auth.uid!!)
        myref.get().addOnSuccessListener {
            val user=it.toObject<User>()
            itemsCollectionRef.orderBy("date",Query.Direction.DESCENDING).get().addOnSuccessListener { it1 ->
                for (item in it1) {
                    val p = item.toObject<Post>()
                    if ((p.uid in user!!.to) || (p.uid == Firebase.auth.uid))
                        posts.add(p)
                }
                adapter?.updateList(posts)
            }
        }
    }
}