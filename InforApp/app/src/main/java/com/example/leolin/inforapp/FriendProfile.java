package com.example.leolin.inforapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendProfile extends AppCompatActivity {

    private ImageView icon;
    private TextView name,brief;
    private ListView listView;
    private ArrayList<Profile> mList = new ArrayList<Profile>();
    private Fadapter adapter;
    private Username USERNAME;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_friend_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();

        //TODO: replace with connect
        icon.setBackgroundResource(R.drawable.flamingo);
        name.setText(USERNAME.getUSERNAME());
        brief.setText("JIZZ. I LOVE INFOR.");

        mList.add(new Profile(R.drawable.ic_create_black_24dp,"Post Questions"));
        mList.add(new Profile(R.drawable.ic_check_circle_black_24dp,"Answer Questions"));
        mList.add(new Profile(R.drawable.ic_group_black_24dp,"Friends"));

        adapter = new Fadapter(FriendProfile.this,mList);
        listView.setAdapter(adapter);
    }

    public void init(){
        icon = (ImageView) findViewById(R.id.friend_icon);
        name = (TextView) findViewById(R.id.friendname);
        brief = (TextView) findViewById(R.id.friend_brief);
        listView = (ListView) findViewById(R.id.friend_status);
        USERNAME = (Username) getApplication();
    }

    public class Fadapter extends BaseAdapter{
        private ArrayList<Profile> list = new ArrayList<Profile>();
        LayoutInflater inflater;

        public Fadapter(Context context,ArrayList<Profile> list){
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        private class ViewHolder {
            ImageView friendIcon;
            TextView des;
        }

        @Override
        public int getCount(){
            return list.size();
        }
        @Override
        public Object getItem(int pos){
            return pos;
        }
        @Override
        public long getItemId(int pos){
            return pos;
        }
        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.friend_profile_list_item,parent,false);
                holder.friendIcon = (ImageView) convertView.findViewById(R.id.friend_check_icon);
                holder.des = (TextView) convertView.findViewById(R.id.friend_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.friendIcon.setBackgroundResource(mList.get(pos).getIcon());
            holder.des.setText(mList.get(pos).getItem());

            return convertView;
        }
    }
}
