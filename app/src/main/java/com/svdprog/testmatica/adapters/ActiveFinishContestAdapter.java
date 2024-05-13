package com.svdprog.testmatica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.active.ActiveContest;

import java.util.ArrayList;

// This adapter is designed to display a list of completed active tests in the class section...

public class ActiveFinishContestAdapter extends ArrayAdapter<ActiveContest> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ActiveContest> contestList;
    public Context context;
    public ActiveFinishContestAdapter(Context context, int resource, ArrayList<ActiveContest> contests) {
        super(context, resource, contests);
        this.contestList = contests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ActiveContest contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.contest_title.setText(contest.getTitle());
        viewHolder.contest_quanity.setText(android.text.Html.fromHtml(Integer.toString(contest.getEd()) + " вопросов"));
        viewHolder.contest_result.setText(android.text.Html.fromHtml("Оценка: " + Integer.toString(contest.getMark()) + " (набрано " + Integer.toString(contest.getPercent()) + "% тестов)"));
        viewHolder.comment.setText(android.text.Html.fromHtml("<b><i>Комментарий от учителя (куратора):</b> " + contest.getComment() + "</i>"));
        return convertView;
    }
    private class ViewHolder {
        // Initialization of the graphical interface elements of this block:
        final TextView contest_title;
        final TextView contest_quanity;
        final TextView contest_result, comment;
        ViewHolder(View view){
            contest_title = view.findViewById(R.id.contest_title);
            contest_quanity = view.findViewById(R.id.contest_quanity);
            contest_result = view.findViewById(R.id.contest_result);
            comment = view.findViewById(R.id.comment);
        }
    }
}

