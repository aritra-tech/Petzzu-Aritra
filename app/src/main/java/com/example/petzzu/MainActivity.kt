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

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= Firebase.auth

        val secondActbutton=findViewById<MaterialButton>(R.id.signup)
        secondActbutton.setOnClickListener {
            val Intent = Intent(this,Signuppage::class.java)
            startActivity(Intent)
        }

        val loginButton=findViewById<MaterialButton>(R.id.loginbtn)
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email=findViewById<EditText>(R.id.email)
        val password=findViewById<EditText>(R.id.password)

        if(email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val inputemail=email.text.toString()
        val inputpassword=password.text.toString()

        auth.signInWithEmailAndPassword(inputemail,inputpassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val Intent = Intent(this,Dashboard::class.java)
                    startActivity(Intent)

                    Toast.makeText(this, "Authentication Successfull.",
                        Toast.LENGTH_SHORT).show()


                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error Occurred ${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }
    }

}