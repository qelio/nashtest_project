package com.svdprog.testmatica.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svdprog.testmatica.admin.AdminPullManagement;
import com.svdprog.testmatica.admin.AdminUsersManagement;
import com.svdprog.testmatica.contests.AllContests;
import com.svdprog.testmatica.R;
import com.svdprog.testmatica.student.StudentClass;
import com.svdprog.testmatica.teacher.TeacherClass;
import com.svdprog.testmatica.user.UserProfile;
import com.svdprog.testmatica.user.UserRegistration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// A fragment for registering a new account, logging into an account and viewing other various account information...

public class ProfileFragment extends Fragment {
    TextView error_registry;
    EditText login_registry, password_registry;
    View v;
    public LinearLayout linear1, linear2;
    public RelativeLayout linear3;
    Button student_button;
    SharedPreferences settings_profile;
    // Data ads responsible for displaying information on the profile page:
    ImageButton profile_exit, key1, key2, key3;
    ImageView profile_imghref;
    TextView profile_init, profile_login, profile_id, profile_pol, profile_email, profile_city, profile_levelxp, profile_progress_start, profile_progress_finish, student_info;
    ProgressBar profile_progress;
    // Here are the objects of the report card:
    TextView contest_title1, contest_title2, contest_title3, contest_title4, contest_title5;
    TextView contest_date1, contest_date2, contest_date3, contest_date4, contest_date5;
    TextView contest_result1, contest_result2, contest_result3, contest_result4, contest_result5;
    TextView contest_loading;
    Button profile_all_contests;
    LinearLayout contest_linear1, contest_linear2, contest_linear3, contest_linear4, contest_linear5, adminlinear;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, null);
        // Initialization of elements of the graphical interface of the mobile application:
        login_registry = (EditText) v.findViewById(R.id.login_registry);
        password_registry = (EditText) v.findViewById(R.id.password_registry);
        Button enter_registry = (Button) v.findViewById(R.id.enter_registry);
        Button register_registry = (Button) v.findViewById(R.id.register_registry);
        student_button = (Button) v.findViewById(R.id.student_button);
        error_registry = (TextView) v.findViewById(R.id.error_registry);
        linear1 = (LinearLayout) v.findViewById(R.id.linear1);
        linear2 = (LinearLayout) v.findViewById(R.id.linear2);
        linear3 = (RelativeLayout) v.findViewById(R.id.linear3);
        adminlinear = (LinearLayout) v.findViewById(R.id.adminlinear);
        key1 = (ImageButton) v.findViewById(R.id.key1);
        key2 = (ImageButton) v.findViewById(R.id.key2);
        key3 = (ImageButton) v.findViewById(R.id.key3);
        linear1.setVisibility(View.GONE);
        linear2.setVisibility(View.GONE);
        linear3.setVisibility(View.VISIBLE);
        profile_exit = (ImageButton) v.findViewById(R.id.profile_exit);
        // Data ads responsible for displaying information on the profile page:
        profile_imghref = (ImageView) v.findViewById(R.id.profile_imghref);
        profile_init = (TextView) v.findViewById(R.id.profile_init);
        profile_login = (TextView) v.findViewById(R.id.profile_login);
        profile_id = (TextView) v.findViewById(R.id.profile_id);
        profile_pol = (TextView) v.findViewById(R.id.profile_pol);
        profile_email = (TextView) v.findViewById(R.id.profile_email);
        profile_city = (TextView) v.findViewById(R.id.profile_city);
        profile_levelxp = (TextView) v.findViewById(R.id.profile_levelxp);
        profile_progress_start = (TextView) v.findViewById(R.id.profile_progress_start);
        profile_progress_finish = (TextView) v.findViewById(R.id.profile_progress_finish);
        profile_progress = (ProgressBar) v.findViewById(R.id.profile_progress);
        student_info = (TextView) v.findViewById(R.id.student_info);
        // Here are the objects of the report card:
        profile_all_contests = (Button) v.findViewById(R.id.profile_all_contests);
        contest_title1 = (TextView) v.findViewById(R.id.contest_title1);
        contest_title2 = (TextView) v.findViewById(R.id.contest_title2);
        contest_title3 = (TextView) v.findViewById(R.id.contest_title3);
        contest_title4 = (TextView) v.findViewById(R.id.contest_title4);
        contest_title5 = (TextView) v.findViewById(R.id.contest_title5);
        contest_date1 = (TextView) v.findViewById(R.id.contest_date1);
        contest_date2 = (TextView) v.findViewById(R.id.contest_date2);
        contest_date3 = (TextView) v.findViewById(R.id.contest_date3);
        contest_date4 = (TextView) v.findViewById(R.id.contest_date4);
        contest_date5 = (TextView) v.findViewById(R.id.contest_date5);
        contest_result1 = (TextView) v.findViewById(R.id.contest_result1);
        contest_result2 = (TextView) v.findViewById(R.id.contest_result2);
        contest_result3 = (TextView) v.findViewById(R.id.contest_result3);
        contest_result4 = (TextView) v.findViewById(R.id.contest_result4);
        contest_result5 = (TextView) v.findViewById(R.id.contest_result5);
        contest_loading = (TextView) v.findViewById(R.id.contest_loading);
        contest_linear1 = (LinearLayout) v.findViewById(R.id.contest_linear1);
        contest_linear2 = (LinearLayout) v.findViewById(R.id.contest_linear2);
        contest_linear3 = (LinearLayout) v.findViewById(R.id.contest_linear3);
        contest_linear4 = (LinearLayout) v.findViewById(R.id.contest_linear4);
        contest_linear5 = (LinearLayout) v.findViewById(R.id.contest_linear5);
        student_button.setVisibility(View.GONE);
        student_info.setVisibility(View.GONE);
        // Getting settings that contain information about logging in to your account, even when restarting the application:
        settings_profile = getActivity().getSharedPreferences("settings_profile", MODE_PRIVATE);
        if (settings_profile.contains("login") && settings_profile.contains("password")){
            new GetUser(settings_profile.getString("login", "-"), settings_profile.getString("password", "-"), true).execute("https://testmatica.ru/api/get_user.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
        }
        else{
            linear3.setVisibility(View.GONE);
            linear1.setVisibility(View.VISIBLE);
        }
        enter_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear1.setVisibility(View.GONE);
                linear3.setVisibility(View.VISIBLE);
                // We start the flow of receiving information about the user:
                new GetUser(login_registry.getText().toString(), password_registry.getText().toString(), false).execute("https://testmatica.ru/api/get_user.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ");
                // When you click on the account login button, we hide the system keyboard from the screen:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        // When you click on the registration button, go to the activity of creating a new account:
        register_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://testmatica.ru/regist1.php"));
                // startActivity(browser);
                Intent registration = new Intent(getContext(), UserRegistration.class);
                startActivity(registration);
            }
        });
        // Opening a dialog box to confirm the account logout:
        profile_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Выход из аккаунта")
                        .setMessage("Вы действительно хотите выйти из аккаунта?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                linear2.setVisibility(View.GONE);
                                linear3.setVisibility(View.VISIBLE);
                                SharedPreferences.Editor ed = settings_profile.edit();
                                ed.remove("login");
                                ed.remove("password");
                                ed.apply();
                                login_registry.getText().clear();
                                password_registry.getText().clear();
                                linear3.setVisibility(View.GONE);
                                linear1.setVisibility(View.VISIBLE);
                                }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Отмена", null).setIcon(R.drawable.exit_profile).show();
            }
        });
        return v;
    }
    // A stream for getting user information:
    public class GetUser extends AsyncTask<String, String, String> {
        public String login = "";
        public String password = "";
        public boolean in_memory = false;
        public GetUser(String login, String password, boolean in_memory){
            this.login = login;
            this.password = password;
            this.in_memory = in_memory;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error_registry.setVisibility(View.INVISIBLE);
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
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                // If the user is not found, we inform the user about it:
                if (jsonObject1.getInt("id") == 0){
                    password_registry.getText().clear();
                    error_registry.setVisibility(View.VISIBLE);
                    linear3.setVisibility(View.GONE);
                    linear1.setVisibility(View.VISIBLE);
                }
                else{
                    // If you have just logged in, then save your username and password in the general settings of the application:
                    if(!in_memory) {
                        settings_profile = getActivity().getSharedPreferences("settings_profile", MODE_PRIVATE);
                        SharedPreferences.Editor ed = settings_profile.edit();
                        ed.putString("login", login_registry.getText().toString());
                        ed.putString("password", password_registry.getText().toString());
                        ed.apply();
                    }
                    // Next comes the standard filling in of all fields of the personal account section. But only if the download was successful and the account is logged in:
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
                    // After creating the object, we proceed to the main filling of interface objects for the user:
                    profile_init.setText(user.getName() + " " + user.getFamily());
                    profile_login.setText(android.text.Html.fromHtml("Логин: " + user.getLogin()));
                    // The Picasso one-liner is used to quickly load an image from the network without having to create a separate function in the stream:
                    Picasso.with(getActivity()).load(user.getImg_link()).into(profile_imghref);
                    profile_id.setText(android.text.Html.fromHtml("<i>Персональный id:</i> " + Integer.toString(user.getId())));
                    profile_pol.setText(android.text.Html.fromHtml("<i>Пол:</i> " + user.getPol()));
                    profile_email.setText(android.text.Html.fromHtml("<i>Email:</i> " + user.getEmail()));
                    profile_city.setText(android.text.Html.fromHtml("<i>Город:</i> " + user.getCity()));
                    // Next comes the filling of the progressBar based on the following conditions underlying the profile page on the site:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 26 (Android 8.0+)
                        // Set Minimum value for ProgressBar. Work with API Level 26+ (Android 8.0+)
                        profile_progress.setMin(0);
                    }
                    // Новичок - [0; 40), Ученик - [40; 100), Знаток - [100; 200),  Продвинутый - [200; 350),  Профессионал - [350; 550),  Мастер - [550; 800),
                    // Мыслитель - [800; 1100),  Учёный - [1100; 1450),  Мудрец - [1450; + беск.)
                    profile_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500)));
                    if (user.getLevelxp() >= 0 && user.getLevelxp() < 40){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Новичок (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(39);
                        profile_progress_start.setText("0 XP");
                        profile_progress_finish.setText("39 XP");
                    }
                    if (user.getLevelxp() >= 40 && user.getLevelxp() < 100){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Ученик (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(99);
                        profile_progress_start.setText("40 XP");
                        profile_progress_finish.setText("99 XP");
                    }
                    if (user.getLevelxp() >= 100 && user.getLevelxp() < 200){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Знаток (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(199);
                        profile_progress_start.setText("100 XP");
                        profile_progress_finish.setText("199 XP");
                    }
                    if (user.getLevelxp() >= 200 && user.getLevelxp() < 350){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Продвинутый (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(349);
                        profile_progress_start.setText("200 XP");
                        profile_progress_finish.setText("349 XP");
                    }
                    if (user.getLevelxp() >= 350 && user.getLevelxp() < 550){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Профессионал (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(549);
                        profile_progress_start.setText("350 XP");
                        profile_progress_finish.setText("549 XP");
                    }
                    if (user.getLevelxp() >= 550 && user.getLevelxp() < 800){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Мастер (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(799);
                        profile_progress_start.setText("550 XP");
                        profile_progress_finish.setText("800 XP");
                    }
                    if (user.getLevelxp() >= 800 && user.getLevelxp() < 1100){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Мыслитель (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(1099);
                        profile_progress_start.setText("800 XP");
                        profile_progress_finish.setText("1099 XP");
                    }
                    if (user.getLevelxp() >= 1100 && user.getLevelxp() < 1450){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Учёный (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(1449);
                        profile_progress_start.setText("1100 XP");
                        profile_progress_finish.setText("1449 XP");
                    }
                    if (user.getLevelxp() >= 1450){
                        profile_levelxp.setText(android.text.Html.fromHtml("Текущий уровень: <i><b>Мудрец (" + user.getLevelxp() + " XP)</b></i>"));
                        profile_progress.setMax(1449);
                        profile_progress_start.setText("1450 XP");
                        profile_progress_finish.setText("∞ XP");
                    }
                    profile_progress.setProgress(user.getLevelxp());
                    new GetResultTest().execute("https://testmatica.ru/api/get_result_test.php?authorization_key=t2EyVwlZsLwP7TeAlyrJ&user_id=" + Integer.toString(user.getId()) + "&quanity=5");
                    profile_all_contests.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), AllContests.class);
                            i.putExtra("user_id", user.getId());
                            startActivity(i);
                        }
                    });
                    // Changing the profile page depending on the account role:
                    if (user.getDolg() == 1) {
                        student_info.setText("Теперь прохождение активных тестов и просмотр их результатов доступны в мобильном приложении:");
                        student_button.setText("Мои задания");
                        student_button.setVisibility(View.VISIBLE);
                        student_info.setVisibility(View.VISIBLE);
                        student_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), StudentClass.class);
                                startActivity(i);
                            }
                        });
                    }
                    else{
                        // If the account is a teacher's account, then we add a special block:
                        student_info.setText("Теперь создание активных тестов и просмотр их результатов доступны в мобильном приложении:");
                        student_button.setText("Учительская");
                        student_button.setVisibility(View.VISIBLE);
                        student_info.setVisibility(View.VISIBLE);
                        student_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), TeacherClass.class);
                                startActivity(i);
                            }
                        });
                    }
                    // Changing the profile page depending on whether the account has administrator rights:
                    if (user.getAdminuser() == 1){
                       adminlinear.setVisibility(View.VISIBLE);
                       key1.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent intent = new Intent(getActivity(), AdminUsersManagement.class);
                               startActivity(intent);
                           }
                       });
                        key2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), AdminPullManagement.class);
                                startActivity(intent);
                            }
                        });
                        key3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), AllContests.class);
                                intent.putExtra("user_id", 0);
                                startActivity(intent);
                            }
                        });
                    }
                    else{
                        adminlinear.setVisibility(View.GONE);
                    }
                    linear3.setVisibility(View.GONE);
                    linear2.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // Getting the first five results of the completed tests from the account:
    private class GetResultTest extends AsyncTask<String, String, String> {
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
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result_test");
                if (jsonArray.length() == 0){
                    // If no completed thematic test is found:
                    contest_loading.setText("К сожалению, вы пока не завершили ни один тест...");
                }
                else {
                    contest_loading.setVisibility(View.GONE);
                    profile_all_contests.setVisibility(View.VISIBLE);
                }
                // Filling in blocks depending on whether the user has solved the tests:
                if (jsonArray.length() >= 1){
                    contest_linear1.setVisibility(View.VISIBLE);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    contest_title1.setText(jsonObject1.getString("title"));
                    contest_date1.setText("Дата: " + jsonObject1.getString("date"));
                    contest_result1.setText("Результат: " + jsonObject1.getString("kerm"));
                }
                if (jsonArray.length() >= 2){
                    contest_linear2.setVisibility(View.VISIBLE);
                    JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                    contest_title2.setText(jsonObject2.getString("title"));
                    contest_date2.setText("Дата: " + jsonObject2.getString("date"));
                    contest_result2.setText("Результат: " + jsonObject2.getString("kerm"));
                }
                if (jsonArray.length() >= 3){
                    contest_linear3.setVisibility(View.VISIBLE);
                    JSONObject jsonObject3 = jsonArray.getJSONObject(2);
                    contest_title3.setText(jsonObject3.getString("title"));
                    contest_date3.setText("Дата: " + jsonObject3.getString("date"));
                    contest_result3.setText("Результат: " + jsonObject3.getString("kerm"));
                }
                if (jsonArray.length() >= 4){
                    contest_linear4.setVisibility(View.VISIBLE);
                    JSONObject jsonObject4 = jsonArray.getJSONObject(3);
                    contest_title4.setText(jsonObject4.getString("title"));
                    contest_date4.setText("Дата: " + jsonObject4.getString("date"));
                    contest_result4.setText("Результат: " + jsonObject4.getString("kerm"));
                }
                if (jsonArray.length() >= 5){
                    contest_linear5.setVisibility(View.VISIBLE);
                    JSONObject jsonObject5 = jsonArray.getJSONObject(4);
                    contest_title5.setText(jsonObject5.getString("title"));
                    contest_date5.setText("Дата: " + jsonObject5.getString("date"));
                    contest_result5.setText("Результат: " + jsonObject5.getString("kerm"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}