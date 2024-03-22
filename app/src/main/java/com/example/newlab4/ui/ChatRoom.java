package com.example.newlab4.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newlab4.R;
import com.example.newlab4.databinding.ActivityChatRoomBinding;
import com.example.newlab4.databinding.ReceivedMessageBinding;
import com.example.newlab4.databinding.SentMessageBinding;
import com.google.android.material.snackbar.Snackbar;
import androidx.room.Room;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatMessage.ChatRoomViewModel chatModel ;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    RecyclerView.Adapter<MyRowHolder> myAdpter;
    ChatMessageDAO mDAO;
    private int opID; // 1 for insert and 2 for delete
    private  ChatMessage deletedMessage;
    private int deletPostion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // setSupportActionBar(binding.toolbar);



        binding= ActivityChatRoomBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.toolbar);


        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatMessage.ChatRoomViewModel.class);

       MessageDatabase db= Room.databaseBuilder(getApplicationContext(),MessageDatabase.class,"database-name").build();
mDAO= db.cmDAO();

        messages = chatModel.messages.getValue();


        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
                runOnUiThread( () -> binding.recyclerView.setAdapter( myAdpter )); //You can then load the RecyclerView
            });
        }




        binding.recyclerView.setAdapter(myAdpter=new RecyclerView.Adapter<MyRowHolder>(){

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                if (viewType==0)
                {
                    SentMessageBinding binding= SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());}
                else {
                    ReceivedMessageBinding binding= ReceivedMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }

            }
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position){
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());

            }
            @Override
            public int getItemCount(){
                return messages.size();

            }
            @Override
            public  int getItemViewType(int position){
                ChatMessage obj = messages.get(position);
                if (obj.isSentButton()==true)

                    return 0; // sender message
                else
                    return 1;// receiver message
            }



        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.send.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage obj=new ChatMessage(binding.edittext.getText().toString(),currentDateandTime,true);
            messages.add(obj);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.loninsertMessage(obj);});
            myAdpter.notifyItemInserted(messages.size()-1);
            binding.edittext.setText("");
            opID=1;

        });
        binding.receive.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage obj=new ChatMessage(binding.edittext.getText().toString(),currentDateandTime,false);
            messages.add(obj);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.loninsertMessage(obj);});
            myAdpter.notifyItemInserted(messages.size()-1);

            binding.edittext.setText("");
            opID=1;

        });

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;



        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
            itemView.setOnClickListener(e->{
                int position=getAbsoluteAdapterPosition();
                ChatMessage m=messages.get(position);

                builder.setMessage("Do you want to delete the message:" +messageText.getText());
                builder.setTitle("Question:");
                builder.setNegativeButton("NO", (dialog, cl)->{});
                builder.setPositiveButton( "Yes", (dialog, cl)->{
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->
                    {
                        mDAO.deleteMessage(m);});

                    messages. remove(position);
                    myAdpter.notifyItemRemoved(position);
                    Snackbar.make(e,"You deleted message #" , Snackbar.LENGTH_LONG)
                            .setAction (  "Undo", clk -> {

                                messages.add (position, m); myAdpter.notifyItemInserted(position);})
                            .show();

                    deletedMessage = m;
                    deletPostion=position;
                    opID=2;

                });
                builder.create().show();

            });
            messageText=itemView.findViewById(R.id.message);
            timeText=itemView.findViewById(R.id.time);

        }
    }


            }
