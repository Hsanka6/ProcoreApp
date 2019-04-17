package com.haasith.procore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplitActivity extends AppCompatActivity {
    RecyclerView negRecylerView;
    RecyclerView posRecylerView;

    ArrayList<PullRequest> prs = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();
    ArrayList<PullRequest> negPr = new ArrayList<>();
    ArrayList<PullRequest> posPr = new ArrayList<>();
    ArrayList<String> fileChanges = new ArrayList<>();

    ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading...");
        nDialog.setTitle("Getting Github Data");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();

        this.setTitle("Split View");
        posRecylerView = findViewById(R.id.pos);
        negRecylerView = findViewById(R.id.neg);
        getDiffFileRequest(this, "https://api.github.com/repos/square/picasso/pulls/"+String.valueOf(getIntent().getExtras().getInt("PullRequestNum")));
    }

    //sets up recyclerview on the main thread
    private void setUpRecyclerView(final Context c, final ArrayList<PullRequest> posPr, final ArrayList<PullRequest> negPr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nDialog.dismiss();
                negRecylerView.setLayoutManager(new LinearLayoutManager(c));
                negRecylerView.setAdapter(new DifferenceAdaptor(c, negPr));

                posRecylerView.setLayoutManager(new LinearLayoutManager(c));
                posRecylerView.setAdapter(new DifferenceAdaptor(c, posPr));

            }
        });
    }
    // create an support action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.split_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //passes unison pr list to differencelistactivity
        if (id == R.id.unisonViewButton) {
            System.out.println("Hit unison button");
            Intent myIntent = new Intent(this, DifferenceListActivity.class);

            myIntent.putParcelableArrayListExtra("PRS", prs);

            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    //get diff file from restful api request response
    private void getDiffFileRequest(final Context c, String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        try {
                            JSONObject j = new JSONObject(res);
                            System.out.println(j.toString());
                            printLines(j.getString("diff_url"));
                            processChanges1();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void processChanges1() {
        System.out.println("in processChanges");
        for(int i = 0; i < fileChanges.size();i++){

            String[] lines = fileChanges.get(i).split("\n");
            PullRequest pr = new PullRequest();
            PullRequest neg = new PullRequest();
            PullRequest pos = new PullRequest();

            System.out.println(lines.length + " is length");
            String codelines = "";
            String negLines = "";
            String posLines = "";

            for(int j =0; j < lines.length;j++){
                String root = lines[j].substring(0,1);

                if(lines[j].length() == 1){
                    root = " ";
                }
                /// System.out.println("root is " + root  + " " + root.equals("+ ")  +" "+ root.equals("- ") );
                switch(root){
                    case"+":
                        if (lines[j].charAt(1) == '+') {
                            pr.setNewFile(lines[j]);
                            pos.setNewFile(lines[j]);
                            neg.setNewFile(lines[j]);
                        }
                        else{
                            codelines += lines[j] + "\n";
                            posLines += lines[j] + "\n";
                        }
                        break;
                    case"-":
                        if (lines[j].charAt(1) == '-') {
                            pr.setNewFile(lines[j]);
                            pos.setNewFile(lines[j]);
                            neg.setNewFile(lines[j]);
                        }
                        else{
                            codelines += lines[j] + "\n";
                            negLines += lines[j] + "\n";
                        }
                        break;
                    case"d":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"i":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"@@":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    default:
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                }
            }
            pr.setCodeLines(codelines);
            neg.setCodeLines(negLines);
            pos.setCodeLines(posLines);
            prs.add(pr);
            posPr.add(pos);
            negPr.add(neg);

        }
        setUpRecyclerView(this, posPr, negPr);
    }

    //concatenates all lines of the diff file
    void printLines(String link){
        String diffString = "";
        try {
            URL url = new URL(link);
            // Read all the text line by line
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                diffString = "";

                diffString += str + "\n";
                fileChanges.add(diffString);
            }

            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }



}
