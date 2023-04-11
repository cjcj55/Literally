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






        TextView textView = view.findViewById(R.id.leaderboard);
        textView.setText(Arrays.toString(amount));












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