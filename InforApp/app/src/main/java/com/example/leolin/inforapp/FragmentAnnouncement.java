package com.example.leolin.inforapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentAnnouncement extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_announcement,container,false);

        ListView listView = (ListView) view.findViewById(R.id.announce_list);
        final ArrayList<Announcement> mList = new ArrayList<Announcement>();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        mList.add(new Announcement(R.drawable.flamingo,"星期一社課",df.format(c)));
        c = Calendar.getInstance().getTime();
        mList.add(new Announcement(R.drawable.chika,"我是油宅",df.format(c)));
        c = Calendar.getInstance().getTime();
        mList.add(new Announcement(R.drawable.kato,"我是智障",df.format(c)));

        final anAdapter mAdapter = new anAdapter(mList,getActivity());
        listView.setAdapter(mAdapter);

        //TODO:check user is root or not

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                //Toast.makeText(getActivity(),"JIZZZZZZ",Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete This Announcement")
                        .setMessage("Do you reaaly want to delete this announcement?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Log.d("JIZZ",String.valueOf(i));
                                mList.remove(i);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                return true;
            }
        });

        return view;
    }
    public class anAdapter extends BaseAdapter {
        private ArrayList<Announcement> mData;
        private Context context;
        private LayoutInflater inflater;

        private class ViewHolder{
            LinearLayout llcontainer;
            ImageView publisher;
            TextView atitle,adate;
        }

        public anAdapter(ArrayList<Announcement> mData,Context context){
            this.mData = mData;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount(){
            return mData.size();
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
        public View getView(final int pos,View convertView,ViewGroup parent){
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.announcement_list_item,null);
                holder.publisher = (ImageView) convertView.findViewById(R.id.publisher);
                holder.atitle = (TextView) convertView.findViewById(R.id.announce_title);
                holder.adate = (TextView) convertView.findViewById(R.id.publish_date);
                holder.llcontainer = (LinearLayout) convertView.findViewById(R.id.announce_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.publisher.setBackgroundResource(mData.get(pos).publisher);
            holder.atitle.setText(mData.get(pos).atitle);
            holder.adate.setText(mData.get(pos).adate);
            holder.llcontainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),mData.get(pos).atitle,Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }
}
