package com.example.findlost.ui.lost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findlost.R;

import java.util.ArrayList;

import Adapters.PostAdapter;
import Models.Post;

public class LostFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private LostViewModel lostViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lostViewModel = new ViewModelProvider(this).get(LostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lost, container, false);

//        final TextView textView = root.findViewById(R.id.text_home);
//        lostViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        recyclerView = (RecyclerView) root.findViewById(R.id.postRecyclerView);

        lostViewModel.getData().observe(getViewLifecycleOwner(), userListUpdateObserver);
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