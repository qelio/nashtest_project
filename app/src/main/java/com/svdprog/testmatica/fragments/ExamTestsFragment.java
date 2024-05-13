package com.svdprog.testmatica.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.svdprog.testmatica.contests.ExamTests;
import com.svdprog.testmatica.R;
import com.svdprog.testmatica.student.SchoolSubject;

public class ExamTestsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam_tests, null);
        // Next comes the processing of clicks on the buttons to call the activity of exams.
        Button math_button1 = (Button) v.findViewById(R.id.math_button1);
        Button rus_button1 = (Button) v.findViewById(R.id.rus_button1);
        Button inf_button1 = (Button) v.findViewById(R.id.inf_button1);
        // In this case, index 1 is used for the OGE, and index 2 is used for the Unified State Exam^
        SchoolSubject math_subject = new SchoolSubject(1);
        // Processing all types of Math exams:
        math_subject.settings_subject(1, 9, true, false);
        math_subject.settings_subject(2, 11, true, false);
        math_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exam_intent_math = new Intent(getActivity(), ExamTests.class);
                exam_intent_math.putExtra(SchoolSubject.class.getSimpleName(), math_subject);
                exam_intent_math.putExtra("current_tab", 0);
                startActivity(exam_intent_math);
            }
        });
        SchoolSubject rus_subject = new SchoolSubject(2);
        // Processing all types of Rus exams:
        rus_subject.settings_subject(1, 1, false);
        rus_subject.settings_subject(2, 11, false);
        rus_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exam_intent_rus = new Intent(getActivity(), ExamTests.class);
                exam_intent_rus.putExtra(SchoolSubject.class.getSimpleName(), rus_subject);
                exam_intent_rus.putExtra("current_tab", 0);
                startActivity(exam_intent_rus);
            }
        });
        SchoolSubject inf_subject = new SchoolSubject(3);
        // Processing all types of Inf exams:
        inf_subject.settings_subject(1, 1, false);
        inf_subject.settings_subject(2, 1, false);
        inf_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exam_intent_inf = new Intent(getActivity(), ExamTests.class);
                exam_intent_inf.putExtra(SchoolSubject.class.getSimpleName(), inf_subject);
                exam_intent_inf.putExtra("current_tab", 0);
                startActivity(exam_intent_inf);
            }
        });
        return v;
    }
}