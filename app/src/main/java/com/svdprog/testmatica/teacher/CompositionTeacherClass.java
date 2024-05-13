package com.svdprog.testmatica.teacher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svdprog.testmatica.contests.AllContests;
import com.svdprog.testmatica.R;
import com.svdprog.testmatica.user.UserProfile;
import com.svdprog.testmatica.adapters.AdapterCompositionClass;

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

// This class is designed to view all the students in the class, as well as track their activity...

public class CompositionTeacherClass extends AppCompatActivity {
    public TextView textView1;
    public ProgressBar progressBar1;
    public ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composition_teacher_class);
        Bundle arguments = getIntent().getExtras();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Состав класса");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        textView1 = (TextView) findViewById(R.id.textView1);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        listView1 = (ListView) findViewById(R.id.listView1);
        // Start a stream to get a list of the composition of the selected class from the database:
        new GetComposition(listView1, progressBar1, textView1).execute("https://testmatica.ru/api/composition_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&idclass=" + Integer.toString(arguments.getInt("idclass")));
    }
    // Stream to get a list of the composition of the selected class from the database:
    private class GetComposition extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public GetComposition(ListView contestList, ProgressBar progressBar, TextView textView1){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contestList.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.GONE);
            contestList.setVisibility(View.VISIBLE);
            try{
                ArrayList<UserProfile> users = new ArrayList<UserProfile>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Getting a list of the composition of the selected class from the database to a common array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        UserProfile user = new UserProfile();
                        user.setId(jsonObject1.getInt("id"));
                        user.setLogin(jsonObject1.getString("login"));
                        user.setName(jsonObject1.getString("name"));
                        user.setFamily(jsonObject1.getString("family"));
                        user.setPol(jsonObject1.getString("pol"));
                        user.setEmail(jsonObject1.getString("email"));
                        user.setCity(jsonObject1.getString("city"));
                        user.setAdminuser(jsonObject1.getInt("adminuser"));
                        user.setDate(jsonObject1.getString("date"));
                        user.setIp(jsonObject1.getString("ip"));
                        user.setBrowser(jsonObject1.getString("browser"));
                        user.setDolg(jsonObject1.getInt("dolg"));
                        user.setImghref(jsonObject1.getInt("imghref"));
                        user.setLevelxp(jsonObject1.getInt("levelxp"));
                        user.setCheckclass(jsonObject1.getString("checkclass"));
                        user.setIdclass(jsonObject1.getInt("idclass"));
                        user.setDiary(jsonObject1.getInt("diary"));
                        users.add(user);
                    }
                    // Calling the adapter to display a list of the composition of the selected class from the database:
                    Intent intent = new Intent(CompositionTeacherClass.this, AllContests.class);
                    AdapterCompositionClass adapter = new AdapterCompositionClass(CompositionTeacherClass.this, R.layout.composition_class, users, intent, progressBar, contestList, textView1);
                    contestList.setAdapter(adapter);
                }
                else{
                    progressBar.setVisibility(View.GONE);
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