package com.svdprog.testmatica.student;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.adapters.ActiveContestAdapter;
import com.svdprog.testmatica.adapters.ActiveFinishContestAdapter;
import com.svdprog.testmatica.active.ActiveContest;
import com.svdprog.testmatica.contests.TitleTest;
import com.svdprog.testmatica.user.UserProfile;

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

// Activity for joining a class by code, selecting an active test, viewing completed tests...

public class StudentClass extends AppCompatActivity {
    LinearLayout linear_class1, linear_class2;
    RelativeLayout linear_class3;
    SharedPreferences settings_profile;
    EditText class_code;
    TextView error_code;
    Button class_button;
    // Objects responsible for the management and viewing interface of the class:
    TextView class_text, textView2, textView3;
    ProgressBar progressBar2, progressBar3;
    ListView listView2, listView3;
    Button class_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Мои задания");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        settings_profile = getSharedPreferences("settings_profile", MODE_PRIVATE);
        // Initialization of elements of the graphical interface of the mobile application:
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        listView2 = (ListView) findViewById(R.id.listview2);
        listView3 = (ListView) findViewById(R.id.listview3);
        class_text = (TextView) findViewById(R.id.class_text);
        class_exit = (Button) findViewById(R.id.class_exit);
        linear_class1 = (LinearLayout) findViewById(R.id.linear_class1);
        linear_class2 = (LinearLayout) findViewById(R.id.linear_class2);
        linear_class3 = (RelativeLayout) findViewById(R.id.linear_class3);
        class_code = (EditText) findViewById(R.id.class_code);
        error_code = (TextView) findViewById(R.id.error_code);
        class_button = (Button) findViewById(R.id.class_button);
        // Starting the flow of getting information about the class of this user:
        new GetUserClass(settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_user.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Сведения");
        // Starting the flow of getting information about the class of this user and information about it:
        new GetInfoClass(settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_info_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        // Calling a dialog box to confirm the exit from the class:
        class_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StudentClass.this)
                        .setTitle("Покинуть класс")
                        .setMessage("Вы действительно хотите покинуть данный класс?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Покинуть", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteClass(settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_info_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                                new GetUserClass(settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_user.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Отмена", null).setIcon(R.drawable.exit_class).show();
                }
        });
        // Setting up the top menu to switch between different pages:
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        new GetActiveContest(listView2, progressBar2, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Активные тесты");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag3");
        new GetFinishContest(listView3, progressBar3, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Решенные тесты");
        tabHost.addTab(tabSpec);
        // Setting the currently selected tab:
        tabHost.setCurrentTab(0);
    }
    @Override
    protected void onResume() {
        // When returning to this activity from a test, an adapter update of both solved tests is required:
        super.onResume();
        listView2.setAdapter(null);
        new GetActiveContest(listView2, progressBar2, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        listView3.setAdapter(null);
        new GetFinishContest(listView3, progressBar3, settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
    }
    // A stream for getting information about completed tests from the database:
    private class GetFinishContest extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public String login = "";
        public String password = "";
        public GetFinishContest(ListView contestList, ProgressBar progressBar, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
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
                ArrayList<ActiveContest> contests = new ArrayList<ActiveContest>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result_test");
                int quanity_array = 0;
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Getting information about completed tests from a database to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.getInt("delete_test") == 0 && jsonObject1.getInt("status") == 1) {
                            contests.add(new ActiveContest(jsonObject1.getInt("id"), jsonObject1.getInt("id_test"), jsonObject1.getString("name_test"), jsonObject1.getString("com_test"), jsonObject1.getString("date_test"), jsonObject1.getInt("quanity"), jsonObject1.getInt("percent"), jsonObject1.getInt("status"), jsonObject1.getInt("mark"), jsonObject1.getInt("delete_test"), jsonObject1.getInt("for_five"), jsonObject1.getInt("for_four"), jsonObject1.getInt("for_three"), jsonObject1.getInt("class"), jsonObject1.getInt("type_test")));
                            quanity_array += 1;
                        }
                    }
                    // Calling the adapter to display information about completed tests from the database:
                    ActiveFinishContestAdapter adapter = new ActiveFinishContestAdapter(StudentClass.this, R.layout.result_active_test, contests);
                    if(quanity_array != 0) {
                        progressBar.setVisibility(View.GONE);
                        contestList.setVisibility(View.VISIBLE);
                        contestList.setAdapter(adapter);
                    }
                    else{
                        // If there is no information, that is, there are no completed tests, we inform the user about it:
                        progressBar.setVisibility(View.GONE);
                        textView3.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    textView3.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // A stream for getting a list of active tests from a user from the database:
    private class GetActiveContest extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public String login = "";
        public String password = "";
        public GetActiveContest(ListView contestList, ProgressBar progressBar, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
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
                ArrayList<ActiveContest> contests = new ArrayList<ActiveContest>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result_test");
                int quanity_array = 0;
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding a list of active user tests from a database to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.getInt("delete_test") == 0 && jsonObject1.getInt("status") == 0) {
                            contests.add(new ActiveContest(jsonObject1.getInt("id"), jsonObject1.getInt("id_test"), jsonObject1.getString("name_test"), jsonObject1.getString("com_test"), jsonObject1.getString("date_test"), jsonObject1.getInt("quanity"), jsonObject1.getInt("percent"), jsonObject1.getInt("status"), jsonObject1.getInt("mark"), jsonObject1.getInt("delete_test"), jsonObject1.getInt("for_five"), jsonObject1.getInt("for_four"), jsonObject1.getInt("for_three"), jsonObject1.getInt("class"), jsonObject1.getInt("type_test")));
                            quanity_array += 1;
                        }
                    }
                    Intent intent = new Intent(StudentClass.this, TitleTest.class);
                    ActiveContestAdapter adapter = new ActiveContestAdapter(StudentClass.this, R.layout.row, contests, intent);
                    if(quanity_array != 0) {
                        progressBar.setVisibility(View.GONE);
                        contestList.setVisibility(View.VISIBLE);
                        contestList.setAdapter(adapter);
                    }
                    else{
                        // If there is no information, we inform the user about it:
                        progressBar.setVisibility(View.GONE);
                        textView2.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    textView2.setVisibility(View.VISIBLE);
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
    // The thread for getting the class to which the user is attached:
    private class GetUserClass extends AsyncTask<String, String, String> {
        public String login = "";
        public String password = "";
        public GetUserClass(String login, String password){
            this.login = login;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linear_class3.setVisibility(View.VISIBLE);
            error_code.setVisibility(View.GONE);
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
                // Next comes the standard filling in of all fields of the personal account section. But only if the download was successful and the account is logged in:
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
                if (user.getIdclass() != 0){
                    linear_class3.setVisibility(View.GONE);
                    linear_class2.setVisibility(View.VISIBLE);
                }
                else{
                    linear_class3.setVisibility(View.GONE);
                    linear_class1.setVisibility(View.VISIBLE);
                    class_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // When joining a class, the user needs to update the information in the database:
                            new SetClass(user.getId(), class_code.getText().toString()).execute("https://testmatica.ru/api/set_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // A thread for sending a command to join a class of a certain user:
    private class SetClass extends AsyncTask<String, String, String> {
        public int user_id;
        public String class_code1 = "";
        public SetClass(int user_id, String class_code){
            this.user_id = user_id;
            this.class_code1 = class_code;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linear_class1.setVisibility(View.GONE);
            linear_class3.setVisibility(View.VISIBLE);
            error_code.setVisibility(View.GONE);
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
                    urlConnection.getOutputStream().write( ("user_id=" + this.user_id + "&class_code=" + this.class_code1).getBytes());
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
                JSONArray jsonArray = jsonObject.getJSONArray("set_class");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                if (jsonObject1.getBoolean("status")){
                    // Updating information about our user's class:
                    new GetInfoClass(settings_profile.getString("login", "-"), settings_profile.getString("password", "-")).execute("https://testmatica.ru/api/get_info_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                    linear_class3.setVisibility(View.GONE);
                    linear_class2.setVisibility(View.VISIBLE);
                }
                else{
                    // If the connection was unsuccessful, we inform the user about it and activate the re-connection:
                    linear_class3.setVisibility(View.GONE);
                    linear_class1.setVisibility(View.VISIBLE);
                    error_code.setVisibility(View.VISIBLE);
                    class_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // When joining a class, the user needs to update the information in the database:
                            new SetClass(user_id, class_code.getText().toString()).execute("https://testmatica.ru/api/set_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // A stream for getting information about a class from a database:
    private class GetInfoClass extends AsyncTask<String, String, String> {
        public String login = "";
        public String password = "";
        public GetInfoClass(String login, String password){
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
                JSONArray jsonArray = jsonObject.getJSONArray("info_class");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                // Showing the user which class he is attached to:
                String class_view = "На данный момент вы привязаны к классу <b>" + jsonObject1.getString("title") + "</b> <i>(Код для присоединения: " + jsonObject1.getString("NTcode") + ")</i>. Создатель класса: <b>" + jsonObject1.getString("nameteacher") + " " + jsonObject1.getString("familyteacher") + "</b>. При желании вы можете покинуть класс!";
                class_text.setText(android.text.Html.fromHtml(class_view));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // A thread for detaching a user from the current class:
    private class DeleteClass extends AsyncTask<String, String, String> {
        public String login = "";
        public String password = "";
        public DeleteClass(String login, String password){
            this.login = login;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linear_class2.setVisibility(View.GONE);
            linear_class3.setVisibility(View.VISIBLE);
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&delete=" + "true").getBytes());
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
            linear_class3.setVisibility(View.GONE);
            linear_class1.setVisibility(View.VISIBLE);
        }
    }
}