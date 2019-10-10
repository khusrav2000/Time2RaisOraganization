package com.example.organization;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.MessagesFragment.OnListFragmentInteractionListener;
import com.example.organization.data.model.Conversation;
import com.example.organization.data.model.room.Messenger;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Conversation} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MessagesListRecyclerViewAdapter extends RecyclerView.Adapter<MessagesListRecyclerViewAdapter.ViewHolder> {

    private final List<Messenger> mValues;
    public static final String IMAGE_URL_PREFIX = "https://drive.google.com/uc?export=download&id=";
    private final OnListFragmentInteractionListener mListener;

    public MessagesListRecyclerViewAdapter(List<Messenger> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        System.out.println("!-----------" + "Posiiontion " + position);
        //holder.mIdView.setText(mValues.get(position).id);
        //holder.mContentView.setText(mValues.get(position).content);
        holder.conversationName.setText(holder.mItem.getName());
        holder.conversationLastMessage.setText(holder.mItem.getLastMessge());
        holder.conversationLastMessageTime.setText("--");
        Picasso picasso = Picasso.get();

        picasso.load(IMAGE_URL_PREFIX + holder.mItem.getIconUri())
                .fit()
                .centerCrop()
                .into(holder.conversationImg);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        if (mValues == null)
            return 0;

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView conversationName;
        public final TextView conversationLastMessage;
        public final TextView conversationLastMessageTime;
        public final ImageView conversationImg;


        public Messenger mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
           // mContentView = (TextView) view.findViewById(R.id.content);
            conversationName = view.findViewById(R.id.conversation_name);
            conversationLastMessage = view.findViewById(R.id.conversation_last_message);
            conversationLastMessageTime = view.findViewById(R.id.conversations_last_message_time);
            conversationImg = view.findViewById(R.id.conversation_img);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
