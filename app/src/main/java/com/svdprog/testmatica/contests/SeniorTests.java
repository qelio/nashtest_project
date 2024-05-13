package com.svdprog.testmatica.contests;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.student.SchoolSubject;
import com.svdprog.testmatica.adapters.ContestAdapter;
import com.svdprog.testmatica.adapters.ContestAdapterSecond;

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

// Activity to display a list by class of all senior thematic tests in the selected subject area...

public class SeniorTests extends AppCompatActivity {
    ProgressBar progressBar5;
    ProgressBar progressBar6;
    ProgressBar progressBar7;
    ProgressBar progressBar8;
    ProgressBar progressBar9;
    ProgressBar progressBar10;
    ProgressBar progressBar11;
    ListView contestList5;
    ListView contestList6;
    ListView contestList7;
    ListView contestList8;
    ListView contestList9;
    ListView contestList10;
    ListView contestList11;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    TextView textView9;
    TextView textView10;
    TextView textView11;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_tests);
        Bundle arguments = getIntent().getExtras();
        SchoolSubject school_subject = (SchoolSubject) arguments.getSerializable(SchoolSubject.class.getSimpleName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(school_subject.getName_subject() + ". Старшие классы");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
        progressBar6 = (ProgressBar) findViewById(R.id.progressBar6);
        progressBar7 = (ProgressBar) findViewById(R.id.progressBar7);
        progressBar8 = (ProgressBar) findViewById(R.id.progressBar8);
        progressBar9 = (ProgressBar) findViewById(R.id.progressBar9);
        progressBar10 = (ProgressBar) findViewById(R.id.progressBar10);
        progressBar11 = (ProgressBar) findViewById(R.id.progressBar11);
        contestList5 = findViewById(R.id.listview5);
        contestList6 = findViewById(R.id.listview6);
        contestList7 = findViewById(R.id.listview7);
        contestList8 = findViewById(R.id.listview8);
        contestList9 = findViewById(R.id.listview9);
        contestList10 = findViewById(R.id.listview10);
        contestList11 = findViewById(R.id.listview11);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView11 = (TextView) findViewById(R.id.textView11);
        // Creating an upper dock for the selected thematic area to be divided into certain types:
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag5");
        if (school_subject.getAvail_tests(5)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(5)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the first type in the thematic direction:
            new GetContest(contestList5, progressBar5, school_subject.getAny_var(5)).execute(temp);
        }
        else{
            textView5.setVisibility(View.VISIBLE);
            contestList5.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab5);
        tabSpec.setIndicator("5");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag6");
        if (school_subject.getAvail_tests(6)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(6)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the second type in the thematic direction:
            new GetContest(contestList6, progressBar6, school_subject.getAny_var(6)).execute(temp);
        }
        else{
            textView6.setVisibility(View.VISIBLE);
            contestList6.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab6);
        tabSpec.setIndicator("6");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag7");
        if (school_subject.getAvail_tests(7)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(7)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the third type in the thematic direction:
            new GetContest(contestList7, progressBar7, school_subject.getAny_var(7)).execute(temp);
        }
        else{
            textView7.setVisibility(View.VISIBLE);
            contestList7.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab7);
        tabSpec.setIndicator("7");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag8");
        if (school_subject.getAvail_tests(8)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(8)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the fourth type in the thematic direction:
            new GetContest(contestList8, progressBar8, school_subject.getAny_var(8)).execute(temp);
        }
        else{
            textView8.setVisibility(View.VISIBLE);
            contestList8.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab8);
        tabSpec.setIndicator("8");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag9");
        if (school_subject.getAvail_tests(9)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(9)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the fifth type in the thematic direction:
            new GetContest(contestList9, progressBar9, school_subject.getAny_var(9)).execute(temp);
        }
        else{
            textView9.setVisibility(View.VISIBLE);
            contestList9.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab9);
        tabSpec.setIndicator("9");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag10");
        if (school_subject.getAvail_tests(10)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(10)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the sixth type in the thematic direction:
            new GetContest(contestList10, progressBar10, school_subject.getAny_var(10)).execute(temp);
        }
        else{
            textView10.setVisibility(View.VISIBLE);
            contestList10.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab10);
        tabSpec.setIndicator("10");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag11");
        if (school_subject.getAvail_tests(11)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(11)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the seventh type in the thematic direction:
            new GetContest(contestList11, progressBar11, school_subject.getAny_var(11)).execute(temp);
        }
        else{
            textView11.setVisibility(View.VISIBLE);
            contestList11.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab11);
        tabSpec.setIndicator("11");
        tabHost.addTab(tabSpec);
        // Setting the currently selected tab:
        tabHost.setCurrentTab(arguments.getInt("current_tab"));
    }
    // A stream for downloading available thematic tests in this thematic direction according to the selected type of test:
    private class GetContest extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public boolean any_var;
        // Usage scheme: "0" - 1 variant, "1" - 2 variants
        public GetContest(ListView contestList, ProgressBar progressBar, boolean any_var){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.any_var = any_var;
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
            try{
                ArrayList<Contest> contests = new ArrayList<Contest>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("unitestv1");
                for(int i = 0; i < jsonArray.length(); i++){
                    // Adding all available thematic tests in this type in the selected thematic direction to the general array:
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    int id = jsonObject1.getInt("id");
                    String title = jsonObject1.getString("title");
                    String var1 = jsonObject1.getString("var1");
                    String var2 = jsonObject1.getString("var2");
                    int ed = jsonObject1.getInt("ed");
                    int levelxp = jsonObject1.getInt("levelxp");
                    int numberclass = jsonObject1.getInt("numberclass");
                    int typetest = jsonObject1.getInt("typetest");
                    contests.add(new Contest(id, title, var1, var2, ed, levelxp, numberclass, typetest));
                }
                // The choice depends on the number of options in the thematic test (the type of adapter will depend):
                Intent intent = new Intent(SeniorTests.this, TitleTest.class);
                if (any_var == false){
                    // Calling the adapter to display all available thematic tests in this type for the selected thematic direction:
                    ContestAdapter adapter = new ContestAdapter(SeniorTests.this, R.layout.row, contests, intent);
                    contestList.setAdapter(adapter);
                }
                if (any_var == true){
                    // Calling the adapter to display all available thematic tests in this type for the selected thematic direction:
                    ContestAdapterSecond adapter = new ContestAdapterSecond(SeniorTests.this, R.layout.row_second, contests, intent);
                    contestList.setAdapter(adapter);
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