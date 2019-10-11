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


import java.util.List;


public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {

    private  List<Messages> mValues;
    //private  OnListFragmentInteractionListener mListener;
    private  LayoutInflater mInflater;
    private  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public MessageRecyclerViewAdapter(Context context) { mInflater = LayoutInflater.from(context); }


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
       System.out.println("!-----------------------------! position "+ position);
        if(mValues.get(position).getInput()!= null) {
            holder.mTextView.setText(mValues.get(position).getInput());
            Drawable drawable = ContextCompat.getDrawable(mInflater.getContext(), R.drawable.send_message_background_radius);
            holder.mTextView.setBackground(drawable);
            System.out.println("input: "+holder.mItem.getInput());
            holder.linearLayout.setGravity(Gravity.START);
        }
        else if(mValues.get(position).getOutput()!= null) {
            holder.mTextView.setText(mValues.get(position).getOutput());
            Drawable drawable = ContextCompat.getDrawable(mInflater.getContext(), R.drawable.message_text_left);
            holder.mTextView.setBackground(drawable);
            holder.linearLayout.setGravity(Gravity.END);
            /*ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );*/

            holder.linearLayout.setGravity(Gravity.START);

           // params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
           // holder.linearLayout.setLayoutParams(params);
           // holder.mTextView.setLayoutParams(params);
            System.out.println("output: "+holder.mItem.getOutput());
        }
        holder.mDateView.setText(mValues.get(position).getDateTimePost().toString());

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
}
