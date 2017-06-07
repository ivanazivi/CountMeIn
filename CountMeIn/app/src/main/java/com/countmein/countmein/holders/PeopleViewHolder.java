package com.countmein.countmein.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.countmein.countmein.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Home on 5/21/2017.
 */

public class PeopleViewHolder extends RecyclerView.ViewHolder{
    public CardView cv;
    public TextView messageUser;
    public Button button;
    public CheckBox checkBox;


    public SimpleDraweeView userPhoto;

    public PeopleViewHolder(View itemView){
        super(itemView);

        cv = (CardView) itemView.findViewById(R.id.people_card_view);

        messageUser = (TextView) itemView.findViewById(R.id.messageUser);
        userPhoto=(SimpleDraweeView) itemView.findViewById(R.id.userPhoto);
        button=(Button)itemView.findViewById(R.id.addPeopleButton);
        checkBox=(CheckBox)itemView.findViewById(R.id.checkfriend);
    }
}