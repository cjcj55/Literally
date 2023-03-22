package com.cjcj55.literallynot.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MySQLHelper {
    public static boolean registerAccount(String username, String password, String email, String firstName, String lastName, Context context, Activity activity) {
        if (checkInputs(username, password, email, firstName, lastName)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    "http://18.223.125.204/register.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "User could not register", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("pass", password);
                    params.put("email", email);
                    params.put("firstName", firstName);
                    params.put("lastName", lastName);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(stringRequest);
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkInputs(String username, String password, String email, String firstName, String lastName) {
        boolean check = true;
        if (username.isBlank() || password.isBlank() || email.isBlank() || firstName.isBlank() || lastName.isBlank() || !email.contains("@") || !email.contains(".")) {
            check = false;
        }

        if (username.isBlank()) {
//            binding.firstnameErrorText.setText("This Field is Required");
//            binding.firstnameErrorText.setVisibility(view.VISIBLE);
        } else {

        }
        if (password.isBlank()) {

        } else if (password.length() < 7) {

        } else {

        }
        if (email.isBlank()) {

        } else if (!email.contains("@") || !email.contains(".")) {

        } else {

        }
        if (firstName.isBlank()) {

        } else {

        }
        if (lastName.isBlank()) {

        } else {

        }
        return check;
    }

    public static void login(String userNameOrEmail, String password, Context context, Activity activity) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://18.223.125.204/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                JSONObject sessionData = jsonObject.getJSONObject("sessionData");
                                int uid = sessionData.getInt("user_id");
                                String un = sessionData.getString("username");
                                String firstName = sessionData.getString("firstName");
                                String lastName = sessionData.getString("lastName");
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("user_id", uid);
                                editor.putString("username", un);
                                editor.putString("firstName", firstName);
                                editor.putString("lastName", lastName);
                                editor.apply();
                            } else {
                                Toast.makeText(context, "Invalid username/email or password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username_or_email", userNameOrEmail);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
}
