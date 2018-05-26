package com.example.leolin.inforapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalProfilePage extends AppCompatActivity {

    private String[] setting_content = {"Brief","Photo","Password","Setting","About"};
    private int[] icons = {R.drawable.ic_account_box_black_24dp,R.drawable.ic_insert_photo_black_24dp,
             R.drawable.ic_lock_black_24dp, R.drawable.ic_settings_black_24dp,R.drawable.ic_help_black_24dp};
    private ListView listView_setting;
    private SimpleAdapter mAdapter;
    private View item;
    private String userbrief = "Your Current Brief: ";
    private String password = "jizz7122";
    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private ImageView image;
    private OkHttpClient client;
    private Username USERNAME;
    private View mPasswordChange;
    private View mProgress;

    public static final int PICK_IMAGE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        image = (ImageView) findViewById(R.id.setting_photo_sticker);

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        USERNAME = (Username) getApplication();
        password = USERNAME.getPASSWORD();

        setAdaper();
        listViewItemClick();
    }
    public void setAdaper(){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        for(int i = 0;i < 5;++i){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("icons",icons[i]);
            map.put("content",setting_content[i]);
            list.add(map);
        }

        mAdapter = new SimpleAdapter(PersonalProfilePage.this,list,
                R.layout.personal_profile_item_list,new String[]{"icons","content"},
                new int[]{R.id.setting_icon,R.id.setting_content});
        listView_setting = (ListView) findViewById(R.id.list_profile);
        listView_setting.setAdapter(mAdapter);
    }
    public void listViewItemClick(){
        //l.setClickable(true);
        listView_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("before","jizzzzzzz");
                onItemChosen(i);
            }
        });
    }

    public void onItemChosen(int pos){
        Log.d("jizz",String.valueOf(pos));
        Intent intent = new Intent();
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalProfilePage.this);

        switch(pos){
            case 0:
                //Username
                ///
                item = LayoutInflater.from(PersonalProfilePage.this).inflate(R.layout.dialog_brief,null);
                TextView briefText = (TextView) item.findViewById(R.id.dialog_text);
                Log.d("JIZZZZZZZZZZ",briefText.getText().toString());
                briefText.setText(userbrief);
                ///
                builder.setTitle("Set Brief")
                        .setView(item)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText editText = (EditText) item.findViewById(R.id.dialog_change_name);
                                if(editText.getText().toString() != "") userbrief = "Your Current Brief: " + editText.getText().toString();
                                //usernameText.setText(nUsername);
                                RequestBody formBody = new FormBody.Builder()
//                                        .add("User")
//                                        .add("Brief")
                                        .build();
                                Request request = new Request.Builder()
                                        .url("http://")
                                        .post(formBody)
                                        .build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        });
                                    }
                                });

                                Toast.makeText(getApplicationContext(),userbrief,Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

                break;
            case 1:
                //Photo
//                Intent mIntent = new Intent();
//                mIntent.setType("image/*");
//                mIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(mIntent, "Select Picture"), PICK_IMAGE);

//                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                getIntent.setType("image/*");
//
//                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                pickIntent.setType("image/*");
//
//                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
//                startActivityForResult(chooserIntent, PICK_IMAGE);
                final Intent gallery_Intent = new Intent(getApplicationContext(), PickFromGallery.class);
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
                break;
            case 2:
                //Password
                item = LayoutInflater.from(PersonalProfilePage.this).inflate(R.layout.dialog_password_reset,null);

                final AlertDialog dialogPw = new AlertDialog.Builder(PersonalProfilePage.this)
                        .setTitle("Update Your Password")
                        .setView(item)
                        .setPositiveButton("Ok",null)
                        .setNegativeButton("Cancel",null)
                        .create();
                dialogPw.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button button = ((AlertDialog) dialogPw).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("Enter Password","Check");
                                EditText newPassword = (EditText) item.findViewById(R.id.new_password);
                                EditText verifyPassword = (EditText) item.findViewById(R.id.verify_new_password);
                                EditText currentPassword = (EditText) item.findViewById(R.id.current_password);
                                final String nPass = newPassword.getText().toString();
                                String vPass = verifyPassword.getText().toString();
                                String cPass = currentPassword.getText().toString();

                                Log.d("Enter Password",nPass);
                                Log.d("Enter Password",vPass);
                                Log.d("Enter Password",cPass);
                                Log.d("Enter Password",password);

                                if(nPass.equals("") || vPass.equals("") || cPass.equals("")){
                                    Toast.makeText(getApplicationContext(),"No Enter!",Toast.LENGTH_SHORT).show();
                                } else {
                                    if(!nPass.equals(vPass)){
                                        verifyPassword.requestFocus();
                                        Toast.makeText(getApplicationContext(),"Verify Password is Wrong",Toast.LENGTH_SHORT).show();
                                    } else if(password.equals(nPass)){
                                        newPassword.requestFocus();
                                        Toast.makeText(getApplicationContext(),"New Password and Current One Are the Same",Toast.LENGTH_SHORT).show();
                                    } else if(!cPass.equals(password)){
                                        currentPassword.requestFocus();
                                        Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                                    } else {
                                        RequestBody formBody = new FormBody.Builder()
                                                .add("user",USERNAME.getUSERNAME())
                                                .add("oPasswd",USERNAME.getPASSWORD())
                                                .add("nPasswd",nPass)
                                                .build();
                                        final Request request = new Request.Builder()
                                                .url("http://group.infor.org/android/changePasswd")
                                                .post(formBody)
                                                .build();
                                        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(PersonalProfilePage.this,"Changed Failed.",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                final String result = response.body().string();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        resetPassword(result,nPass);
                                                        //{"changed" : true/false
                                                        // "errorType": 0 -> password error
                                                        //  1 -> no this user
                                                        // }
                                                    }
                                                });
                                            }
                                        });
                                        //Toast.makeText(getApplicationContext(),"Update Successfully",Toast.LENGTH_SHORT).show();
                                        dialogPw.dismiss();
                                    }
                                }
                            }
                        });
                    }
                });
                dialogPw.show();
