package com.haasith.procore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class PullRequestListActivity extends AppCompatActivity {

    RecyclerView prRecyclerView;
    ArrayList<OpenRequest> openPrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_request_list);
        prRecyclerView = findViewById(R.id.prList);
        openPrs = getIntent().getParcelableArrayListExtra("prList");
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        prRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        prRecyclerView.setAdapter(new PRListAdapter(this,openPrs));
    }
}
