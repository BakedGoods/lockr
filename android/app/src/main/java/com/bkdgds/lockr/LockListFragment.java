package com.bkdgds.lockr;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class LockListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;
    private ListAdapter mAdapter;

    public static LockListFragment newInstance() {
        LockListFragment fragment = new LockListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LockListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);

        //finds all locks
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Access");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.include("owner");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> lockList, ParseException e)
            {
            if (e == null) {
                if (!lockList.isEmpty()){
                    mAdapter = new LockAdapter(getActivity(), lockList);
                    mListView.setAdapter(mAdapter);
                }
            }
            else{
                Log.d("Recommendation", "Error " + e.getCode() + " : " + e.getMessage());
            }
        } });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

}
