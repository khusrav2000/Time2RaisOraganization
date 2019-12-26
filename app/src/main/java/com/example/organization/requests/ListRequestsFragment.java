package com.example.organization.requests;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.organization.R;
import com.example.organization.data.LoginDataSource;
import com.example.organization.data.NetworkClient;
import com.example.organization.data.apis.Initiator;
import com.example.organization.data.model.Request;
import com.example.organization.data.model.Restaurant.RestaurantInformation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListRequestsFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private ListRequestsFragment.OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    String lastSearchText;

    MyListRequestsRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListRequestsFragment() {
    }


    @SuppressWarnings("unused")
    public static ListRequestsFragment newInstance(int columnCount) {
        ListRequestsFragment fragment = new ListRequestsFragment();
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
        View view = inflater.inflate(R.layout.fragment_requests_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            loadRequests();
            //recyclerView.setAdapter(new MyListEventsRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListRequestsFragment.OnListFragmentInteractionListener) {
            mListener = (ListRequestsFragment.OnListFragmentInteractionListener) context;
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
        void onListFragmentInteraction(RestaurantInformation item);
    }

    private void loadRequests(){

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Initiator iOrganization = retrofit.create(Initiator.class);

        // TODO: Поменять лимит для количество event-ов.
        Call call = iOrganization.getRequests(LoginDataSource.getInitiator().getToken(), 25);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()) {

                    List<RestaurantInformation> requests = (List<RestaurantInformation>) response.body();
                    System.out.println("----------------Request are loaded ---------------------------");
                    for (RestaurantInformation restaurantInformation: requests){
                        System.out.println(restaurantInformation.toString());
                    }
                    setAdapter(requests);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void setAdapter(List<RestaurantInformation> requests){
        adapter = new MyListRequestsRecyclerViewAdapter(getActivity(), requests, mListener);
        recyclerView.setAdapter(adapter);
        if (lastSearchText != null){
            adapter.getFilter().filter(lastSearchText);
        }
    }

    public void setFilter(String textPattern) {
        lastSearchText = textPattern;
        if (textPattern != null) {
            adapter.getFilter().filter(lastSearchText);
        }
    }


}
