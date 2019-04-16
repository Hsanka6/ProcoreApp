package com.haasith.procore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    ArrayList<OpenRequest> openRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context c = this;

        getSupportActionBar().hide();
        Button prListButton = findViewById(R.id.prListButton);
        prListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPullRequest(c,"https://api.github.com/repos/square/picasso/pulls");
            }
        });
    }

    //get open requests from github pr rest api
    private void getPullRequest(final Context c, String s) {
        Request request = new Request.Builder()
                .url(s)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        e.printStackTrace();
                        // Error and make a toast
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(c,getString(R.string.NetworkError),Toast.LENGTH_LONG);
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        try {
                            JSONArray j = new JSONArray(res);
                            for (int i = 0; i < j.length();i++){
                                JSONObject object = j.getJSONObject(i);
                                if(object.getString("state").equals("open")) {
                                    OpenRequest op = new OpenRequest(object.getInt("number"), object.getString("title"));
                                    openRequests.add(op);
                                }
                            }
                            Intent myIntent = new Intent(c, PullRequestListActivity.class);
                            myIntent.putParcelableArrayListExtra("prList", openRequests);
                            startActivity(myIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
