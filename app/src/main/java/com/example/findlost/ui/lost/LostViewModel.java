package com.example.findlost.ui.lost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Models.Post;

public class LostViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<Post>> mPostList;

    public LostViewModel() {
        mPostList = new MutableLiveData<>();
        ArrayList<Post> list = new ArrayList<>();
        list.add(new Post("abanijnfi", "Apoorv Mishra", "20 Jan", "Books"));
        list.add(new Post("abanijnfi", "Apoorv Mishra", "20 Jan", "Books"));
        list.add(new Post("abanijnfi", "Apoorv Mishra", "20 Jan", "Books"));
        list.add(new Post("abanijnfi", "Apoorv Mishra", "20 Jan", "Books"));
        list.add(new Post("abanijnfi", "Apoorv Mishra", "20 Jan", "Books"));
        mPostList.postValue(list);
    }

    public LiveData<ArrayList<Post>> getData() {
        return mPostList;
    }
}