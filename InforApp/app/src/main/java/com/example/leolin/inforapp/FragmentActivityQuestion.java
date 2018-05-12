package com.example.leolin.inforapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FilterReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentActivityQuestion extends Fragment {

    private LinearLayout lcontainer;
    private EditText searchBox;
    private ListView listView;

    private ArrayList<Question> mList = new ArrayList<Question>();
    private Madapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question,container,false);

        searchBox = (EditText) view.findViewById(R.id.search_question_box);
        listView = (ListView) view.findViewById(R.id.question_list);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapter.getFilter().filter(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mList.add(new Question(R.drawable.flamingo,"Why I'm so handsome","Appearance"));
        mList.add(new Question(R.drawable.chika,"Why I'm so oil","Personality"));
        mList.add(new Question(R.drawable.kato,"Why I'm so idiot","Intellect"));

        mAdapter = new Madapter(getActivity(),mList);
        listView.setAdapter(mAdapter);
        return view;
    }

    public class Madapter extends BaseAdapter implements Filterable{

        private ArrayList<Question> mOriginValue;
        private ArrayList<Question> mDisplayValue;
        LayoutInflater inflater;

        private class ViewHolder{
            LinearLayout llContainer;
            ImageView asker;
            TextView qtitle,qtag;
        }

        public Madapter(Context context,ArrayList<Question> mQuestion){
            this.mOriginValue = mQuestion;
            this.mDisplayValue = mQuestion;
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
        public View getView(final int pos,View convertView ,ViewGroup parent){
            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.questions_list_item,null);
                holder.llContainer = (LinearLayout) convertView.findViewById(R.id.lcontainer);
                holder.asker = (ImageView) convertView.findViewById(R.id.question_asker);
                holder.qtitle = (TextView) convertView.findViewById(R.id.question_title);
                holder.qtag = (TextView) convertView.findViewById(R.id.question_tags);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.asker.setBackgroundResource(mDisplayValue.get(pos).asker);
            holder.qtitle.setText(mDisplayValue.get(pos).qtitle);
            holder.qtag.setText(mDisplayValue.get(pos).qtag);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),mDisplayValue.get(pos).qtitle,Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
        @Override
        public Filter getFilter(){
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results){
                    mDisplayValue = (ArrayList<Question>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint){
                    FilterResults results = new FilterResults();
                    ArrayList<Question> FilterArrList = new ArrayList<Question>();

                    if(mOriginValue == null){
                        mOriginValue = new ArrayList<Question>(mDisplayValue);
                    }

                    if(constraint == null || constraint.length() == 0){
                        results.count = mOriginValue.size();
                        results.values = mOriginValue;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for(int i = 0;i < mOriginValue.size();++i){
                            String data = mOriginValue.get(i).qtitle;
                            if(data.toLowerCase().contains(constraint.toString())){
                                FilterArrList.add(new Question(mOriginValue.get(i).asker
                                        ,mOriginValue.get(i).qtitle,mOriginValue.get(i).qtag));
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
