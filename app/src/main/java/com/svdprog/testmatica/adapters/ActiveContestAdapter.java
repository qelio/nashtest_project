package com.svdprog.testmatica.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.active.ActiveContest;
import com.svdprog.testmatica.contests.DecisionTest;
import com.svdprog.testmatica.dialogs.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// Adapter for configuring the list of active incomplete tests from the user...

public class ActiveContestAdapter extends ArrayAdapter<ActiveContest> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ActiveContest> contestList;
    public Intent intent;
    public Context context;
    public ActiveContestAdapter(Context context, int resource, ArrayList<ActiveContest> contests, Intent intent) {
        super(context, resource, contests);
        this.contestList = contests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.intent = intent;
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
        viewHolder.title_test.setText(contest.getTitle());
        String temp1 = "";
        temp1 = "<b>" + Integer.toString(contest.getEd()) + "</b> вопросов";
        viewHolder.any_exercise.setText(android.text.Html.fromHtml(temp1));
        temp1 = "<b>Время:</b> не ограничено";
        viewHolder.time.setText(android.text.Html.fromHtml(temp1));
        viewHolder.comment.setText(android.text.Html.fromHtml("<b><i>Комментарий от учителя (куратора):</b> " + contest.getComment() + "</i>"));
        viewHolder.comment.setVisibility(View.VISIBLE);
        viewHolder.button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = "https://testmatica.ru/api/get_active_exercise.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&id_test=" + contest.getId();
                new GetExercise(contest, context, intent).execute(temp);
            }
        });

        return convertView;
    }
    private class ViewHolder {
        // Initialization of the graphical interface elements of this block:
        final Button button_test;
        final TextView title_test;
        final TextView any_exercise;
        final TextView time, comment;
        ViewHolder(View view){
            button_test = view.findViewById(R.id.button_test);
            title_test = view.findViewById(R.id.title_test);
            any_exercise = view.findViewById(R.id.any_exercise);
            time = view.findViewById(R.id.level_xp);
            comment = view.findViewById(R.id.comment);
        }
    }
    // Creating a transition to another activity, if the user wants to perform this test:
    private class GetExercise extends AsyncTask<String, String, String> {
        public ActiveContest contest;
        public Context context1;
        public Intent intent1;
        // Creating a dialog box to inform about the download
        final DialogFragment loading = new DialogFragment((Activity) context);
        public GetExercise(ActiveContest contest, Context context, Intent intent){
            this.contest = contest;
            this.context1 = context;
            this.intent1 = intent;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.startLoadingdialog();
        }
        @Override
        protected String doInBackground(String... url1){
            String current = "";
            String JSON_URL = url1[0];
            try{
                URL url;
                HttpURLConnection urlConnection = null;
                try{
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    int data = isr.read();
                    while(data != - 1){
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return current;
        }
        @Override
        protected void onPostExecute(String s) {
            try{
                // TYPES OF LINK (NASHTEST DATABASE):
                // 1 - <img alt=\"\" src=\"https://sun9-27.userapi.com/impf/0ZcBV5TrzLd0itiE3pAKb7KCPVKAdH90tYvdvQ/2UefMEl0K0w.jpg?size=290x123&amp;quality=96&amp;sign=5fd07caaddd743cc029a3bb9bf771a2a&amp;type=album\" />
                // 2 - <img alt=\"\" src=\"/upload/image/Test12/Var2/9.JPG\" style=\"height:225px; width:505px\" />
                // 3 - <img alt=\"\" src=\"http://testmatica.ru/upload/image/oge2020/Test36/15.JPG\" />
                DecisionTest demo_test = new DecisionTest(contest.getEd(), contest.getTitle(), true);
                demo_test.setFor_five(contest.getFor_five());
                demo_test.setFor_four(contest.getFor_four());
                demo_test.setFor_three(contest.getFor_three());
                demo_test.setPercent(contest.getPercent());
                demo_test.setStatus(contest.getStatus());
                demo_test.setMark(contest.getMark());
                demo_test.setDelete_test(contest.getDelete_test());
                demo_test.setDate_test(contest.getDate());
                demo_test.setId_test(contest.getId());
                demo_test.setPersonal_id(contest.getPersonal_id());
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("active_exercise");
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    int id = jsonObject1.getInt("id");
                    String title = jsonObject1.getString("title");
                    String link = jsonObject1.getString("cond");
                    int fine = jsonObject1.getInt("fine");
                    String answer = jsonObject1.getString("any");
                    String result = "";
                    // Converting a link from a database to a standard-looking link that the function understands correctly:
                    char[] link_char = link.toCharArray();
                    int link_type = 2;
                    for (int j = 0; j < link_char.length; j++){
                        if ((link_char[j] == 'a') && (link_char[j + 1] == 'p') && (link_char[j + 2] == 'i')){
                            link_type = 1;
                            break;
                        }
                        if ((link_char[j] == 't') && (link_char[j + 1] == 'e') && (link_char[j + 2] == 's') && (link_char[j + 3] == 't') && (link_char[j + 4] == 'm')){
                            link_type = 3;
                            break;
                        }
                    }
                    if (link_type == 1){
                        int start = 20;
                        int finish = link_char.length - 5;
                        for (int j = start; j <= finish; j++){
                            result += link_char[j];
                        }
                    }
                    if (link_type == 2){
                        int start = 17;
                        int finish = link_char.length - 5;
                        result += "https://testmatica.ru";
                        for (int j = start; j <= finish; j++){
                            result += link_char[j];
                        }
                    }
                    if (link_type == 3){
                        int start = 37;
                        int finish = link_char.length - 5;
                        result += "https://testmatica.ru";
                        for (int j = start; j <= finish; j++){
                            result += link_char[j];
                        }
                    }
                    link = result;
                    // Filling in the created class with the necessary data:
                    demo_test.setId(i + 1, id);
                    demo_test.setFine(i + 1, fine);
                    demo_test.setLink(i + 1, link);
                    demo_test.setAnswer(i + 1, answer);
                }
                intent1.putExtra(DecisionTest.class.getSimpleName(), demo_test);
                context1.startActivity(intent1);
                loading.finishdialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
