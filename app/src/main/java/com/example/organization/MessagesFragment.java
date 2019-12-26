package com.example.organization;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.organization.data.model.Conversation;
import com.example.organization.data.model.room.Messenger;
import com.example.organization.room.MessengerViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MessagesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    private MessengerViewModel mMessengerViewModel;
    String lastSearchText;
    MessagesListRecyclerViewAdapter adapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessagesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessagesFragment newInstance(int columnCount) {
        MessagesFragment fragment = new MessagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_messages_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mMessengerViewModel = ViewModelProviders.of(this).get(MessengerViewModel.class);
            setAdapter(mMessengerViewModel.getAllMessenger().getValue());

            mMessengerViewModel.getAllMessenger().observe(this, new Observer<List<Messenger>>() {
                @Override
                public void onChanged(@Nullable List<Messenger> messengers) {
                    //adapter.setMessenger(messengers);
                    setAdapter(messengers);
                }
            });

            setSearchListener();


        }
        return view;
    }

    public void setSearchListener(){
        SearchView searchView = getActivity().findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setFilter(newText);
                System.out.println("IT IS WORK _______________--------------");
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setFilter("");
                System.out.println("Close!!!!");
                return false;
            }
        });
    }

    private void setAdapter(List<Messenger> conversations) {
        //List<Messenger> conversations = new LinkedList<>();
        //conversations.add(new Conversation());
        adapter = new MessagesListRecyclerViewAdapter(getActivity(), getViewLifecycleOwner(), getActivity().getApplication(), conversations, mListener);
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
        void onListFragmentInteraction(Messenger item);
    }
}
