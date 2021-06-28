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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    ActivityCreatePostBinding binding;
    TextView send_post_btn;
    int status = 0; //1-lost 2-found

    private ActivityResultLauncher<Intent> launchAddMedia = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                            uploadImage(result.getData());
                        //doSomeOperations();
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
                String strStatus="";
                if(status == 1)
                    strStatus = "LOST";
                else
                    strStatus = "FOUND";
                intent.putExtra("status", strStatus);
                intent.putExtra("username",getIntent().getStringExtra("UserName"));

                launchAddMedia.launch(intent);

                //bring back the image file here.
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
        //upload to cloudinary.
        Map config = new HashMap();
        config.put("cloud_name", "dntacvap3");
        config.put("api_key","127881442217581");
        config.put("api_secret", "7j562Dpj5TGdEtQfJHeXJNmMxrA");


        Cloudinary cloudinary = new Cloudinary(config);
        MediaManager.init(this, config);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    String requestId = MediaManager.get().upload(file.getAbsolutePath()).option("folder","FindLost/Posts/").option("public_id",file.getName()).callback(new UploadCallback() {
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
                            Log.i("REQUEST UPLOAD----", resultData.toString());

                            /*
                            SEND THE SECURE URL TO DATABASE...USE THIS URL TO LOAD POST.

                            resultData.get("secure_url");
                             */

                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {

                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }
                    }).dispatch();
                    //Log.i("--CLOUD MAP----",result.toString());

            }
        });
        thread.start();


//        CloudinaryService cloudinaryService = new CloudinaryService(config, CreatePostActivity.this);
//        Map resultData = cloudinaryService.UploadTask(file);

    }
}