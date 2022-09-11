package com.example.damoim

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

import kotlinx.android.synthetic.main.activity_create.*


class CreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        setUpEvents()
        setValues()
    }


    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
//            어떤 사진을 골랏는지? 파악해보자
//            임시 : 고른 사진을 profileImg에 바로 적용만 (서버전송 X)

//            data? => 이전 화면이 넘겨준 intent
//            data?.data => 선택한 사진이 들어있는 경로 정보 (Uri)
                val dataUri = it.data?.data

//            Uri -> 이미지뷰의 사진 (GLide)
                Glide.with(this).load(dataUri).into(moimImg)
            }
        }

    fun setUpEvents() {
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
            // 글이 쓰여진 시간은 Firebase 서버의 시간으로 설정
            post.writeTime = ServerValue.TIMESTAMP
            // 글쓴 사람의 ID 는 디바이스의 아이디로 할당
            post.postId = newRef.key.toString()
            newRef.setValue(post)
            // 저장성공 토스트 알림을 보여주고 Activity 종료
            Toast.makeText(applicationContext, "저장완료.", Toast.LENGTH_SHORT).show()
            finish()
        }
        moimImg.setOnClickListener {
            //            갤러리를 개발자가 이용 : 유저 허락을 받아야한다. => 권한 세팅
//            TedPermission 라이브러리
            Toast.makeText(this, "됨", Toast.LENGTH_SHORT).show()
            val pl = object : PermissionListener {
                override fun onPermissionGranted() {
//            권한이 OK
                    val myIntent = Intent()

                    //            갤러리로 사진을 가지러 이동(추가작업) => Intent (3) / (4) 결합
                    myIntent.action = Intent.ACTION_PICK
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE

                    startForResult.launch(myIntent)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    //권한이 Denied
                }
            }

            //            권한이 OK 일 때
            TedPermission.create()
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                //            테드 퍼미션이 지원하는 Denied 경우의 Alert
                .setDeniedMessage("[설정] > [권한]에서 갤러리 권한을 열어주세요.")
                .check()

        }
    }

    fun setValues() {

//        setMoimData()
    }

//    fun setMoimData() {
//        Glide.with(this)
//            .load(MoimData.loginUser!!.moimImg)
//            .into(moimImg)
//    }
//

}
