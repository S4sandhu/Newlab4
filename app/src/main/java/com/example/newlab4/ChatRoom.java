package com.example.newlab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newlab4.databinding.ActivityChatRoomBinding;
import com.example.newlab4.databinding.SentMessageBinding;

import java.util.ArrayList;


public class ChatRoom extends AppCompatActivity {

        // Now you can access views using binding.<viewId>
        // For example:
        // binding.textView.setText("Hello, World!");
        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
            }

        }
    ActivityChatRoomBinding binding;
    ArrayList<String> messages = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Send.setOnClickListener(click -> {
            messages.add(binding.editText.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.editText.setText("");
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {

                    @NonNull
                    @Override
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                        return new MyRowHolder(binding.getRoot());
                    }

                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                        holder.timeText.setText("");
                        String obj = messages.get(position);
                        holder.messageText.setText(obj);
                    }

                    @Override
                    public int getItemCount() {
                        return messages.size();
                    }

                    @Override
                    public int getItemViewType(int position){
                        return 0;
                    }
                }
        );
    }
    }
