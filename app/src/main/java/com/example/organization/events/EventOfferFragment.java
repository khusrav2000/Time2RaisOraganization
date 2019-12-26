package com.example.organization.events;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.organization.EmptyAdapter;
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
public class EventOfferFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView noEventsText;

    MyEventOfferRecyclerViewAdapter adapter;
    String filterText;

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

        noEventsText = view.findViewById(R.id.text_no_events);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.event_offers_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_and_refresh_event_offers);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        //recyclerView.setAdapter(new MyEventOfferRecyclerViewAdapter(DummyContent.ITEMS, mListener));

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                loadEventOffer();
            }
        });

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
                    System.out.println(response.body());
                    List<EventToOffer> eventToOffers = (List<EventToOffer>) response.body();
                    setAdapter(eventToOffers);

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void setAdapter(List<EventToOffer> eventToOffers){
        System.out.println("UPDATEEEEEEE!1");
        if (eventToOffers != null) {
            for (EventToOffer eventToOffer : eventToOffers) {
                System.out.println(eventToOffer + " +++++++++++++++++++");
            }
            if (eventToOffers.size() == 0){
                noEventsText.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new EmptyAdapter());
            } else {
                noEventsText.setVisibility(View.GONE);
                adapter = new MyEventOfferRecyclerViewAdapter(getActivity(), eventToOffers, mListener, getContext());
                recyclerView.setAdapter(adapter);
                if (filterText != null && filterText.length() > 0){
                    setFilter(filterText);
                }
            }
        } else {
            noEventsText.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new EmptyAdapter());
        }
    }

    public void setFilter(String text){
        filterText = text;
        if (adapter != null) {
            adapter.getFilter().filter(filterText);
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

    @Override
    public void onRefresh() {
        loadEventOffer();
    }



    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(EventToOffer item);
    }
}
