package com.svdprog.testmatica.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.svdprog.testmatica.admin.PullManagement;
import com.svdprog.testmatica.R;

import java.util.ArrayList;

// This adapter is designed to display the history of user authorization sessions...

public class AdminPullManagementAdapter extends ArrayAdapter<PullManagement> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<PullManagement> contestList;
    public Context context;
    public AdminPullManagementAdapter(Context context, int resource, ArrayList<PullManagement> contests) {
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
        final PullManagement contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.contest_title.setText(android.text.Html.fromHtml("<b>Информация об устройстве:</b> " + contest.getIp() + ", " + contest.getBrowser()));
        viewHolder.user_id.setText(android.text.Html.fromHtml("<b>Идентификатор пользователя (ID):</b> " + Integer.toString(contest.getId_user())));
        viewHolder.contest_date.setText(android.text.Html.fromHtml("<b>Дата:</b> " + contest.getDate()));
        viewHolder.contest_result.setTextColor(Color.parseColor("#4160A0"));
        viewHolder.contest_result.setText(android.text.Html.fromHtml("<b>ID записи:</b> " + contest.getId()));
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