package com.example.petzzu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PetCommunity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Toolbar mainToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_community);

        firebaseAuth=FirebaseAuth.getInstance();
        mainToolBar=findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Pet Community");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser= firebaseAuth.getCurrentUser();
        if(currentuser==null){
            startActivity(new Intent(PetCommunity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.profile_menu){
            startActivity(new Intent(PetCommunity.this,SetUpActivity.class));
        } else if (item.getItemId()==R.id.sign_out_menu) {
            firebaseAuth.signOut();
            startActivity(new Intent(PetCommunity.this,MainActivity.class));
            finish();
        }
        return true;
    }
}