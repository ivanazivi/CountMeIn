package com.countmein.countmein;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Home on 4/13/2017.
 */

public class ChatRoomAdapter extends ArrayAdapter<Chat> {

    public ChatRoomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ChatRoomAdapter(Context context, int resource, List<Chat> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.singlerow, null);
        }

        Chat p  = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.naslov);
            TextView tt2 = (TextView) v.findViewById(R.id.opis);
            TextView tt3 = (TextView) v.findViewById(R.id.datum);

            if (tt1 != null) {
                tt1.setText(p.getNaslov());
            }

            if (tt2 != null) {
                tt2.setText(p.getOpis());
            }

            if (tt3 != null) {
                tt3.setText(p.getDatum().toString());
            }
        }

        return v;
    }

}
