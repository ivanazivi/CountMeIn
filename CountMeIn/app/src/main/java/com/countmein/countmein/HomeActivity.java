package com.countmein.countmein;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        callbackManager = CallbackManager.Factory.create();

        List<Chat> podaci=getData();

        final ListView lv = (ListView) findViewById(R.id.list);

// get data from the table by the ListAdapter
        ChatRoomAdapter customAdapter = new ChatRoomAdapter(this, R.layout.singlerow, podaci);

        lv .setAdapter(customAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String selected1 = ((TextView) view.findViewById(R.id.naslov)).getText().toString();
                String selected2 = ((TextView) view.findViewById(R.id.opis)).getText().toString();
                String selected3 = ((TextView) view.findViewById(R.id.datum)).getText().toString();


                Toast toast = Toast.makeText(getApplicationContext(), selected1, Toast.LENGTH_SHORT);
                toast.show();
                Intent i = new Intent(HomeActivity.this, SelectedActivity.class);
                i.putExtra("naslov", selected1);
                i.putExtra("opis", selected2);

                i.putExtra("datum", selected3);

                startActivity(i);


            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    public List<Chat> getData(){

        Chat c1=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c2=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c3=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c4=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c5=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c6=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c7=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c8=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c9=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());
        Chat c10=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());

        Chat c11=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());

        Chat c12=new Chat("Rodjendan 1","Ovo je rodjendan", new Date());

        ArrayList<Chat> lista= new ArrayList<Chat>();
        lista.add(c1);
        lista.add(c2);
        lista.add(c3);
        lista.add(c4);
        lista.add(c5);
        lista.add(c6);
        lista.add(c7);
        lista.add(c8);
        lista.add(c9);
        lista.add(c10);
        lista.add(c11);
        return lista;

    }


}