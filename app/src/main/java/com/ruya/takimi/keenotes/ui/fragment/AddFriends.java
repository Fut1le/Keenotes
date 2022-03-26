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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruya.takimi.keenotes.R;
import com.ruya.takimi.keenotes.adapters.addFriends.AddFriendAdapter;
import com.ruya.takimi.keenotes.adapters.addFriends.RecyclerViewTouchHelper;
import com.ruya.takimi.keenotes.models.Friends;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class AddFriends extends Fragment {

    private List<Friends> usersList;
    private AddFriendAdapter usersAdapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_add_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.refreshLayout);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerViewTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerViewTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                if (viewHolder instanceof AddFriendAdapter.ViewHolder) {
                    // get the removed item_video name to display it in snack bar
                    String name = usersList.get(viewHolder.getAdapterPosition()).getName();

                    // backup of removed item_video for undo purpose
                    final Friends deletedItem = usersList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    Snackbar snackbar = Snackbar
                            .make(view.findViewById(R.id.layout), "Friend request sent to " + name, Snackbar.LENGTH_LONG);

                    // remove the item_video from recycler view
                    usersAdapter.removeItem(viewHolder.getAdapterPosition(), snackbar, deletedIndex, deletedItem);

                }
            }
        });

        usersList = new ArrayList<>();
        usersAdapter = new AddFriendAdapter(usersList, view.getContext(), view.findViewById(R.id.layout));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(usersAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllUsers();
            }
        });

        getAllUsers();

    }

    public void getAllUsers() {
        usersList.clear();
        usersAdapter.notifyDataSetChanged();
        getView().findViewById(R.id.default_item).setVisibility(View.GONE);
        refreshLayout.setRefreshing(true);

        firestore.collection("Users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.getDocuments().isEmpty()) {

                            for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    if (!doc.getDocument().getId().equals(mAuth.getCurrentUser().getUid())) {
                                        Friends friends = doc.getDocument().toObject(Friends.class).withId(doc.getDocument().getString("id"));
                                        usersList.add(friends);
                                        usersAdapter.notifyDataSetChanged();
                                        refreshLayout.setRefreshing(false);
                                    }

                                }
                            }

                            if(usersList.isEmpty()){
                                refreshLayout.setRefreshing(false);
                                getView().findViewById(R.id.default_item).setVisibility(View.VISIBLE);
                            }

                        }else{
                            refreshLayout.setRefreshing(false);
                            getView().findViewById(R.id.default_item).setVisibility(View.VISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        refreshLayout.setRefreshing(false);
                        Toasty.error(getView().getContext(), "Some technical error occurred", Toasty.LENGTH_SHORT,true).show();
                        Log.w("Error", "listen:error", e);

                    }
                });



    }


}
