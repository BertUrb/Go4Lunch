package com.mjcdouai.go4lunch.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mjcdouai.go4lunch.R;
import com.mjcdouai.go4lunch.databinding.FragmentChatBinding;
import com.mjcdouai.go4lunch.model.ChatMessage;
import com.mjcdouai.go4lunch.ui.ChatViewHolder;

import java.util.Objects;


public class ChatFragment extends Fragment {

    private FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> adapter;
    private FragmentChatBinding mBinding;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://go4lunch-6e797-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference myRef = database.getReference("message");

    public ChatFragment() {
        // Required empty public constructor
    }

       public static ChatFragment newInstance() {
           return new ChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = FragmentChatBinding.inflate(inflater,container,false);

        View view = mBinding.getRoot();



        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(myRef, ChatMessage.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(options) {
            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message, parent, false);
                return new ChatViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(adapter.getItemCount()>0) {
                    mBinding.chatRecyclerview.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessage model) {
                holder.setMessageText(model.getMessageText());
                holder.setMessageTime(model.getMessageTime());
                holder.setUserName(model.getMessageUser());
            }
        };


        mBinding.chatRecyclerview.setAdapter(adapter);

        mBinding.fab.setOnClickListener(view1 -> fabOnclickListener());

        mBinding.input.setOnKeyListener((view1, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
            {
                switch (keyEvent.getKeyCode())
                {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        fabOnclickListener();
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        return view;
    }

    private void fabOnclickListener() {
        EditText input = mBinding.input;

        // Read the input field and push a new instance
        // of ChatMessage to the Firebase database
            myRef.push()
                .setValue(new ChatMessage(input.getText().toString(),
                        Objects.requireNonNull(FirebaseAuth.getInstance()
                                        .getCurrentUser())
                                .getDisplayName()
                        )

                );

        // Clear the input
        input.setText("");

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}

