package com.cjcj55.literallynot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjcj55.literallynot.databinding.FragmentScoreboardBinding;
import com.cjcj55.literallynot.db.MySQLHelper;
import java.util.Arrays;
import android.widget.TextView;


public class scoreboard extends Fragment {

    private FragmentScoreboardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentScoreboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        int[] amount = {30, 55, 132, 2231, 12};
        Arrays.sort(amount);






        TextView textView1 = view.findViewById(R.id.pos1);
        textView1.setText("1");

        TextView textView2 = view.findViewById(R.id.pos2);
        textView2.setText("2");

        TextView textView3 = view.findViewById(R.id.pos3);
        textView3.setText("3");

        TextView textView4 = view.findViewById(R.id.pos4);
        textView4.setText("4");

        TextView textView5 = view.findViewById(R.id.pos5);
        textView5.setText("5");

        TextView textView6 = view.findViewById(R.id.pos6);
        textView6.setText("6");

        TextView textView7 = view.findViewById(R.id.pos7);
        textView7.setText("7");

        TextView textView8 = view.findViewById(R.id.pos8);
        textView8.setText("8");

        TextView textView9 = view.findViewById(R.id.pos9);
        textView9.setText("9");

        TextView textView10 = view.findViewById(R.id.pos10);
        textView10.setText("10");












        binding.homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(scoreboard.this)
                        .navigate(R.id.action_scoreboard_to_MainScreen);
            }
        });
        binding.reportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(scoreboard.this)
                        .navigate(R.id.action_scoreboard_to_ReportScreen);
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(scoreboard.this)
                        .navigate(R.id.action_scoreboard_to_accountMenu);
            }
        });


    }
}