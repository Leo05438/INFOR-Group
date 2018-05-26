package com.example.leolin.inforapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QuestionBody extends AppCompatActivity {

    private ArrayList<MThread> gData = new ArrayList<MThread>();
    private ArrayList<ArrayList<Message>> iData = new ArrayList<ArrayList<Message>>();
    private QMessageExpandAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_question_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView questionTitle = (TextView) findViewById(R.id.question_title_display);
        questionTitle.setText("JIZZ?");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gData.add(new MThread(R.drawable.flamingo,"Pooh","why am I so jizz?"
                ,df.format(c),"10 times"));
        gData.add(new MThread(R.drawable.chika,"oToToT","why am I so oil?"
                ,df.format(c),"11 times"));
        gData.add(new MThread(R.drawable.kato,"Polarz","why am I so yee?"
                ,df.format(c),"7122 times"));

        iData.add(new ArrayList<Message>());
        iData.get(0).add(new Message(R.drawable.flamingo,"Pooh","JIZZZZZZ"));
        iData.get(0).add(new Message(R.drawable.chika,"oToToT","OILLLLLL"));
        iData.get(0).add(new Message(R.drawable.kato,"Polarz","YEEEEEEEE"));

        iData.add(new ArrayList<Message>());
        iData.get(1).add(new Message(R.drawable.chika,"oToToT","OILLLLLL"));
        iData.get(1).add(new Message(R.drawable.kato,"Polarz","YEEEEEEEE"));
        iData.get(1).add(new Message(R.drawable.flamingo,"Pooh","JIZZZZZZ"));

        iData.add(new ArrayList<Message>());
        iData.get(2).add(new Message(R.drawable.chika,"oToToT","OILLLLLL"));
        iData.get(2).add(new Message(R.drawable.kato,"Polarz","YEEEEEEEE"));
        iData.get(2).add(new Message(R.drawable.flamingo,"Pooh","JIZZZZZZ"));

        adapter = new QMessageExpandAdapter(gData,iData,QuestionBody.this);
        ExpandableListView exlistview = (ExpandableListView) findViewById(R.id.thread);
        exlistview.setAdapter(adapter);
    }
    public class QMessageExpandAdapter extends BaseExpandableListAdapter {

        private ArrayList<MThread> gData;
        private ArrayList<ArrayList<Message>> iData;
        private LayoutInflater inflater;
        private Context context;
        private String comment;

        private class ViewGroupHolder{
            ImageView icon;
            TextView qustionTitle,questionBody,postTimes,currentTime,name;
            Button comment;
        }

        private class ViewItemHolder {
            ImageView icon;
            TextView name,message;
        }

        private QMessageExpandAdapter(ArrayList<MThread> gData,ArrayList<ArrayList<Message>> iData,Context context){
            this.gData = gData;
            this.iData = iData;
            inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getGroupCount(){
            return gData.size();
        }

        @Override
        public int getChildrenCount(int pos){
            return iData.get(pos).size();
        }

        @Override
        public MThread getGroup(int pos){
            return gData.get(pos);
        }

        @Override
        public Message getChild(int gpos,int ipos){
            return iData.get(gpos).get(ipos);
        }

        @Override
        public long getGroupId(int pos){
            return pos;
        }

        @Override
        public long getChildId(int gpos,int ipos){
            return ipos;
        }

        @Override
        public boolean hasStableIds(){
            return false;
        }

        @Override
        public View getGroupView(final int pos, boolean isExpandable, View convertView, ViewGroup parent){

            ViewGroupHolder holder;

            if(convertView == null){
                holder = new ViewGroupHolder();
                convertView = inflater.inflate(R.layout.activity_question_body,parent,false);
                holder.icon = (ImageView) convertView.findViewById(R.id.asker_sticker);
                holder.name = (TextView) convertView.findViewById(R.id.asker_name);
                holder.questionBody = (TextView) convertView.findViewById(R.id.question_body);
                holder.currentTime = (TextView) convertView.findViewById(R.id.post_time);
                holder.postTimes = (TextView) convertView.findViewById(R.id.post_quantity);
                holder.comment= (Button) convertView.findViewById(R.id.leave_message);
                convertView.setTag(holder);
            } else {
                holder = (ViewGroupHolder) convertView.getTag();
            }

            holder.icon.setBackgroundResource(gData.get(pos).getIcon());
            holder.icon.setFocusable(false);
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(QuestionBody.this,gData.get(pos).getName(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(QuestionBody.this,FriendProfile.class);
                    startActivity(intent);
                }
            });
            holder.name.setText(gData.get(pos).getName());
            holder.name.setFocusable(false);
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(QuestionBody.this,gData.get(pos).getName(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(QuestionBody.this,FriendProfile.class);
                    startActivity(intent);
                }
            });
            holder.questionBody.setText(gData.get(pos).getQuestionBody());
            holder.currentTime.setText(gData.get(pos).getCurrentTime());
            holder.postTimes.setText(gData.get(pos).getPostTimes());
            holder.comment.setFocusable(false);
            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View item = inflater.inflate(R.layout.leave_comment_dialog,null);
                    new AlertDialog.Builder(context)
                            .setTitle("Leave Your Comment")
                            .setView(item)
                            .setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText mComment = (EditText) item.findViewById(R.id.comment);
                                    comment = mComment.getText().toString();
                                    QuestionBody.this.iData.get(pos).add(new Message(R.drawable.flamingo,"Pooh",comment));
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                }
            });

            return convertView;
        }
        @Override
        public View getChildView(int gpos,int ipos,boolean isLastChild,View convertView,ViewGroup parent){

            ViewItemHolder holder;

            if(convertView == null){
                holder = new ViewItemHolder();
                convertView = inflater.inflate(R.layout.message_exlist_item,parent,false);
                holder.icon = (ImageView) convertView.findViewById(R.id.message_leaver_sticker);
                holder.name = (TextView) convertView.findViewById(R.id.message_leaver_name);
                holder.message = (TextView) convertView.findViewById(R.id.message_body);
                convertView.setTag(holder);
            } else {
                holder = (ViewItemHolder) convertView.getTag();
            }

            holder.icon.setBackgroundResource(iData.get(gpos).get(ipos).getIcon());
            holder.name.setText(iData.get(gpos).get(ipos).getName());
            holder.message.setText(iData.get(gpos).get(ipos).getMessageBody());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int gpos,int ipos){
            return true;
        }

    }

}
