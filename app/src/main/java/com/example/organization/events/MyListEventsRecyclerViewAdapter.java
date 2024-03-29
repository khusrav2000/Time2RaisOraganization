package com.example.organization.events;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.data.model.EventToOffer;
import com.example.organization.events.ListEventsFragment.OnListFragmentInteractionListener;
import com.example.organization.R;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.Photo;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListEventsRecyclerViewAdapter extends RecyclerView.Adapter<MyListEventsRecyclerViewAdapter.ViewHolder> implements Filterable {

    // Список event-ов, которые мы получаем из сервера.
    // Events - Это модель для помещения в него получаемых данных из сервера.
    private final List<Events> mValues;
    private List<Events> mValuesFull;

    private final OnListFragmentInteractionListener mListener;
    Activity activity;

    CheckBox searchByName;
    CheckBox searchByZipCode;

    // Ссылка на хранилище фотографий этого проета.
    // Фотографии находяться в GoogleDrive-е.
    String StorageUrl = "https://drive.google.com/uc?export=download&id=";

    View getContexts;

    public MyListEventsRecyclerViewAdapter(Activity activity, List<Events> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mValuesFull = new ArrayList<>(items);
        searchByName = activity.findViewById(R.id.search_by_name);
        searchByZipCode = activity.findViewById(R.id.search_by_zip_code);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Скопируем layout, которое будем помещать в каждую строку ListView.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event_line, parent, false);
        getContexts = view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.nameEvent.setText(mValues.get(position).getName());
        holder.eventStartEndTime.setText(mValues.get(position).getStart() + mValues.get(position).getEnd());

        // Устанавливаем дату проведения event-а в нужном формате.
        String format = "MMM dd yyyy";
        SimpleDateFormat format1= new SimpleDateFormat(format);
        Date date1 = null;
        try {
            date1 =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(mValues.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.eventDate.setText(format1.format(date1));

        // Список URL адресов фотографий event-а.
        List<Photo> photos = mValues.get(position).getPhotos();

        if (photos != null) {
            // Если у event-а есть фотографии, то ищем какая из них главная и устанваливем его.
            for (Photo photo : photos) {
                if (photo.isIsmain()) {
                    Picasso picasso = Picasso.get();
                    picasso.load(StorageUrl + photo.getUrl())
                            .resize(125, 98)        // Длина и ширина фотографии.
                            .centerCrop()                                 // Get center. Берём ценрт фотографии, все что помешается в заданный размер.
                            .into(holder.eventMainImage);

                }
            }
        }
        else {
            // Если у event-а нет фотографии то устанавливаем фотографию no photo.
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

    // Получения количество event-ов с списке.
    @Override
    public int getItemCount() {
        if (mValues != null)
            return mValues.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return ourFilter;
    }

    private Filter ourFilter = new Filter() {

        int searchBy = 1;
        public void setSearchBy(int i){
            searchBy = i;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Events> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mValuesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                if (searchByName.isChecked()) {
                    for (Events item : mValuesFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                } else {
                    /*for (Events item : mValuesFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }*/
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

            // Поля event-а.
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
