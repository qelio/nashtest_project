package com.svdprog.testmatica.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svdprog.testmatica.teacher.NewTeacherClass;
import com.svdprog.testmatica.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// This adapter is intended for a section with the assignment of a specific test to a class...

public class SetActiveTestAdapter extends ArrayAdapter<NewTeacherClass> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<NewTeacherClass> contestList;
    public Context context;
    public ProgressBar progressBar;
    public TextView textView1;
    public ListView listView;
    public String login = "", password = "";
    public int id_test;
    public SetActiveTestAdapter(Context context, int resource, ArrayList<NewTeacherClass> classes, ProgressBar progressBar, ListView listView, String login, String password, TextView textView1, int id_test) {
        super(context, resource, classes);
        this.contestList = classes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.progressBar = progressBar;
        this.listView = listView;
        this.login = login;
        this.password = password;
        this.textView1 = textView1;
        this.id_test = id_test;
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
        final NewTeacherClass contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.delete_class.setVisibility(View.GONE);
        viewHolder.people_class.setText("Назначить тест");
        viewHolder.title_class.setText("Название класса: " + contest.getTitle());
        viewHolder.code_class.setText(android.text.Html.fromHtml("Код для присоединения: <b>" + contest.getNTcode() + "</b>"));
        // Starting the flow when you click on the button assigning a certain test to a certain class:
        viewHolder.people_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetActiveTest(login, password, id_test, contest.getId()).execute("https://testmatica.ru/api/service_active_tests/set_active_test.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            }
        });
        return convertView;
    }
    private class ViewHolder {
        final Button people_class;
        final Button delete_class;
        final TextView title_class;
        final TextView code_class;
        ViewHolder(View view){
            people_class = view.findViewById(R.id.people_class);
            delete_class = view.findViewById(R.id.delete_class);
            title_class = view.findViewById(R.id.title_class);
            code_class = view.findViewById(R.id.code_class);
        }
    }
    // A thread for sending a command to assign a specific test to a specific class:
    private class SetActiveTest extends AsyncTask<String, String, String> {
        public String login = "", password = "";
        public int set_test, set_class;
        public SetActiveTest(String login, String password, int set_test, int set_class){
            this.login = login;
            this.password = password;
            this.set_test = set_test;
            this.set_class = set_class;
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&set_test=" + this.set_test + "&set_class=" + this.set_class).getBytes());
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
            // Exit from the activity when the server request is successful:
            ((Activity) context).finish();
        }
    }
}
