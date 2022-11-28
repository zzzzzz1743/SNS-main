//package com.example.sns
//
//import android.annotation.SuppressLint
//import android.content.ContentUris
//import android.content.Intent
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.View
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import com.bumptech.glide.Glide
//import com.google.firebase.FirebaseApp
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.ktx.storage
//
//public class UpdateProfileActivity : AppCompatActivity() {
//    lateinit var storage: FirebaseStorage
//
//    var introduction: String?= null
//    var userId: String?= null
//
//    val db: FirebaseFirestore = Firebase.firestore
//    val userCollectionRef = db.collection("users")
//
//    val rogin : String = "tpbfKr5lNPTafrIhQv3wSTOZZ5v2"
//
//   @SuppressLint("WrongViewCast", "MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//       super.onCreate(savedInstanceState)
//       setContentView(R.layout.update_profile)
//       FirebaseApp.initializeApp(this)
//
//       storage = Firebase.storage
//       val pfUpdate_Back = findViewById<ImageButton>(R.id.pfUpdate_Back)
//       val profileUpdateSave = findViewById<Button>(R.id.profileUpdateSave)
//       val imageRef1 = storage.getReferenceFromUrl("gs://snsprojectprofile.appspot.com/pngwing.com.png")
//       val profile_image = findViewById<ImageView>(R.id.profile_image)
//
//       getProfileImg(rogin, profile_image)
//
//       pfUpdate_Back.setOnClickListener {
//           startActivity(Intent(this@UpdateProfileActivity, ProfileActivity::class.java))
//       }
//
//       profileUpdateSave.setOnClickListener {
//           updateDataSave(rogin)
//       }
//
//    }
//
//    private fun updateDataSet(itemID: String) {
//        userCollectionRef.document(itemID).get().addOnSuccessListener {
//            userId = it["id"] as String
//            introduction = it["intro_text"] as String
//
//            val enterId = findViewById<EditText>(R.id.enterId)
//            val enterIntro = findViewById<EditText>(R.id.enterIntro)
//
//            enterId.setText(userId)
//            enterIntro.setText(introduction)
//
//        }
//    }
//
//    private fun updateDataSave(itemID: String) {
//        val enterIntro = findViewById<EditText>(R.id.enterIntro)
//        val updateIntro = enterIntro.text.toString()
//
//        userCollectionRef.document(itemID).update("intro_text", updateIntro).addOnSuccessListener {
//
//        }
//    }
//
//    private fun getProfileImg(rogin: String, profile_image: ImageView){
//        val n="Profile_picture/${rogin}.png"
//        val profileRef=storage.reference.child(n)
//        profileRef.downloadUrl.addOnCompleteListener {
//            if(it.isSuccessful){
//                Glide.with(this).load(it.result).into(profile_image)
//            }
//        }
//    }
//
//    private fun uploadNewProfileImage(file_id: Long, rogin: String) {
//        val n="Profile_picture/${rogin}.png"
//        val profileRef=storage.reference.child(n)
//        val contentUri = ContentUris.withAppendedId(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, file_id)
//        profileRef.putFile(contentUri).addOnCompleteListener {
//
//        }
//    }
//
//    /*
//    public fun uploadDialog(setContentView: View) {
//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//            == PackageManager.PERMISSION_GRANTED) {
//            val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, null, null, null)
//
//            AlertDialog.Builder(this)
//                .setTitle("Choose Photo")
//                .setCursor(cursor, { _, i ->
//                    cursor?.run {
//                        moveToPosition(i)
//                        val idIdx = getColumnIndex(MediaStore.Images.ImageColumns._ID)
//                        uploadNewProfileImage(getLong(idIdx), rogin)
//                    }
//                }, MediaStore.Images.ImageColumns.DISPLAY_NAME).create().show()
//        } else {
//            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
//        }
//    }
//
//     */
//
//    public fun profileImageOnClick(setContentView: View) {
//    }
//
//}