package com.countmein.countmein.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.countmein.countmein.R;

/**
 * Created by sweet_000 on 5/9/2017.
 */

public class ActivityViewHolder extends RecyclerView.ViewHolder{
    public CardView cv;
    public TextView vName;
    public TextView vDescription;
    public TextView vDate;
    public CheckBox checkBox;

    public ActivityViewHolder(View itemView){
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardview);
        vName = (TextView) itemView.findViewById(R.id.activity_name);
        vDescription = (TextView) itemView.findViewById(R.id.activity_description);
        vDate = (TextView) itemView.findViewById(R.id.activity_date);
        checkBox=(CheckBox)itemView.findViewById(R.id.checkfriend);
    }

}
