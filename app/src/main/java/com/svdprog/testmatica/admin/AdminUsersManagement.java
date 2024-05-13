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

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.user.UserManagement;
import com.svdprog.testmatica.adapters.AdminUsersManagementAdapter;

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

public class AdminUsersManagement extends AppCompatActivity {
    ListView users_listview;
    ProgressBar users_progressbar;
    TextView users_textview;
    SharedPreferences settings_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_management);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Управление пользователями");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        users_listview = (ListView) findViewById(R.id.users_listview);
        users_progressbar = (ProgressBar) findViewById(R.id.users_progressbar);
        users_textview = (TextView) findViewById(R.id.users_textview);
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Starting a stream to display a list of all registered users in the site database:
        new GetUsersManagement(settings_profile.getString("login", "-"), settings_profile.getString("password", "-"), users_listview, users_progressbar, users_textview).execute("https://testmatica.ru/api/admin_management/users_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
    }
    // A stream for displaying a list of all registered users in the site database:
    private class GetUsersManagement extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public GetUsersManagement(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
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
            contestList.setVisibility(View.VISIBLE);
            try{
                ArrayList<UserManagement> contests = new ArrayList<UserManagement>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("users_management");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding a list of all registered users in the site database to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        contests.add(new UserManagement(jsonObject1.getInt("id"), jsonObject1.getString("login"), jsonObject1.getString("pass"), jsonObject1.getString("name"), jsonObject1.getString("family"), jsonObject1.getString("pol"), jsonObject1.getString("email"), jsonObject1.getString("city"), jsonObject1.getInt("adminuser"), jsonObject1.getString("date"), jsonObject1.getString("ip"), jsonObject1.getString("browser"), jsonObject1.getInt("dolg"), jsonObject1.getInt("imghref"), jsonObject1.getInt("levelxp"), jsonObject1.getString("checkclass"), jsonObject1.getInt("idclass"), jsonObject1.getInt("diary")));
                    }
                    // Calling the adapter to display a list of all registered users in the database of this site:
                    AdminUsersManagementAdapter adapter = new AdminUsersManagementAdapter(AdminUsersManagement.this, R.layout.user_management, contests, login, password, contestList, progressBar, textView1);
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