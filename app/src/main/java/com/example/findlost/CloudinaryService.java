package com.example.findlost;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.findlost.CreatePostActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryService {

    private Map config;
    private Context context;
    public Map result;

    public CloudinaryService(Map config, Context context){
        this.config = config;
        this.context = context;
    }

    public Map UploadTask(File file){

        MediaManager.init(context, config);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String requestId = MediaManager.get().upload(file.getAbsolutePath()).option("folder","FindLost/Posts/").option("public_id",file.getName()).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(context, "UPLOAD INITIATED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Toast.makeText(context, "UPLOAD successful", Toast.LENGTH_SHORT).show();
                        //Log.i("REQUEST UPLOAD----", requestId);
                        Log.i("REQUEST UPLOAD----", resultData.toString());

                        result = resultData;

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
        return result;
    }


}
