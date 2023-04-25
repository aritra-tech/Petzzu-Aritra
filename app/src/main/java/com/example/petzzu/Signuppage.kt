package com.example.petzzu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Signuppage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signuppage)

        auth= Firebase.auth
        val secondActbutton=findViewById<MaterialButton>(R.id.signbtn)
        secondActbutton.setOnClickListener {
            performSignUp()
        }
    }
    private fun performSignUp() {
        val username=findViewById<EditText>(R.id.user)
        val email=findViewById<EditText>(R.id.email)
        val password=findViewById<EditText>(R.id.password)
        val confirmPass=findViewById<EditText>(R.id.password1)

        if(email.text.isEmpty() || password.text.isEmpty() || username.text.isEmpty() ||confirmPass.text.isEmpty() || password.text!=confirmPass.text){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
            return
        }
        val inputemail=email.text.toString()
        val inputpassword=password.text.toString()

        auth.createUserWithEmailAndPassword(inputemail,inputpassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val Intent = Intent(this,Dashboard::class.java)
                    startActivity(Intent)

                    Toast.makeText(this, "Authentication Successfull.",
                        Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error Occurred ${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }
    }
}