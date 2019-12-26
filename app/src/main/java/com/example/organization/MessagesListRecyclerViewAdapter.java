package com.example.organization;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.organization.MessagesFragment.OnListFragmentInteractionListener;
import com.example.organization.data.model.Conversation;
import com.example.organization.data.model.Restaurant.RestaurantInformation;
import com.example.organization.data.model.room.Messages;
import com.example.organization.data.model.room.Messenger;
import com.example.organization.events.ListMyEventsFragment;
import com.example.organization.room.MessengerRepository;
import com.example.organization.room.MessengerViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Conversation} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MessagesListRecyclerViewAdapter extends RecyclerView.Adapter<MessagesListRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final List<Messenger> mValues;
    private List<Messenger> mValuesFull;
    public static final String IMAGE_URL_PREFIX = "https://drive.google.com/uc?export=download&id=";
    private final OnListFragmentInteractionListener mListener;
    Application application;
    LifecycleOwner lifecycleOwner;
    CheckBox searchByName;
    CheckBox searchByZipCode;

    public MessagesListRecyclerViewAdapter(Activity activity, LifecycleOwner lifecycleOwner, Application application, List<Messenger> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        if (items !=  null) {
            mValuesFull = new ArrayList<>(items);
        } else {
            mValuesFull = new ArrayList<>();
        }
        this.application = application;
        this.lifecycleOwner = lifecycleOwner;
        searchByName = activity.findViewById(R.id.search_by_name);
        searchByZipCode = activity.findViewById(R.id.search_by_zip_code);
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

        holder.conversationLastMessageTime.setText("++++++");
        Picasso picasso = Picasso.get();

        MessengerViewModel mMessengerViewModel = new MessengerViewModel(application);

        System.out.println("IDDDDDDDDDDDDEEEEEEEEEEEE = " + holder.mItem.getMessengerId());
        mMessengerViewModel.getAllMessagesByMessengerId(holder.mItem.getMessengerId())
                .observe(lifecycleOwner, new Observer<List<Messages>>() {

            @Override
            public void onChanged(@Nullable List<Messages> messages) {
                System.out.println("ITS WOOOOOOOOOOOOOOOOOOOOOOORKS");
                if (messages != null && messages.size() > 0) {

                    holder.conversationLastMessage.setText(messages.get(0).getInput() + messages.get(0).getOutput());
                    System.out.println("----------- " + messages.get(0).getInput() + messages.get(0).getOutput());
                }
            }
        });

        picasso.load(IMAGE_URL_PREFIX + holder.mItem.getIconUri())
                .fit()
                .placeholder(R.drawable.photo)
                .error(R.drawable.no_photo)
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

    @Override
    public Filter getFilter() {
        return ourFilter;
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

    private Filter ourFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Messenger> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mValuesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                if (searchByName.isChecked()) {
                    for (Messenger item : mValuesFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                } else {

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mValues.clear();
            mValues.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
