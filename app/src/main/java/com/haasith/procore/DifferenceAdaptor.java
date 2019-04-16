package com.haasith.procore;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DifferenceAdaptor extends RecyclerView.Adapter<DifferenceAdaptor.CustomViewHolder> {
    private ArrayList<PullRequest> prList;
    private Context context;

    public DifferenceAdaptor(Context c, ArrayList<PullRequest> prs) {
        this.prList = prs;
        this.context = c;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.diff_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.codeLines.setText((process(prList.get(position).codeLines)));
        holder.newFile.setText(prList.get(position).newFile);
        holder.oldFile.setText(prList.get(position).oldFile);

    }

    /*
    Finds the plus lines and colors it green and finds subtract lines are red and line numbers are blue
     */
    private SpannableString process(String codeLines) {
        SpannableString hashText = new SpannableString(codeLines);
        //regex to find lines that start with + and highlights green
        Matcher positiveMatcher = Pattern.compile("^\\+(.*)").matcher(hashText);
        while (positiveMatcher.find())
        {
            hashText.setSpan(new ForegroundColorSpan(context.getColor(R.color.colorPrimary)), positiveMatcher.start(), positiveMatcher.end(), 0);
        }
        //regex to find lines that start with - and highlights red
        Matcher negativeMatcher = Pattern.compile("^\\-(.*)").matcher(hashText);
        while (negativeMatcher.find())
        {
            hashText.setSpan(new ForegroundColorSpan(Color.RED), negativeMatcher.start(), negativeMatcher.end(), 0);
        }
        //regex to find part that start with @@ and ends with @@ and highlights blue
        Matcher matcher = Pattern.compile("\\@\\@([ +,@A-Za-z0-9_-]+)\\@\\@").matcher(hashText);
        while (matcher.find())
        {
            hashText.setSpan(new ForegroundColorSpan(Color.BLUE), matcher.start(), matcher.end(), 0);
        }
        return  hashText;
    }

    @Override
    public int getItemCount() {
        return prList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView newFile;
        public TextView oldFile;
        public TextView codeLines;

        // Creates a constructor that accepts the entire item row and looks up view ids
        public CustomViewHolder(View itemView) {
            super(itemView);
            newFile = itemView.findViewById(R.id.newFile);
            oldFile = itemView.findViewById(R.id.oldFile);
            codeLines = itemView.findViewById(R.id.codeLines);

        }
    }
}
