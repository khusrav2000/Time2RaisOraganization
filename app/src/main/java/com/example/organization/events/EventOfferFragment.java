package com.example.organization.events;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.organization.R;
import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.EventToOffer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventOfferFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventOfferFragment() {
    }


    @SuppressWarnings("unused")
    public static EventOfferFragment newInstance(int columnCount) {
        EventOfferFragment fragment = new EventOfferFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventoffer_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            loadEventOffer();
            //recyclerView.setAdapter(new MyEventOfferRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }

    private void loadEventOffer() {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator initiator = retrofit.create(Initiator.class);
        Call call = initiator.getEventOffers(LoginDataSource.getInitiator().getToken(), 25);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200){
                    List<EventToOffer> eventToOffers = (List<EventToOffer>) response.body();
                    setAdapter(eventToOffers);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    private void setAdapter(List<EventToOffer> eventToOffers){
        if (eventToOffers != null) {
            recyclerView.setAdapter(new MyEventOfferRecyclerViewAdapter(eventToOffers, mListener));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(EventToOffer item);
    }
}
