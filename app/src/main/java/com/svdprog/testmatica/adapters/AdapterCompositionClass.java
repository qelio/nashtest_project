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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svdprog.testmatica.contests.AllContests;
import com.svdprog.testmatica.R;
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

// This adapter is designed to display the composition of the class in the activity (session) of the teacher...

public class AdapterCompositionClass extends ArrayAdapter<UserProfile> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<UserProfile> contestList;
    public Intent intent;
    public Context context;
    public ProgressBar progressBar;
    public TextView textView1;
    public ListView listView;
    public AdapterCompositionClass(Context context, int resource, ArrayList<UserProfile> users, Intent intent, ProgressBar progressBar, ListView listView, TextView textView1) {
        super(context, resource, users);
        this.contestList = users;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.intent = intent;
        this.context = context;
        this.progressBar = progressBar;
        this.listView = listView;
        this.textView1 = textView1;
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
        final UserProfile user = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.profile_init.setText(user.getName() + " " + user.getFamily());
        viewHolder.profile_login.setText(android.text.Html.fromHtml("Логин: " + user.getLogin()));
        // The Picasso one-liner is used to quickly load an image from the network without having to create a separate function in the stream:
        Picasso.with(context).load(user.getImg_link()).into(viewHolder.profile_imghref);
        // Handler for removing a user from a teacher's class with an additional confirmation in the dialog box:
        viewHolder.delete_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Открепление ученика")
                        .setMessage("Вы действительно хотите открепить ученика от данного класса?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Открепить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteComposition(listView, progressBar, textView1, "https://testmatica.ru/api/composition_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&idclass=" + user.getIdclass()).execute("https://testmatica.ru/api/composition_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&delete_iduser=" + user.getId());
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Отмена", null).setIcon(R.drawable.nodone).show();
            }
        });
        viewHolder.all_contests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("user_id", user.getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    private class ViewHolder {
        // Initialization of the graphical interface elements of this block:
        final TextView profile_init;
        final TextView profile_login;
        final Button all_contests;
        final Button delete_people;
        final ImageView profile_imghref;
        ViewHolder(View view){
            profile_login = view.findViewById(R.id.profile_login);
            profile_init = view.findViewById(R.id.profile_init);
            all_contests = view.findViewById(R.id.all_contests);
            delete_people = view.findViewById(R.id.delete_people);
            profile_imghref= view.findViewById(R.id.profile_imghref);
        }
    }
    // Getting data about the composition of the teacher's class from the website database in the stream:
    private class GetComposition extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public GetComposition(ListView contestList, ProgressBar progressBar, TextView textView1){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contestList.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
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
            contestList.setVisibility(View.VISIBLE);
            // Checking the correctness and availability of the received line from the respondent on the site:
            try{
                ArrayList<UserProfile> users = new ArrayList<UserProfile>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
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
                        users.add(user);
                    }
                    Intent intent = new Intent(context, AllContests.class);
                    AdapterCompositionClass adapter = new AdapterCompositionClass(context, R.layout.composition_class, users, intent, progressBar, contestList, textView1);
                    contestList.setAdapter(adapter);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    textView1.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // A thread for removing a student from the class, when the user (teacher) clicks the appropriate button:
    private class DeleteComposition extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String finish;
        public DeleteComposition(ListView contestList, ProgressBar progressBar, TextView textView1, String finish){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.finish = finish;
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
            contestList.setAdapter(null);
            // Updating the list of students from the site database:
            new GetComposition(contestList, progressBar, textView1).execute(finish);
        }
    }
}
