package com.cherryfish.stephanie.myfavoroutepics;


import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static TextView addSelfie, showSelfies, showTravels;
    FragmentManager fragmentManager;
    public static int teal, navy;
    public static String searchTerm;
    public static List<String> captions;
    public static List<String> urls;
    public static int selfieListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get size of previously saved selfie list
        SharedPreferences settings = getSharedPreferences("Selfie", 0);
        selfieListSize = settings.getInt("selfieNumber", 0);

        setContentView(R.layout.activity_main);

        //initialize colors
        teal = ContextCompat.getColor(getApplicationContext(), R.color.Teal);
        navy = ContextCompat.getColor(getApplicationContext(), R.color.Navy);

        //initialize buttons/views
        addSelfie = (TextView) findViewById(R.id.add_selfie);
        addSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSelfie();
            }
        });

        showSelfies = (TextView) findViewById(R.id.selfies);
        showSelfies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelfieList();
            }
        });

        showTravels = (TextView) findViewById(R.id.travels);
        showTravels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTravelsList();
            }
        });

        //set up fragment manager to navigate through the app
        fragmentManager = getSupportFragmentManager();

        //if no saved state, show selfie listview
        if (savedInstanceState == null) {
            showSelfieList();
        } else {

        }
    }
    // display selfie view - will show selfie list if previously added selfies
    // otherwise will display a message to add selfies
    public void showSelfieList() {

        SelfieList selfieList = new SelfieList();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, selfieList, "SelfieList")
                .addToBackStack(null)
                .commit();
    }


    // displays a dialog box to allow user to search for a travel destination
    // will show list after user searches
    public void showTravelsList() {
        TravelSearchDialog travelSearchDialog = new TravelSearchDialog();
        travelSearchDialog.show(getSupportFragmentManager(), "TravelSearchDialog");


    }

    // shows dialog box for user to add a selfie and caption
    public void addSelfie() {
        AddSelfieDialog addSelfieDialog = new AddSelfieDialog();
        addSelfieDialog.show(getSupportFragmentManager(), "AddSelfieDialog");
    }

    // manage back button press
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            super.onBackPressed();
            super.onBackPressed();
        } else {
            super.onBackPressed();

        }
        Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (f instanceof SelfieList){
            MainActivity.showSelfies.setBackgroundColor(MainActivity.teal);
            MainActivity.showSelfies.setTextColor(MainActivity.navy);
            MainActivity.showTravels.setBackgroundColor(MainActivity.navy);
            MainActivity.showTravels.setTextColor(MainActivity.teal);

        } else if (f instanceof TravelList) {
            MainActivity.showSelfies.setBackgroundColor(MainActivity.navy);
            MainActivity.showSelfies.setTextColor(MainActivity.teal);
            MainActivity.showTravels.setBackgroundColor(MainActivity.teal);
            MainActivity.showTravels.setTextColor(MainActivity.navy);
        }
    }

    //store size of selfie list in shared prefs to load selfies when app reloaded
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences settings = getSharedPreferences("Selfie", 0);
        SharedPreferences.Editor editor = settings.edit();
        if (urls != null) {
            editor.putInt("selfieNumber", MainActivity.urls.size());
        }
        else
            editor.putInt("selfieNumber", 0);

        editor.commit();
    }
}

