package com.svdprog.testmatica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.contests.ResultContest;

import java.util.ArrayList;

// Adapter for displaying a complete list of passed tests by all users of the resource...

public class AllContestsAdapter extends ArrayAdapter<ResultContest> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ResultContest> contestList;
    public Context context;
    public AllContestsAdapter(Context context, int resource, ArrayList<ResultContest> contests) {
        super(context, resource, contests);
        this.contestList = contests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ResultContest contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.contest_title.setText(contest.getTitle());
        viewHolder.contest_date.setText("Дата: " + contest.getDate());
        viewHolder.contest_result.setText("Результат: " + contest.getKerm());
        viewHolder.user_id.setText(android.text.Html.fromHtml("<b>Идентификатор пользователя (ID):</b> " + Integer.toString(contest.getUser_id())));
        return convertView;
    }
    private class ViewHolder {
        final TextView contest_title, contest_date, contest_result, user_id;
        ViewHolder(View view){
            contest_title = (TextView) view.findViewById(R.id.contest_title);
            contest_date = (TextView) view.findViewById(R.id.contest_date);
            contest_result = (TextView) view.findViewById(R.id.contest_result);
            user_id = (TextView) view.findViewById(R.id.user_id);
        }
    }
}

