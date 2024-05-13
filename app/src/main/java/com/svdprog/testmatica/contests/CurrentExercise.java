package com.svdprog.testmatica.contests;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svdprog.testmatica.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// This activity is designed to display the current question in the control test being performed...

public class CurrentExercise extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_exercise);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        ProgressBar progress_bar = (ProgressBar)findViewById(R.id.progressBar);
        TextView texttop1 = (TextView)findViewById(R.id.texttop1);
        TextView fine = (TextView)findViewById(R.id.fine);
        ImageView image1 = (ImageView)findViewById(R.id.image1);
        ImageView imagetext1 = (ImageView)findViewById(R.id.imagetext1);
        EditText edittext1 = (EditText)findViewById(R.id.edittext1);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Bundle arguments = getIntent().getExtras();
        DecisionTest demo_test = (DecisionTest) arguments.getSerializable(DecisionTest.class.getSimpleName());
        // If the account is a teacher's or administrator's account, the answer will be shown in the input field prompt:
        if(demo_test.isAdmin_user()){
            edittext1.setHint("Правильный ответ: " + demo_test.getAnswer(demo_test.getCurrent()));
        }
        actionBar.setTitle(demo_test.getTitle());
        boolean empty_current = false;
        // If the current task is the last one in the list, we change the function and text of the button being pressed:
        if (demo_test.getCurrent() == demo_test.getQuanity()){
            empty_current = true;
            button2.setText("Завершить");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 26 (Android 8.0+)
            // Set Minimum value for ProgressBar. Work with API Level 26+ (Android 8.0+)
            progress_bar.setMin(1);
        }
        // Filling the upper dock depending on the current task, depending on the total number:
        progress_bar.setProgress(demo_test.getCurrent() + 1); // Default 0.
        progress_bar.setMax(demo_test.getQuanity() + 1);  // Default 100.
        progress_bar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500)));
        String temp = "Задание №" + Integer.toString(demo_test.getCurrent()) + " (базовый номер: " + Integer.toString(demo_test.getId(demo_test.getCurrent())) + ")";
        texttop1.setText(temp);
        temp = "<i>Выполнили <u>верно</u>:</i> <b>" + Integer.toString(demo_test.getFine(demo_test.getCurrent())) + getEmoji(0x1F9D1) + "</b> ";
        fine.setText(android.text.Html.fromHtml(temp));
        // The Picasso one-liner is used to quickly load an image from the network without having to create a separate function in the stream:
        Picasso.with(CurrentExercise.this).load(demo_test.getLink(demo_test.getCurrent())).into(imagetext1);
        // When you click on enter, you need to minimize the keyboard
        edittext1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (demo_test.getEnter()){
                        edittext1.setEnabled(false);
                        demo_test.setUser(demo_test.getCurrent(), edittext1.getText().toString());
                        button2.setVisibility(View.VISIBLE);
                        button1.setClickable(false);
                        // Checking the presence of a checkbox on the cover page at the beginning of the test
                        if (edittext1.getText().toString().equals(demo_test.getAnswer(demo_test.getCurrent()))){
                            // We inform the user that the specified answer is correct, and add one correctly solved task:
                            image1.setImageResource(R.drawable.done);
                            button1.setBackgroundColor(getResources().getColor(R.color.paris_green));
                            button1.setText("Верно");
                            // Adding a counter to the current task on the server:
                            String temp = "https://testmatica.ru/api/set_fine.php?authorization_key="+ getResources().getString(R.string.authorization_key) + "&id=" + demo_test.getId(demo_test.getCurrent());
                            new SetFine().execute(temp);
                        }
                        else {
                            // We inform the user that the specified answer is incorrect:
                            button1.setBackgroundColor(Color.RED);
                            button1.setText("Неверно");
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        // Checking the correctness of the answer by clicking on the button:
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext1.setEnabled(false);
                demo_test.setUser(demo_test.getCurrent(), edittext1.getText().toString());
                button2.setVisibility(View.VISIBLE);
                button1.setClickable(false);
                if (edittext1.getText().toString().equals(demo_test.getAnswer(demo_test.getCurrent()))){
                    // We inform the user that the specified answer is correct, and add one correctly solved task:
                    image1.setImageResource(R.drawable.done);
                    button1.setBackgroundColor(getResources().getColor(R.color.paris_green));
                    button1.setText("Верно");
                    // Adding a counter to the current task on the server:
                    String temp = "https://testmatica.ru/api/set_fine.php?authorization_key="+ getResources().getString(R.string.authorization_key) + "&id=" + demo_test.getId(demo_test.getCurrent());
                    new SetFine().execute(temp);
                }
                else {
                    // We inform the user that the specified answer is incorrect:
                    button1.setBackgroundColor(Color.RED);
                    button1.setText("Неверно");
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.setClickable(false);
                if (demo_test.getCurrent() != demo_test.getQuanity()) {
                    // Go to the next question, restart the activity with new parameters:
                    demo_test.setCurrent(demo_test.getCurrent() + 1);
                    Intent intent = new Intent(CurrentExercise.this, CurrentExercise.class);
                    intent.putExtra(DecisionTest.class.getSimpleName(), demo_test);
                    startActivity(intent);
                    finish();
                }
                else{
                    // If the current question is the last one, we switch to the activity of completing the test:
                    Intent intent = new Intent(CurrentExercise.this, FinishTest.class);
                    intent.putExtra(DecisionTest.class.getSimpleName(), demo_test);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    // A small function for the correct display of emojis in text layouts:
    public String getEmoji(int uc){
        return new String(Character.toChars(uc));
    }
    // A thread for updating the counter of correct answers for a given question in the site database:
    private class SetFine extends AsyncTask<String, String, String> {
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
            // There may be a handler here if suddenly adding a view to the test fails with an error, but since it is not provided by the application interface, I may not have it...
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle("Выход из теста")
                        .setMessage("Вы действительно хотите покинуть данный тематический тест?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Выход", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Продолжить", null).setIcon(R.drawable.edit_off).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выход из теста")
                .setMessage("Вы действительно хотите покинуть данный тематический тест?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Выход", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Продолжить", null).setIcon(R.drawable.edit_off).show();
    }
}