package com.bkdgds.lockr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

public class LockAdapter extends ArrayAdapter <ParseObject>{
    private final Context context;
    private final List<ParseObject> locks;

    public LockAdapter(Context context, List<ParseObject> locks) {
        super(context, R.layout.lock_layout, locks); // change to what layout i create
        this.context = context;
        this.locks = locks;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lock_layout, parent, false);
        ParseObject access = locks.get(position);
        ParseObject lock = access.getParseObject("lock");
        try {
            lock.fetchIfNeeded();

            ImageButton lockBtn = (ImageButton) rowView.findViewById(R.id.lockBtn);
            lockBtn.setTag(lock);

            TextView lockName = (TextView) rowView.findViewById(R.id.lockName);
            lockName.setText(lock.getString("name"));
            lockName.setTag(lock);

            if(lock.getString("state").equals("open")){
                lockBtn.setImageResource(R.drawable.ic_lock_open_black_24dp);
            }
            else{
                lockBtn.setImageResource(R.drawable.ic_lock_outline_black_24dp);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return rowView;
    }

}


