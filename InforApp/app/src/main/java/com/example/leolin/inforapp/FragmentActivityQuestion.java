package com.example.leolin.inforapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    private boolean[] itemcheck;

    private LinearLayout lcontainer;
    private EditText searchBox;
    private ListView listView;
    private Button tagSearch;
    private SwipeRefreshLayout mRefresh;

    private ArrayList<String> mTags = new ArrayList<String>();
    private ArrayList<Question> mList = new ArrayList<Question>();
    private Madapter mAdapter;
    private String[] body = {"JIZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ","OILLLLLLLLLLLLLLLL","JIZZ"};
    private Username USERNAME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question,container,false);

        mTags.add("Java");
        mTags.add("C++");
        mTags.add("Html");
        mTags.add("JavaScript");
        mTags.add("Python");
        itemcheck = new boolean[mTags.size()];

        searchBox = (EditText) view.findViewById(R.id.search_question_box);
        listView = (ListView) view.findViewById(R.id.question_list);
        tagSearch = (Button) view.findViewById(R.id.set_filter);
        tagSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Choose Tags to Search Questions")
                        .setMultiChoiceItems(mTags.toArray(new String[mTags.size()]),itemcheck,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                                    }
                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                StringBuilder sb = new StringBuilder();
                                boolean empty = true;
                                for(int i = 0;i < itemcheck.length;++i){
                                    if(itemcheck[i]){
                                        sb.append(mTags.get(i));
                                        sb.append(" ");
                                        empty = false;
                                    }
                                }
                                if(!empty){
                                    Toast.makeText(getActivity(),sb.toString(),Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(),"Nothing",Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(false);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("BeforeTextChanged","IN");
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapter.getFilter().filter(charSequence.toString());
                Log.d("onTextChanged","IN");
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("AfterTextChanged","IN");
            }
        });
        mList.add(new Question(R.drawable.flamingo,"Why I'm so handsome","Appearance","Pooh"));
        mList.add(new Question(R.drawable.chika,"Why I'm so oil","Personality","oToToT"));
        mList.add(new Question(R.drawable.kato,"Why I'm so idiot","Intellect","Polarz"));

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
            String username;
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
                convertView = inflater.inflate(R.layout.questions_list_item,parent,false);
                holder.llContainer = (LinearLayout) convertView.findViewById(R.id.lcontainer);
                holder.asker = (ImageView) convertView.findViewById(R.id.question_asker);
                holder.qtitle = (TextView) convertView.findViewById(R.id.question_title);
                holder.qtag = (TextView) convertView.findViewById(R.id.question_tags);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.asker.setBackgroundResource(mDisplayValue.get(pos).getAsker());
            holder.asker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),mDisplayValue.get(pos).getUsername(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),FriendProfile.class);
                    startActivity(intent);
                }
            });
            holder.username = mDisplayValue.get(pos).getUsername();
            holder.qtitle.setText(mDisplayValue.get(pos).getQtitle());
            holder.qtag.setText(mDisplayValue.get(pos).getQtag());
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),mDisplayValue.get(pos).getQtitle(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),QuestionBody.class);
                    startActivity(intent);

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
                            String data = mOriginValue.get(i).getQtitle();
                            if(data.toLowerCase().contains(constraint.toString())){
                                FilterArrList.add(new Question(mOriginValue.get(i).getAsker()
                                        ,mOriginValue.get(i).getQtitle(),mOriginValue.get(i).getQtag()
                                        ,mOriginValue.get(i).getUsername()));
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
