package com.example.organization.requests;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.data.model.Request;
import com.example.organization.R;
import com.example.organization.data.model.Photo;
import com.squareup.picasso.Picasso;
import com.example.organization.requests.ListMyRequestsFragment.OnListFragmentInteractionListener;

import java.nio.Buffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListMyRequestsRecyclerViewAdapter extends RecyclerView.Adapter<MyListMyRequestsRecyclerViewAdapter.ViewHolder> {


    private final List<Request> mValues;

    private final ListMyRequestsFragment.OnListFragmentInteractionListener mListener;


    String StorageUrl = "https://drive.google.com/uc?export=download&id=";

    View getContexts;


    public MyListMyRequestsRecyclerViewAdapter(List<Request> requests, ListMyRequestsFragment.OnListFragmentInteractionListener listener) {
        mValues = requests;
        mListener = listener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Скопируем layout, которое будем помещать в каждую строку ListView.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_my_request_line, parent, false);
        getContexts = view;
        return new ViewHolder(view);
    }

    private String getCorrectTime(String date){
        String format = "HH:mm aa";
        SimpleDateFormat format1= new SimpleDateFormat(format);
        Date date1 = null;
        try {
            date1 =new SimpleDateFormat("HH:mm:ss").parse(date);
        } catch (ParseException e) {
            //e.printStackTrace();
        }

        if (date1 == null) {
            try {
                date1 = new SimpleDateFormat("HH:mm:ss").parse("10:10:00");
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }
        return format1.format(date1);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.myNameRequest.setText(mValues.get(position).getName());
        holder.myRequestStartEndTime.setText(
                getCorrectTime(mValues.get(position).getStart()) + " - " +
                        getCorrectTime(mValues.get(position).getEnd()));




        holder.myRequestDate.setText(getCorrectDate(mValues.get(position).getDate()));


        /*List<Photo> photos = mValues.get(position).getPhotos();
        */
        List<Photo> photos = null;
        if (photos != null) {

            for (Photo photo : photos) {
                if (photo.isIsmain()) {
                    Picasso picasso = Picasso.get();
                    picasso.load(StorageUrl + photo.getUrl())
                            .resize(125, 98)        // Длина и ширина фотографии.
                            .centerCrop()                                 // Get center. Берём ценрт фотографии, все что помешается в заданный размер.
                            .into(holder.myRequestMainImage);

                }
            }
        }
        else {
            // Если у event-а нет фотографии то устанавливаем фотографию no photo.
            Picasso picasso = Picasso.get();
            picasso.load(R.drawable.no_photo).
                    resize(125,98)
                    .centerCrop()
                    .into(holder.myRequestMainImage);
        }

        // Отслеживания на нажатия на кнопку detail для request-ов.
        holder.editMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    // Информация о request-е идёк в MainClass в метод onListFragmentInteraction через класс ListRequestsFragment.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

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

        public final ImageView myRequestMainImage;
        public final TextView myNameRequest;
        public final TextView myRequestStartEndTime;
        public final TextView myRequestDate;
        final Button editMyRequest;

        public Request mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            // Поля event-а.
            myRequestMainImage = view.findViewById(R.id.my_request_main_image);
            myNameRequest = view.findViewById(R.id.my_request_name);
            myRequestStartEndTime = view.findViewById(R.id.my_request_start_end_time);
            myRequestDate = view.findViewById(R.id.my_request_date);
            editMyRequest = view.findViewById(R.id.my_request_edit_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + myNameRequest.getText() + "'";
        }
    }

    private String getCorrectDate(String date){
        String format = "MMM dd yyyy";
        SimpleDateFormat format1= new SimpleDateFormat(format);
        System.out.println(" DATE = " + date);
        Date date1 = null;
        try {
            date1 =new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            //e.printStackTrace();
        }

        if (date1 == null){
            try {
                date1 =new SimpleDateFormat("yyyy-MM-dd").parse("2000-10-10T10:10");
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }
        return format1.format(date1);
    }
}
