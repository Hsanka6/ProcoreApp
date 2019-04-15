package com.haasith.procore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class SplitActivity extends AppCompatActivity {


    RecyclerView negRecylerView;
    RecyclerView posRecylerView;

    ArrayList<PullRequest> neg = new ArrayList<>();
    ArrayList<PullRequest> prs = new ArrayList<>();

    ArrayList<PullRequest> pos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        posRecylerView = findViewById(R.id.pos);
        negRecylerView = findViewById(R.id.neg);
        neg = getIntent().getParcelableArrayListExtra("NEG");
        pos = getIntent().getParcelableArrayListExtra("POS");
        prs = getIntent().getParcelableArrayListExtra("PRS");
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        negRecylerView.setLayoutManager(new LinearLayoutManager(this));
        negRecylerView.setAdapter(new DifferenceAdaptor(this,neg));

        posRecylerView.setLayoutManager(new LinearLayoutManager(this));
        posRecylerView.setAdapter(new DifferenceAdaptor(this,pos));
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.split_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.unisonViewButton) {
            System.out.println("Hit unison button");
            Intent myIntent = new Intent(this, DifferenceListActivity.class);

            myIntent.putParcelableArrayListExtra("PRS", prs);

            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
