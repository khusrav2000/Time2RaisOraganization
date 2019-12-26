package com.example.organization.requests;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.organization.R;
import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.Request;

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

public class ListMyRequestsFragment extends Fragment  {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private ListMyRequestsFragment.OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    MyListMyRequestsRecyclerViewAdapter adapter;

    String lastSearchText;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListMyRequestsFragment() {
    }


    @SuppressWarnings("unused")
    public static ListMyRequestsFragment newInstance(int columnCount) {
        ListMyRequestsFragment fragment = new ListMyRequestsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_requests_list, container, false);

        // Set the adapter
        if (view.findViewById(R.id.my_requests_list) instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.my_requests_list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            loadMyRequest();
            //recyclerView.setAdapter(new MyListEventsRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        LinearLayout createRequest = (LinearLayout) view.findViewById(R.id.create_request);
        createRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddingRequest();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListMyRequestsFragment.OnListFragmentInteractionListener) {
            mListener = (ListMyRequestsFragment.OnListFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Request item);
    }

    private void loadMyRequest(){

        // Загрузка списка request-ов с сервера.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator iOrganization = retrofit.create(Initiator.class);

        // TODO: Поменять лимит для количество event-ов.
        Call call = iOrganization.getMyRequests(LoginDataSource.getInitiator().getToken(), 25);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()) {
                    List<Request> requests = (List<Request>) response.body();
                    System.out.println("----------------MyRequests are loaded ---------------------------");
                    setAdapter(requests);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void setAdapter(List<Request> requests){
        adapter = new MyListMyRequestsRecyclerViewAdapter(getActivity(), requests, mListener);
        recyclerView.setAdapter(adapter);

        if (lastSearchText != null){
            adapter.getFilter().filter(lastSearchText);
        }
    }

    public void setFilter(String textPattern) {
        lastSearchText = textPattern;
        if (textPattern != null){
            adapter.getFilter().filter(lastSearchText);
        }
    }


    private void startAddingRequest() {
        Intent intent = new Intent(getActivity(), AddingRequest.class);
        startActivity(intent);
    }
}
