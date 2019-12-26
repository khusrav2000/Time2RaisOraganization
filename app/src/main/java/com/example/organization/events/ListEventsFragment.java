package com.example.organization.events;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.organization.EmptyAdapter;
import com.example.organization.R;
import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.Events;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListEventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;

    MyListEventsRecyclerViewAdapter adapter;


    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView noEventsText;

    String filterText;

    public ListEventsFragment() {
    }


    @SuppressWarnings("unused")
    public static ListEventsFragment newInstance(int columnCount) {
        ListEventsFragment fragment = new ListEventsFragment();
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
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        // Set the adapter

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.events_list);

        noEventsText = view.findViewById(R.id.text_no_events);



        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_events);
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
                loadEvents();
            }
        });

        //recyclerView.setAdapter(new MyListEventsRecyclerViewAdapter(DummyContent.ITEMS, mListener));

        return view;
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
        loadEvents();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Events item);
    }

    private void loadEvents(){

        // Загрузка списка event-ов с сервера.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator iOrganization = retrofit.create(Initiator.class);

        // TODO: Поменять лимит для количество event-ов.
        Call call = iOrganization.getEvents(LoginDataSource.getInitiator().getToken(), 25);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()) {
                    List<Events> events = (List<Events>) response.body();
                    System.out.println("----------------Events are loaded ---------------------------");
                    setAdapter(events);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setAdapter(List<Events> events){
        if (events != null) {
            if (events.size() == 0){
                noEventsText.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new EmptyAdapter());
            } else {
                noEventsText.setVisibility(View.GONE);
                adapter = new MyListEventsRecyclerViewAdapter(getActivity(), events, mListener);
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
        if (adapter != null ) {
            adapter.getFilter().filter(filterText);
        }
    }
}