//                builder.setTitle("Update Your Password")
//                        .setView(item)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                EditText newPassword = (EditText) item.findViewById(R.id.new_password);
//                                EditText verifyPassword = (EditText) item.findViewById(R.id.verify_new_password);
//                                EditText currentPassword = (EditText) item.findViewById(R.id.current_password);
//                                String nPass = newPassword.getText().toString();
//                                String vPass = verifyPassword.getText().toString();
//                                String cPass = currentPassword.getText().toString();
//
//                                if(nPass == "" || vPass == "" || cPass == ""){
//                                    Toast.makeText(getApplicationContext(),"No Enter!",Toast.LENGTH_SHORT).show();
//                                } else {
//                                    if(cPass != password){
//                                        Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
//                                    } else if(password == nPass){
//                                        Toast.makeText(getApplicationContext(),"New Password and Current One Are the Same",Toast.LENGTH_SHORT).show();
//                                    } else if(nPass != vPass){
//                                        Toast.makeText(getApplicationContext(),"Verify Password is Wrong",Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        password = nPass;
//                                    }
//                                }
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).show();
                break;
            case 3:
                intent.setClass(PersonalProfilePage.this,SettingPage.class);
                startActivity(intent);
                break;
            case 4:
                builder.setTitle("About")
                        .setMessage("It's an App developed by an INFOR member.")
                        .show();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
//        if(requestCode == PICK_IMAGE && data != null){
//            try{
//                Uri imageUri = data.getData();
//                InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                ImageView imageView = (ImageView) findViewById(R.id.setting_photo_sticker);
//
//                int pWidth = selectedImage.getWidth();
//                int pHeight = selectedImage.getHeight();
//                Log.d("ImageWidth",String.valueOf(pWidth));
//                Log.d("ImageHeight",String.valueOf(pHeight));
//
//                LinearLayout layout = (LinearLayout) findViewById(R.id.activity_personal_profile);
//                layoutWidth = layout.getWidth();
//                layoutHeight = layout.getHeight();
//                Log.d("LayoutWidth",String.valueOf(layoutWidth));
//                Log.d("LayoutHeight",String.valueOf(layoutHeight));
//
//                while(pWidth >= layoutWidth || pHeight >= layoutHeight){
//                    pHeight = pHeight / 2;
//                    pWidth = pWidth / 2;
//                }
//                Log.d("ScaledImageWidth",String.valueOf(pWidth));
//                Log.d("ScaledImageHeight",String.valueOf(pHeight));
//
//                Bitmap scalemap = Bitmap.createScaledBitmap(selectedImage,pWidth,pHeight,false);
//
//
//                /*ViewTreeObserver vto = layout.getViewTreeObserver();
//                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
//                            this.layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                        } else {
//                            this.layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        }
//                    }
//                    layoutWidth  = layout.getMeasuredWidth();
//                    layoutHeight = layout.getMeasuredHeight();
//                });*/
//
//                imageView.setImageBitmap(scalemap);
//            } catch (FileNotFoundException e){
//                e.printStackTrace();
//            }
//        }
        if(requestCode == GALLERY_ACTIVITY_CODE && data != null && resultCode == Activity.RESULT_OK){
            String imagePath = data.getStringExtra("PicturePath");
            performCrop(imagePath);
        } else if(requestCode == RESULT_CROP && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = extras.getParcelable("data");

            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageBitmap(bitmap);
        }
    }

    public void performCrop(String picUri){
        try{
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri,"image/*");
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("aspectX",1);
            cropIntent.putExtra("aspectY",1);
            cropIntent.putExtra("outputX",500);
            cropIntent.putExtra("outputY",500);
            cropIntent.putExtra("return-data",true);

            startActivityForResult(cropIntent,RESULT_CROP);
        } catch(ActivityNotFoundException anfe) {
            anfe.printStackTrace();
        }
    }
    private void resetPassword(String response,String nPass){
        if(!response.equals("")){
            StringBuilder all = new StringBuilder();
            for(int i = 1;i < response.length();++i){
                if(response.charAt(i - 1) == ':'){
                    for(int j = i;response.charAt(j) != ',' && response.charAt(j) != '}';++j){
                        all.append(response.charAt(j));
                    }
                    all.append(" ");
                }
            }
            final String[] params = all.toString().split(" ");
            if(params[0].equals("false")){
                if(params[1].equals("0")){
                    Toast.makeText(PersonalProfilePage.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                } else if(params[1].equals("1")){
                    Toast.makeText(PersonalProfilePage.this,"User doesn't exist",Toast.LENGTH_SHORT).show();
                }
            } else {
                USERNAME.setPASSWORD(nPass);
                //Toast.makeText(PersonalProfilePage.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                Toast.makeText(PersonalProfilePage.this,USERNAME.getUSERNAME() + " " + USERNAME.getPASSWORD(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}