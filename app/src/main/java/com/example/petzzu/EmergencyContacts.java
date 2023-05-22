package com.example.petzzu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.petzzu.databinding.ActivityEmergencyContactsBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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

        contactPermissions=new String[]{Manifest.permission.WRITE_CONTACTS};

        binding.thumbnailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });
        
        binding.favSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isWriteContactPermisionEnabled()){
                    saveContact();
                }
                else {
                    requestWriteContactPermission();
                }
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

        ArrayList<ContentProviderOperation> cpo=new ArrayList<>();
        int rawContentId= cpo.size();
        cpo.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null)
                .build());
        cpo.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                        .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContentId)
                        .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,firstName)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,lastName)
                .build());

        cpo.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContentId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phoneMobile)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        cpo.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContentId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phoneHome)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        cpo.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContentId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA,email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());
        cpo.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContentId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA,address)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE,ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        byte[] imageBytes=imageUriToBytes();
        if (imageBytes!=null){
            cpo.add(ContentProviderOperation.newInsert(
                            ContactsContract.RawContacts.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContentId)
                    .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,imageBytes)
                    .build());
        }
        else {

        }
        try {
            ContentProviderResult[] results=getContentResolver().applyBatch(ContactsContract.AUTHORITY,cpo);
            Log.d(TAG,"saveContact: Saved..");
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"saveContact: "+e.getMessage());
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private byte[] imageUriToBytes() {
        Bitmap bitmap;
        ByteArrayOutputStream baos=null;
        try {
            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),image_uri);
            baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
            return baos.toByteArray();
        }
        catch (Exception e){
            Log.d(TAG,"imageUriToBytes:" +e.getMessage());
            return null;
        }

    }

    private void openGalleryIntent() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private boolean isWriteContactPermisionEnabled(){
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestWriteContactPermission(){
        ActivityCompat.requestPermissions(this,contactPermissions,WRITE_CONTACT_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0){
            if (requestCode==WRITE_CONTACT_PERMISSION_CODE){
                boolean haveWriteContactPermission=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if (haveWriteContactPermission){
                    saveContact();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
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