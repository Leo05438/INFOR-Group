package com.example.leolin.inforapp;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AskQuestion extends AppCompatActivity {

    private Button btnTag;
    private Button btnPost;
    private String[] tags = {"Algorithm","Android","C#","C/C++","Html","Java","JavaScript",
            "Machine Learning","Other","Python","Unity","Web"};
    private List<Boolean> checkedList = new ArrayList<Boolean>();
    private static final int requestCode = 7122;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.acitivity_ask_question);

        for(String s : tags){
            checkedList.add(false);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnTag = (Button) findViewById(R.id.question_tag);
        btnTag.setOnClickListener(chooseTags);

        btnPost = (Button) findViewById(R.id.post_question);
        btnPost.setOnClickListener(chooseTags);

    }

    public View.OnClickListener chooseTags = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.question_tag:
                    new AlertDialog.Builder(AskQuestion.this)
                            .setTitle("Choose Your Question's Tags")
                            .setMultiChoiceItems(tags, new boolean[tags.length],
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                            checkedList.set(which, isChecked);
                                        }
                                    })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    StringBuilder sb = new StringBuilder();
                                    for(int j = 0;j < checkedList.size();++j){
                                        if(checkedList.get(j)){
                                            sb.append("#");
                                            sb.append(tags[j]);
                                            sb.append(" ");
                                        }
                                    }
                                    TextView text = (TextView) findViewById(R.id.tags);
                                    text.setText(sb.toString());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                    break;
                case R.id.post_question:
                    //TODO:Upload to Server
                    EditText askTitle = (EditText) findViewById(R.id.question_title);
                    EditText askContent = (EditText) findViewById(R.id.question_textarea);

                    if(askTitle.equals("") || askContent.equals("")){
                        Toast.makeText(AskQuestion.this,"Please fill the question fully",Toast.LENGTH_SHORT).show();
                    } else {
                        String questionTitle = askTitle.getText().toString();
                        String questionBody = askContent.getText().toString();
                        Question newQ = new Question(R.drawable.flamingo,questionTitle,questionBody);
                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putSerializable("newQuestion",newQ);
                        intent.putExtras(b);
                        setResult(requestCode,intent);
                        finish();
                    }

                    break;

            }
        }
    };

}
