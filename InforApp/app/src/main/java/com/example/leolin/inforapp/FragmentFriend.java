package com.example.leolin.inforapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.WatchEvent;
import java.util.ArrayList;

public class FragmentFriend extends Fragment {

    private EditText searchBox;
    private ListView listView;

    private ArrayList<Friend> mList = new ArrayList<Friend>();
    private fAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend,container,false);

        searchBox = (EditText) view.findViewById(R.id.search_friend);
        listView = (ListView) view.findViewById(R.id.friend_list);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mList.add(new Friend(R.drawable.flamingo,"Pooh"));
        mList.add(new Friend(R.drawable.chika,"oToToT"));
        mList.add(new Friend(R.drawable.kato,"Polarz"));

        adapter = new fAdapter(getActivity(),mList);
        listView.setAdapter(adapter);

        return view;
    }

    public class fAdapter extends BaseAdapter implements Filterable{
        private ArrayList<Friend> mOriginValue;
        private ArrayList<Friend> mDisplayValue;
        LayoutInflater inflater;

        private class ViewHolder{
            LinearLayout fcontainer;
            ImageView icon;
            TextView name;
        }

        public fAdapter(Context context,ArrayList<Friend> mFriend){
            this.mOriginValue = mFriend;
            this.mDisplayValue = mFriend;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount(){
            return mDisplayValue.size();
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
                convertView = inflater.inflate(R.layout.friend_list_item,parent,false);
                holder.fcontainer = (LinearLayout) convertView.findViewById(R.id.fcontainer);
                holder.icon = (ImageView) convertView.findViewById(R.id.friend_icon);
                holder.name = (TextView) convertView.findViewById(R.id.friend_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setBackgroundResource(mDisplayValue.get(pos).getIcon());
            holder.name.setText(mDisplayValue.get(pos).getName());
            holder.fcontainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),mDisplayValue.get(pos).getName(),Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
        @Override
        public Filter getFilter(){
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mDisplayValue = (ArrayList<Friend>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<Friend> FilterArrList = new ArrayList<Friend>();

                    if(mOriginValue == null){
                        mOriginValue = new ArrayList<Friend>(mDisplayValue);
                    }
                    if(constraint == null || constraint.length() == 0){
                        results.count = mOriginValue.size();
                        results.values = mOriginValue;
                    } else {
                        constraint =  constraint.toString().toLowerCase();

                        for(int i = 0;i < mOriginValue.size();++i){
                            String data = mOriginValue.get(i).getName();
                            if(data.toLowerCase().contains(constraint.toString())){
                                FilterArrList.add(new Friend(mOriginValue.get(i).getIcon(),
                                        mOriginValue.get(i).getName()));
                            }
                            results.count = FilterArrList.size();
                            results.values = FilterArrList;
                        }
                    }
                    return results;
                }
            };
            return filter;
        }
    }
}
