package com.example.msd_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msd_project.JSONHandler;
import com.example.msd_project.OnSwipeTouchListener;
import com.example.msd_project.R;
import com.example.msd_project.VolleyListener;
import com.example.msd_project.adapters.MatchesListView;
import com.example.msd_project.db.AppDatabase;
import com.example.msd_project.db.MatchDAO;
import com.example.msd_project.models.Match;

import java.util.ArrayList;
import java.util.List;

//displays current and upcoming matches for all teams
public class Matches extends AppCompatActivity implements VolleyListener {

    private ListView listView;
    MatchDAO matchDAO;
    AppDatabase appDatabase;
    MatchesListView matchesListView;
    int matchday;
    JSONHandler jsonHandler;
    List<Match> matches;
    private int scrollDownMatchdayCounter;

    boolean isLoadingMoreData = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        appDatabase = AppDatabase.getInstance(this);

        matchday = appDatabase.getTableTeamDAO().getTableTeams().get(0).getMatchday();

        //is incremented once a new request is fetched and is used to fetch data for the
        //next matchday
        scrollDownMatchdayCounter = matchday;

        Log.d("MATCHDAY", String.valueOf(matchday));
        jsonHandler = new JSONHandler( this);

        //url uses matchday variable to fetch matchesfor current matchday
        String url = String.format("http://api.football-data.org/v4/competitions/2021/matches/?season=2023&matchday=%s", matchday);

        TextView textView = (TextView) findViewById(R.id.matches_header);
        textView.setText("Premier League Matches");

        listView = (ListView)findViewById(R.id.matches_list);

        //empty list fed in, updated later
        matches = new ArrayList<Match>();
        matchesListView = new MatchesListView(this, matches);
        listView.setAdapter(matchesListView);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            int previousFirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Reference for detecting if user is at bottom of list:
                //https://stackoverflow.com/questions/30519695/how-to-know-if-listview-scroll-has-reached-the-bottom-top
                if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0 && !matches.isEmpty()) {
                //End reference
                    if (!isLoadingMoreData) {

                        isLoadingMoreData = true;
                        Log.d("DETECT DOWN", "DETECT DOWN");
                        //increment matchdaycounter and use this new value to fetch the next matchday data
                        scrollDownMatchdayCounter++;
                        String url = String.format("http://api.football-data.org/v4/competitions/2021/matches/?season=2023&matchday=%s", scrollDownMatchdayCounter);
                        jsonHandler.matchesRequest(url);
                    }
                }
            }
        });

        listView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                finish();
            }
        });

        matchDAO = appDatabase.getMatchDAO();
        matchDAO.deleteMatches();

        jsonHandler.teamMatchesRequest(url);
    }

    //Used by Volley request to update matches listview once data is fetched and database is populated
    @Override
    public void requestFinished(boolean existance)
    {
        MatchDAO matchDAO = appDatabase.getMatchDAO();
        List<Match> newMatches = matchDAO.getMatches();

        Log.d("New matches size: ", String.valueOf(newMatches.size()));
        Log.d("example", newMatches.get(0).getHomeTeam());


        matches.clear();
        matches.addAll(newMatches);

        Log.d("HELLO", matches.get(0).getHomeTeam());

        Log.d("Newest matches size: ", String.valueOf(matches.size()));

        //notify view matches list is updated
        matchesListView.notifyDataSetChanged();

        Toast.makeText((Context) Matches.this, "Updated", Toast.LENGTH_LONG).show();
        isLoadingMoreData = false;
    }
}