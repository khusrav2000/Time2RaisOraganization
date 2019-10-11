package com.example.organization.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;


import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.SendMessage;
import com.example.organization.data.model.room.Dao.MessagesDao;
import com.example.organization.data.model.room.Dao.MessengerDao;
import com.example.organization.data.model.room.Messages;
import com.example.organization.data.model.room.Messenger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessengerRepository {

    private MessengerDao                messengerDao;
    private MessagesDao                 messagesDao;
    private LiveData<List<Messenger>>   allMessengers;
    private LiveData<List<Messages>>    allMessages;
    private List<Messages>              messagesByDocId;


    MessengerRepository(Application application){
        MessengerRoomDatabase db = MessengerRoomDatabase.getDatabase(application);

        messengerDao = db.MessengerDao();
        messagesDao  = db.Messagesdao();

        allMessengers = messengerDao.getAllMessengers();
        allMessages   = messagesDao.getAllMessagesByMessengerId(0);



    }

    LiveData<List<Messenger>> getAllMessengers() {
        return allMessengers;
    }

    LiveData<List<Messages>> getAllMessagesByMessengerId(int messengerId){

        allMessages = messagesDao.getAllMessagesByMessengerId(messengerId);

        return allMessages;

    }

    Messages getMessagesByDocId(String doc_id){
        return  messagesDao.getMessageByDocId(doc_id);
    }
    public void insert (Messenger messenger) {
        new insertMessenger(messengerDao).execute(messenger);
    }

    public void updateDocId(String doc_id, int messages_id){
        new updateMessage(messagesDao, messages_id).execute(doc_id);
    }

    public int insertMessageWithSending (Messages messages, SendMessage sendMessage)  {

        try {
            return new insertMessageWithSending(messagesDao, sendMessage).execute(messages).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public int insertMessageWithOutSending (Messages messages)  {

        try {
            return new insertMessageWithOutSending(messagesDao).execute(messages).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void syncMessages (DocumentSnapshot snapshot, int messagerId){
        new syncMessages(messagesDao, messagerId).execute(snapshot);
    }

    public void deleteZero(){
        new deleteZero(messengerDao).execute(true);
    }
    /*public LiveData<Date> getLastMessages(int messengerId){
        return messagesDao.getLastMessageTime(messengerId);
    }*/

    public LiveData<Integer> getAllCountsMessages(boolean typeMessages){
        return messagesDao.getAllCauntsNewMessage(typeMessages);
    }

    public LiveData<Integer> getCountMessase(boolean typeMessages, int messengerId){
        return messagesDao.getCauntNewMessage(typeMessages, messengerId);
    }

    public void sendMessages (SendMessage sendMessage, int messages_id){ new SendMessages(sendMessage, messages_id).execute(sendMessage);}

    private static class syncMessagesTask extends AsyncTask<Integer, Void, Void>{

        private FirebaseFirestore mFirestore ;
        // ...
        syncMessagesTask(){
            mFirestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
            mFirestore.setFirestoreSettings(settings);

        }



        @Override
        protected Void doInBackground(Integer... integers) {

            //mDatabase.child("MessangerRestouran").equalTo(integers[0],"RestouranID").
            System.out.println("integers="+integers[0]);
            mFirestore.collection("MessangerRestouran")
                    .whereEqualTo("Initiator.InitiatorID",integers[0])
                    .whereEqualTo("Restouran.RestouranID",integers[1])
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if (task.getResult().getDocuments().size() > 0){

                            DocumentReference reference = task.getResult().getDocuments().get(0).getReference();

                            reference.collection("Messages").orderBy("DateTimePost").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful())
                                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {

                                            System.out.println(snapshot.getId()+": ");
                                            System.out.println("DateTimePost: "+snapshot.get("DateTimePost"));
                                            System.out.println("Text: "+snapshot.get("Text"));
                                            System.out.println("WMessage: "+snapshot.get("WMessage"));
                                            System.out.println("-------------------------------------");
                                        }


                                }
                            });

                        }
                    }
                }
            });

            return null;
        }
    }

    private static class syncMessages extends AsyncTask<DocumentSnapshot, Void, Void>{

        private MessagesDao mAsyncTaskDao;
        private int messengerId;

        syncMessages(MessagesDao dao, int messengerId) {
            this.mAsyncTaskDao = dao;
            this.messengerId = messengerId;

        }



        @Override
        protected Void doInBackground(DocumentSnapshot... snapshots) {


            if (mAsyncTaskDao.getMessageByDocId(snapshots[0].getId()) == null){
                String text =(String) snapshots[0].get("Text");
                String wm =(String) snapshots[0].get("WMessage");
                Timestamp timestamp = (Timestamp) snapshots[0].get("DateTimePost");
                long messagesId = (long) snapshots[0].get("MessageId");



                //  long messageId =(long) snapshots[0].get("MessageId");

                Messages messages = null;
                if ( wm.equals("I") )
                    messages = new Messages(messengerId, text, null, false, timestamp.toDate());
                else if ( wm.equals("R") ) {

                    messages = new Messages( messengerId, null, text, false, timestamp.toDate());

                    Messages dbMessage =  mAsyncTaskDao.getMessageById((int) messagesId);

                    if (dbMessage != null){

                        mAsyncTaskDao.updateDocId(snapshots[0].getId(), dbMessage.getMessageId());
                        return null;
                    }


                }
                if ( messages != null ) {

                    System.out.println(messages.toString());
                    messages.setDocID(snapshots[0].getId());

                }
                mAsyncTaskDao.insert(messages);


            }
            return null;
        }
    }


    private static class insertMessenger extends AsyncTask<Messenger, Void, Void> {

        private MessengerDao mAsyncTaskDao;

        insertMessenger(MessengerDao dao) {
            mAsyncTaskDao = dao;
        }



        @Override
        protected Void doInBackground(Messenger... messengers) {
            mAsyncTaskDao.insert(messengers[0]);

            return null;
        }
    }

    private static class insertMessageWithSending extends AsyncTask<Messages, Integer, Integer> {

        private MessagesDao mAsyncTaskDao;
        private SendMessage sendMessage;

        insertMessageWithSending(MessagesDao dao, SendMessage sendMessage) {
            this.mAsyncTaskDao = dao;
            this.sendMessage = sendMessage;
        }



        @Override
        protected Integer doInBackground(Messages... messages) {

            int id = (int) mAsyncTaskDao.insert(messages[0]);

            Retrofit retrofit = NetworkClient.getRetrofitClient();
           Initiator iOrganization = retrofit.create(Initiator.class);

            sendMessage.setMessageId(id);

            Call call = iOrganization.sendMessages(LoginDataSource.getInitiator().getToken(), sendMessage);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    if ( response.isSuccessful() ) {
                        com.example.organization.data.model.Messages resultMessages = (com.example.organization.data.model.Messages) response.body();
                        //mAsyncTaskDao.updateDocId(resultMessages.getMessages(), sendMessage.getMessageId());

                      //  MessengerViewModel model = new MessengerViewModel();
                      //  new updateMessage(mAsyncTaskDao, sendMessage.getMessageId()).execute(resultMessages.getMessages());


                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });


                    return id;

        }
    }

    private static class insertMessageWithOutSending extends AsyncTask<Messages, Integer, Integer> {

        private MessagesDao mAsyncTaskDao;


        insertMessageWithOutSending(MessagesDao dao) {
            this.mAsyncTaskDao = dao;

        }



        @Override
        protected Integer doInBackground(Messages... messages) {

            int id = (int) mAsyncTaskDao.insert(messages[0]);


            return id;
        }
    }

    public static class updateMessage extends AsyncTask<String, Void, Void> {

        private MessagesDao mAsyncTaskDao;
        private int messages_id;

        updateMessage(MessagesDao dao, int messages_id) {
            mAsyncTaskDao = dao;
            messages_id = messages_id;
        }



        @Override
        protected Void doInBackground(String... doc_id) {
            mAsyncTaskDao.updateDocId(doc_id[0],messages_id);

            return null;
        }
    }

    private static class SendMessages extends AsyncTask<SendMessage, Void, Boolean> {

        private SendMessage sendMessage;
        private int messages_id;

        SendMessages(SendMessage sendMessage, int messages_id) {
            sendMessage = sendMessage;
            messages_id = messages_id;

        }



        @Override
        protected Boolean doInBackground(SendMessage... messages) {

            Retrofit retrofit = NetworkClient.getRetrofitClient();
            Initiator iOrganization = retrofit.create(Initiator.class);

            Call call = iOrganization.sendMessages(LoginDataSource.getInitiator().getToken(),messages[0]);
            try {
                Response response = call.execute();

                if(response.isSuccessful()) {

                    com.example.organization.data.model.Messages resultMessages = (com.example.organization.data.model.Messages) response.body();


                    return true;
                }
                else
                    return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
    }
    private static class deleteZero extends AsyncTask<Boolean, Void, Void> {

        private MessengerDao mAsyncTaskDao;

        deleteZero(MessengerDao dao) {
            mAsyncTaskDao = dao;
        }



        @Override
        protected Void doInBackground(Boolean ... bool) {

            if (bool[0]){
                mAsyncTaskDao.deleteAllZeroMessengers();
            }
            return null;
        }
    }

}


