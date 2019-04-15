package com.haasith.procore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class PRListAdapter extends RecyclerView.Adapter<PRListAdapter.CustomViewHolder> {

    private ArrayList<OpenRequest> prList;
    OkHttpClient client = new OkHttpClient();
    ArrayList<PullRequest> prs = new ArrayList<>();
    ArrayList<PullRequest> negPr = new ArrayList<>();
    ArrayList<PullRequest> posPr = new ArrayList<>();
    ArrayList<String> fileChanges = new ArrayList<>();
    private Context context;

    public PRListAdapter(Context c, ArrayList<OpenRequest> prs) {
        this.prList = prs;
        this.context = c;
    }

    @Override
    public PRListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pull_request_item, null);

        PRListAdapter.CustomViewHolder viewHolder = new PRListAdapter.CustomViewHolder(view, prList.get(i));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PRListAdapter.CustomViewHolder holder, int position) {
        holder.prTitle.setText(prList.get(position).prTitle);
        holder.prNumber.setText(String.valueOf("#"+prList.get(position).prNumber));

    }


    @Override
    public int getItemCount() {
        return prList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //CustomViewHolder contain a member variables
        public TextView prTitle;
        public TextView prNumber;
        private OpenRequest op;


        // We also create a constructor that accepts the entire item row and does the id lookups
        public CustomViewHolder(View itemView, final OpenRequest op) {
            super(itemView);
            prNumber = itemView.findViewById(R.id.pullRequestNum);
            prTitle= itemView.findViewById(R.id.pullRequestTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            negPr.clear();
            posPr.clear();
            prs.clear();
            int position = getAdapterPosition();
            int pullNumber = prList.get(position).prNumber;

            System.out.println("Picked this pr number " + pullNumber);
            //doGetRequest(c,"https://api.github.com/repos/Yalantis/Koloda/pulls/422");
            //Yelp/yelp-fusion/pull/283
            Log.d("Button", "is pressed");
            doGetRequest(context,"https://api.github.com/repos/gabrielemariotti/cardslib/pulls/"+String.valueOf(pullNumber));
        }
    }
    private void doGetRequest(final Context c, String url) {
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

            System.out.println("positive lines are " +posLines);
//
            System.out.println("negative lines are " +negLines);

            pr.setCodeLines(codelines);
            neg.setCodeLines(negLines);
            pos.setCodeLines(posLines);
            prs.add(pr);
            posPr.add(pos);
            negPr.add(neg);

        }
        Intent myIntent = new Intent(context, SplitActivity.class);

        myIntent.putParcelableArrayListExtra("PRS", prs);
        myIntent.putParcelableArrayListExtra("NEG", negPr);
        myIntent.putParcelableArrayListExtra("POS", posPr);

        context.startActivity(myIntent);

    }






    void printLines(String link){

        System.out.println("in print lines" + link);
        String diffString = "";
        try {
            URL url = new URL(link);
            // Read all the text line by line
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            int i = 0;
            while ((str = in.readLine()) != null) {
             //   System.out.println("string line is " +i + " " + str);
                diffString = "";
//                if(i >= 1 && str.charAt(0) == 'd') {
//                    fileChanges.add(diffString);
//                    diffString = "";
//                }
//                else {
//                    diffString += str + "\n";
//                }
                diffString += str + "\n";
                fileChanges.add(diffString);

                i +=1;
            }

            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

       // System.out.println("this is filechanges "+fileChanges.size());
        return;
    }


}
