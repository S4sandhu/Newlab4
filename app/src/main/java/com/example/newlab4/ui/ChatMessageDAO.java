package com.example.newlab4.ui;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ChatMessageDAO  {
    @Insert
     void loninsertMessage(ChatMessage m);
    @Query("select * from ChatMessage")
    List<ChatMessage> getAllMessages();

    @Delete
    void deleteMessage (ChatMessage m);

}
