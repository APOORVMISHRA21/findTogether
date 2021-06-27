package com.example.findlost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.findlost.databinding.ActivityCreatePostBinding;
import com.example.findlost.databinding.ActivityMainBinding;

public class CreatePostActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    ActivityCreatePostBinding binding;
    TextView send_post_btn;
    int status = 0; //1-lost 2-found

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
                intent.putExtra("status", status);
                intent.putExtra("username",getIntent().getStringExtra("UserName"));
                /*

                implement  ------------ registerForActivityResult.----------------------

                 */
                startActivityForResult(intent, 1);
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
}