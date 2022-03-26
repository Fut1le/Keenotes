package com.ruya.takimi.keenotes.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruya.takimi.keenotes.R;
import com.ruya.takimi.keenotes.adapters.UsersAdapter;
import com.ruya.takimi.keenotes.models.Users;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class SendMessage extends Fragment {

    private List<Users> usersList;
    private UsersAdapter usersAdapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_message_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = view.findViewById(R.id.messageList);
        refreshLayout=view.findViewById(R.id.refreshLayout);

        usersList = new ArrayList<>();
        usersAdapter = new UsersAdapter(usersList, view.getContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(usersAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                usersList.clear();
                usersAdapter.notifyDataSetChanged();
                startListening();
            }
        });
        usersList.clear();
        usersAdapter.notifyDataSetChanged();
        startListening();

    }

    public void startListening() {
        getView().findViewById(R.id.default_item).setVisibility(View.GONE);
        refreshLayout.setRefreshing(true);

        firestore.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Friends")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    Users users = doc.getDocument().toObject(Users.class).withId(doc.getDocument().getId());
                                    usersList.add(users);
                                    usersAdapter.notifyDataSetChanged();
                                    refreshLayout.setRefreshing(false);
                                }
                            }

                            if(usersList.isEmpty()){
                                refreshLayout.setRefreshing(false);
                                getView().findViewById(R.id.default_item).setVisibility(View.VISIBLE);
                            }

                        }else{
                            getView().findViewById(R.id.default_item).setVisibility(View.VISIBLE);
                            refreshLayout.setRefreshing(false);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toasty.error(getView().getContext(), "Some technical error occurred", Toasty.LENGTH_SHORT,true).show();
                        refreshLayout.setRefreshing(false);
                        Log.w("Error", "listen:error", e);

                    }
                });
    }

}
