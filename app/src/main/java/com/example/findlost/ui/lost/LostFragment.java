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
import com.example.findlost.ui.found.FoundViewModel;

import java.util.ArrayList;

import Adapters.PostAdapter;
import Models.Post;

public class LostFragment extends Fragment {

    private LostViewModel lostViewModel;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(list==null)
        {
            list = new ArrayList<>();
        }
        lostViewModel = new ViewModelProvider(this).get(LostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lost, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.postRecyclerView);
        lostViewModel.getData().observe(getViewLifecycleOwner(), userListUpdateObserver);
    }

    private Observer<ArrayList<Post>> userListUpdateObserver = new Observer<ArrayList<Post>>() {
        @Override
        public void onChanged(ArrayList<Post> userArrayList) {
            list.clear();
            list = userArrayList;
            postAdapter = new PostAdapter(list, requireActivity());
            recyclerView.setAdapter(postAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        lostViewModel.getData().observe(getViewLifecycleOwner(), userListUpdateObserver);
        if(postAdapter!=null)
            postAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(postAdapter!=null)
            postAdapter.notifyDataSetChanged();
    }
}