package com.example.organization.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.Constants;
import com.example.organization.R;
import com.example.organization.data.model.EventToOffer;
import com.example.organization.data.model.Photo;
import com.example.organization.events.EventOfferFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyEventOfferRecyclerViewAdapter extends RecyclerView.Adapter<MyEventOfferRecyclerViewAdapter.ViewHolder> {

    private final List<EventToOffer> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyEventOfferRecyclerViewAdapter(List<EventToOffer> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_eventoffer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem.getPhotos() != null) {
            Photo mainImage = null;
            for(Photo photo : holder.mItem.getPhotos()){
                if (photo.isIsmain()){
                    mainImage = photo;
                    break;
                }
            }

            if (mainImage != null) {
                Picasso picasso = Picasso.get();
                picasso.load(Constants.IMAGE_URL_PREFIX + mainImage.getUrl())
                        .fit()
                        .placeholder(R.drawable.photo)
                        .error(R.drawable.no_photo)
                        .into(holder.eventMainImage);
            }
        }
        holder.eventName.setText(holder.mItem.getName());

        holder.acceptEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private EventToOffer mItem;
        private final ImageView eventMainImage;
        private final TextView eventName;
        private final TextView startEndTime;
        private final TextView eventDate;
        private final Button acceptEvent;
        private final Button cancelEvent;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            eventMainImage = view.findViewById(R.id.event_offer_main_image);
            eventName = view.findViewById(R.id.restaurant_name);
            startEndTime = view.findViewById(R.id.event_start_end_time);
            eventDate = view.findViewById(R.id.event_date);
            acceptEvent = view.findViewById(R.id.accept_event_button);
            cancelEvent = view.findViewById(R.id.cancel_event_button);

        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}
