package com.example.organization.requests;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organization.data.model.Request;
import com.example.organization.requests.ListRequestsFragment.OnListFragmentInteractionListener;
import com.example.organization.R;
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
public class MyListRequestsRecyclerViewAdapter extends RecyclerView.Adapter<MyListRequestsRecyclerViewAdapter.ViewHolder> {

    // Список event-ов, которые мы получаем из сервера.
    // Events - Это модель для помещения в него получаемых данных из сервера.
    private final List<Request> mValues;

    private final ListRequestsFragment.OnListFragmentInteractionListener mListener;

    // Ссылка на хранилище фотографий этого проета.
    // Фотографии находяться в GoogleDrive-е.
    String StorageUrl = "https://drive.google.com/uc?export=download&id=";

    View getContexts;

    public MyListRequestsRecyclerViewAdapter(List<Request> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public MyListRequestsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Скопируем layout, которое будем помещать в каждую строку ListView.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request_line, parent, false);
        getContexts = view;
        return new MyListRequestsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyListRequestsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.nameRequest.setText(mValues.get(position).getName());
        holder.requestStartEndTime.setText(mValues.get(position).getStart() + mValues.get(position).getEnd());

        // Устанавливаем дату проведения event-а в нужном формате.
        String format = "MMM dd yyyy";
        SimpleDateFormat format1= new SimpleDateFormat(format);
        Date date1 = null;
        try {
            date1 =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(mValues.get(position).getDate());
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        if (date1 != null)
            holder.requestDate.setText(format1.format(date1));
        else holder.requestDate.setText("--");
        // Список URL адресов фотографий event-а.
        /*List<Photo> photos = mValues.get(position).getPhotos();
        */
        List<Photo> photos = null;
        if (photos != null) {
            // Если у event-а есть фотографии, то ищем какая из них главная и устанваливем его.
            for (Photo photo : photos) {
                if (photo.isIsmain()) {
                    Picasso picasso = Picasso.get();
                    picasso.load(StorageUrl + photo.getUrl())
                            .resize(125, 98)        // Длина и ширина фотографии.
                            .centerCrop()                                 // Get center. Берём ценрт фотографии, все что помешается в заданный размер.
                            .into(holder.requestMainImage);

                }
            }
        }
        else {
            // Если у event-а нет фотографии то устанавливаем фотографию no photo.
            Picasso picasso = Picasso.get();
            picasso.load(R.drawable.no_photo).
                    resize(125,98)
                    .centerCrop()
                    .into(holder.requestMainImage);
        }

        // Отслеживания на нажатия на кнопку detail для request-ов.
        holder.requestDetail.setOnClickListener(new View.OnClickListener() {
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
        public final ImageView requestMainImage;
        public final TextView nameRequest;
        public final TextView requestStartEndTime;
        public final TextView requestDate;
        public final Button requestDetail;

        public Request mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            // Поля event-а.
            requestMainImage = view.findViewById(R.id.request_main_image);
            nameRequest = view.findViewById(R.id.request_name);
            requestStartEndTime = view.findViewById(R.id.request_start_end_time);
            requestDate = view.findViewById(R.id.request_date);
            requestDetail = view.findViewById(R.id.request_detail_button);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameRequest.getText() + "'";
        }
    }
}
