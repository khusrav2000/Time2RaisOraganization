package com.example.organization.requests;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.organization.R;

public class RequestsTabbedFragment extends Fragment {

    private RequestsTabbedFragment.OnFragmentInteractionListener mListener;

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
        RequestsSectionsPagerAdapter sectionsPagerAdapter = new RequestsSectionsPagerAdapter(getActivity(), getChildFragmentManager());

        ViewPager viewPager = view.findViewById(R.id.requests_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = view.findViewById(R.id.requests_tabs);

        tabs.setupWithViewPager(viewPager);

        return view;
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
