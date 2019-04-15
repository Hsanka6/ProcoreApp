package com.haasith.procore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class DifferenceListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<PullRequest> prs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difference_list);
        recyclerView = findViewById(R.id.recyclerView);

        System.out.println("IN Difference list activity");
        prs = (ArrayList<PullRequest>) getIntent().getSerializableExtra("PRS");

//
//        for(int i = 0; i < prs.size();i++){
//            Log.d("newFile is ", prs.get(i).newFile);
//        }

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DifferenceAdaptor(this,prs));
    }

}
