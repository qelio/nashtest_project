package com.svdprog.testmatica.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svdprog.testmatica.teacher.ActiveTestForTeacher;
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
import java.util.ArrayList;

// This adapter is designed to display a list of active tests in the graphical interface...

public class AdapterActiveTestForTeacher extends ArrayAdapter<ActiveTestForTeacher> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ActiveTestForTeacher> contestList;
    public Context context;
    public boolean isAdded;
    // Initialization of some elements from past activity, as well as the user's password and login:
    public ProgressBar progressBar;
    public ListView listView;
    public TextView textView;
    public String login = "", password = "";
    public int id_test;
    public AdapterActiveTestForTeacher(Context context, int resource, ArrayList<ActiveTestForTeacher> contests, boolean isAdded, ListView listView, ProgressBar progressBar, TextView textView, int id_test, String login, String password) {
        super(context, resource, contests);
        this.contestList = contests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.isAdded = isAdded;
        this.progressBar = progressBar;
        this.listView = listView;
        this.textView = textView;
        this.id_test = id_test;
        this.login = login;
        this.password = password;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ActiveTestForTeacher contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.button.setVisibility(View.VISIBLE);
        viewHolder.text.setVisibility(View.GONE);
        // Processing of the button with the added active test at the user:
        if (isAdded == true){
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteAddedQuestions(listView, progressBar, textView, id_test, contest.getActive_gen(), login, password).execute("https://testmatica.ru/api/service_active_tests/composition_active.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                }
            });
        }
        // Otherwise, if the task has not been added yet, the button visibility is created and it is possible to add the task by the teacher:
        else{
            viewHolder.button.setText("Добавить задание");
            if (contest.getAdd() == 1){
                viewHolder.button.setVisibility(View.GONE);
                viewHolder.text.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AddQuestion(listView, progressBar, textView, id_test, contest.getId(), login, password).execute("https://testmatica.ru/api/service_active_tests/composition_active.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                        contest.setAdd(1);
                        viewHolder.button.setVisibility(View.GONE);
                        viewHolder.text.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        viewHolder.fine.setText(android.text.Html.fromHtml("<i>Выполнили <u>верно</u>:</i> <b>" + Integer.toString(contest.getFine()) + getEmoji(0x1F9D1) + "</b> "));
        viewHolder.title.setText(android.text.Html.fromHtml("<b>" + contest.getTitle() + "</b>"));
        viewHolder.id.setText(android.text.Html.fromHtml("<b>Идентификатор вопроса:</b> " + Integer.toString(contest.getId())));
        viewHolder.any.setText(android.text.Html.fromHtml("<b>Правильный ответ:</b> " + contest.getAny()));
        // The Picasso one-liner is used to quickly load an image from the network without having to create a separate function in the stream:
        Picasso.with(context).load(contest.getCond()).into(viewHolder.image);
        return convertView;
    }
    private class ViewHolder {
        // Initialization of the graphical interface elements of this block:
        final TextView fine, id, title, any, text;
        final Button button;
        final ImageView image;
        ViewHolder(View view){
        fine = view.findViewById(R.id.fine);
        id = view.findViewById(R.id.id);
        title = view.findViewById(R.id.title);
        any = view.findViewById(R.id.any);
        text = view.findViewById(R.id.text);
        button = view.findViewById(R.id.button);
        image = view.findViewById(R.id.image);
        }
    }
    // A thread for adding a question, when the user clicks the corresponding button:
    private class AddQuestion extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public int id_test, new_question;
        public AddQuestion(ListView contestList, ProgressBar progressBar, TextView textView, int id_test, int new_question, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView = textView;
            this.login = login;
            this.password = password;
            this.id_test = id_test;
            this.new_question = new_question;
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&id_test=" + this.id_test + "&new_question=" + this.new_question).getBytes());
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
            }
    }
    // The thread for deleting the added question, when the user clicks the corresponding button:
    private class DeleteAddedQuestions extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public int id_test, delete_question;

        public DeleteAddedQuestions(ListView contestList, ProgressBar progressBar, TextView textView, int id_test, int delete_question, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView = textView;
            this.login = login;
            this.password = password;
            this.id_test = id_test;
            this.delete_question = delete_question;
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&id_test=" + this.id_test + "&delete_question=" + this.delete_question).getBytes());
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
            new GetAddedQuestions(contestList, progressBar, textView, id_test, login, password).execute("https://testmatica.ru/api/service_active_tests/composition_active.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
    }
    // Stream to get the added question questions by default:
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
            contestList.setAdapter(null);
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
            // Checking the correctness and availability of the received line from the respondent on the site:
            try{
                ArrayList<ActiveTestForTeacher> classes = new ArrayList<ActiveTestForTeacher>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("composition_active");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        classes.add(new ActiveTestForTeacher(jsonObject1.getInt("active_gen"), jsonObject1.getInt("id"), jsonObject1.getString("title"), jsonObject1.getString("cond"), jsonObject1.getString("any"), jsonObject1.getInt("levelxp"), jsonObject1.getInt("class"), jsonObject1.getInt("typetest"), jsonObject1.getInt("fine"), jsonObject1.getInt("seen")));
                    }
                    AdapterActiveTestForTeacher adapter = new AdapterActiveTestForTeacher(context, R.layout.active_test_for_teacher, classes, true, contestList, progressBar, textView, id_test, login, password);
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
    // A small and functional method taken on the Internet to convert the code into the corresponding emoji :)
    public String getEmoji(int uc){
        return new String(Character.toChars(uc));
    }
}
