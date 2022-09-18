package com.example.damoim

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_create.createMoimBtn
import kotlinx.android.synthetic.main.activity_create.groupNameEdt
import kotlinx.android.synthetic.main.activity_create.locationEdt
import kotlinx.android.synthetic.main.activity_create.moimImg
import kotlinx.android.synthetic.main.activity_create.purposeEdt
import java.text.SimpleDateFormat
import java.util.*


class CreateActivity : AppCompatActivity() {
    //    // Post 객체 생성
//    val post = Post()
//    // Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
//    val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        setUpEvents()
        setValues()
    }


    fun setUpEvents() {
        moimImg.setOnClickListener {
            selectImage()
        }
        createMoimBtn.setOnClickListener {
            uploadImage()
            finish()

        }

    }

    fun setValues() {

    }


    private fun uploadImage() {
        if (selectedPhotoUri == null) return

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val ref = FirebaseStorage.getInstance().getReference("images/$fileName")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            moimImg.setImageURI(null)
            Log.d("CreateActivity", "Successfully uploaded${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {

                Log.d("CreateActivity", "File Location: $it")
                saveMoimTofirebaseDatabase(it.toString())
            }
        }
    }

    private fun saveMoimTofirebaseDatabase(moimImgUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/Posts/$uid")

        val posts = Post(
            uid,
            purposeEdt.text.toString(),
            groupNameEdt.text.toString(),
            locationEdt.text.toString(),
            moimImgUrl
        )

        ref.setValue(posts)
            .addOnSuccessListener {
                Log.d("CreateActivity", "firebase Database에 저장되었습니다.")
            }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK

        startActivityForResult(intent, 0)
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            moimImg.setBackgroundDrawable(bitmapDrawable)
        }
    }
}

