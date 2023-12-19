package com.example.msd_project;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.msd_project.db.AppDatabase;
import com.example.msd_project.db.MatchDAO;
import com.example.msd_project.db.TableTeamDAO;
import com.example.msd_project.models.Match;
import com.example.msd_project.models.TableTeam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//handles all volley requests and inserts data into room database
public class JSONHandler {

    private final String API_KEY = "";
    private AppDatabase appDatabase;
    private RequestQueue mQueue;
    private Context context;

    public JSONHandler(Context context)
    {
        this.appDatabase = AppDatabase.getInstance(context);
        this.mQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    //request for standings table
    public void standingsTableRequest(String url) {

        VolleyListener volleyListener = (VolleyListener)context;
        TableTeamDAO tableTeamDAO = appDatabase.getTableTeamDAO();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tableTeamDAO.deleteTableTeams();

                            JSONObject season = response.getJSONObject("season");
                            int matchday = season.getInt("currentMatchday");
                            Log.d("TEST", season.toString());
                            JSONArray jsonArray = response.getJSONArray("standings");// the standings data.
                            JSONObject embedded = jsonArray.getJSONObject(0);// multiple standings in the above for some reason(3). This gets the first one, which has accurate information. Not sure what the other 2 are but they're not needed so they're not relevant.
                            JSONArray table = embedded.getJSONArray("table");//the table of teams 1 - 20

                            int numberOfTeams = table.length();

                            tableTeamDAO.deleteTableTeams();

                            for (int i = 0; i < numberOfTeams; i++)
                            {
                                JSONObject teamDetails = table.getJSONObject(i);
                                JSONObject teamNameAndImageDetails = teamDetails.getJSONObject("team");//name and image is it's own embed within the details entry

                                int position = teamDetails.getInt("position");
                                int teamId = teamNameAndImageDetails.getInt("id");
                                String name = teamNameAndImageDetails.getString("name");
                                String shortName = teamNameAndImageDetails.getString("shortName");
                                int matchesPlayed = teamDetails.getInt("playedGames");
                                int wins = teamDetails.getInt("won");
                                int draws = teamDetails.getInt("draw");
                                int losses = teamDetails.getInt("lost");
                                int points = teamDetails.getInt("points");
                                String imageURL = teamNameAndImageDetails.getString("crest");

                                //image resource uses the team shortname in lowercase and with "_" in place of spaces for resource name
                                //this essentially assigns the image resource name
                                imageURL = shortName;
                                imageURL = imageURL.replaceAll(" ", "_").toLowerCase();

                                //Current date and time of fetch
                                Date d = new Date();
                                String dateTime = d.toString().substring(0, d.toString().length()-14);

                                TableTeam tableTeam = new TableTeam();

                                tableTeam.setPosition(position);
                                tableTeam.setId(teamId);
                                tableTeam.setTeamName(name);
                                tableTeam.setTeamNameShort(shortName);
                                tableTeam.setMatchesPlayed(matchesPlayed);
                                tableTeam.setWins(wins);
                                tableTeam.setDraws(draws);
                                tableTeam.setLosses(losses);
                                tableTeam.setPoints(points);
                                tableTeam.setImageURL(imageURL);
                                tableTeam.setDateTime(dateTime);
                                tableTeam.setMatchday(matchday);

                                tableTeamDAO.insert(tableTeam);
                            }
                            List<TableTeam> tableTeamList = tableTeamDAO.getTableTeams();

                            //for log
                            for (int i = 0; i < numberOfTeams; i++)
                            {
                                TableTeam tableTeam = tableTeamList.get(i);
                                Log.d("listOfTeams: ", tableTeam.getPosition() + " "
                                        + tableTeam.getId()
                                        + tableTeam.getTeamName() + " "
                                        + tableTeam.getTeamNameShort() + " "
                                        + tableTeam.getMatchesPlayed() + " "
                                        + tableTeam.getWins() + " "
                                        + tableTeam.getDraws() + " "
                                        + tableTeam.getLosses() + " "
                                        + tableTeam.getPoints() + " "
                                        + tableTeam.getImageURL() + " "
                                        + tableTeam.getMatchday());
                            }
                            volleyListener.requestFinished(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }) {
            // Reference: Applying headers to the GET request was learned from this stackoverflow question:
            // https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", API_KEY);

                return params;
            }
        };

