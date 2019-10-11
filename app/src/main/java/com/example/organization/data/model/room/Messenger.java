package com.example.organization.data.model.room;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "messenger")
public class Messenger extends ViewModel {

    //messenger id equals initiator id
    @SerializedName("messengerID")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "messenger_id")
    int messengerId;

   /* @SerializedName("InitID")
    @ColumnInfo(name = "initiator_id")
    int initId;
*/


    @SerializedName("Name")
    @ColumnInfo(name = "name")
    String name;

    @SerializedName("Email")
    @ColumnInfo(name = "email")
    String email;

    @SerializedName("CountNewPost")
    @ColumnInfo(name = "n_new_post")
    public int nNewPost;

    @SerializedName("iconUrl")
    @ColumnInfo(name = "uri")
    String iconUri;

    @SerializedName("LastMessage")
    @ColumnInfo(name = "last_message")
    public String lastMessage;

    /*@ColumnInfo(name = "doc_id")
    String docID;*/

    public Messenger(int messengerId,  String name, String email, int nNewPost, String iconUri, String lastMessage) {
        this.messengerId = messengerId;
        //      this.initId = initId;
        this.name = name;
        this.email = email;
        this.nNewPost = nNewPost;
        this.iconUri = iconUri;
        this.lastMessage = lastMessage;
    }

    public int getMessengerId() {
        return messengerId;
    }

    public void setMessengerId(int messengerId) {
        this.messengerId = messengerId;
    }

    /*
        public int getInitId() {
            return initId;
        }

        public void setInitId(int initId) {
            this.initId = initId;
        }
    */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getnNewPost() {
        return nNewPost;
    }

    public void setnNewPost(int nNewPost) {
        this.nNewPost = nNewPost;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }


    public String getLastMessge() {
        return lastMessage;
    }

    public void setLastMessge(String lastMessge) {
        this.lastMessage = lastMessge;
    }

    @Override
    public String toString() {
        return "Messenger{" +
                "messengerId=" + messengerId +
                //              ", initId=" + initId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nNewPost=" + nNewPost +
                ", iconUri='" + iconUri + '\'' +
                ", lastMessge='" + lastMessage + '\'' +
                '}';
    }
}
