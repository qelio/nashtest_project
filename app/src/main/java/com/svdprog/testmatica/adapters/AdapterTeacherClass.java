package com.svdprog.testmatica.adapters;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.svdprog.testmatica.teacher.CompositionTeacherClass;
import com.svdprog.testmatica.teacher.NewTeacherClass;
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

// Adapter for displaying a list of created classes in the teacher's account...

public class AdapterTeacherClass extends ArrayAdapter<NewTeacherClass> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<NewTeacherClass> contestList;
    public Intent intent;
    public Context context;
    public ProgressBar progressBar;
    public TextView textView1;
    public ListView listView;
    public String login = "", password = "";
    public AdapterTeacherClass(Context context, int resource, ArrayList<NewTeacherClass> classes, Intent intent, ProgressBar progressBar, ListView listView, String login, String password, TextView textView1) {
        super(context, resource, classes);
        this.contestList = classes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.intent = intent;
        this.context = context;
        this.progressBar = progressBar;
        this.listView = listView;
        this.login = login;
        this.password = password;
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
        final NewTeacherClass contest = contestList.get(position);
        // Content filling of the graphical interface elements of this block:
        viewHolder.title_class.setText("Название класса: " + contest.getTitle());
        viewHolder.code_class.setText(android.text.Html.fromHtml("Код для присоединения: <b>" + contest.getNTcode() + "</b><br/>(нажмите на код, чтобы скопировать его в буфер обмена)"));
        viewHolder.code_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When you click on this text, the class attachment code is copied to the clipboard:
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", contest.getNTcode());
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(context,
                        "Код присоединения скопирован в буфер обмена...", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        // Switching to the activity of viewing students who are in the class:
        viewHolder.people_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("idclass", contest.getId());
                context.startActivity(intent);
            }
        });
        // Calling a dialog box to confirm the deletion of this class:
        viewHolder.delete_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Удаление класса")
                        .setMessage("Вы действительно хотите удалить данный класс?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteTeacherClass(contest.getId(), login, password).execute("https://testmatica.ru/api/delete_teacher_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Отмена", null).setIcon(R.drawable.nodone).show();
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
    // A thread for sending a command to delete one of the classes to the server:
    private class DeleteTeacherClass extends AsyncTask<String, String, String> {
        public int id;
        public String login = "", password = "";
        public DeleteTeacherClass(int id, String login, String password){
            this.id = id;
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
                    urlConnection.getOutputStream().write( ("login=" + this.login + "&password=" + this.password + "&id=" + this.id).getBytes());
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
            listView.setAdapter(null);
            // Updating the list of created classes from the teacher's account:
            new GetTeacherClass(listView, progressBar, login, password).execute("https://testmatica.ru/api/get_teacher_class.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
            // This method does not require receiving the server's response back, but nevertheless it can be read in the "newteacherclass" object...
        }
    }
    // Stream to download the list of created classes from the teacher's account:
    private class GetTeacherClass extends AsyncTask<String, String, String> {
        public ProgressBar progressBar;
        public ListView contestList;
        public String login = "", password = "";
        public GetTeacherClass(ListView contestList, ProgressBar progressBar, String login, String password){
            this.contestList = contestList;
            this.progressBar = progressBar;
            this.login = login;
            this.password = password;
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
                ArrayList<NewTeacherClass> classes = new ArrayList<NewTeacherClass>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("newteacherclass");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Adding a list of created classes from a teacher's account to a shared array:
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        String title = jsonObject1.getString("title");
                        int idteacher = jsonObject1.getInt("idteacher");
                        String NTcode = jsonObject1.getString("NTcode");
                        classes.add(new NewTeacherClass(id, title, idteacher, NTcode));
                    }
                    Intent intent = new Intent(context, CompositionTeacherClass.class);
                    // Calling the adapter to display the list of created classes from the teacher account:
                    AdapterTeacherClass adapter = new AdapterTeacherClass(context, R.layout.teacher_class, classes, intent, progressBar, contestList, login, password, textView1);
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
}

