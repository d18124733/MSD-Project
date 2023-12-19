package com.example.msd_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.example.msd_project.JSONHandler;
import com.example.msd_project.OnSwipeTouchListener;
import com.example.msd_project.R;
import com.example.msd_project.VolleyListener;
import com.example.msd_project.adapters.MatchesListView;
import com.example.msd_project.db.AppDatabase;
import com.example.msd_project.db.MatchDAO;
import com.example.msd_project.models.Match;

import java.util.List;

//displays matches for selected team
public class TeamMatches extends AppCompatActivity implements VolleyListener {

    private ListView listView;
    final int NO_OF_MATCHDAYS = 38;
    AppDatabase appDatabase;
    private MatchDAO matchDAO;
    String teamName;
    int teamId;
    int matchday;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_matches);

        JSONHandler jsonHandler = new JSONHandler(this);

        appDatabase = AppDatabase.getInstance(this);

        //Gets the selected team and related data from the standings table
        Bundle b = getIntent().getExtras();
        teamName = b.getString("team");
        teamId = b.getInt("id");
        matchday = b.getInt("matchday");

        TextView textView = (TextView) findViewById(R.id.team_matches_header);
        textView.setText("Team Matches for " + teamName);

        listView = (ListView)findViewById(R.id.team_matches_list);

        listView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                finish();
            }
        });

        //Gets the match info for the team from the database/API
        matchDAO = AppDatabase.getInstance(this).getMatchDAO();
        List<Match> matches = matchDAO.searchMatchesByTeam(teamName);
        Log.d("Matches.size", String.valueOf(matches.size()));
        if(matches.size() != NO_OF_MATCHDAYS) {
            String url = String.format("http://api.football-data.org/v4/teams/%s/matches?competitions=2021&season=2023", teamId);

            //Call to fetch matches and update listview
            jsonHandler.teamMatchesRequest(url);
        }
        //displays matches list view if data does not need to be updated
        else {
            MatchesListView matchesListView = new MatchesListView(TeamMatches.this, matches);
            listView.setAdapter(matchesListView);
        }
    }

    //Used by Volley request to update matches listview once data is fetched and database is populated
    @Override
    public void requestFinished(boolean existance)
    {
        matchDAO = appDatabase.getMatchDAO();
        List<Match>matches = matchDAO.searchMatchesByTeam(teamName);

        Log.d("New team matches size: ", String.valueOf(matches.size()));
        for (int i = 0; i < matches.size(); i++)
        {
            Match match = matches.get(i);
            Log.d("Team matches Output: ",
                    match.getHomeTeam() + " "
                            + match.getAwayTeam()
                            + match.getMatchday());
        }

        MatchesListView matchesListView = new MatchesListView(TeamMatches.this, matches);
        listView.setAdapter(matchesListView);

        //matchday used to set position of view, user sees current matchday as first option
        listView.setSelection(matchday);
    }
}