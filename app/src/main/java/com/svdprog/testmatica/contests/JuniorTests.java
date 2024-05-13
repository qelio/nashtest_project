package com.svdprog.testmatica.contests;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

// Activity to display a list by class of all junior thematic tests in the selected subject area...

public class JuniorTests extends AppCompatActivity {
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;
    ProgressBar progressBar4;
    ListView contestList1;
    ListView contestList2;
    ListView contestList3;
    ListView contestList4;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    LinearLayout general_linear;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junior_tests);
        Bundle arguments = getIntent().getExtras();
        SchoolSubject school_subject = (SchoolSubject) arguments.getSerializable(SchoolSubject.class.getSimpleName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(school_subject.getName_subject() + ". Начальные классы");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
        contestList1 = findViewById(R.id.listview1);
        contestList2 = findViewById(R.id.listview2);
        contestList3 = findViewById(R.id.listview3);
        contestList4 = findViewById(R.id.listview4);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        general_linear = (LinearLayout) findViewById(R.id.general_linear);
        // Creating an upper dock for the selected thematic area to be divided into certain types:
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        if (school_subject.getAvail_tests(1)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(1)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the first type in the thematic direction:
            new GetContest(contestList1, progressBar1, school_subject.getAny_var(1)).execute(temp);
        }
        else{
            textView1.setVisibility(View.VISIBLE);
            contestList1.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("1 класс");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        if (school_subject.getAvail_tests(2)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(2)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the second type in the thematic direction:
            new GetContest(contestList2, progressBar2, school_subject.getAny_var(2)).execute(temp);
        }
        else{
            textView2.setVisibility(View.VISIBLE);
            contestList2.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("2 класс");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        if (school_subject.getAvail_tests(3)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(3)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the third type in the thematic direction:
            new GetContest(contestList3, progressBar3, school_subject.getAny_var(3)).execute(temp);
        }
        else{
            textView3.setVisibility(View.VISIBLE);
            contestList3.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("3 класс");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        if (school_subject.getAvail_tests(4)){
            String temp = "https://testmatica.ru/api/get_contest.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&numberclass=" + Integer.toString(school_subject.getNumber_class(4)) + "&typetest=" + Integer.toString(school_subject.getType_tests());
            // Start of the background stream to display a list of all tests of the fourth type in the thematic direction:
            new GetContest(contestList4, progressBar4, school_subject.getAny_var(4)).execute(temp);
        }
        else{
            textView4.setVisibility(View.VISIBLE);
            contestList4.setVisibility(View.GONE);
        }
        tabSpec.setContent(R.id.tab4);
        tabSpec.setIndicator("4 класс");
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
                Intent intent = new Intent(JuniorTests.this, TitleTest.class);
                if (any_var == false){
                    // Calling the adapter to display all available thematic tests in this type for the selected thematic direction:
                    ContestAdapter adapter = new ContestAdapter(JuniorTests.this, R.layout.row, contests, intent);
                    contestList.setAdapter(adapter);
                }
                if (any_var == true){
                    // Calling the adapter to display all available thematic tests in this type for the selected thematic direction:
                    ContestAdapterSecond adapter = new ContestAdapterSecond(JuniorTests.this, R.layout.row_second, contests, intent);
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