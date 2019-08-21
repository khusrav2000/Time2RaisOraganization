package com.example.organization;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.ListEventsFragment.OnListFragmentInteractionListener;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.Photo;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListEventsRecyclerViewAdapter extends RecyclerView.Adapter<MyListEventsRecyclerViewAdapter.ViewHolder> {

    private final List<Events> mValues;
    private final OnListFragmentInteractionListener mListener;
    String StorageUrl = "https://drive.google.com/uc?export=download&id=";

    View getContexts;

    public MyListEventsRecyclerViewAdapter(List<Events> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listevents, parent, false);
        getContexts = view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.nameEvent.setText(mValues.get(position).getName());
        holder.eventStartEndTime.setText(mValues.get(position).getStart() + mValues.get(position).getEnd());


        String format = "MMM dd yyyy";
        SimpleDateFormat format1= new SimpleDateFormat(format);
        Date date1 = null;
        try {
            date1 =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(mValues.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.eventDate.setText(format1.format(date1).toString());

        List<Photo> photos = mValues.get(position).getPhotos();

        if (photos != null) {
            for (Photo photo : photos) {
                if (photo.isIsmain()) {
                    Picasso picasso = Picasso.get();
                    picasso.load(StorageUrl + photo.getUrl())
                            .resize(125, 98)
                            .centerCrop()
                            .into(holder.eventMainImage);

                }
            }
        }
        else {
            Picasso picasso = Picasso.get();
            picasso.load(R.drawable.no_photo).
                    resize(125,98)
                    .centerCrop()
                    .into(holder.eventMainImage);
        }

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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView eventMainImage;
        public final TextView nameEvent;
        public final TextView eventStartEndTime;
        public final TextView eventDate;

        public Events mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            eventMainImage = view.findViewById(R.id.event_main_image);
            nameEvent = view.findViewById(R.id.event_name);
            eventStartEndTime = view.findViewById(R.id.event_start_end_time);
            eventDate = view.findViewById(R.id.event_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventStartEndTime.getText() + "'";
        }
    }
}