        mQueue.add(request);
    }

    //request for individual team matches
    public void teamMatchesRequest(String url) {

        MatchDAO matchDAO = appDatabase.getMatchDAO();
        VolleyListener volleyListener = (VolleyListener)context;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            matchDAO.deleteMatches();

                            Log.d("Response", response.toString());
                            JSONArray jsonArray = response.getJSONArray("matches");
                            int matchdays = jsonArray.length();
                            Log.d("Length: ", String.valueOf(matchdays));

                            for (int i = 0; i < matchdays; i++) {
                                Match match = new Match();

                                JSONObject matchFetch = jsonArray.getJSONObject(i);
                                int matchId = matchFetch.getInt("id");
                                JSONObject homeTeam = matchFetch.getJSONObject("homeTeam");
                                String homeTeamName = homeTeam.getString("name");
                                String homeTeamNameShort = homeTeam.getString("shortName");
                                JSONObject awayTeam = matchFetch.getJSONObject("awayTeam");
                                String awayTeamName = awayTeam.getString("name");
                                String awayTeamNameShort = awayTeam.getString("shortName");
                                String datetime = matchFetch.getString("utcDate");
                                int matchday = matchFetch.getInt("matchday");
                                JSONObject score = matchFetch.getJSONObject("score");
                                JSONObject fulltime = score.getJSONObject("fullTime");
                                String status = matchFetch.getString("status");

                                //image resource uses the team shortname in lowercase and with "_" in place of spaces for resource name
                                //this essentially assigns the image resource name for the team from the drawable folder
                                String homeTeamImageResource = homeTeamNameShort.replaceAll(" ", "_").toLowerCase();
                                String awayTeamImageResource = awayTeamNameShort.replaceAll(" ", "_").toLowerCase();

                                //convert UTC into date and time strings
                                String [] split = datetime.split("T");
                                String date = split[0].replaceAll("-","/");
                                String time = split[1].replace("Z","");
                                time = time.substring(0, time.length()-3); //shaves off the seconds so only HH:MM

                                match.setId(matchId);
                                match.setHomeTeam(homeTeamName);
                                match.setAwayTeam(awayTeamName);
                                match.setDate(date);
                                match.setTime(time);
                                match.setMatchday(matchday);
                                match.setHomeTeamShort(homeTeamNameShort);
                                match.setAwayTeamShort(awayTeamNameShort);
                                match.setHomeTeamImageName(homeTeamImageResource);
                                match.setAwayTeamImageName(awayTeamImageResource);
                                match.setStatus(status);

                                //if goals not null add to match, otherwise do nothing
                                try {
                                    int homeTeamGoals = fulltime.getInt("home");
                                    int awayTeamGoals = fulltime.getInt("away");
                                    match.setHomeTeamGoals(homeTeamGoals);
                                    match.setAwayTeamGoals(awayTeamGoals);
                                } catch (JSONException e) {

                                }
                                matchDAO.insert(match);
                            }
                            //Calls to update the listview in implemented activity once the request is finished fetching data and loaded into sql database
                            volleyListener.requestFinished(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }) {
            // Reference: Applying headers to the GET request was learned from this stackoverflow question:
            // https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", API_KEY);

                return params;
            }
        };

        mQueue.add(request);
    }

    //request for matches
    public void matchesRequest(String url) {

        MatchDAO matchDAO = appDatabase.getMatchDAO();
        VolleyListener volleyListener = (VolleyListener)context;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response", response.toString());
                            JSONArray jsonArray = response.getJSONArray("matches");
                            int matchdays = jsonArray.length();
                            Log.d("Length: ", String.valueOf(matchdays));

                            for (int i = 0; i < matchdays; i++) {
                                Match match = new Match();

                                JSONObject matchFetch = jsonArray.getJSONObject(i);
                                int matchId = matchFetch.getInt("id");
                                JSONObject homeTeam = matchFetch.getJSONObject("homeTeam");
                                String homeTeamName = homeTeam.getString("name");
                                String homeTeamNameShort = homeTeam.getString("shortName");
                                JSONObject awayTeam = matchFetch.getJSONObject("awayTeam");
                                String awayTeamName = awayTeam.getString("name");
                                String awayTeamNameShort = awayTeam.getString("shortName");
                                String datetime = matchFetch.getString("utcDate");
                                int matchday = matchFetch.getInt("matchday");
                                JSONObject score = matchFetch.getJSONObject("score");
                                JSONObject fulltime = score.getJSONObject("fullTime");
                                String status = matchFetch.getString("status");

                                //image resource uses the team shortname in lowercase and with "_" in place of spaces for resource name
                                //this essentially assigns the image resource name for the team from the drawable folder
                                String homeTeamImageResource = homeTeamNameShort.replaceAll(" ", "_").toLowerCase();
                                String awayTeamImageResource = awayTeamNameShort.replaceAll(" ", "_").toLowerCase();

                                //convert UTC into date and time strings
                                String [] split = datetime.split("T");
                                String date = split[0].replaceAll("-","/");
                                String time = split[1].replace("Z","");
                                time = time.substring(0, time.length()-3); //shaves off the seconds so only HH:MM

                                match.setId(matchId);
                                match.setHomeTeam(homeTeamName);
                                match.setAwayTeam(awayTeamName);
                                match.setDate(date);
                                match.setTime(time);
                                match.setMatchday(matchday);
                                match.setHomeTeamShort(homeTeamNameShort);
                                match.setAwayTeamShort(awayTeamNameShort);
                                match.setHomeTeamImageName(homeTeamImageResource);
                                match.setAwayTeamImageName(awayTeamImageResource);
                                match.setStatus(status);

                                //if goals not null add to match, otherwise do nothing
                                try {
                                    int homeTeamGoals = fulltime.getInt("home");
                                    int awayTeamGoals = fulltime.getInt("away");
                                    match.setHomeTeamGoals(homeTeamGoals);
                                    match.setAwayTeamGoals(awayTeamGoals);
                                } catch (JSONException e) {

                                }
                                matchDAO.insert(match);
                            }
                            //Calls to update the listview in implemented activity once the request is finished fetching data and loaded into sql database
                            volleyListener.requestFinished(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }) {
            // Reference: Applying headers to the GET request was learned from this stackoverflow question:
            // https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", API_KEY);

                return params;
            }
        };

        mQueue.add(request);
    }
}
