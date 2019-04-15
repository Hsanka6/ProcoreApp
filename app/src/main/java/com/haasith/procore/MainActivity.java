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
    ArrayList<PullRequest> prs = new ArrayList<>();
    ArrayList<PullRequest> negPr = new ArrayList<>();
    ArrayList<PullRequest> posPr = new ArrayList<>();
    ArrayList<String> fileChanges = new ArrayList<>();

    ArrayList<OpenRequest> openRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context c = this;
        Button splitButton = findViewById(R.id.splitButton);

        Button unisonButton = findViewById(R.id.unisonButton);

        Button prListButton = findViewById(R.id.prListButton);


        splitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //doGetRequest(c,"https://api.github.com/repos/Yalantis/Koloda/pulls/422");
                    //Yelp/yelp-fusion/pull/283
                    Log.d("Button", "is pressed");
                    doGetRequest(c,"https://api.github.com/repos/gabrielemariotti/cardslib/pulls/567", "split");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        unisonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    doGetRequest(c,"https://api.github.com/repos/gabrielemariotti/cardslib/pulls/567", "unison");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        prListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPullRequest(c,"https://api.github.com/repos/gabrielemariotti/cardslib/pulls");

            }
        });



    }

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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }


    private void doGetRequest(final Context c, String url, final String type) throws IOException{
        Request request = new Request.Builder()
                .url(url)
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
                            JSONObject j = new JSONObject(res);
                            System.out.println(j.toString());
                            printLines(j.getString("diff_url"));
                            processChanges1(type);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void processChanges() {

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
                String root = lines[j].substring(0,2);

               /// System.out.println("root is " + root  + " " + root.equals("+ ")  +" "+ root.equals("- ") );
                switch(root){
                    case"++":
                        pr.setNewFile(lines[j]);
                        pos.setNewFile(lines[j]);
                        neg.setNewFile(lines[j]);
                        break;
                    case"--":
                        pr.setOldFile(lines[j]);
                        pos.setOldFile(lines[j]);
                        neg.setOldFile(lines[j]);
                        break;
                    case"+ ":
                        System.out.println("root equal " + root +" "+lines[j]);
                        codelines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"+\t":
                        codelines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"- ":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        break;
                    case"-\t":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        break;
                    case"  ":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"di":
                        break;
                    case"in":
                        break;
                    case"@@":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    default:
                        System.out.println("root equal default pos check" + root.equals("+ ") +" neg check "+root.equals("- ") +" "+ "root is actually " + root.equals("+\t") +" is line "+lines[j] + " root is actually" + root + "this is the root");

                        codelines += lines[j] + "\n";
                        if(!root.equals("+ ")) {
                            negLines += lines[j] + "\n";
                        }
                        if(!root.equals("- ")) {
                            posLines += lines[j] + "\n";
                        }
                        break;
                }
            }

            System.out.println("posiitve lines are " +posLines);

            System.out.println("negative lines are " +negLines);

            pr.setCodeLines(codelines);
            neg.setCodeLines(negLines);
            pos.setCodeLines(posLines);
            prs.add(pr);
            posPr.add(pos);
            negPr.add(neg);

        }


        Intent myIntent = new Intent(this, SplitActivity.class);

        myIntent.putParcelableArrayListExtra("PRS", prs);
        myIntent.putParcelableArrayListExtra("NEG", negPr);
        myIntent.putParcelableArrayListExtra("POS", posPr);

        startActivity(myIntent);

    }



    private void processChanges1(String type) {

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
                    case"+ ":
                        System.out.println("root equal " + root +" "+lines[j]);
                        codelines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"+\t":
                        codelines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"- ":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        break;
                    case"-\t":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        break;
                    case"  ":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    case"d":
                        break;
                    case"i":
                        break;
                    case"@@":
                        codelines += lines[j] + "\n";
                        negLines += lines[j] + "\n";
                        posLines += lines[j] + "\n";
                        break;
                    default:
                       // System.out.println("root equal default pos check" + root.equals("+ ") +" neg check "+root.equals("- ") +" "+ "root is actually " + root.equals("+\t") +" is line "+lines[j] + " root is actually" + root + "this is the root");

                        codelines += lines[j] + "\n";
                        if(!root.equals("+ ")) {
                            negLines += lines[j] + "\n";
                        }
                        if(!root.equals("- ")) {
                            posLines += lines[j] + "\n";
                        }
                        break;
                }
            }

            System.out.println("posiitve lines are " +posLines);

            System.out.println("negative lines are " +negLines);

            pr.setCodeLines(codelines);
            neg.setCodeLines(negLines);
            pos.setCodeLines(posLines);
            prs.add(pr);
            posPr.add(pos);
            negPr.add(neg);

        }


        Intent myIntent = new Intent(this, SplitActivity.class);

        myIntent.putParcelableArrayListExtra("PRS", prs);
        myIntent.putParcelableArrayListExtra("NEG", negPr);
        myIntent.putParcelableArrayListExtra("POS", posPr);

        startActivity(myIntent);

    }






    void printLines(String link){
        String diffString = "";
        try {
            URL url = new URL(link);
             // Read all the text line by line
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            int i = 0;
            while ((str = in.readLine()) != null) {



                if(i >= 1 && str.charAt(0) == 'd') {
                    fileChanges.add(diffString);
                    diffString = "";
                }
                else {
                    diffString += str + "\n";
                }
                i +=1;
            }

//            for(int a = 0; a < fileChanges.size();a++){
//                System.out.println(a +" is "+ fileChanges.get(a));
//            }

            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return;
    }








}
