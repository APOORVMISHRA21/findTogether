package com.example.findlost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.findlost.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    static String registerUrl = "http://find-lost.herokuapp.com/register";

    private TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!isValidEmail(s.toString())) {
                binding.registerEmailLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            }
            else {
                binding.registerEmailLayout.setBoxStrokeColor(getResources().getColor(R.color.green));
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    };

    private TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(binding.registerPassword.getText().toString().equals(s.toString())){
                binding.registerPwdLayout.setBoxStrokeColor(getResources().getColor(R.color.green));
            }
            else {
                binding.registerPwdLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            }
        }
        @Override
        public void afterTextChanged(Editable s) { }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        binding.registerEmail.addTextChangedListener(emailWatcher);

        binding.registerConfirmPassword.addTextChangedListener(passwordWatcher);

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidEmail(binding.registerEmail.getText().toString())) {
                    if(binding.registerConfirmPassword.getText().toString().equals(binding.registerPassword.getText().toString())) {

                        if(binding.registerPassword.getText().toString().length()<5){
                            Toast.makeText(RegisterActivity.this, "Password min length should be 5", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            User user = new User(binding.registerEmail.getText().toString(), binding.registerPassword.getText().toString());
                            registerUser(user);
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Passwords didn't matched", Toast.LENGTH_SHORT);
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(User user) {
        JSONObject registrationForm = new JSONObject();
        try{
            registrationForm.put("email", user.getEmail());
            registrationForm.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(registrationForm.toString(), MediaType.parse("application/json; charset=utf-8"));

        postRequest(registerUrl, requestBody);
    }

    public void postRequest(String postUrl, RequestBody body){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().url(postUrl).post(body).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(RegisterActivity.this, "TRY AGAIN !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean validPassword(String pwd1, String pwd2) {
        if(pwd2.equals(pwd1)){
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean isValidEmail(String email)
    {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else {
            return false;
        }

    }
}

