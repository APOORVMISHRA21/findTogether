package com.example.findlost;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.findlost.databinding.ActivityLogInBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding binding;
    private String email, password;

    static String loginUrl = "http://find-lost.herokuapp.com/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = binding.loginEmail.getText().toString();
                password = binding.loginPassword.getText().toString();

                loginUser(email, password);
            }
        });

    }


    public void loginUser(String email, String password){
        JSONObject loginForm = new JSONObject();
        try{
            loginForm.put("email",email);
            loginForm.put("password", password);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(loginForm.toString(), MediaType.parse("application/json; charset=utf-8"));

        postRequest(requestBody);
    }

    public void postRequest(RequestBody requestBody){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().url(loginUrl).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(LogInActivity.this, "LOGIN UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!response.isSuccessful()){
                                try (ResponseBody responseBody = response.body()){
                                    Toast.makeText(LogInActivity.this, responseBody.string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Toast.makeText(LogInActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                JSONObject userJson = null;

                                try (ResponseBody responseBody = response.body()) {
                                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                        intent.putExtra("userdata", responseBody.string());
                                        startActivity(intent);
                                        finish();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}