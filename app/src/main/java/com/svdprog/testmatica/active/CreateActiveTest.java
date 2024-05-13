package com.svdprog.testmatica.active;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.svdprog.testmatica.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// Activity, where a new active test is created by the teacher...

public class CreateActiveTest extends AppCompatActivity {
    SharedPreferences settings_profile;
    Spinner spinner1, spinner2;
    EditText name_test, comment_test, for_five, for_four, for_three;
    ProgressBar progressBar;
    LinearLayout linear;
    Button create_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_active_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Создание нового теста");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Initialization of elements of the graphical interface of the mobile application:
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        name_test = (EditText) findViewById(R.id.name_test);
        comment_test = (EditText) findViewById(R.id.comment_test);
        for_five = (EditText) findViewById(R.id.for_five);
        for_four = (EditText) findViewById(R.id.for_four);
        for_three = (EditText) findViewById(R.id.for_three);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        linear = (LinearLayout) findViewById(R.id.linear);
        create_test = (Button) findViewById(R.id.create_test);
        create_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name_test.getText().toString().equals("") && !comment_test.getText().toString().equals("") && !for_five.getText().toString().equals("") && !for_four.getText().toString().equals("") && !for_three.getText().toString().equals("")){
                    // If all the fields are filled in, we start the background data sending stream:
                    new SendActiveTest(settings_profile.getString("login", "-"), settings_profile.getString("password", "-"), name_test.getText().toString(), comment_test.getText().toString(), Integer.parseInt(for_five.getText().toString()), Integer.parseInt(for_four.getText().toString()), Integer.parseInt(for_three.getText().toString()), spinner1.getSelectedItemPosition(), spinner2.getSelectedItemPosition()).execute("https://testmatica.ru/api/service_active_tests/create_active_test.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                }
                // Checking fields for emptiness, if the field is empty, we return errors to the corresponding fields:
                else{
                    if (name_test.getText().toString().equals("")){
                        name_test.setError("Поле не должно быть пустым!");
                    }
                    if (comment_test.getText().toString().equals("")){
                        comment_test.setError("Поле не должно быть пустым!");
                    }
                    if (for_five.getText().toString().equals("")){
                        for_five.setError("Поле не должно быть пустым!");
                    }
                    if (for_four.getText().toString().equals("")){
                        for_four.setError("Поле не должно быть пустым!");
                    }
                    if (for_three.getText().toString().equals("")){
                        for_three.setError("Поле не должно быть пустым!");
                    }
                }
            }
        });
    }
    // In this thread, a new record of the active test is created in the site database:
    private class SendActiveTest extends AsyncTask<String, String, String> {
        public String login = "", password = "", name_test = "", com_test = "";
        public int for_five, for_four, for_three, class_test, typetest;
        public SendActiveTest(String login, String password, String name_test, String com_test, int for_five, int for_four, int for_three, int class_test, int typetest){
            int selected1 = 0, selected2 = 0;
            // We bring the received data to a specific format for storage in the database:
            if (class_test == 0){
                selected1 = 1;
            }
            else if (class_test == 1){
                selected1 = 2;
            }
            else if (class_test == 2){
                selected1 = 3;
            }
            else if (class_test == 3){
                selected1 = 5;
            }
            else if (class_test == 4){
                selected1 = 6;
            }
            else if (class_test == 5){
                selected1 = 4;
            }
            else{
                selected1 = 404;
            }
            if (typetest == 0){
                selected2 = 1;
            }
            else if (typetest == 1){
                selected2 = 2;
            }
            else if (typetest == 2){
                selected2 = 3;
            }
            else if (typetest == 3){
                selected2 = 4;
            }
            else if (typetest == 4){
                selected2 = 5;
            }
            else if (typetest == 5){
                selected2 = 6;
            }
            else if (typetest == 6){
                selected2 = 7;
            }
            else if (typetest == 7){
                selected2 = 8;
            }
            else if (typetest == 8){
                selected2 = 12;
            }
            else if (typetest == 9){
                selected2 = 10;
            }
            else if (typetest == 10){
                selected2 = 13;
            }
            else if (typetest == 11){
                selected2 = 9;
            }
            else if (typetest == 12){
                selected2 = 11;
            }
            else{
                selected2 = 404;
            }
            this.login = login;
            this.password = password;
            this.name_test = name_test;
            this.com_test = com_test;
            this.for_five = for_five;
            this.for_four = for_four;
            this.for_three = for_three;
            this.class_test = selected1;
            this.typetest = selected2;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linear.setVisibility(View.GONE);
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&name_test=" + this.name_test + "&com_test=" + this.com_test + "&for_five=" + this.for_five + "&for_four=" + this.for_four + "&for_three=" + this.for_three + "&class=" + this.typetest + "&typetest=" + this.class_test).getBytes());
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
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("create_active");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                // If a positive response is received from the server, then we switch to another activity to edit the active test:
                Intent intent = new Intent(CreateActiveTest.this, StructureActiveTest.class);
                intent.putExtra("id_test", jsonObject1.getInt("id_test"));
                startActivity(intent);
                finish();
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