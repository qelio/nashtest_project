package com.svdprog.testmatica.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

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

// The activity is designed to create a new account by the user...

public class UserRegistration extends AppCompatActivity {
    LinearLayout user_man, user_woman, user_teacher, user_student, image_1, image_2, image_3, image_4, image_5, image_6, linear_class1;
    RelativeLayout linear_class2;
    EditText user_login, user_password, user_name, user_family, user_email, user_city;
    Button register;
    ScrollView scrollview;
    public String pol = "Мужчина";
    public int imghref = 1, dolg = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Регистрация");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initialization of elements of the graphical interface of the mobile application:
        user_login = (EditText) findViewById(R.id.user_login);
        user_password = (EditText) findViewById(R.id.user_password);
        user_name = (EditText) findViewById(R.id.user_name);
        user_family = (EditText) findViewById(R.id.user_family);
        user_email = (EditText) findViewById(R.id.user_email);
        user_city = (EditText) findViewById(R.id.user_city);
        user_man = (LinearLayout) findViewById(R.id.user_man);
        user_woman = (LinearLayout) findViewById(R.id.user_woman);
        user_teacher = (LinearLayout) findViewById(R.id.user_teacher);
        user_student = (LinearLayout) findViewById(R.id.user_student);
        image_1 = (LinearLayout) findViewById(R.id.image_1);
        image_2 = (LinearLayout) findViewById(R.id.image_2);
        image_3 = (LinearLayout) findViewById(R.id.image_3);
        image_4 = (LinearLayout) findViewById(R.id.image_4);
        image_5 = (LinearLayout) findViewById(R.id.image_5);
        image_6 = (LinearLayout) findViewById(R.id.image_6);
        linear_class1 = (LinearLayout) findViewById(R.id.linear_class1);
        linear_class2 = (RelativeLayout) findViewById(R.id.linear_class2);
        scrollview = (ScrollView) findViewById(R.id.scrollView);
        register = (Button) findViewById(R.id.register);
        // Selecting the gender of the registered user by clicking on the icons:
        user_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                user_man.startAnimation(animation);
                user_man.setBackground(getDrawable(R.drawable.black_blue_registration));
                user_woman.setBackground(getDrawable(R.drawable.blue_registration));
                pol = "Мужчина";
            }
        });
        user_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                user_woman.startAnimation(animation);
                user_woman.setBackground(getDrawable(R.drawable.black_blue_registration));
                user_man.setBackground(getDrawable(R.drawable.blue_registration));
                pol = "Женщина";
            }
        });
        // Selecting the type of registered user by clicking on the icons:
        user_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                user_teacher.startAnimation(animation);
                user_teacher.setBackground(getDrawable(R.drawable.black_blue_registration));
                user_student.setBackground(getDrawable(R.drawable.blue_registration));
                dolg = 2;
            }
        });
        user_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                user_student.startAnimation(animation);
                user_student.setBackground(getDrawable(R.drawable.black_blue_registration));
                user_teacher.setBackground(getDrawable(R.drawable.blue_registration));
                dolg = 1;
            }
        });
        // Selecting an icon from the suggested ones of the registered user, when clicking on the icons:
        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                image_1.startAnimation(animation);
                updateImageButton();
                image_1.setBackground(getDrawable(R.drawable.black_blue_registration));
                imghref = 1;
            }
        });
        image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                image_2.startAnimation(animation);
                updateImageButton();
                image_2.setBackground(getDrawable(R.drawable.black_blue_registration));
                imghref = 2;
            }
        });
        image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                image_3.startAnimation(animation);
                updateImageButton();
                image_3.setBackground(getDrawable(R.drawable.black_blue_registration));
                imghref = 3;
            }
        });
        image_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                image_4.startAnimation(animation);
                updateImageButton();
                image_4.setBackground(getDrawable(R.drawable.black_blue_registration));
                imghref = 4;
            }
        });
        image_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                image_5.startAnimation(animation);
                updateImageButton();
                image_5.setBackground(getDrawable(R.drawable.black_blue_registration));
                imghref = 5;
            }
        });
        image_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(UserRegistration.this, R.anim.base_animation);
                image_6.startAnimation(animation);
                updateImageButton();
                image_6.setBackground(getDrawable(R.drawable.black_blue_registration));
                imghref = 6;
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                new UserRegister(user_login.getText().toString(), user_name.getText().toString(), user_password.getText().toString(), user_family.getText().toString(), user_city.getText().toString(), pol, user_email.getText().toString(), dolg, imghref).execute("https://testmatica.ru/api/registration/regist.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
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
    // Resetting the user's button selection, when changing the selection:
    public void updateImageButton(){
        image_1.setBackground(getDrawable(R.drawable.blue_registration));
        image_2.setBackground(getDrawable(R.drawable.blue_registration));
        image_3.setBackground(getDrawable(R.drawable.blue_registration));
        image_4.setBackground(getDrawable(R.drawable.blue_registration));
        image_5.setBackground(getDrawable(R.drawable.blue_registration));
        image_6.setBackground(getDrawable(R.drawable.blue_registration));
    }
    // Stream for user registration in the site database:
    private class UserRegister extends AsyncTask<String, String, String> {
        public String login, name, pass, family, city, pol, email;
        public int dolg, imghref;
        public UserRegister(String login, String name, String pass, String family, String city, String pol, String email, int dolg, int imghref){
            this.login = login;
            this.name = name;
            this.pass = pass;
            this.family = family;
            this.city = city;
            this.pol = pol;
            this.email = email;
            this.dolg = dolg;
            this.imghref = imghref;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linear_class1.setVisibility(View.GONE);
            linear_class2.setVisibility(View.VISIBLE);
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
                    urlConnection.getOutputStream().write(("login=" + this.login + "&pass=" + this.pass + "&name=" + this.name + "&family=" + this.family + "&city=" + this.city + "&pol=" + this.pol  + "&email=" + this.email + "&dolg=" + this.dolg + "&imghref=" + this.imghref).getBytes());
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
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("registration");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                // Analyzing server responses:
                switch (jsonObject1.getInt("error")){
                    case 101:
                        linear_class1.setVisibility(View.VISIBLE);
                        linear_class2.setVisibility(View.GONE);
                        // Scrolling up the page and informing about the errors that have occurred:
                        user_login.setError("Длина логина недопустима (от 3 до 90 символов)");
                        scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                        break;
                    case 102:
                        linear_class1.setVisibility(View.VISIBLE);
                        linear_class2.setVisibility(View.GONE);
                        // Scrolling up the page and informing about the errors that have occurred:
                        user_name.setError("Длина имени недопустима (от 1 до 90 символов)");
                        scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                        break;
                    case 103:
                        linear_class1.setVisibility(View.VISIBLE);
                        linear_class2.setVisibility(View.GONE);
                        // Scrolling up the page and informing about the errors that have occurred:
                        user_password.setError("Длина пароля недопустима (от 1 до 40 символов)");
                        scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                        break;
                    case 104:
                        linear_class1.setVisibility(View.VISIBLE);
                        linear_class2.setVisibility(View.GONE);
                        // Scrolling up the page and informing about the errors that have occurred:
                        user_family.setError("Длина фамилии недопустима (от 1 до 90 символов)");
                        scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                        break;
                    case 105:
                        linear_class1.setVisibility(View.VISIBLE);
                        linear_class2.setVisibility(View.GONE);
                        // Scrolling up the page and informing about the errors that have occurred:
                        user_city.setError("Длина города недопустима (от 1 до 90 символов)");
                        scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                        break;
                    case 106:
                        linear_class1.setVisibility(View.VISIBLE);
                        linear_class2.setVisibility(View.GONE);
                        // Scrolling up the page and informing about the errors that have occurred:
                        user_login.setError("К сожалению, данный логин занят! Попробуйте использовать другой логин.");
                        scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                        break;
                    case 200:
                        // Message about successful registration and return to previous activity:
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Регистрация прошла успешно! Выполните авторизацию...", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
