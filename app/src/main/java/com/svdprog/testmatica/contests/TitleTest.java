package com.svdprog.testmatica.contests;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

// The title page when starting the test...

public class TitleTest extends AppCompatActivity {
    DecisionTest demo_test;
    SharedPreferences settings_profile;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Начало тестирования");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        TextView text1 = (TextView)findViewById(R.id.text1);
        TextView people_easy = (TextView)findViewById(R.id.people_easy);
        TextView text3 = (TextView)findViewById(R.id.text3);
        Button button1 = (Button)findViewById(R.id.button1);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        Bundle arguments = getIntent().getExtras();
        demo_test = (DecisionTest) arguments.getSerializable(DecisionTest.class.getSimpleName());
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // If the account is a teacher's or administrator's account, the answer will be shown in the input field prompt:
        new GetUser(settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_user.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        text1.setText(demo_test.getTitle());
        if (demo_test.isActive_test() == false) {
            // Changing the appearance depending on the thematic test:
            String temp = "https://testmatica.ru/api/set_seen.php?authorization_key=" + getResources().getString(R.string.authorization_key) + "&id=" + demo_test.getId(1);
            new SetSeen().execute(temp);
            String temp1 = "<u><i>Просмотров</i></u>:  <b>" + Integer.toString(demo_test.getSeen()) + getEmoji(0x1F441) + "</b> ";
            people_easy.setText(android.text.Html.fromHtml(temp1));
        }
        else{
            // Changing the appearance depending on the active test:
            String temp1 = "<u><i>Дата создания</i></u>:  <b>" + demo_test.getDate_test() + " " + getEmoji(0x23F0) + "</b> ";
            people_easy.setText(android.text.Html.fromHtml(temp1));
            String parametrs = "<b><u>Параметры теста:</u> </b><br/><br/><b>Время выполнения:</b> <i>не ограничено</i><br/><b>Количество вопросов:</b> <i>" + Integer.toString(demo_test.getQuanity()) + "</i><br/>";
            parametrs += "<b>Критерии оценивания:</b><br/>&nbsp;&nbsp;&nbsp;<i>оценка \"5\" - "  + Integer.toString(demo_test.getFor_five()) + "%<br/>" + "&nbsp;&nbsp;&nbsp;<i>оценка \"4\" - "  + Integer.toString(demo_test.getFor_four()) + "%<br/>" + "&nbsp;&nbsp;&nbsp;<i>оценка \"3\" - "  + Integer.toString(demo_test.getFor_three()) + "%";
            text3.setText(android.text.Html.fromHtml(parametrs));
            text3.setVisibility(View.VISIBLE);
        }
        // The beginning of an active or thematic test, the transition to the next activity:
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setClickable(false);
                if (checkBox.isChecked()){
                    demo_test.setEnter(true);
                }
                Intent intent = new Intent(TitleTest.this, CurrentExercise.class);
                intent.putExtra(DecisionTest.class.getSimpleName(), demo_test);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle("Выход из теста")
                        .setMessage("Вы действительно хотите покинуть данный тематический тест?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Выход", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Продолжить", null).setIcon(R.drawable.edit_off).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // A small function for the correct display of emojis in text layouts:
    public String getEmoji(int uc){
        return new String(Character.toChars(uc));
    }
    // A stream for getting user information:
    private class GetUser extends AsyncTask<String, String, String> {
        public String login = "";
        public String password = "";
        public GetUser(String login, String password){
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
                // Granting administrator rights to the user depending on the value in the database:
                if (jsonObject1.getInt("id") == 0){
                    demo_test.setAdmin_user(false);
                }
                else{
                    if (jsonObject1.getInt("adminuser") == 1 || jsonObject1.getInt("dolg") == 2){
                        demo_test.setAdmin_user(true);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // Stream for updating the number of views for this test:
    private class SetSeen extends AsyncTask<String, String, String> {
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
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выход из теста")
                .setMessage("Вы действительно хотите покинуть данный тематический тест?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Выход", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Продолжить", null).setIcon(R.drawable.edit_off).show();
    }
}