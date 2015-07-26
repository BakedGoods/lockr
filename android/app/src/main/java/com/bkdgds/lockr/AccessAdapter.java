package com.bkdgds.lockr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class AccessAdapter extends ArrayAdapter <ParseObject>{
    private final Context context;
    private final List<ParseObject> aList;
    private final ParseUser owner;

    public AccessAdapter(Context context, List<ParseObject> aList, ParseUser owner) {
        super(context, R.layout.access_layout, aList); // change to what layout i create
        this.context = context;
        this.aList = aList;
        this.owner = owner;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.access_layout, parent, false);
        ParseObject access = aList.get(position);
        ParseUser user = access.getParseUser("user");

        TextView userTxt = (TextView)rowView.findViewById(R.id.username);
        userTxt.setText(user.getUsername());

        ImageButton btn = (ImageButton)rowView.findViewById(R.id.delBtn);
        btn.setTag(access);

        if(!owner.getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) || user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            btn.setVisibility(View.GONE);
        }
        return rowView;
    }

}


