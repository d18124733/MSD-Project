package com.example.msd_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.msd_project.JSONHandler;
import com.example.msd_project.R;
import com.example.msd_project.VolleyListener;
import com.example.msd_project.db.TableTeamDAO;

import com.example.msd_project.db.AppDatabase;

public class MainActivity extends AppCompatActivity implements VolleyListener {

    ListView menu_list;
    private TableTeamDAO tableTeamDAO;
    private final int OFFSET = 1;

    //main menu options
    private String menuNames[] = {
            "Table",
            "Matches",
            "Teams"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise database fetch table dao
        AppDatabase database = AppDatabase.getInstance(this);
        tableTeamDAO = database.getTableTeamDAO();

        JSONHandler jsonHandler = new JSONHandler(MainActivity.this);

        //if no standings table in room database, send volley request
        if(tableTeamDAO.getTableTeams().isEmpty())
        {
            jsonHandler.standingsTableRequest("http://api.football-data.org/v4/competitions/PL/standings?season=2023");
        }

        //main  menu
        menu_list = (ListView) findViewById(R.id.menu_list);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, menuNames);
        menu_list.setAdapter(adapter);

        //header
        TextView header = new TextView(this);
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setTextSize(20);
        header.setText("Premier League Football App");
        header.setGravity(Gravity.CENTER);
        menu_list.addHeaderView(header);

        //starts new activities on user input with menu
        menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //OFFSET neccessary to avoid clicking header of list
                //Standings table
                if (i == 0 + OFFSET) {
                    Intent intent = new Intent(MainActivity.this, Standings.class);
                    startActivity(intent);
                }
                //Matches list
                if (i == 1 + OFFSET) {
                    Intent intent = new Intent(MainActivity.this, Matches.class);
                    startActivity(intent);
                }
                //Teams list
                if (i == 2 + OFFSET) {
                    Intent intent = new Intent(MainActivity.this, Teams.class);
                    startActivity(intent);
                }
            }
        });
    }

    //callback for jsonhandler, not used for mainactivity but has to be implemented to use jsonhandler
    public void requestFinished(boolean hello)
    {

    }
}
