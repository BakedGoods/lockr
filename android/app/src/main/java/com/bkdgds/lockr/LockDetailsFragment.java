package com.bkdgds.lockr;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class LockDetailsFragment extends Fragment {

    public static ParseObject lock;

    private OnFragmentInteractionListener mListener;

    private AccessAdapter mAdapter;

    public static LockDetailsFragment newInstance(ParseObject lockObj) {
        LockDetailsFragment fragment = new LockDetailsFragment();
        lock = lockObj;
        return fragment;
    }

    public LockDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_lock_details, container, false);
        TextView textView = (TextView) view.findViewById(R.id.lockName);
        textView.setText(lock.getString("name"));

        try {
            TextView owner = (TextView) view.findViewById(R.id.lockOwner);
            final ParseUser user = lock.getParseUser("owner");
            user.fetchIfNeeded();
            owner.setText("Owner: " + user.getUsername());

            ImageButton lockBtn = (ImageButton) view.findViewById(R.id.lockBtn);
            if(lock.getString("state").equals("open")){
                lockBtn.setImageResource(R.drawable.ic_lock_open_white_24dp);
            }
            else{
                lockBtn.setImageResource(R.drawable.ic_lock_outline_white_24dp);
            }

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Access");
            query.whereEqualTo("lock", lock);
            query.include("user");
            //query.whereNotEqualTo("user", ParseUser.getCurrentUser());
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> accessList, ParseException e) {
                    if (e == null) {
                        mAdapter = new AccessAdapter(getActivity(), accessList, user);
                        ListView listView = (ListView)view.findViewById(R.id.listView);
                        listView.setAdapter(mAdapter);
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
