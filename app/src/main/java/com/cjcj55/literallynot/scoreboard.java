package com.cjcj55.literallynot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.cjcj55.literallynot.databinding.FragmentScoreboardBinding;
import com.cjcj55.literallynot.db.LBEntry;
import com.cjcj55.literallynot.db.LeaderboardCallback;
import com.cjcj55.literallynot.db.MySQLHelper;

import java.util.List;


public class scoreboard extends Fragment {

    private FragmentScoreboardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScoreboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        MySQLHelper.getLeaderboard(getContext(), new LeaderboardCallback() {
            @Override
            public void onSuccess(List<LBEntry> leaderboard) {
                int length = leaderboard.size();

                if (length >= 1) {
                    TextView textView1 = view.findViewById(R.id.pos1);
                    textView1.setText("1st: " +  (leaderboard.get(0)).getName() + ", " + (leaderboard.get(0)).getNumFiles());
                }

                if (length >= 2) {
                    TextView textView2 = view.findViewById(R.id.pos2);
                    textView2.setText("2nd: " +  (leaderboard.get(1)).getName() + ", " + (leaderboard.get(1)).getNumFiles());
                }

                if (length >= 3) {
                    TextView textView3 = view.findViewById(R.id.pos3);
                    textView3.setText("3rd: " +  (leaderboard.get(2)).getName() + ", " + (leaderboard.get(2)).getNumFiles());
                }

                if (length >= 4) {
                    TextView textView4 = view.findViewById(R.id.pos4);
                    textView4.setText("4th: " +  (leaderboard.get(3)).getName() + ", " + (leaderboard.get(3)).getNumFiles());
                }

                if (length >= 5) {
                    TextView textView5 = view.findViewById(R.id.pos5);
                    textView5.setText("5th: " +  (leaderboard.get(4)).getName() + ", " + (leaderboard.get(4)).getNumFiles());
                }
            }

            @Override
            public void onFailure() {

            }
        });

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