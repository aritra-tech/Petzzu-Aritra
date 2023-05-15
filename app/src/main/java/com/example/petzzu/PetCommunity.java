package com.example.petzzu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PetCommunity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Toolbar mainToolBar;
    private FirebaseFirestore firestore;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_community);

        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        mainToolBar=findViewById(R.id.main_toolbar);
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PetCommunity.this));
        fab=findViewById(R.id.floatingActionButton);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Pet Community");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PetCommunity.this,AddPostActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser= firebaseAuth.getCurrentUser();
        if(currentuser==null){
            startActivity(new Intent(PetCommunity.this,MainActivity.class));
            finish();
        }else {
            String currentUserId=firebaseAuth.getCurrentUser().getUid();
            firestore.collection("usersImage").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (!task.getResult().exists()){
                            startActivity(new Intent(PetCommunity.this,SetUpActivity.class));
                            finish();
                        }
                    }
                }
            });
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