package com.example.findlost;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.example.findlost.databinding.ActivityCreatePostBinding;
import com.example.findlost.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Models.Post;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CreatePostActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    ActivityCreatePostBinding binding;
    private JSONObject userJson = null;
    private Map resultData = new HashMap();
    private ConfigCloudinary Config;
    private Post post;
    TextView send_post_btn;
    int status = 0;                         //1-lost 2-found
    String strStatus="";

    static String postUrl = "http://find-lost.herokuapp.com/post";


    private ActivityResultLauncher<Intent> LaunchAddMedia = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                            uploadImage(result.getData());
                    }
                }
            });

    private TextWatcher toggleWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { checkCondition(); }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }
    };

    private TextWatcher categoryWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { checkCondition(); }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }
    };

    private TextWatcher descWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { checkCondition(); }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }
    };

    private CompoundButton.OnCheckedChangeListener btnCheckLost = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                status = 1;
                binding.btnFound.setChecked(false);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener btnCheckFound = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                status = 2;
                binding.btnLost.setChecked(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.createPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Share Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        send_post_btn = (TextView) binding.appbar.findViewById(R.id.send_post);

        binding.btnLost.setOnCheckedChangeListener(btnCheckLost);
        binding.btnFound.setOnCheckedChangeListener(btnCheckFound);

        //enable POST only when all required fields filled.
        binding.createPostCategory.addTextChangedListener(categoryWatcher);
        binding.createPostDescription.addTextChangedListener(descWatcher);
        binding.btnLost.addTextChangedListener(toggleWatcher);
        binding.btnFound.addTextChangedListener(toggleWatcher);

        //sending intent for adding image file.
        binding.createPostMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePostActivity.this, AddMediaActivity.class);

                if(status == 1)
                    strStatus = "LOST";
                else
                    strStatus = "FOUND";

                try {
                    JSONObject user =  new JSONObject(getIntent().getStringExtra("userdata"));
                    intent.putExtra("status", strStatus);
                    intent.putExtra("username",
                            user.getString("firstName")+user.getString("lastName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LaunchAddMedia.launch(intent);
            }
        });

        send_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPostToPush(post);
                finish();
            }
        });
    }

    public void checkCondition(){
        if(status == 1 || status == 2){
            if(!binding.createPostCategory.getText().toString().isEmpty()){
                if(!binding.createPostDescription.getText().toString().isEmpty()){
                    send_post_btn.setTextColor(getResources().getColor(R.color.black));
                }
                else{
                    send_post_btn.setTextColor(getResources().getColor(R.color.defaultText));
                }
            }
            else{
                send_post_btn.setTextColor(getResources().getColor(R.color.defaultText));
            }
        }
        else{
            send_post_btn.setTextColor(getResources().getColor(R.color.defaultText));
        }
    }

    public void uploadImage(Intent intent) {
        File file = new File(intent.getStringExtra("image_path"));

        Log.i("----CLOUDINARY----", intent.getStringExtra("image_path"));

        Config = new ConfigCloudinary();
        MediaManager.init(this, Config.getMap());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String requestId = MediaManager.get()
                        .upload(file.getAbsolutePath())
                        .option("folder","FindLost/Posts/")
                        .option("public_id",file.getName())
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                                Toast.makeText(CreatePostActivity.this, "UPLOAD INITIATED", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }
                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                Toast.makeText(CreatePostActivity.this, "UPLOAD successful", Toast.LENGTH_SHORT).show();
                                //Log.i("REQUEST UPLOAD----", requestId);
                                Log.i("REQUEST UPLOAD----", resultData.get("secure_url").toString());

                                SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE, MMM d", Locale.US);

                                try {
                                    userJson = new JSONObject(getIntent().getStringExtra("userdata"));

                                     post = new Post(userJson.getString("firstName")+userJson.getString("lastName"),
                                            mDateFormat.format(new Date()),
                                            binding.createPostCategory.getText().toString(),
                                            resultData.get("secure_url").toString(),
                                            binding.createPostDescription.getText().toString());

                                    post.setCreatorId(userJson.getString("_id"));
                                    post.setStatus(strStatus);
                                    //send post to server
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }
                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        }).dispatch();
            }
        });
        thread.start();
    }

    private void createPostToPush(Post post){
        JSONObject postForm = new JSONObject();
        try{
            postForm.put("creatorId", post.getCreatorId());
            postForm.put("status",post.getStatus());
            postForm.put("creatorName", post.getCreatorName());
            postForm.put("category", post.getCategory());
            postForm.put("creationDate", post.getCreationDate());
            postForm.put("mediaUrl", post.getMediaUrl());
            postForm.put("description", post.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(postForm.toString(), MediaType.parse("application/json; charset=utf-8"));

        postRequest(postUrl, requestBody, post);
    }

    private void postRequest(String postUrl, RequestBody requestBody, Post post){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().url(postUrl).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try{
                    runOnUiThread(()->{

                        if(!response.isSuccessful()){
                            try (ResponseBody responseBody = response.body()){
                                //Toast.makeText(CreatePostActivity.this, responseBody.string(), Toast.LENGTH_SHORT).show();
                                Log.i("---POST UNSUCCESS---", responseBody.string()+"%%%%%%%%%%%%%%%%%%%%%%%%");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(CreatePostActivity.this, "Post Successful " , Toast.LENGTH_SHORT).show();
                            Log.i("-------POST SUCCESS---", "~~~~~~~~~~~~@@@@@@@@@@@~~~~~~~~~~~~");
                            finish();
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

}