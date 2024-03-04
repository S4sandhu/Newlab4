package ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newlab4.R;
import com.example.newlab4.databinding.ActivityChatRoomBinding;
import com.example.newlab4.databinding.SentMessageBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.ChatRoomViewModel;


public class ChatRoom extends AppCompatActivity {

    ChatRoomViewModel chatModel;
    ArrayList<String> messages = new ArrayList<>();
    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<>());
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Send.setOnClickListener(click -> {
            String message = binding.editText.getText().toString();
            String currentTime = getCurrentTime();
            messages.add(String.valueOf(new ChatMessage(message, currentTime, true)));
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.editText.setText("");
        });

        binding.recieve.setOnClickListener(click -> {
            String message = "Received message"; // You can set the received message here
            String currentTime = getCurrentTime();
            messages.add(String.valueOf(new ChatMessage(message, currentTime, false)));
            myAdapter.notifyItemInserted(messages.size() - 1);
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

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
            public int getItemViewType(int position) {
                return 0;
            }
        });
    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        return sdf.format(new Date());
    }
}

