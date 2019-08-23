package com.example.organization.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.events.ListMyEventsFragment.OnListFragmentInteractionListener;
import com.example.organization.R;
import com.example.organization.data.model.Events;
import com.example.organization.data.model.Photo;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListMyEventsRecyclerViewAdapter extends RecyclerView.Adapter<MyListMyEventsRecyclerViewAdapter.ViewHolder> {


    private final List<Events> mValues;

    private final OnListFragmentInteractionListener mListener;


    String StorageUrl = "https://drive.google.com/uc?export=download&id=";

    View getContexts;


    public MyListMyEventsRecyclerViewAdapter(List<Events> events, OnListFragmentInteractionListener listener) {
        mValues = events;
        mListener = listener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Скопируем layout, которое будем помещать в каждую строку ListView.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_my_event_line, parent, false);
        getContexts = view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.myNameEvent.setText(mValues.get(position).getName());
        holder.myEventStartEndTime.setText(mValues.get(position).getStart() + mValues.get(position).getEnd());


        String format = "MMM dd yyyy";
        SimpleDateFormat format1= new SimpleDateFormat(format);
        Date date1 = null;
        try {
            date1 =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(mValues.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.myEventDate.setText(format1.format(date1));


        List<Photo> photos = mValues.get(position).getPhotos();

        if (photos != null) {

            for (Photo photo : photos) {
                if (photo.isIsmain()) {
                    Picasso picasso = Picasso.get();
                    picasso.load(StorageUrl + photo.getUrl())
                            .resize(125, 98)        // Длина и ширина фотографии.
                            .centerCrop()                                 // Get center. Берём ценрт фотографии, все что помешается в заданный размер.
                            .into(holder.myEventMainImage);

                }
            }
        }
        else {
            // Если у event-а нет фотографии то устанавливаем фотографию no photo.
            Picasso picasso = Picasso.get();
            picasso.load(R.drawable.no_photo).
                    resize(125,98)
                    .centerCrop()
                    .into(holder.myEventMainImage);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView myEventMainImage;
        public final TextView myNameEvent;
        public final TextView myEventStartEndTime;
        public final TextView myEventDate;

        public Events mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            // Поля event-а.
            myEventMainImage = view.findViewById(R.id.my_event_main_image);
            myNameEvent = view.findViewById(R.id.my_event_name);
            myEventStartEndTime = view.findViewById(R.id.my_event_start_end_time);
            myEventDate = view.findViewById(R.id.my_event_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + myEventStartEndTime.getText() + "'";
        }
    }
}
