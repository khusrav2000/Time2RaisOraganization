package com.example.organization;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.organization.data.model.room.Messages;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;


import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {

    private  List<Messages> mValues;
    //private  OnListFragmentInteractionListener mListener;
    private  LayoutInflater mInflater;
    private  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private TextView showMessageText;

    public MessageRecyclerViewAdapter(Context context, TextView text) {
        mInflater = LayoutInflater.from(context);
        showMessageText = text;
    }


    public MessageRecyclerViewAdapter(List<Messages> items) {
        mValues = items;
        //mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.send_message_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        showMessageText.setText(getCorrectDate(mValues.get(position).getDateTimePost().getTime()));

        System.out.println("!-----------------------------! position "+ position);
        if(mValues.get(position).getInput() != null) {
            holder.mTextView.setText(mValues.get(position).getInput());
            Drawable drawable = ContextCompat.getDrawable(mInflater.getContext(), R.drawable.message_text_left);
            holder.mTextView.setBackground(drawable);
            System.out.println("input: "+holder.mItem.getInput());
            //holder.mTextView.setGravity(Gravity.START);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.START;
            holder.mTextView.setLayoutParams(params);
            holder.linearLayout.setGravity(Gravity.START);

            holder.mDateView.setText(getCorrectTime(mValues.get(position).getDateTimePost().getTime()));
            holder.mDateView.setLayoutParams(params);
        }
        else if(mValues.get(position).getOutput() != null) {
            holder.mTextView.setText(mValues.get(position).getOutput());
            Drawable drawable = ContextCompat.getDrawable(mInflater.getContext(), R.drawable.send_message_background_radius);
            holder.mTextView.setBackground(drawable);
            //holder.mTextView.setGravity(Gravity.END);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.END;
            holder.mTextView.setLayoutParams(params);
            holder.linearLayout.setGravity(Gravity.END);

            holder.mDateView.setText(getCorrectTime(mValues.get(position).getDateTimePost().getTime()));
            holder.mDateView.setLayoutParams(params);
            /*ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );*/

           // params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
           // holder.linearLayout.setLayoutParams(params);
           // holder.mTextView.setLayoutParams(params);
            System.out.println("output: "+holder.mItem.getOutput());
        }


        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if(mValues == null)
            return 0;
        else
        return mValues.size();
    }


    public void setMessenger(List<Messages> messages){
        mValues = messages;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout linearLayout;
        public final TextView mTextView;
        public final TextView mDateView;
        public Messages mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(R.id.message_text);
            mDateView = (TextView) view.findViewById(R.id.message_time);
            linearLayout = (LinearLayout) view.findViewById(R.id.message_linear);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText() + "'";
        }
    }

    private String getCorrectTime(long date){
        String format = "HH:mm";

        System.out.println(date);

        SimpleDateFormat format1= new SimpleDateFormat(format);
        Date date1 = new Date(date);
         //e.printStackTrace();


        if (date1 == null) {
            return "---";
        }
        return format1.format(date1);
    }

    private String getCorrectDate(long date){
        String format = "MMM dd";

        System.out.println(date);

        SimpleDateFormat format1= new SimpleDateFormat(format);
        Date date1 = new Date(date);
        //e.printStackTrace();


        if (date1 == null) {
            return "---";
        }
        return format1.format(date1);
    }

}
