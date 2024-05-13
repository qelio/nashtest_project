package com.svdprog.testmatica.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.svdprog.testmatica.R;

// A fragment of the main page of the application, there is little program code, mainly design

public class TitleFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_title, null);
        // Initialization of elements of the graphical interface of the mobile application:
        Button btn1 = (Button) v.findViewById(R.id.btn1);
        Button btn2 = (Button) v.findViewById(R.id.btn2);
        Button btn3 = (Button) v.findViewById(R.id.btn3);
        Button btn4 = (Button) v.findViewById(R.id.btn4);
        // Initialization of elements of the graphical interface of the mobile application:
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://testmatica.ru/olimpics/olymp.php"));
                startActivity(browser);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://testmatica.ru/instruction_people.php"));
                startActivity(browser);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/slava.samokhvalov"));
                startActivity(browser);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/slava.samokhvalov"));
                startActivity(browser);
            }
        });
        return v;
    }
}