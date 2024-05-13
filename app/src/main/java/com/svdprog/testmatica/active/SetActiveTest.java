package com.svdprog.testmatica.active;

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
import com.svdprog.testmatica.adapters.SetActiveTestAdapter;
import com.svdprog.testmatica.teacher.NewTeacherClass;

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

// The activity of assigning an active test to a specific class...

public class SetActiveTest extends AppCompatActivity {
    public ListView listView1;
    public ProgressBar progressBar1;
    public TextView textView1;
    Bundle arguments;
    SharedPreferences settings_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_active_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Назначить тест");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        arguments = getIntent().getExtras();
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Initialization of elements of the graphical interface of the mobile application:
        listView1 = (ListView) findViewById(R.id.listView1);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        textView1 = (TextView) findViewById(R.id.textView1);
        // Starting a stream to get information about available classes:
        new GetTeacherClass(listView1, progressBar1, textView1, settings_profile.getString("login", "-"), settings_profile.getString("password", "-"), arguments.getInt("id_test")).execute("https://testmatica.ru/api/get_teacher_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
    }
    // Stream for getting information about available classes:
    private class GetTeacherClass extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public int id_test;
        public GetTeacherClass(ListView contestList, ProgressBar progressBar, TextView textView, String login, String password, int id_test){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.login = login;
            this.password = password;
            this.id_test = id_test;
            this.textView = textView;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contestList.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password).getBytes());
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
                ArrayList<NewTeacherClass> classes = new ArrayList<NewTeacherClass>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("newteacherclass");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        // Creating an array of the received classes of this user:
                        int id = jsonObject1.getInt("id");
                        String title = jsonObject1.getString("title");
                        int idteacher = jsonObject1.getInt("idteacher");
                        String NTcode = jsonObject1.getString("NTcode");
                        classes.add(new NewTeacherClass(id, title, idteacher, NTcode));
                    }
                    // Calling the adapter to display the received classes of this user:
                    SetActiveTestAdapter adapter = new SetActiveTestAdapter(SetActiveTest.this, R.layout.teacher_class, classes, progressBar, contestList, login, password, textView, id_test);
                    contestList.setAdapter(adapter);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
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