package com.example.organization.data.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.net.ParseException;

import com.example.organization.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//import org.charitable.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "messages",
        foreignKeys = @ForeignKey(
                entity = Messenger.class,
                parentColumns = "messenger_id",
                childColumns = "messenger_id",
                onDelete = CASCADE),
//, @Index( value = {"doc_id"}, unique = true)
        indices = {@Index(value = {"messenger_id"}, unique = false)})
public class Messages {

    @SerializedName("messagesID")
    @Expose(serialize = false)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "messages_id")
    int messageId;

    @SerializedName("messengerID")
    @ColumnInfo(name = "messenger_id")
    int messenger_id;

    @SerializedName("input")
    @ColumnInfo(name = "input")
    String input;

    @SerializedName("output")
    @ColumnInfo(name = "output")
    String output;

    @SerializedName("isnew")
    @ColumnInfo(name = "isnew")
    Boolean isNew;

    @SerializedName("DateTimePost")
    @ColumnInfo(name = "date_time_post")
    @TypeConverters({Converters.class})
    Date dateTimePost;

    @ColumnInfo(name = "doc_id")
    String docID;

    @Ignore
    public Messages(int messageId, int messenger_id, String input, String output, Boolean isNew, Date dateTimePost, String docID) {
        this.messageId = messageId;
        this.messenger_id = messenger_id;
        this.input = input;
        this.output = output;
        this.isNew = isNew;
        this.dateTimePost = dateTimePost;
        this.docID = docID;
    }

    public Messages(int messenger_id, String input, String output, Boolean isNew, Date dateTimePost) {
        this.messenger_id = messenger_id;
        this.input = input;
        this.output = output;
        this.isNew = isNew;
        this.dateTimePost = dateTimePost;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getMessenger_id() {
        return messenger_id;
    }

    public void setMessenger_id(int messenger_id) {
        this.messenger_id = messenger_id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Date getDateTimePost() {
        return dateTimePost;
    }

    public void setDateTimePost(Date dateTimePost) {
        this.dateTimePost = dateTimePost;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messageId=" + messageId +
                ", messenger_id=" + messenger_id +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", isNew=" + isNew +
                ", dateTimePost=" + dateTimePost +
                ", docID='" + docID + '\'' +
                '}';
    }

    public static class Converters {

        static SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT);

        @TypeConverter
        public static Date fromTimestamp(String value) {
            if (value != null) {
                try {
                    return df.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                    return null;
                }

            } else {
                return null;
            }
        }
        @TypeConverter
        public Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public Long dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                return date.getTime();
            }
        }
    }
}
