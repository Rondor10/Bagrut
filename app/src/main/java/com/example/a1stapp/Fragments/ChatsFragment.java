package com.example.a1stapp.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.a1stapp.Models.Chat;
import com.example.a1stapp.R;
import com.example.a1stapp.Models.User;
import com.example.a1stapp.Adapters.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    boolean isUserOnline = false;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("chats");
        //TODO: Not single because - if someone sends me a message and we have never talked, his message must be shown.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(firebaseUser.getUid())) {
                        if(!usersList.contains(chat.getReceiver())) {
                            usersList.add(chat.getReceiver());
                        }
                    }
                    if(chat.getReceiver().equals(firebaseUser.getUid())) {
                        if(!usersList.contains(chat.getSender())) {
                            usersList.add(chat.getSender());
                        }
                    }
                }
                readChats();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        //TODO: Not single because - If a user logs out, his connection status doesn't change.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if(user.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        isUserOnline = user.isShowOnline();
                    }
                    //display 1 user from chats
                    for (String id : usersList) {
                        if(user.getKey().equals(id)) {
                            if (mUsers.size() != 0) {
                                for(User user1 : mUsers) {
                                    if(!user.getKey().equals(user1.getKey())) {
                                        mUsers.add(user);
                                        break;
                                    }
                                }
                            } else {
                                mUsers.add(user);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true, isUserOnline);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}