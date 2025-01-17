package com.cjcj55.literallynot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.cjcj55.literallynot.databinding.FragmentAccountMenuBinding;
import com.cjcj55.literallynot.db.MySQLHelper;

public class AccountMenu extends Fragment {

    private FragmentAccountMenuBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentAccountMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            TextView textView = view.findViewById(R.id.CustomWelcomeName);
            TextView textViewLastName = view.findViewById(R.id.CustomLastName);
            TextView textViewUsername = view.findViewById(R.id.CustomUsername);

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
            int user_id = sharedPreferences.getInt("user_id", -1);
            String username = sharedPreferences.getString("username", "");
            String firstName = sharedPreferences.getString("firstName", "");
            String lastName = sharedPreferences.getString("lastName", "");
            if(firstName.length()>=7)
            {

                firstName = firstName.substring(0, 7) ;
            }

            textView.setText("First Name: " + firstName);


            if(lastName.length()>=7)
            {
                lastName =lastName.substring(0, 7) ;
            }

            textViewLastName.setText("Last Name: " + lastName);
            if (username.length()>=20)
            {
                username = username.substring(0,20);
            }
            textViewUsername.setText("Username: " + username);



        binding.logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get a reference to the shared preferences file
                SharedPreferences sharedPref = getContext().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                // Retrieve the saved state of the serviceRunning variable
                boolean serviceRunning = sharedPref.getBoolean("isServiceRunning", false);
                System.out.println("HERE" + serviceRunning);
                if(serviceRunning)
                {
                    Intent forIntent = new Intent(getActivity(), ForegroundService.class);
                    getActivity().stopService(forIntent);
                }
                //Clear the SharedPreferences file
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();


                MySQLHelper.logout(getContext(), AccountMenu.this);
            }
        });
        binding.homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountMenu.this)
                        .navigate(R.id.action_accountMenu_to_MainScreen);
            }
        });
        binding.reportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountMenu.this)
                        .navigate(R.id.action_accountMenu_to_ReportScreen);
            }
        });

        binding.scorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountMenu.this)
                        .navigate(R.id.action_accountMenu_to_scoreboard);
            }
        });


    }
}