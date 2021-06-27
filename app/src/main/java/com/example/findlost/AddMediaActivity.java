package com.example.findlost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.findlost.databinding.ActivityAddMediaBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddMediaActivity extends AppCompatActivity {

    ActivityAddMediaBinding binding;

    PreviewView mPreviewView;
    public static int camInstance = CameraSelector.LENS_FACING_BACK;
    private Executor executor = Executors.newSingleThreadExecutor();

    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
                                                                "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPreviewView = binding.preview;

        //check if permissions granted
        if(allPermissionsGranted()){
            //start the camera
            startCamera();
        }
        else{
            //request permissions.
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1001);
        }

    }

    private void startCamera(){
        final ListenableFuture<ProcessCameraProvider> cameraProviderInFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderInFuture.addListener(()->{
            try{
                ProcessCameraProvider cameraProvider = cameraProviderInFuture.get();
                //if you get a camera wow...! bind the preview class to it.
                bindPreview(cameraProvider);
            }
            catch (InterruptedException | ExecutionException e) {
                //chill..! this will not happen
            }
        }, ContextCompat.getMainExecutor(this));
    }

    //this method won't work outside onCreate.
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider){

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(camInstance).build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.preview.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        ImageCapture imageCapture = builder.build();

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);


        // if lens facing changes then restart the camera with new lens.
        binding.fabSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camInstance == CameraSelector.LENS_FACING_BACK)
                    camInstance = CameraSelector.LENS_FACING_FRONT;
                else
                    camInstance = CameraSelector.LENS_FACING_BACK;

                cameraProvider.unbindAll();

                if(allPermissionsGranted()){
                    startCamera(); //start camera if permission has been granted by user
                } else{
                    ActivityCompat.requestPermissions(AddMediaActivity.this, REQUIRED_PERMISSIONS, 1001);
                }
            }
        });

        binding.fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click photos
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                String fileName = mDateFormat.format(new Date()) + getIntent().getStringExtra("status") + getIntent().getStringExtra("username") + ".jpg";
                File file = new File(getBatchDirectoryName(), fileName);

                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();

                imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Intent intent = new Intent();
                        intent.putExtra("image_path", getBatchDirectoryName()+ fileName);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                });

            }
        });
    }


    public String getBatchDirectoryName(){
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/FindLost/posts";
        File dir = new File(app_folder_path);

        return app_folder_path;
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (allPermissionsGranted()) {
                startCamera();
            }
            else {
                Toast.makeText(this, "Permissions not granted by the user", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}