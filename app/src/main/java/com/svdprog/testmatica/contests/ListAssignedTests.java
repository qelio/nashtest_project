package com.svdprog.testmatica.contests;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.adapters.ListAssignedTestsAdapter;

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

// This class is designed to store information from this function list_assigned_tests.php...

public class ListAssignedTests extends AppCompatActivity {
    ListView contests_listview;
    ProgressBar contests_progressbar;
    TextView contests_textview;
    SharedPreferences settings_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assigned_tests);
        Bundle arguments = getIntent().getExtras();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(arguments.getString("title_test"));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        contests_listview = (ListView) findViewById(R.id.contests_listview);
        contests_progressbar = (ProgressBar) findViewById(R.id.contests_progressbar);
        contests_textview = (TextView) findViewById(R.id.contests_textview);
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Starting a stream to get a list of passed tests from the site database:
        new GetAssignedTests(settings_profile.getString("login", "-"), settings_profile.getString("password", "-"), contests_listview, contests_progressbar, contests_textview, arguments.getInt("id_test")).execute("https://testmatica.ru/api/service_active_tests/list_assigned_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
    }
    // Stream for getting a list of passed tests from the site database:
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
                        // Stream for getting a list of passed tests from the site database:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        contests.add(new AssignedTest(jsonObject1.getInt("id"), jsonObject1.getInt("id_test"), jsonObject1.getInt("id_teacher"), jsonObject1.getInt("id_user"), jsonObject1.getString("name"), jsonObject1.getString("family"), jsonObject1.getString("name_class"), jsonObject1.getInt("percent"), jsonObject1.getInt("status"), jsonObject1.getInt("mark"), jsonObject1.getInt("delete_test")));
                    }
                    // Calling the adapter to display a list of passed tests from the site database:
                    ListAssignedTestsAdapter adapter = new ListAssignedTestsAdapter(ListAssignedTests.this, R.layout.list_assigned_tests, contests, login, password, contestList, progressBar, textView1);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}