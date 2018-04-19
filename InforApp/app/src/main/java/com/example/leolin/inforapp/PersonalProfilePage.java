package com.example.leolin.inforapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalProfilePage extends AppCompatActivity {

    private String[] setting_content = {"Username","Photo","Password","Email","Setting","About"};
    private int[] icons = {R.drawable.ic_account_box_black_24dp,R.drawable.ic_insert_photo_black_24dp,
             R.drawable.ic_lock_black_24dp,R.drawable.ic_email_black_24dp
            ,R.drawable.ic_settings_black_24dp,R.drawable.ic_help_black_24dp};
    private ListView listView_setting;
    private SimpleAdapter mAdapter;
    private View item;
    private String username = "Your Current Username: ";

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

        setAdaper();
        listViewItemClick();
    }
    public void setAdaper(){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        for(int i = 0;i < 6;++i){
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
        // for 0
        if(pos == 0) item = LayoutInflater.from(PersonalProfilePage.this).inflate(R.layout.dialog_username,null);
        final TextView usernameText = (TextView) item.findViewById(R.id.dialog_text);

        switch(pos){
            case 0:
                ///
                Log.d("JIZZZZZZZZZZ",usernameText.getText().toString());
                usernameText.setText(username);
                ///
                builder.setTitle("Set Username")
                        .setView(item)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText editText = (EditText) item.findViewById(R.id.dialog_change_name);
                                if(editText.getText().toString() != "") username = "Your Current Username: " + editText.getText().toString();
                                //usernameText.setText(nUsername);

                                Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
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
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                //intent.setClass(PersonalProfilePage.this,);
                break;
            case 5:
                break;
        }
        //startActivity(intent);
    }
}
