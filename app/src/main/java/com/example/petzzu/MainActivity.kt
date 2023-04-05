package com.example.petzzu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val secondActbutton=findViewById<MaterialButton>(R.id.signup)
        secondActbutton.setOnClickListener {
            val Intent = Intent(this,Signuppage::class.java)
            startActivity(Intent)
        }
    }

}