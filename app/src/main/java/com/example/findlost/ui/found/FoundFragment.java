package com.example.findlost.ui.found;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findlost.CreatePostActivity;
import com.example.findlost.R;
import com.example.findlost.databinding.FragmentFoundBinding;
import com.example.findlost.ui.lost.LostViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Adapters.PostAdapter;
import Models.Post;

public class FoundFragment extends Fragment {

    private FoundViewModel foundViewModel;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        foundViewModel = new ViewModelProvider(requireActivity()).get(FoundViewModel.class);

        return inflater.inflate(R.layout.fragment_lost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("vvvv","vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        recyclerView = (RecyclerView) view.findViewById(R.id.postRecyclerView);
        foundViewModel.getData().observe(getViewLifecycleOwner(), userListUpdateObserver);
        if(postAdapter!=null){
            postAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        foundViewModel.getData().observe(getViewLifecycleOwner(), userListUpdateObserver);
        if(postAdapter!=null){
            postAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        foundViewModel.getData().observe(getViewLifecycleOwner(), userListUpdateObserver);
        if(postAdapter!=null){
            postAdapter.notifyDataSetChanged();
        }
    }

    private Observer<ArrayList<Post>> userListUpdateObserver = new Observer<ArrayList<Post>>() {
        @Override
        public void onChanged(ArrayList<Post> userArrayList) {
            Log.i("USER ARRAY LIST", userArrayList.toString());
            postAdapter = new PostAdapter(userArrayList, requireActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerView.setAdapter(postAdapter);

        }
    };
}