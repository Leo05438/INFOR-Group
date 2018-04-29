package com.example.leolin.inforapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class SettingPage extends AppCompatActivity {

    private ArrayList<MGroup> gData = null;
    private ArrayList<ArrayList<MItem>> iData = null;
    private ArrayList<MItem> lData = null;
    private Context mContext;
    private ExpandableListView exlist_setting;
    private SettingExpandableListAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        setData();
        //TODO:Add functions in expandablelistview item
    }

    private void setData(){
        mContext = SettingPage.this;
        exlist_setting = (ExpandableListView) findViewById(R.id.exlistview_setting);

        gData = new ArrayList<MGroup>();
        iData = new ArrayList<ArrayList<MItem>>();

        gData.add(new MGroup("Notifications"));
        gData.add(new MGroup("Report"));
        //gData.add(new MGroup());

        //Notification
        lData = new ArrayList<MItem>();
        lData.add(new MItem("Disable notifications light"));
        lData.add(new MItem("Disable notification vibration"));
        lData.add(new MItem("Disable cue tone"));
        iData.add(lData);

        //Report
        lData = new ArrayList<MItem>();
        lData.add(new MItem("Recieve official message"));
        iData.add(lData);

        mAdapter = new SettingExpandableListAdapter(gData,iData,mContext);
        exlist_setting.setAdapter(mAdapter);

        exlist_setting.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });

    }

}
