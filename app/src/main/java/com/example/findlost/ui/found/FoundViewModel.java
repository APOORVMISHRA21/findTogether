package com.example.findlost.ui.found;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Models.Post;

public class FoundViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<Post>> mPostList;

    public FoundViewModel() {
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