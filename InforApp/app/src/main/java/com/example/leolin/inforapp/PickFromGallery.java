package com.example.leolin.inforapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class PickFromGallery extends AppCompatActivity {

    private static final int RESULT_SELECT_IMAGE = 1;
    private String mImagePath;
    private File imageFile = null;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        try{
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
            startActivityForResult(chooserIntent, RESULT_SELECT_IMAGE);
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent,RESULT_SELECT_IMAGE);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RESULT_SELECT_IMAGE && data != null){
            try{
                Uri selectImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectImage,filePathColumn,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Intent returnFromGallery = new Intent();
                returnFromGallery.putExtra("PicturePath",picturePath);
                setResult(RESULT_OK,returnFromGallery);
                finish();
            } catch(Exception e) {
                e.printStackTrace();
                Intent returnFromGalleryIntent = new Intent();
                setResult(RESULT_CANCELED, returnFromGalleryIntent);
                finish();
            }
        } else {
            Intent returnFromGalleryIntent = new Intent();
            setResult(RESULT_CANCELED, returnFromGalleryIntent);
            finish();
        }
    }
}
