package com.haasith.procore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PRListAdapter extends RecyclerView.Adapter<PRListAdapter.CustomViewHolder> {

    private ArrayList<OpenRequest> prList;

    private Context context;

    public PRListAdapter(Context c, ArrayList<OpenRequest> prs) {
        this.prList = prs;
        this.context = c;
    }

    @Override
    public PRListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pull_request_item, null);

        PRListAdapter.CustomViewHolder viewHolder = new PRListAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PRListAdapter.CustomViewHolder holder, int position) {
        holder.prTitle.setText(prList.get(position).prTitle);
        holder.prNumber.setText(String.valueOf(prList.get(position).prNumber));
    }


    @Override
    public int getItemCount() {
        return prList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        //CustomViewHolder contain a member variables
        public TextView prTitle;
        public TextView prNumber;

        // We also create a constructor that accepts the entire item row and does the id lookups
        public CustomViewHolder(View itemView) {
            super(itemView);
            prNumber = itemView.findViewById(R.id.pullRequestNum);
            prTitle= itemView.findViewById(R.id.pullRequestTitle);

        }
    }

}
