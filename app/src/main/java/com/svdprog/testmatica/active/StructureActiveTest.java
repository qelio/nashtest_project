package com.svdprog.testmatica.active;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.adapters.AdapterActiveTestForTeacher;
import com.svdprog.testmatica.teacher.ActiveTestForTeacher;

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

// This activity is intended for editing an active test, and it is also launched when an active test is created...

public class StructureActiveTest extends AppCompatActivity {
    SharedPreferences settings_profile;
    TextView textView1, textView2;
    ListView listView1, listView2;
    ProgressBar progressBar1, progressBar2;
    ImageButton previous_page, next_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_active_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Редактирование активного теста");
        actionBar.setHomeButtonEnabled(true);;
        actionBar.setDisplayHomeAsUpEnabled(true);
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Initialization of elements of the graphical interface of the mobile application:
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        listView1 = (ListView) findViewById(R.id.listView1);
        listView2 = (ListView) findViewById(R.id.listView2);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        previous_page = (ImageButton) findViewById(R.id.previous_page);
        next_page = (ImageButton) findViewById(R.id.next_page);
        Bundle arguments = getIntent().getExtras();
        // Running a background thread to get the added tasks in the active test data:
        new GetAddedQuestions(listView2, progressBar2, textView2, arguments.getInt("id_test"), settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/service_active_tests/composition_active.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        // Starting a background stream to get available tasks in the selected thematic area:
        new GetPagesQuestions(listView1, progressBar1, textView1, arguments.getInt("id_test"), settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/service_active_tests/composition_unitests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        // Adding a top navigation menu to select available and added test questions:
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Все вопросы");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Добавленные");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);
        // Updating each block by clicking on the navigation menu:
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            public void onTabChanged(String tabId) {
                new GetAddedQuestions(listView2, progressBar2, textView2, arguments.getInt("id_test"), settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/service_active_tests/composition_active.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                new GetPagesQuestions(listView1, progressBar1, textView1, arguments.getInt("id_test"), settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/service_active_tests/composition_unitests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            }
        });
    }
    // Background stream to get available tasks in the selected thematic area:
    private class GetPagesQuestions extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public int id_test;
        public GetPagesQuestions(ListView contestList, ProgressBar progressBar, TextView textView, int id_test, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView = textView;
            this.login = login;
            this.password = password;
            this.id_test = id_test;
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
            try{
                ArrayList<ActiveTestForTeacher> classes = new ArrayList<ActiveTestForTeacher>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("composition_unitests");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                if (jsonObject1.getInt("pages") != 0){
                    // Getting the number of pages with available tasks in the selected thematic direction:
                    if (jsonObject1.getInt("pages") == 1){
                        next_page.setVisibility(View.GONE);
                        previous_page.setVisibility(View.GONE);
                    }
                    // Starting a stream with a known number of pages:
                    new GetQuestions(contestList, progressBar, textView, id_test, login, password, 1, jsonObject1.getInt("pages")).execute("https://testmatica.ru/api/service_active_tests/composition_unitests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    next_page.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // Stream to get the total number of questions and their content:
    private class GetQuestions extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public int id_test, current_page, pages;
        public GetQuestions(ListView contestList, ProgressBar progressBar, TextView textView, int id_test, String login, String password, int current_page, int pages){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView = textView;
            this.login = login;
            this.password = password;
            this.id_test = id_test;
            this.current_page = current_page;
            this.pages = pages;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contestList.setVisibility(View.GONE);
            contestList.setAdapter(null);
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            // Implementation of visibility of navigation buttons for pagination of available questions:
            if (pages == current_page){
                next_page.setVisibility(View.GONE);
                previous_page.setVisibility(View.VISIBLE);
            }
            else if(current_page > 1){
                next_page.setVisibility(View.VISIBLE);
                previous_page.setVisibility(View.VISIBLE);
            }
            else if(current_page == 1){
                next_page.setVisibility(View.VISIBLE);
                previous_page.setVisibility(View.GONE);
            }
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&id_test=" + this.id_test + "&page=" + this.current_page).getBytes());
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
            try{
                ArrayList<ActiveTestForTeacher> classes = new ArrayList<ActiveTestForTeacher>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("composition_unitests");
                if (jsonArray.length() != 0){
                    progressBar.setVisibility(View.GONE);
                    contestList.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding all questions in this thematic area to the general array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        classes.add(new ActiveTestForTeacher(0, jsonObject1.getInt("id"), jsonObject1.getString("title"), jsonObject1.getString("cond"), jsonObject1.getString("any"), jsonObject1.getInt("levelxp"), jsonObject1.getInt("class"), jsonObject1.getInt("typetest"), jsonObject1.getInt("fine"), jsonObject1.getInt("seen"), jsonObject1.getInt("add")));
                    }
                    // Calling the adapter to page-by-page display of available questions:
                    AdapterActiveTestForTeacher adapter = new AdapterActiveTestForTeacher(StructureActiveTest.this, R.layout.active_test_for_teacher, classes, false, contestList, progressBar, textView, id_test, login, password);
                    contestList.setAdapter(adapter);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
                // Implementation of the next page button:
                next_page.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetQuestions(contestList, progressBar, textView, id_test, login, password, current_page + 1, pages).execute("https://testmatica.ru/api/service_active_tests/composition_unitests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                    }
                });
                // Implementation of the previous page button:
                previous_page.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetQuestions(contestList, progressBar, textView, id_test, login, password, current_page - 1, pages).execute("https://testmatica.ru/api/service_active_tests/composition_unitests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // Background thread to get the added tasks in the active test data:
    private class GetAddedQuestions extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public int id_test;
        public GetAddedQuestions(ListView contestList, ProgressBar progressBar, TextView textView, int id_test, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView = textView;
            this.login = login;
            this.password = password;
            this.id_test = id_test;
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
                ArrayList<ActiveTestForTeacher> classes = new ArrayList<ActiveTestForTeacher>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("composition_active");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding the added questions in the selected active test to the general array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        classes.add(new ActiveTestForTeacher(jsonObject1.getInt("active_gen"), jsonObject1.getInt("id"), jsonObject1.getString("title"), jsonObject1.getString("cond"), jsonObject1.getString("any"), jsonObject1.getInt("levelxp"), jsonObject1.getInt("class"), jsonObject1.getInt("typetest"), jsonObject1.getInt("fine"), jsonObject1.getInt("seen")));
                    }
                    // Calling the adapter to display the added questions in the selected active test:
                    AdapterActiveTestForTeacher adapter = new AdapterActiveTestForTeacher(StructureActiveTest.this, R.layout.active_test_for_teacher, classes, true, contestList, progressBar, textView, id_test, login, password);
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