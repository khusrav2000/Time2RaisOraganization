package com.example.organization.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.organization.data.model.SendMessage;
import com.example.organization.data.model.room.Messages;
import com.example.organization.data.model.room.Messenger;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.Date;
import java.util.List;

public class MessengerViewModel extends AndroidViewModel {

    private MessengerRepository mRepository;

    private LiveData<List<Messenger>> mAllMessenger;
    private LiveData<List<Messages>> mMessagesByMessengerId;
    private LiveData<Date>              lastMessagesTime;
    private LiveData<Integer>           allCountsMessages;
    private LiveData<Integer>           countMessase;



    public MessengerViewModel(@NonNull Application application) {
        super(application);

        mRepository            = new MessengerRepository(application);
        mAllMessenger          = mRepository.getAllMessengers();
        //lastMessagesTime       = mRepository.getLastMessages(0);
        allCountsMessages      = mRepository.getAllCountsMessages(true);
        countMessase           = mRepository.getCountMessase(true, 0);
        mMessagesByMessengerId = mRepository.getAllMessagesByMessengerId(0);
    }
    public LiveData<List<Messenger>> getAllMessenger() { return mAllMessenger; }

    public Messages getLastMessage(int messengerId){
        return mRepository.getLastMessage(messengerId);
    }

    public LiveData<List<Messages>> getAllMessagesByMessengerId(int messengerId) {
        mMessagesByMessengerId = mRepository.getAllMessagesByMessengerId(messengerId);
        return mMessagesByMessengerId;
    }

    public Messages getMessagesByDocId(String doc_id){
        return mRepository.getMessagesByDocId(doc_id);
    }

    public LiveData<Date> getLastMessagesTime() {
        return lastMessagesTime;
    }

    public LiveData<Integer> getAllCountsMessages() {
        return allCountsMessages;
    }

    public LiveData<Integer> getCountMessase() {
        return countMessase;
    }

    public void insert(Messenger messenger) {
        System.out.println("MessengerViewModel :    "+messenger.toString());
        mRepository.insert(messenger);

    }

    public  void updateDocId(String doc_id, int messages_id){
        mRepository.updateDocId(doc_id, messages_id);
    }
    public int insertMessageWithSending(Messages messages, SendMessage sendMessage) {
        return mRepository.insertMessageWithSending(messages, sendMessage);
    }

    public int insertMessageWithOutSending(Messages messages) {
        return mRepository.insertMessageWithOutSending(messages);
    }

    public void syncMessage(DocumentSnapshot snapshot, int messagerId){mRepository.syncMessages( snapshot, messagerId);}

    public void sendMessage(SendMessage sendMessage, int message_id){
        mRepository.sendMessages(sendMessage , message_id);
    }

    public void deleteZeroMessenger(){
        mRepository.deleteZero();
    }
}
