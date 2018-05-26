package com.example.leolin.inforapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private Username USERNAME;
    private static final String TAG = "Preference";
    public static final String prefAccount = "User";
    public static final String LOGINED = "Login";
    private TextView name;
    private ImageView icon;
    private View hView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this,AskQuestion.class);
                startActivity(intent);
            }
        });
        USERNAME = (Username) getApplication();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        hView = navigationView.getHeaderView(0);
        name = (TextView) hView.findViewById(R.id.username_navigation);
        name.setText(USERNAME.getUSERNAME());
        icon = (ImageView) hView.findViewById(R.id.user_icon);
        icon.setImageResource(R.drawable.flamingo);
        LinearLayout backgroundImage = (LinearLayout) hView.findViewById(R.id.background_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backgroundImage.setBackground(getResources().getDrawable(R.drawable.close));
            //drawable = res.getDrawable(R.drawable.blue, getTheme());
            //mLayout.setBackground(drawable);
        } else {
            backgroundImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.close));
            //drawable = res.getDrawable(R.drawable.blue);
            //mLayout.setBackgroundDrawable(drawable);
        }

        initData();

        Fragment f = new FragmentActivityQuestion();
        getFragmentManager().beginTransaction().replace(R.id.content_menu,f).commit();

        setProfileImageClickable();
        //setQuestionList();

        USERNAME = (Username)getApplication();

        Log.d("MenuActivity onCreate","Again");
    }

    public void initData(){
//        name = (TextView) hView.findViewById(R.id.username_navigation);
//        name.setText(USERNAME.getUSERNAME());
//        icon = (ImageView) hView.findViewById(R.id.user_icon);
//        icon.setBackgroundResource(R.drawable.flamingo);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        switch(id){
            case R.id.nav_question:
                    fab.show();
                    Fragment fQuestion = new FragmentActivityQuestion();
                    getFragmentManager().beginTransaction().replace(R.id.content_menu,fQuestion).commit();

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp, MenuActivity.this.getTheme()));
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                    }

                break;
            case R.id.nav_friend:

                fab.hide();
                Fragment fFriend = new FragmentFriend();
                getFragmentManager().beginTransaction().replace(R.id.content_menu,fFriend).commit();

                break;
            case R.id.nav_announce:

                fab.hide();
                Fragment fAnnounce = new FragmentAnnouncement();
                getFragmentManager().beginTransaction().replace(R.id.content_menu,fAnnounce).commit();

                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_logout:
                SharedPreferences setting = getSharedPreferences(TAG,0);
                setting.edit()
                        .putString(prefAccount,USERNAME.getUSERNAME())
                        .putBoolean(LOGINED,false)
                        .apply();
                USERNAME.setUSERNAME("");
                USERNAME.setPASSWORD("");
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this,LoginPage.class);
                startActivity(intent);
                finish();
                //TODO:Logout the user
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void setProfileImageClickable(){
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        View header = nav.getHeaderView(0);
        ImageView img = (ImageView) header.findViewById(R.id.user_icon);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("MenuActivity","jizz");
                //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                //drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this,PersonalProfilePage.class);
                startActivity(intent);
            }
        });
    }
}
