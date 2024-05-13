package com.svdprog.testmatica.contests;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.svdprog.testmatica.R;
import com.svdprog.testmatica.adapters.AllContestsAdapter;

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

// This activity is designed to display a complete list of completed tests from the user:

public class AllContests extends AppCompatActivity {
    ListView contests_listview;
    ProgressBar contests_progressbar;
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contests);
        Bundle arguments = getIntent().getExtras();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        contests_listview = (ListView) findViewById(R.id.contests_listview);
        contests_progressbar = (ProgressBar) findViewById(R.id.contests_progressbar);
        textView1 = (TextView) findViewById(R.id.textView1);
        // Separation of activity functions when viewed on behalf of the administrator, a full report of the testing system is displayed:
        if (arguments.getInt("user_id") == 0){
            Toast toast = Toast.makeText(AllContests.this,
                    "В данном отчёте слишком много информации, выполняет загрузка последних 100 записей...", Toast.LENGTH_LONG);
            toast.show();
            actionBar.setTitle("Отчет системы автоматического тестирования");
            new GetResultTests(contests_listview, contests_progressbar, textView1).execute("https://testmatica.ru/api/get_result_test.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
        else{
            // Separation of activity functions when viewed on behalf of a regular user, a list of passed tests of this user is displayed:
            actionBar.setTitle("Полный список тестов");
            new GetResultTests(contests_listview, contests_progressbar, textView1).execute("https://testmatica.ru/api/get_result_test.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&user_id=" + arguments.get("user_id"));
        }
    }
    // A stream for getting a list of passed tests with the specified parameters from the site database:
    private class GetResultTests extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public GetResultTests(ListView contestList, ProgressBar progressBar, TextView textView1){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
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
                ArrayList<ResultContest> contests = new ArrayList<ResultContest>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result_test");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding a list of completed tests with the specified parameters from the site database to the general array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        int user_id = jsonObject1.getInt("userid");
                        String title = jsonObject1.getString("title");
                        String date = jsonObject1.getString("date");
                        String kerm = jsonObject1.getString("kerm");
                        contests.add(new ResultContest(id, user_id, title, date, kerm));
                    }
                    // Calling the adapter to display a list of passed tests with the specified parameters from the site database:
                    AllContestsAdapter adapter = new AllContestsAdapter(AllContests.this, R.layout.result_test, contests);
                    contestList.setAdapter(adapter);
                }
                else{
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