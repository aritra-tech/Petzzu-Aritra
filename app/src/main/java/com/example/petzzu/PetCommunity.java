package com.example.petzzu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.petzzu.Adapter.PostAdapter;
import com.example.petzzu.Model.Post;
import com.example.petzzu.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PetCommunity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Toolbar mainToolBar;
    private FirebaseFirestore firestore;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private PostAdapter adapter;
    private List<Post> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private List<Users> usersList;
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
        list=new ArrayList<>();
        usersList=new ArrayList<>();
        adapter=new PostAdapter(PetCommunity.this,list,usersList);
        mRecyclerView.setAdapter(adapter);
        fab=findViewById(R.id.floatingActionButton);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Pet Community");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PetCommunity.this,AddPostActivity.class));
            }
        });

        if (firebaseAuth.getCurrentUser()!=null){
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom=!mRecyclerView.canScrollVertically(1);
                    if (isBottom)
                        Toast.makeText(PetCommunity.this, "Reached the bottom", Toast.LENGTH_SHORT).show();
                }
            });
            query=firestore.collection("Posts").orderBy("time",Query.Direction.DESCENDING);
            listenerRegistration=query.addSnapshotListener(PetCommunity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange doc:value.getDocumentChanges()){
                        if (doc.getType()==DocumentChange.Type.ADDED){
                            String postId=doc.getDocument().getId();
                            Post post=doc.getDocument().toObject(Post.class).withId(postId);
                            String postUserId=doc.getDocument().getString("user");
                            firestore.collection("usersImage").document(postUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Users users=task.getResult().toObject(Users.class);
                                        usersList.add(users);
                                        list.add(post);
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(PetCommunity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            });
        }
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