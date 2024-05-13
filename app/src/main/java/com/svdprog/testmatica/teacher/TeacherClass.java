package com.svdprog.testmatica.teacher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.active.CreateActiveTest;
import com.svdprog.testmatica.adapters.AdapterPrepareActiveTest;
import com.svdprog.testmatica.adapters.AdapterTeacherClass;
import com.svdprog.testmatica.active.PrepareActiveTest;

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

// The activity is intended for creating and managing classes for teachers, as well as editing and other operations with active tests...

public class TeacherClass extends AppCompatActivity {
    ProgressBar progressBar1, progressBar2;
    EditText title_class;
    TextView textView1, textView2;
    ListView listView1, listView2;
    SharedPreferences settings_profile;
    ImageButton imageButton;
    Button create_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Учительская");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Initialization of elements of the graphical interface of the mobile application:
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        textView1 = (TextView) findViewById(R.id.textView1);
        listView1 = (ListView) findViewById(R.id.listview1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        textView2 = (TextView) findViewById(R.id.textView2);
        listView2 = (ListView) findViewById(R.id.listview2);
        title_class = (EditText) findViewById(R.id.title_class);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        create_test = (Button) findViewById(R.id.create_test);
        // Switching to the next activity when trying to create a new test:
        create_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherClass.this, CreateActiveTest.class);
                startActivity(intent);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking the class name field for emptiness:
                if (title_class.getText().toString().equals("")){
                    title_class.setError("Поле не должно быть пустым");
                }
                else{
                    // If everything is fine, then we start the thread to send the command to create a new class and hide the system keyboard:
                    new CreateTeacherClass(settings_profile.getString("login", "-"), settings_profile.getString("password", "-"), title_class.getText().toString()).execute("https://testmatica.ru/api/create_teacher_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                    title_class.getText().clear();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        // Creating a top menu to quickly switch tabs between different tasks:
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        // Start the thread to update the list of created classes:
        new GetTeacherClass(listView1, progressBar1, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_teacher_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        // Start the stream to update the list of created active tests:
        new GetActiveTest(listView2, progressBar2, textView2, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/service_active_tests/prepare_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Мои классы");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Мои тесты");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);
    }
    // When the activity resumes, we update some data:
    @Override
    protected void onResume() {
        super.onResume();
        // Start the thread to update the list of created classes:
        new GetTeacherClass(listView1, progressBar1, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_teacher_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        // Start the stream to update the list of created active tests:
        new GetActiveTest(listView2, progressBar2, textView2, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/service_active_tests/prepare_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
    }
    // Stream to update the list of created active tests:
    private class GetActiveTest extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public GetActiveTest(ListView contestList, ProgressBar progressBar, TextView textView, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView = textView;
            this.login = login;
            this.password = password;
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
                ArrayList<PrepareActiveTest> classes = new ArrayList<PrepareActiveTest>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("prepare_active");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding the list of created active tests from the database to the shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        classes.add(new PrepareActiveTest(jsonObject1.getInt("id_test"), jsonObject1.getString("name_test"), jsonObject1.getString("com_test"), jsonObject1.getInt("id_user"), jsonObject1.getString("date_test"), jsonObject1.getInt("for_five"), jsonObject1.getInt("for_four"), jsonObject1.getInt("for_three"), jsonObject1.getInt("delete_test"), jsonObject1.getInt("class"), jsonObject1.getInt("typetest")));
                    }
                    // Calling the adapter to display the created active tests from the database:
                    AdapterPrepareActiveTest adapter = new AdapterPrepareActiveTest(TeacherClass.this, R.layout.prepare_active_test, classes, contestList, progressBar, textView, login, password);
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
    // Stream for creating a new class in the teacher's account:
    private class CreateTeacherClass extends AsyncTask<String, String, String> {
        public String login = "", password = "", title = "";
        public CreateTeacherClass(String login, String password, String title){
            this.title = title;
            this.login = login;
            this.password = password;
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&title=" + this.title).getBytes());
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
            // Updating the list of created classes:
            new GetTeacherClass(listView1, progressBar1, login, password).execute("https://testmatica.ru/api/get_teacher_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            // This method does not require receiving the server's response back, but nevertheless it can be read in the "newteacherclass" object...
        }
    }
    // Stream to get a list of created classes from the database:
    private class GetTeacherClass extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public String login = "", password = "";
        public GetTeacherClass(ListView contestList, ProgressBar progressBar, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.login = login;
            this.password = password;
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
                        // Adding a list of created classes from a database to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        String title = jsonObject1.getString("title");
                        int idteacher = jsonObject1.getInt("idteacher");
                        String NTcode = jsonObject1.getString("NTcode");
                        classes.add(new NewTeacherClass(id, title, idteacher, NTcode));
                    }
                    // Calling the adapter to display a list of created classes from the database:
                    Intent intent = new Intent(TeacherClass.this, CompositionTeacherClass.class);
                    AdapterTeacherClass adapter = new AdapterTeacherClass(TeacherClass.this, R.layout.teacher_class, classes, intent, progressBar, contestList, login, password, textView1);
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