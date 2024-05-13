package com.svdprog.testmatica.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svdprog.testmatica.contests.ListAssignedTests;
import com.svdprog.testmatica.active.PrepareActiveTest;
import com.svdprog.testmatica.R;
import com.svdprog.testmatica.active.SetActiveTest;
import com.svdprog.testmatica.active.StructureActiveTest;

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

// The adapter is designed to display and edit the created active tests:

public class AdapterPrepareActiveTest extends ArrayAdapter<PrepareActiveTest> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<PrepareActiveTest> contestList;
    public Context context;
    public ProgressBar progressBar;
    public ListView listView;
    public TextView textView;
    public String login = "", password = "";
    public AdapterPrepareActiveTest(Context context, int resource, ArrayList<PrepareActiveTest> contests, ListView listView, ProgressBar progressBar, TextView textView, String login, String password) {
        super(context, resource, contests);
        this.contestList = contests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.progressBar = progressBar;
        this.listView = listView;
        this.textView = textView;
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
        final PrepareActiveTest contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.id_test.setText(android.text.Html.fromHtml("<b>ID:</b> " + Integer.toString(contest.getId_test())));
        viewHolder.date_test.setText(android.text.Html.fromHtml("<b>Дата:</b> " + contest.getDate_test()));
        viewHolder.title_test.setText(android.text.Html.fromHtml("<b>" + contest.getName_test() + "</b>"));
        viewHolder.for_mark.setText(android.text.Html.fromHtml("<b>Комментарий к тесту:</b> " + contest.getCom_test()));
        // Calling the next activity of the application, when you click on the button to view the results of the active test:
        viewHolder.result_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, ListAssignedTests.class);
                intent1.putExtra("id_test", contest.getId_test());
                intent1.putExtra("title_test", contest.getName_test());
                context.startActivity(intent1);
            }
        });
        // Calling the next activity of the application, when you click on the active test assignment button:
        viewHolder.set_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(context, SetActiveTest.class);
                intent2.putExtra("id_test", contest.getId_test());
                context.startActivity(intent2);
            }
        });
        // Calling the next activity of the application, when you click on the button to change the active test:
        viewHolder.edit_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(context, StructureActiveTest.class);
                intent3.putExtra("id_test", contest.getId_test());
                context.startActivity(intent3);
            }
        });
        // Opening a confirmation dialog box when clicking on the delete active test button:
        viewHolder.delete_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Удаление теста")
                        .setMessage("Вы действительно хотите удалить данный активный тест?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteActiveTest(listView, progressBar, textView, login, password, contest.getId_test()).execute("https://testmatica.ru/api/service_active_tests/prepare_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Отмена", null).setIcon(R.drawable.nodone).show();
                }
        });
        return convertView;
    }
    private class ViewHolder {
        final TextView id_test, date_test, title_test, for_mark;
        final ImageButton result_active, set_active, edit_active, delete_active;
        ViewHolder(View view){
            id_test = view.findViewById(R.id.id_test);
            date_test = view.findViewById(R.id.date_test);
            title_test = view.findViewById(R.id.title_test);
            for_mark = view.findViewById(R.id.for_mark);
            result_active = view.findViewById(R.id.result_active);
            set_active = view.findViewById(R.id.set_active);
            edit_active = view.findViewById(R.id.edit_active);
            delete_active = view.findViewById(R.id.delete_active);
        }
    }
    // A thread for sending a delete command to a specific active test:
    private class DeleteActiveTest extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView;
        public String login = "", password = "";
        public int delete_test;
        public DeleteActiveTest(ListView contestList, ProgressBar progressBar, TextView textView, String login, String password, int delete_test){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView = textView;
            this.login = login;
            this.password = password;
            this.delete_test = delete_test;
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&delete_test=" + this.delete_test).getBytes());
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
            // Updating the list of available active tests (not deleted):
            new GetActiveTest(contestList, progressBar, textView, login, password).execute("https://testmatica.ru/api/service_active_tests/prepare_active_tests.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
    }
    // Stream for updating the list of available active tests from the user:
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
                        // Adding a list of available active tests from a user to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        classes.add(new PrepareActiveTest(jsonObject1.getInt("id_test"), jsonObject1.getString("name_test"), jsonObject1.getString("com_test"), jsonObject1.getInt("id_user"), jsonObject1.getString("date_test"), jsonObject1.getInt("for_five"), jsonObject1.getInt("for_four"), jsonObject1.getInt("for_three"), jsonObject1.getInt("delete_test"), jsonObject1.getInt("class"), jsonObject1.getInt("typetest")));
                    }
                    // Calling the adapter to display a list of available active tests from the user:
                    AdapterPrepareActiveTest adapter = new AdapterPrepareActiveTest(context, R.layout.prepare_active_test, classes, contestList, progressBar, textView, login, password);
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
    // A small function for the correct display of emojis in text layouts:
    public String getEmoji(int uc){
        return new String(Character.toChars(uc));
    }
}
