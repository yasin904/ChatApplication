package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {


    private lateinit var edtFirstName : EditText
    private lateinit var edtLastName : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var edtConfirmPassword : EditText
    private lateinit var btnSignup: Button

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDBRef : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        edtFirstName = findViewById(R.id.edt_Firstname)
        edtLastName = findViewById(R.id.edt_Lastname)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        edtConfirmPassword = findViewById(R.id.edt_confirm_password)
        btnSignup = findViewById(R.id.btnSignUp)

        btnSignup.setOnClickListener{
            val name = edtFirstName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirm_password = edtConfirmPassword.text.toString()


            signUp(name,email,password,confirm_password)
        }
    }

    private fun signUp(name : String,email: String, password: String,confirm_password : String) {
        // Logic of creating user

        if(password == confirm_password) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                        val intent = Intent(this@Signup, MainActivity::class.java)
                        finish()
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@Signup,
                            "some error occurred,please try again",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
        }
        else{
            Toast.makeText(this@Signup,
                "Password Mismatch, Please enter correct password",
                Toast.LENGTH_SHORT
            ).show()
        }


    }


    private fun addUserToDatabase(name : String, email : String, uid : String){

        mDBRef = FirebaseDatabase.getInstance().getReference()
        mDBRef.child("user").child(uid).setValue(User(name,email,uid))

    }

}