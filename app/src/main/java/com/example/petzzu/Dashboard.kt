package com.example.petzzu

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.button.MaterialButton

class Dashboard : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val secondActbutton=findViewById<ImageView>(R.id.petfolio)
        secondActbutton.setOnClickListener {
            val Intent = Intent(this,Petfolio::class.java)
            startActivity(Intent)
        }
    }
}