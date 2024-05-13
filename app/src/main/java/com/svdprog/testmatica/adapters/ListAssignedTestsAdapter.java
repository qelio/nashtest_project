package com.svdprog.testmatica.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.contests.AssignedTest;

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

// Adapter for displaying a list of all completed and unfulfilled solutions for this active test...

public class ListAssignedTestsAdapter extends ArrayAdapter<AssignedTest> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<AssignedTest> contestList;
    public Context context;
    public ProgressBar progressBar;
    public ListView listView;
    public TextView textView;
    public String login, password;
    public ListAssignedTestsAdapter(Context context, int resource, ArrayList<AssignedTest> contests, String login, String password, ListView listView, ProgressBar progressBar, TextView textView) {
        super(context, resource, contests);
        this.contestList = contests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.login = login;
        this.password = password;
        this.progressBar = progressBar;
        this.listView = listView;
        this.textView = textView;
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
        final AssignedTest contest = contestList.get(position);
        // Splitting an element's style into a completed test:
        if (contest.getStatus() == 0){
            viewHolder.level_xp.setText(android.text.Html.fromHtml("<b>Статус:</b> не выполнено"));
            viewHolder.linear.setVisibility(View.GONE);
            viewHolder.level_xp.setTextColor(Color.parseColor("#EB4C42"));
        }
        // Splitting an element's style into an unfulfilled test:
        else{
            viewHolder.level_xp.setText(android.text.Html.fromHtml("<b>Статус:</b> выполнено"));
            viewHolder.linear.setVisibility(View.VISIBLE);
            viewHolder.percent.setText(android.text.Html.fromHtml("<b>Процент выполнения:</b> " + Integer.toString(contest.getPercent()) + "%"));
            viewHolder.mark.setText(android.text.Html.fromHtml("<b>Оценка:</b> " + Integer.toString(contest.getMark())));
            viewHolder.level_xp.setTextColor(Color.parseColor("#27A32B"));
            viewHolder.percent.setTextColor(Color.parseColor("#27A32B"));
            viewHolder.mark.setTextColor(Color.parseColor("#27A32B"));
        }
        // Content filling of the graphical interface elements of this block:
        viewHolder.id_questions.setText(android.text.Html.fromHtml("<b>Класс ученика:</b> " + contest.getName_class()));
        viewHolder.title_test.setText(android.text.Html.fromHtml("<b>Ученик:</b> " + contest.getName() + " " + contest.getFamily()));
        // Starting the corresponding stream, when you click on the test assignment button again:
        viewHolder.replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReplaceAssignedTests(login, password, listView, progressBar, textView, contest.getId_test(), contest.getId()).execute("https://testmatica.ru/api/service_active_tests/list_assigned_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            }
        });
        // Starting the corresponding stream, when you click on the test assignment button again:
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteAssignedTests(login, password, listView, progressBar, textView, contest.getId_test(), contest.getId()).execute("https://testmatica.ru/api/service_active_tests/list_assigned_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            }
        });
        return convertView;
    }
    private class ViewHolder {
        final TextView id_questions, level_xp, title_test, percent, mark;
        final LinearLayout linear;
        final Button replace, delete;
        ViewHolder(View view){
            id_questions = (TextView) view.findViewById(R.id.id_questions);
            level_xp = (TextView) view.findViewById(R.id.level_xp);
            title_test = (TextView) view.findViewById(R.id.title_test);
            percent = (TextView) view.findViewById(R.id.percent);
            mark = (TextView) view.findViewById(R.id.mark);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            replace = (Button) view.findViewById(R.id.replace);
            delete = (Button) view.findViewById(R.id.delete);
        }
    }
    // Stream to send the assignment command of the selected active test repeatedly:
    private class ReplaceAssignedTests extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public int id_test, replace_test;
        public ReplaceAssignedTests(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1, int id_test, int replace_test){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.id_test = id_test;
            this.replace_test = replace_test;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&id_test=" + this.id_test + "&replace_test=" + this.replace_test).getBytes());
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    int data = isr.read();
                    while(data != - 1){
                        current += (char) data;
                        data = isr.read();
                    }
                    urlConnection.disconnect();
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
            Toast toast = Toast.makeText(context,
                    "Тест назначен заново...", Toast.LENGTH_SHORT);
            toast.show();
            // Updates to the list of solutions of the selected active test:
            new GetAssignedTests(login, password, contestList, progressBar, textView1, id_test).execute("https://testmatica.ru/api/service_active_tests/list_assigned_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
    }
    // A thread for sending a delete command to a specific active test:
    private class DeleteAssignedTests extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public int id_test, delete_test;
        public DeleteAssignedTests(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1, int id_test, int delete_test){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.id_test = id_test;
            this.delete_test = delete_test;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&id_test=" + this.id_test + "&delete_test=" + this.delete_test).getBytes());
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    int data = isr.read();
                    while(data != - 1){
                        current += (char) data;
                        data = isr.read();
                    }
                    urlConnection.disconnect();
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
            Toast toast = Toast.makeText(context,
                    "Тест успешно удален...", Toast.LENGTH_SHORT);
            toast.show();
            // Updates to the list of solutions of the selected active test:
            new GetAssignedTests(login, password, contestList, progressBar, textView1, id_test).execute("https://testmatica.ru/api/service_active_tests/list_assigned_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
    }
    // Stream to get a list of solutions for the selected active test:
    private class GetAssignedTests extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public int id_test;
        public GetAssignedTests(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1, int id_test){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.id_test = id_test;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contestList.setAdapter(null);
            progressBar.setVisibility(View.VISIBLE);
            contestList.setVisibility(View.GONE);
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
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&id_test=" + this.id_test).getBytes());
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    int data = isr.read();
                    while(data != - 1){
                        current += (char) data;
                        data = isr.read();
                    }
                    urlConnection.disconnect();
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
            progressBar.setVisibility(View.GONE);
            contestList.setVisibility(View.VISIBLE);
            try{
                ArrayList<AssignedTest> contests = new ArrayList<AssignedTest>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("assigned_tests");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding a list of solutions for the selected active test to the general array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        contests.add(new AssignedTest(jsonObject1.getInt("id"), jsonObject1.getInt("id_test"), jsonObject1.getInt("id_teacher"), jsonObject1.getInt("id_user"), jsonObject1.getString("name"), jsonObject1.getString("family"), jsonObject1.getString("name_class"), jsonObject1.getInt("percent"), jsonObject1.getInt("status"), jsonObject1.getInt("mark"), jsonObject1.getInt("delete_test")));
                    }
                    // Calling the adapter to display a list of solutions for the selected active test:
                    ListAssignedTestsAdapter adapter = new ListAssignedTestsAdapter(context, R.layout.list_assigned_tests, contests, login, password, contestList, progressBar, textView1);
                    contestList.setAdapter(adapter);
                }
                else{
                    textView1.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

