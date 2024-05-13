package com.svdprog.testmatica.contests;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

// Activity for displaying the results of the passed test, universal (thematic tests and active tests)...

public class FinishTest extends AppCompatActivity {
    SharedPreferences settings_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Завершение теста");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle arguments = getIntent().getExtras();
        DecisionTest demo_test = (DecisionTest) arguments.getSerializable(DecisionTest.class.getSimpleName());
        // Initialization of elements of the graphical interface of the mobile application:
        TextView textedit = (TextView)findViewById(R.id.textedit);
        Button button2 = (Button)findViewById(R.id.button2);
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        int right_answer = 0;
        // Finding the number of correct answers from the total number of answers through the loop:
        for (int i = 1; i <= demo_test.getQuanity(); i++){
            if (demo_test.getAnswer(i).equals(demo_test.getUser(i))){
                right_answer++;
            }
        }
        String url_post = "https://testmatica.ru/api/send_finish_test.php?authorization_key=" + getResources().getString(R.string.authorization_key);
        int levelxp = 0;
        if (right_answer == demo_test.getQuanity()){
            levelxp = demo_test.getLevelxp();
        }
        // The user ID is transmitted to the database via the site API:
        if (demo_test.isActive_test() == false) {
            // Generating text for the user using the number of correct answers and the total number of questions:
            String tmp = "Вы решили верно " + Integer.toString(right_answer) + " заданий из " + Integer.toString(demo_test.getQuanity()) + " заданий. Попробуйте пройти тест еще раз, возможно вы сможете достичь лучшего результата!";
            textedit.setText(tmp);
            if (settings_profile.contains("login") && settings_profile.contains("password")) {
                new SendResult(settings_profile.getString("login", "-"), settings_profile.getString("password", "-"), levelxp, "(Из мобильного приложения) " + demo_test.getTitle(), right_answer, demo_test.getQuanity()).execute("https://testmatica.ru/api/get_user.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            } else {
                new SendFinishTests("(Из мобильного приложения) " + demo_test.getTitle(), 0, right_answer, demo_test.getQuanity(), levelxp).execute(url_post);
            }
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button2.setClickable(false);
                    // If the user wants to start the test from the beginning, we make the transition to the starting activity for the test:
                    for (int i = 1; i <= demo_test.getQuanity(); i++){
                        demo_test.setUser(i, "-");
                    }
                    demo_test.setCurrent(1);
                    Intent intent = new Intent(FinishTest.this, TitleTest.class);
                    intent.putExtra(DecisionTest.class.getSimpleName(), demo_test);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else{
            // When the user passes an active test from the teacher, the assessment is made depending on the evaluation criteria:
            button2.setText("Вернуться назад");
            int mark = 2;
            double temp = (double)right_answer / demo_test.getQuanity() * 100;
            int percent = (int) temp;
            if (percent < demo_test.getFor_three()){
                mark = 2;
            } else if(percent >= demo_test.getFor_three() && percent < demo_test.getFor_four()){
                mark = 3;
            } else if(percent >= demo_test.getFor_four() && percent < demo_test.getFor_five()){
                mark = 4;
            } else if(percent >= demo_test.getFor_five()){
                mark = 5;
            }
            // Starting a stream to send the results of the completed test to the database:
            new SetActiveResult().execute("https://testmatica.ru/api/set_active_result.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&id=" + Integer.toString(demo_test.getPersonal_id()) + "&mark=" + Integer.toString(mark) + "&percent=" + Integer.toString(percent));
            String parametrs = "Вы решили верно " + Integer.toString(right_answer) + " заданий из " + Integer.toString(demo_test.getQuanity()) + " заданий.\n<b>Оценка за тест: " + Integer.toString(mark) + " (набрано " + Integer.toString(percent) + "% тестов)</b>\n" + "Если что-то не получается, никогда не отчаивайтесь. У каждого есть шанс на исправление своих ошибок!";
            textedit.setText(android.text.Html.fromHtml(parametrs));
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
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
    // A stream for sending the results of the completed test to the database:
    private class SendResult extends AsyncTask<String, String, String> {
        public String login = "";
        public String password = "";
        public int levelxp = 0;
        public String title = "";
        public int right = 0;
        public int all = 0;
        public SendResult(String login, String password, int levelxp, String title, int right, int all){
            this.login = login;
            this.password = password;
            this.levelxp = levelxp;
            this.title = title;
            this.right = right;
            this.all = all;
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
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                // Start thread to sending the test result to the general database of the site:
                new SendFinishTests(title, jsonObject1.getInt("id"), right, all, levelxp).execute("https://testmatica.ru/api/send_finish_test.php?authorization_key=" + getResources().getString(R.string.authorization_key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // Sending the test result to the general database of the site:
    private class SendFinishTests extends AsyncTask<String, String, String> {
        public String title = "";
        public String kerm = "";
        public String percent = "";
        public int user_id = 0;
        public int levelxp;
        public SendFinishTests(String title, int user_id, int right, int all, int levelxp){
            this.title = title;
            // Typing the results of the passed test:
            this.kerm = Integer.toString(right) + "/" + Integer.toString(all);
            double percent = (float)right / all;
            this.percent = Integer.toString((int)Math.floor(percent * 100)) + "%";
            this.user_id = user_id;
            this.levelxp = levelxp;
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
                    urlConnection.getOutputStream().write( ("title=" + this.title + "&kerm=" + this.kerm + "&percent=" + this.percent + "&user_id=" + this.user_id + "&levelxp=" + this.levelxp).getBytes());
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
            // There may be a handler here if suddenly adding a view to the test fails with an error.
            // But since it is not provided by the application interface, I may not have it...
        }
    }
    // Sending the test result to the general database of the site:
    private class SetActiveResult extends AsyncTask<String, String, String> {
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
            // There may be a handler here if suddenly adding a view to the test fails with an error.
            // But since it is not provided by the application interface, I may not have it...
        }
    }
}