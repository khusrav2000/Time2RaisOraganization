package com.example.organization.requests;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.organization.R;

public class RequestsTabbedFragment extends Fragment {

    private RequestsTabbedFragment.OnFragmentInteractionListener mListener;
    RequestsSectionsPagerAdapter sectionsPagerAdapter;

    public RequestsTabbedFragment() {
    }


    @SuppressWarnings("unused")
    public static ListRequestsFragment newInstance(int columnCount) {
        ListRequestsFragment fragment = new ListRequestsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.requests_tabbed_fragment, container, false);
        sectionsPagerAdapter = new RequestsSectionsPagerAdapter(getActivity(), getChildFragmentManager());

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.requests_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.requests_tabs);

        tabs.setupWithViewPager(viewPager);
        setSearchListener();

        return view;
    }

    public void setSearchListener(){
        SearchView searchView = getActivity().findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sectionsPagerAdapter.filterItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sectionsPagerAdapter.filterItems(newText);
                System.out.println("IT IS WORK _______________--------------");
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                sectionsPagerAdapter.filterItems("");
                System.out.println("Close!!!!");
                return false;
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestsTabbedFragment.OnFragmentInteractionListener) {
            mListener = (RequestsTabbedFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
