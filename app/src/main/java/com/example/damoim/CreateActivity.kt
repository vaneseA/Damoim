package com.example.damoim

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_create.createMoimBtn
import kotlinx.android.synthetic.main.activity_create.groupNameEdt
import kotlinx.android.synthetic.main.activity_create.locationEdt
import kotlinx.android.synthetic.main.activity_create.moimImg
import kotlinx.android.synthetic.main.activity_create.purposeEdt
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CreateActivity : AppCompatActivity() {


    lateinit var ImageUri: Uri

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
            // Post 객체 생성
            val post = Post()
            // Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
            val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
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
            newRef.setValue(post)
//          파이어베이스 디렉토리로 보냄
            uploadImage()
            // 저장성공 토스트 알림을 보여주고 Activity 종료
//            Toast.makeText(applicationContext, "저장완료.", Toast.LENGTH_SHORT).show()
//            finish()
        }

    }

    fun setValues() {

    }


    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(ImageUri).addOnSuccessListener {
            moimImg.setImageURI(null)
            Toast.makeText(this@CreateActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()
            finish()

        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@CreateActivity, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            moimImg.setImageURI(ImageUri)
        }
    }
}
