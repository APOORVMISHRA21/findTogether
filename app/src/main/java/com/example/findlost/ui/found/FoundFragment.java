package com.example.findlost.ui.found;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findlost.R;
import com.example.findlost.ui.lost.LostViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Adapters.PostAdapter;
import Models.Post;

public class FoundFragment extends Fragment {

    private FoundViewModel foundViewModel;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foundViewModel = new ViewModelProvider(this).get(FoundViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lost, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.postRecyclerView);
        foundViewModel.getData().observe(getViewLifecycleOwner(), userListUpdateObserver);

        fab = (FloatingActionButton) root.findViewById(R.id.addPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to create post
                // put extra as username, profile pic 
            }
        });
        return root;
    }

    private Observer<ArrayList<Post>> userListUpdateObserver = new Observer<ArrayList<Post>>() {
        @Override
        public void onChanged(ArrayList<Post> userArrayList) {
            postAdapter = new PostAdapter(userArrayList, requireActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerView.setAdapter(postAdapter);
        }
    };
}