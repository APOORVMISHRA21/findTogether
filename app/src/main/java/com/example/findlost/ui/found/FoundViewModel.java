package com.example.findlost.ui.found;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.browser.customtabs.PostMessageService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.findlost.CreatePostActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import Models.Post;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FoundViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Post>> mPostList;
    private ArrayList<Post> list;
    private static String postUrl = "http://find-lost.herokuapp.com/post/found";

    public FoundViewModel() {
        if(mPostList == null)
        {
            mPostList = new MutableLiveData<>();
        }
        init();
    }

    private void init(){
        populateList();

    }

    private void populateList(){
        list = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(postUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)  {
                                if(response.isSuccessful()){
                                    try (ResponseBody responseBody = response.body()){
                                        Log.i("---POST RESPONSE --", "##############################   succesfful  @##################");
                                        JSONArray postArray = new JSONArray(responseBody.string());

                                        for(int i=0; i<postArray.length(); i++)
                                        {
                                            JSONObject postObject = postArray.getJSONObject(i);
                                            Post post = new Post(postObject.getString("creatorName"),
                                                    postObject.getString("creationDate"),
                                                    postObject.getString("category"),
                                                    postObject.getString("mediaUrl"),
                                                    postObject.getString("description"));

                                            list.add(post);

                                        }

                                        mPostList.postValue(list);
                                        //response.body().close();
                                        //Log.i("---POST RESPONSE--", postJson.toString());
                                        //Post post = new Post();
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    try {
                                        Log.i("-- UNSUCCESSPOST PULL", "!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~~~~"+response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    public LiveData<ArrayList<Post>> getData() {
        return mPostList;
    }

}