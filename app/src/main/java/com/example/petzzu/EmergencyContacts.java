package com.example.petzzu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.petzzu.databinding.ActivityEmergencyContactsBinding;

public class EmergencyContacts extends AppCompatActivity {
    private ActivityEmergencyContactsBinding binding;
    private static final String TAG="CONTACT_TAG";
    private static final int WRITE_CONTACT_PERMISSION_CODE=100;
    private static final int IMAGE_PICK_GALLERY_CODE=200;
    private String[] contactPermissions;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEmergencyContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.thumbnailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });
        
        binding.favSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
    }

    private void saveContact() {
        String firstName=binding.firstNameEt.getText().toString().trim();
        String lastName=binding.lastNameEt.getText().toString().trim();
        String phoneMobile=binding.mobilephoneEt.getText().toString().trim();
        String phoneHome=binding.phoneHomeEt.getText().toString().trim();
        String email=binding.emailEt.getText().toString().trim();
        String address=binding.addressEt.getText().toString().trim();

    }

    private void openGalleryIntent() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode==IMAGE_PICK_GALLERY_CODE){
                image_uri=data.getData();
                binding.thumbnailIv.setImageURI(image_uri);
            }
            else {
                Toast.makeText(this, "Cancelled!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}