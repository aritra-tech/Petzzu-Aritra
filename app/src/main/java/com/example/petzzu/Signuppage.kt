package com.example.petzzu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class Signuppage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signuppage)
        val secondActbutton=findViewById<MaterialButton>(R.id.signbtn)
        secondActbutton.setOnClickListener {
            val Intent = Intent(this,Dashboard::class.java)
            startActivity(Intent)
        }
    }
}