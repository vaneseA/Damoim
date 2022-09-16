package com.example.damoim

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuthException

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
    // Post 객체 생성
    val post = Post()
    // Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
    val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
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

            //모임지역
            post.location = locationEdt.text.toString()
//          모임이름
            post.groupName = groupNameEdt.text.toString()
            // 모임메세지 EditText 의 텍스트 내용을 할당
            post.purposeMessage = purposeEdt.text.toString()
            //모임 이미지 uri주소를 현재 모임 이미지 주소로 할당
//            post.moimImgUri = moimImg.setImageURI()
            // 글쓴 사람의 ID 는 디바이스의 아이디로 할당
            post.postId = newRef.key.toString()
            uploadImage()
            newRef.setValue(post)
//          파이어베이스 디렉토리로 보냄
            // 저장성공 토스트 알림을 보여주고 Activity 종료
            Toast.makeText(applicationContext, "저장완료.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    fun setValues() {

    }


    private fun uploadImage() {
        if(selectedPhotoUri == null) return

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val ref = FirebaseStorage.getInstance().getReference("images/$fileName")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            moimImg.setImageURI(null)
            Log.d("CreateActivity", "Successfully uploaded${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {

                Log.d("CreateActivity", "File Location: $it")

            }
        }
    }

//    private fun saveMoimTofirebaseDatabase() {
//        val fileName = formatter.format(now)
//        val post = Post()
//        val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
//
//        //          모임이름
//        post.moimImgUri = FirebaseStorage.getInstance().getReference("images/$fileName").toString()
//        newRef.setValue(post)
//        val postId =FirebaseStorage.getInstance().getReference("/Posts/$postId")
//        FirebaseDatabase.getInstance().getReference("/Posts/$postId")
//    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            moimImg.setBackgroundDrawable(bitmapDrawable)
        }
    }
}
