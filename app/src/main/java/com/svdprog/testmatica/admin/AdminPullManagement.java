package com.svdprog.testmatica.admin;

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
import android.widget.Toast;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.adapters.AdminPullManagementAdapter;

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

// This class is designed to display the history of user authorization sessions...

public class AdminPullManagement extends AppCompatActivity {
    ListView contests_listview;
    ProgressBar contests_progressbar;
    TextView textView1;
    SharedPreferences settings_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pull_management);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("История сеансов авторизации пользователей");
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Initialization of elements of the graphical interface of the mobile application:
        contests_listview = (ListView) findViewById(R.id.contests_listview);
        contests_progressbar = (ProgressBar) findViewById(R.id.contests_progressbar);
        textView1 = (TextView) findViewById(R.id.textView1);
        Toast toast = Toast.makeText(AdminPullManagement.this,
                "В данном отчёте слишком много информации, выполняет загрузка последних 100 записей...", Toast.LENGTH_LONG);
        toast.show();
        // Starting a stream to load all logins on the site:
        new GetPullManagement(contests_listview, contests_progressbar, textView1, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/admin_management/pull_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
    }
    // Stream to download all logins on the site:
    private class GetPullManagement extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public GetPullManagement(ListView contestList, ProgressBar progressBar, TextView textView1, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.login = login;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
                    urlConnection.getOutputStream().write(("login=" + this.login + "&password=" + this.password).getBytes());
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
            try{
                ArrayList<PullManagement> contests = new ArrayList<PullManagement>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("pull_management");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding a list of all logins on the site to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        contests.add(new PullManagement(jsonObject1.getInt("id"), jsonObject1.getInt("id_user"), jsonObject1.getString("date"), jsonObject1.getString("ip"), jsonObject1.getString("browser")));
                    }
                    // Calling the adapter to display a list of all logins on the site:
                    AdminPullManagementAdapter adapter = new AdminPullManagementAdapter(AdminPullManagement.this, R.layout.result_test, contests);
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

