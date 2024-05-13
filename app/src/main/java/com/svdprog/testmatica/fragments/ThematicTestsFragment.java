package com.svdprog.testmatica.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.svdprog.testmatica.contests.JuniorTests;
import com.svdprog.testmatica.R;
import com.svdprog.testmatica.student.SchoolSubject;
import com.svdprog.testmatica.contests.SeniorTests;

// A fragment for displaying the prototype types of thematic tests...

public class ThematicTestsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_thematic_tests, null);
        // Initialization of elements of the graphical interface of the mobile application:
        Button math_button1 = (Button) v.findViewById(R.id.math_button1);
        Button math_button2 = (Button) v.findViewById(R.id.math_button2);
        Button rus_button1 = (Button) v.findViewById(R.id.rus_button1);
        Button rus_button2 = (Button) v.findViewById(R.id.rus_button2);
        Button inf_button1 = (Button) v.findViewById(R.id.inf_button1);
        Button inf_button2 = (Button) v.findViewById(R.id.inf_button2);
        Button bio_button1 = (Button) v.findViewById(R.id.bio_button1);
        Button bio_button2 = (Button) v.findViewById(R.id.bio_button2);
        Button obs_button1 = (Button) v.findViewById(R.id.obs_button1);
        Button obs_button2 = (Button) v.findViewById(R.id.obs_button2);
        Button okr_button1 = (Button) v.findViewById(R.id.okr_button1);
        // The following code fragments separated by comments should be changed as the individual subject blocks are filled with thematic tests.
        // Math training tests where "current_tab" is needed to display the desired tab in the child activity:
        SchoolSubject math_subject = new SchoolSubject(1);
        math_subject.settings_subject(1, 1, true, false);
        math_subject.settings_subject(2, 2, true, false);
        math_subject.settings_subject(3, 3, true, true);
        math_subject.settings_subject(4, 4, false);
        math_subject.settings_subject(5, 5, false);
        math_subject.settings_subject(6, 6, false);
        math_subject.settings_subject(7, 7, true, true);
        math_subject.settings_subject(8, 8, true, true);
        math_subject.settings_subject(9, 12, true, true);
        math_subject.settings_subject(10, 10, true, true);
        math_subject.settings_subject(11, 13, true, true);
        math_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent junior_intent_math = new Intent(getActivity(), JuniorTests.class);
                junior_intent_math.putExtra(SchoolSubject.class.getSimpleName(), math_subject);
                junior_intent_math.putExtra("current_tab", 0);
                startActivity(junior_intent_math);
            }
        });
        math_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent senior_intent_math = new Intent(getActivity(), SeniorTests.class);
                senior_intent_math.putExtra(SchoolSubject.class.getSimpleName(), math_subject);
                senior_intent_math.putExtra("current_tab", 2);
                startActivity(senior_intent_math);
            }
        });
        // Rus training tests where "current_tab" is needed to display the desired tab in the child activity:
        SchoolSubject rus_subject = new SchoolSubject(2);
        rus_subject.settings_subject(1, 1, true, true);
        rus_subject.settings_subject(2, 2, true, true);
        rus_subject.settings_subject(3, 3, false);
        rus_subject.settings_subject(4, 4, true, false);
        rus_subject.settings_subject(5, 5, false);
        rus_subject.settings_subject(6, 6, false);
        rus_subject.settings_subject(7, 7, false);
        rus_subject.settings_subject(8, 8, false);
        rus_subject.settings_subject(9, 9, false);
        rus_subject.settings_subject(10, 10, false);
        rus_subject.settings_subject(11, 11, false);
        rus_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent junior_intent_rus = new Intent(getActivity(), JuniorTests.class);
                junior_intent_rus.putExtra(SchoolSubject.class.getSimpleName(), rus_subject);
                junior_intent_rus.putExtra("current_tab", 0);
                startActivity(junior_intent_rus);
            }
        });
        rus_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent senior_intent_rus = new Intent(getActivity(), SeniorTests.class);
                senior_intent_rus.putExtra(SchoolSubject.class.getSimpleName(), rus_subject);
                senior_intent_rus.putExtra("current_tab", 0);
                startActivity(senior_intent_rus);
            }
        });
        // Inf training tests where "current_tab" is needed to display the desired tab in the child activity:
        SchoolSubject inf_subject = new SchoolSubject(3);
        inf_subject.settings_subject(1, 1, false);
        inf_subject.settings_subject(2, 2, false);
        inf_subject.settings_subject(3, 3, false);
        inf_subject.settings_subject(4, 4, false);
        inf_subject.settings_subject(5, 5, false);
        inf_subject.settings_subject(6, 6, false);
        inf_subject.settings_subject(7, 7, false);
        inf_subject.settings_subject(8, 8, false);
        inf_subject.settings_subject(9, 9, false);
        inf_subject.settings_subject(10, 10, false);
        inf_subject.settings_subject(11, 11, false);
        inf_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent junior_intent_inf = new Intent(getActivity(), JuniorTests.class);
                junior_intent_inf.putExtra(SchoolSubject.class.getSimpleName(), inf_subject);
                junior_intent_inf.putExtra("current_tab", 0);
                startActivity(junior_intent_inf);
            }
        });
        inf_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent senior_intent_inf = new Intent(getActivity(), SeniorTests.class);
                senior_intent_inf.putExtra(SchoolSubject.class.getSimpleName(), inf_subject);
                senior_intent_inf.putExtra("current_tab", 0);
                startActivity(senior_intent_inf);
            }
        });
        // Bio training tests where "current_tab" is needed to display the desired tab in the child activity:
        SchoolSubject bio_subject = new SchoolSubject(4);
        bio_subject.settings_subject(1, 1, false);
        bio_subject.settings_subject(2, 2, false);
        bio_subject.settings_subject(3, 3, false);
        bio_subject.settings_subject(4, 4, false);
        bio_subject.settings_subject(5, 5, false);
        bio_subject.settings_subject(6, 6, false);
        bio_subject.settings_subject(7, 7, false);
        bio_subject.settings_subject(8, 8, false);
        bio_subject.settings_subject(9, 9, false);
        bio_subject.settings_subject(10, 10, false);
        bio_subject.settings_subject(11, 11, false);
        bio_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent junior_intent_bio = new Intent(getActivity(), JuniorTests.class);
                junior_intent_bio.putExtra(SchoolSubject.class.getSimpleName(), bio_subject);
                junior_intent_bio.putExtra("current_tab", 0);
                startActivity(junior_intent_bio);
            }
        });
        bio_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent senior_intent_bio = new Intent(getActivity(), SeniorTests.class);
                senior_intent_bio.putExtra(SchoolSubject.class.getSimpleName(), bio_subject);
                senior_intent_bio.putExtra("current_tab", 0);
                startActivity(senior_intent_bio);
            }
        });
        // Obs training tests where "current_tab" is needed to display the desired tab in the child activity:
        SchoolSubject obs_subject = new SchoolSubject(5);
        obs_subject.settings_subject(1, 1, false);
        obs_subject.settings_subject(2, 2, false);
        obs_subject.settings_subject(3, 3, false);
        obs_subject.settings_subject(4, 4, false);
        obs_subject.settings_subject(5, 5, false);
        obs_subject.settings_subject(6, 6, false);
        obs_subject.settings_subject(7, 7, false);
        obs_subject.settings_subject(8, 8, false);
        obs_subject.settings_subject(9, 9, false);
        obs_subject.settings_subject(10, 10, false);
        obs_subject.settings_subject(11, 11, false);
        obs_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent junior_intent_obs = new Intent(getActivity(), JuniorTests.class);
                junior_intent_obs.putExtra(SchoolSubject.class.getSimpleName(), obs_subject);
                junior_intent_obs.putExtra("current_tab", 0);
                startActivity(junior_intent_obs);
            }
        });
        obs_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent senior_intent_obs = new Intent(getActivity(), SeniorTests.class);
                senior_intent_obs.putExtra(SchoolSubject.class.getSimpleName(), obs_subject);
                senior_intent_obs.putExtra("current_tab", 0);
                startActivity(senior_intent_obs);
            }
        });
        // Okr training tests where "current_tab" is needed to display the desired tab in the child activity:
        SchoolSubject okr_subject = new SchoolSubject(6);
        okr_subject.settings_subject(1, 1, false);
        okr_subject.settings_subject(2, 2, false);
        okr_subject.settings_subject(3, 3, false);
        okr_subject.settings_subject(4, 4, false);
        okr_subject.settings_subject(5, 5, false);
        okr_subject.settings_subject(6, 6, false);
        okr_subject.settings_subject(7, 7, false);
        okr_subject.settings_subject(8, 8, false);
        okr_subject.settings_subject(9, 9, false);
        okr_subject.settings_subject(10, 10, false);
        okr_subject.settings_subject(11, 11, false);
        okr_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent junior_intent_okr = new Intent(getActivity(), JuniorTests.class);
                junior_intent_okr.putExtra(SchoolSubject.class.getSimpleName(), okr_subject);
                junior_intent_okr.putExtra("current_tab", 0);
                startActivity(junior_intent_okr);
            }
        });
        return v;
    }
}