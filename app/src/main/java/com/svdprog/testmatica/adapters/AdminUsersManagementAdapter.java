package com.svdprog.testmatica.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.svdprog.testmatica.R;
import com.svdprog.testmatica.user.UserManagement;

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

// Adapter for displaying a list of users from the site database...

public class AdminUsersManagementAdapter extends ArrayAdapter<UserManagement> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<UserManagement> contestList;
    public Context context;
    public ProgressBar progressBar;
    public ListView listView;
    public TextView textView;
    public String login, password;
    public AdminUsersManagementAdapter(Context context, int resource, ArrayList<UserManagement> contests, String login, String password, ListView listView, ProgressBar progressBar, TextView textView) {
        super(context, resource, contests);
        this.contestList = contests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.login = login;
        this.password = password;
        this.progressBar = progressBar;
        this.listView = listView;
        this.textView = textView;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final UserManagement contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.delete_user.setBackgroundColor(Color.parseColor("#EB4C42"));
        viewHolder.type_user.setBackgroundColor(context.getResources().getColor(R.color.paris_green));
        // Changing the button action depending on the type of account being changed:
        if (contest.getAdminuser() == 1){
            viewHolder.adminuser.setText(android.text.Html.fromHtml("<b>Права администратора:</b> да"));
            viewHolder.rights_user.setText("Изъять права администратора");
        }
        else{
            viewHolder.adminuser.setText(android.text.Html.fromHtml("<b>Права администратора:</b> нет"));
            viewHolder.rights_user.setText("Выдать права администратора");
        }
        viewHolder.id_user.setText(android.text.Html.fromHtml("<b>ID:</b> " + Integer.toString(contest.getId())));
        viewHolder.pol.setText(android.text.Html.fromHtml("<b>Пол:</b> " + contest.getPol()));
        viewHolder.profile_init.setText(android.text.Html.fromHtml(contest.getName() + " " + contest.getFamily()));
        viewHolder.profile_login.setText(android.text.Html.fromHtml("<b>Логин:</b> " + contest.getLogin()));
        // The Picasso one-liner is used to quickly load an image from the network without having to create a separate function in the stream:
        Picasso.with(context).load(contest.getImg_link()).into(viewHolder.profile_imghref);
        viewHolder.email.setText(android.text.Html.fromHtml("<b>E-Mail:</b> " + contest.getEmail()));
        viewHolder.city.setText(android.text.Html.fromHtml("<b>Город:</b> " + contest.getCity()));
        viewHolder.date.setText(android.text.Html.fromHtml("<b>Дата регистрации:</b> " + contest.getDate()));
        viewHolder.info.setText(android.text.Html.fromHtml("<b>Информация об устройстве:</b> " + contest.getIp() + ", " + contest.getBrowser()));
        viewHolder.level_xp.setText(android.text.Html.fromHtml("<b>Количество очков XP:</b> " + Integer.toString(contest.getLevelxp()) + " XP"));
        // Changing the button action depending on the user role of the account being changed:
        if (contest.getDolg() == 1){
            viewHolder.dolg.setText(android.text.Html.fromHtml("<b>Тип учетной записи:</b> ученическая"));
        }
        else{
            viewHolder.dolg.setText(android.text.Html.fromHtml("<b>Тип учетной записи:</b> учительская"));
        }
        // Start the stream when you click on the user rights change button:
        viewHolder.rights_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RightsUsersManagement(login, password, listView, progressBar, textView, contest.getId()).execute("https://testmatica.ru/api/admin_management/users_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            }
        });
        // Start the flow when you click on the button to change the type of user account:
        viewHolder.type_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TypeUsersManagement(login, password, listView, progressBar, textView, contest.getId()).execute("https://testmatica.ru/api/admin_management/users_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            }
        });
        // Opening a dialog box to confirm the deletion of the selected account without the possibility of recovery:
        viewHolder.delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Удаление учетной записи")
                        .setMessage("Вы действительно хотите удалить эту учетную запись?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteUsersManagement(login, password, listView, progressBar, textView, contest.getId()).execute("https://testmatica.ru/api/admin_management/users_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Отмена", null).setIcon(R.drawable.nodone).show();
            }
        });
        return convertView;
    }
    private class ViewHolder {
        final TextView id_user, pol, profile_init, profile_login, email, city, date, info, level_xp, adminuser, dolg;
        final ImageView profile_imghref;
        final Button rights_user, type_user, delete_user;
        ViewHolder(View view){
            id_user = (TextView) view.findViewById(R.id.id_user);
            pol = (TextView) view.findViewById(R.id.pol);
            profile_init = (TextView) view.findViewById(R.id.profile_init);
            profile_login = (TextView) view.findViewById(R.id.profile_login);
            email = (TextView) view.findViewById(R.id.email);
            city = (TextView) view.findViewById(R.id.city);
            date = (TextView) view.findViewById(R.id.date);
            info = (TextView) view.findViewById(R.id.info);
            level_xp = (TextView) view.findViewById(R.id.level_xp);
            adminuser = (TextView) view.findViewById(R.id.adminuser);
            dolg = (TextView) view.findViewById(R.id.dolg);
            profile_imghref = (ImageView) view.findViewById(R.id.profile_imghref);
            rights_user = (Button) view.findViewById(R.id.rights_user);
            type_user = (Button) view.findViewById(R.id.type_user);
            delete_user = (Button) view.findViewById(R.id.delete_user);
        }
    }
    private class TypeUsersManagement extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public int type_user;
        public TypeUsersManagement(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1, int type_user){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.type_user = type_user;
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
                    urlConnection.getOutputStream().write(("login=" + this.login + "&password=" + this.password + "&type_user=" + this.type_user).getBytes());
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
            // Flow for changing the type of account being modified:
            Toast toast = Toast.makeText(context,
                    "Тип учетной записи успешно изменён!", Toast.LENGTH_SHORT);
            toast.show();
            new GetUsersManagement(login, password, contestList, progressBar, textView1).execute("https://testmatica.ru/api/admin_management/users_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
    }
    // Flow for changing the access rights of the selected account:
    private class RightsUsersManagement extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public int rights_user;
        public RightsUsersManagement(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1, int rights_user){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.rights_user = rights_user;
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
                    urlConnection.getOutputStream().write(("login=" + this.login + "&password=" + this.password + "&rights_user=" + this.rights_user).getBytes());
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
            // Display notification of successful account type change:
            Toast toast = Toast.makeText(context,
                    "Права учетной записи успешно изменены!", Toast.LENGTH_SHORT);
            toast.show();
            new GetUsersManagement(login, password, contestList, progressBar, textView1).execute("https://testmatica.ru/api/admin_management/users_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
    }
    // Stream to send the delete command to the selected account:
    private class DeleteUsersManagement extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public int delete_user;
        public DeleteUsersManagement(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1, int delete_user){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
            this.delete_user = delete_user;
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
                    urlConnection.getOutputStream().write(("login=" + this.login + "&password=" + this.password + "&delete_user=" + this.delete_user).getBytes());
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
            // Updating the full list of accounts from the database:
            new GetUsersManagement(login, password, contestList, progressBar, textView1).execute("https://testmatica.ru/api/admin_management/users_management.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
    }
    // Stream to get a complete list of accounts from the database:
    private class GetUsersManagement extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public TextView textView1;
        public String login, password;
        public GetUsersManagement(String login, String password, ListView contestList, ProgressBar progressBar, TextView textView1){
            this.login = login;
            this.password = password;
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.textView1 = textView1;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            contestList.setVisibility(View.GONE);
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
                    urlConnection.getOutputStream().write(("login=" + this.login + "&password=" + this.password).getBytes());
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
                ArrayList<UserManagement> contests = new ArrayList<UserManagement>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("users_management");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding a complete list of accounts from a database to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        contests.add(new UserManagement(jsonObject1.getInt("id"), jsonObject1.getString("login"), jsonObject1.getString("pass"), jsonObject1.getString("name"), jsonObject1.getString("family"), jsonObject1.getString("pol"), jsonObject1.getString("email"), jsonObject1.getString("city"), jsonObject1.getInt("adminuser"), jsonObject1.getString("date"), jsonObject1.getString("ip"), jsonObject1.getString("browser"), jsonObject1.getInt("dolg"), jsonObject1.getInt("imghref"), jsonObject1.getInt("levelxp"), jsonObject1.getString("checkclass"), jsonObject1.getInt("idclass"), jsonObject1.getInt("diary")));
                    }
                    // Calling the adapter to display the full list of accounts from the database:
                    AdminUsersManagementAdapter adapter = new AdminUsersManagementAdapter(context, R.layout.user_management, contests, login, password, contestList, progressBar, textView1);
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
}

