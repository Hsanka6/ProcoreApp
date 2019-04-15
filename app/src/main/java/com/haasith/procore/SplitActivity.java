package com.haasith.procore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class SplitActivity extends AppCompatActivity {


    RecyclerView negRecylerView;
    RecyclerView posRecylerView;
    ArrayList<PullRequest> neg = new ArrayList<>();

    ArrayList<PullRequest> pos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        posRecylerView = findViewById(R.id.pos);
        negRecylerView = findViewById(R.id.neg);



        neg = getIntent().getParcelableArrayListExtra("NEG");
        pos = getIntent().getParcelableArrayListExtra("POS");


        setUpRecyclerView();



    }

    private void setUpRecyclerView() {

        negRecylerView.setLayoutManager(new LinearLayoutManager(this));
        negRecylerView.setAdapter(new DifferenceAdaptor(this,neg));

        posRecylerView.setLayoutManager(new LinearLayoutManager(this));
        posRecylerView.setAdapter(new DifferenceAdaptor(this,pos));
    }
}
