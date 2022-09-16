package com.example.damoim

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signUpBtn.setOnClickListener {
            performRegister()

        }
        profileImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }
var seletedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            seletedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,seletedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            profileImg.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {

        val email = emailEdt.text.toString()
        val password = passwordEdt.text.toString()
//            val nickname = nickEdt.text.toString()


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일/패스워드를 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("SignupActivity", "Email is: " + email)
        Log.d("SignupActivity", "Password: $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
//                    Log.d("SignupActivity", "유저 저장 성공, uid 생성: ${it.result.user.uid}")
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("SignupActivity", "유저 저장 실패 : ${it.message}")
                Toast.makeText(this, "유저 저장 실패 : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (seletedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(seletedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("SignupActivity", "Successfully uploaded${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("SignupActivity", "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,nickEdt.text.toString(),profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("SignupActivity","firebase Database에 저장되었습니다.")
            }
    }
}

class User(val uid: String, val username: String, val profileImageUrl:String)
