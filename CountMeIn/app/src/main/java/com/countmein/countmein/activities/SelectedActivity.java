package com.countmein.countmein.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.countmein.countmein.R;
import com.countmein.countmein.beans.ActivityBean;
import com.countmein.countmein.beans.ChatMessageBean;
import com.countmein.countmein.fragments.AttendingActivitiesFragment;
import com.countmein.countmein.fragments.AttendingMyActFragment;
import com.countmein.countmein.fragments.other.MapFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

//@EActivity(R.layout.activity_selected)
public class SelectedActivity extends AppCompatActivity {


    private FirebaseListAdapter<ChatMessageBean> adapter;

    private static final int SIGN_IN_REQUEST_CODE = 1;

    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public final static String AUTH_KEY_FCM = "Your api key";
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        Button showBtn = (Button) findViewById(R.id.show_who_attends);
        final Bundle activeActivityBundle = new Bundle();
        Bundle bundle = getIntent().getExtras();
        ActivityBean activity=(ActivityBean) bundle.getSerializable("data");

        String naslov=activity.getName();
        String opis= activity.getDescription();
        String datum=activity.getDate();
        String lLat = activity.getlLat();
        String lLng = activity.getlLng();
        final String id = activity.getId();

        mTextView1 = (TextView) findViewById(R.id.naslov);
        mTextView2 = (TextView) findViewById(R.id.opis);
        mTextView3 = (TextView) findViewById(R.id.datum);
        mTextView1.setText(naslov);
        mTextView2.setText(opis);
        mTextView3.setText(datum);

        Bundle mapBundle = new Bundle();
        mapBundle.putDouble("markLat", Double.valueOf(lLat));
        mapBundle.putDouble("markLng", Double.valueOf(lLng));
        mapBundle.putInt("isSelectedAcitvity", 1);

        MapFragment fr= new MapFragment();
        fr.setArguments(mapBundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_selected_map,  fr);
        fragmentTransaction.commit();


        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {

            displayChatMessages();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.activity_preview);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeActivityBundle.putString("id", id);
                DialogFragment fragment = AttendingMyActFragment.newIstance();
                fragment.setArguments(activeActivityBundle);
                fragment.show(getSupportFragmentManager(), "whoIsAttending");
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessageBean to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference().child("chatrooms").child(mTextView1.getText().toString())
                        .push()
                        .setValue(new ChatMessageBean(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

               // searchAsync();
                // Clear the input

                input.setText("");
            }
        });



    }

    private void displayChatMessages() {

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        listOfMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listOfMessages.setStackFromBottom(true);

        adapter = new FirebaseListAdapter<ChatMessageBean>(this, ChatMessageBean.class,
                R.layout.message_card_view, FirebaseDatabase.getInstance().getReference().child("chatrooms").child(mTextView1.getText().toString())) {
            @Override
            protected void populateView(View v, ChatMessageBean model, int position) {
                // Get references to the views of message_card_view.xmld_view.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }



}