package com.example.organization.events;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organization.Constants;
import com.example.organization.R;
import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.EventToOffer;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.Photo;
import com.example.organization.events.EventOfferFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyEventOfferRecyclerViewAdapter extends RecyclerView.Adapter<MyEventOfferRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final List<EventToOffer> mValues;
    private List<EventToOffer> mValuesFull;
    private final OnListFragmentInteractionListener mListener;
    Context mContext;
    CheckBox searchByName;
    CheckBox searchByZipCode;

    public MyEventOfferRecyclerViewAdapter(Activity activity, List<EventToOffer> items,
                                           OnListFragmentInteractionListener listener,
                                           Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
        mValuesFull = new ArrayList<>(items);
        searchByName = activity.findViewById(R.id.search_by_name);
        searchByZipCode = activity.findViewById(R.id.search_by_zip_code);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_eventoffer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        System.out.println(holder.mItem.toString());
        if (holder.mItem.getPhotos() != null) {
            System.out.println("HAVE PHOTOS !!!!!!!!!!!!!!!");
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
        holder.eventDate.setText(holder.mItem.getDate());
        holder.startEndTime.setText(holder.mItem.getStart() + "-" + holder.mItem.getEnd());

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        final Initiator initiator = retrofit.create(Initiator.class);

        holder.acceptEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = initiator.acceptEvent(LoginDataSource.getInitiator().getToken(), holder.mItem.getEventId());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.code() == 200){
                            if (mValues.size() > 0) {
                                mValues.remove(position);
                            }
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,mValues.size());
                            String name = holder.mItem.getName();
                            Toast.makeText(mContext, "Accept : " + name, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });

        holder.cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = initiator.cancelEvent(LoginDataSource.getInitiator().getToken(), holder.mItem.getEventId());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.code() == 200){
                            if (mValues.size() > 0) {
                                mValues.remove(position);
                            }
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,mValues.size());
                            String name = holder.mItem.getName();
                            Toast.makeText(mContext, "Cancel : " + name, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public Filter getFilter() {
        return ourFilter;
    }

    private Filter ourFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<EventToOffer> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mValuesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                if (searchByName.isChecked()) {
                    for (EventToOffer item : mValuesFull) {
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
