package com.example.msd_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msd_project.JSONHandler;
import com.example.msd_project.OnSwipeTouchListener;
import com.example.msd_project.R;
import com.example.msd_project.VolleyListener;
import com.example.msd_project.adapters.StandingsTableView;
import com.example.msd_project.db.AppDatabase;
import com.example.msd_project.db.TableTeamDAO;
import com.example.msd_project.models.TableTeam;

import java.util.List;

public class Standings extends AppCompatActivity implements VolleyListener {
    private ListView listView;
    private TableTeamDAO tableTeamDAO;
    private SensorManager sm;
    private final int SHAKE_LIMIT = 7;
    private final int SHAKE_TIME_LIMIT = 300;
    private long lastTime = 0;
    private long currentTime = 0;
    TextView positionHeader;
    TextView updateHeader;
    private SensorEventListener sensorEventListener;
    private int matchday;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
        //delays toast by 4 seconds
        //Reference: https://stackoverflow.com/questions/25461791/android-adding-a-delay-to-the-display-of-toast
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText((Context) Standings.this, "Shake device to refresh", Toast.LENGTH_LONG).show();
            }
        },4000);
        //End reference

        //initialisation
        listView = (ListView)findViewById(R.id.standings_table_list);
        AppDatabase db = AppDatabase.getInstance(this);
        tableTeamDAO = db.getTableTeamDAO();
        JSONHandler jsonHandler = new JSONHandler(this);

        //Gets the standings data from the database and pass it through the StandingsTableView adapter
        tableTeamDAO = AppDatabase.getInstance(this).getTableTeamDAO();
        List<TableTeam> tableTeamList = tableTeamDAO.getTableTeams();
        StandingsTableView standingsTableView = new StandingsTableView(this, tableTeamList);
        listView.setAdapter(standingsTableView);

        matchday = tableTeamList.get(0).getMatchday();

        positionHeader = (TextView) findViewById(R.id.positionHeader);
        updateHeader = (TextView) findViewById(R.id.update_header);

        if(tableTeamDAO.getTableTeams().isEmpty())
        {
            jsonHandler.standingsTableRequest("http://api.football-data.org/v4/competitions/PL/standings?season=2023");
        }
        else {
            updateHeader.setText("Updated: " + tableTeamList.get(0).getDateTime());
        }

        //shake phone handler
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                //shake for time duration made with assistance of:
                //https://stackoverflow.com/questions/5271448/how-to-detect-shake-event-with-android
                double acceleration = Math.sqrt(x * x + y * y + z * z);

                if (acceleration > SHAKE_LIMIT)
                {
                    long diffTime = currentTime-lastTime;
                    currentTime = System.currentTimeMillis();
                    if(lastTime == 0 || (diffTime) > SHAKE_TIME_LIMIT)
                    {
                        lastTime = currentTime;
                        Toast.makeText((Context) Standings.this, "Updated", Toast.LENGTH_LONG).show();
                        Log.d("SHAKE", "DETECT");
                        jsonHandler.standingsTableRequest("http://api.football-data.org/v4/competitions/PL/standings?season=2023");
                    }
                }
            }
        };

        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
        SensorManager.SENSOR_DELAY_UI);

        //Header of standings
        TextView positionHeader = (TextView) findViewById(R.id.positionHeader);
        TextView teamHeader = (TextView) findViewById(R.id.teamHeader);
        ImageView imageHeader = (ImageView) findViewById(R.id.imageHeader);
        TextView matchesPlayedHeader = (TextView) findViewById(R.id.matchesPlayedHeader);
        TextView winsHeader = (TextView) findViewById(R.id.winsHeader);
        TextView drawsHeader = (TextView) findViewById(R.id.drawsHeader);
        TextView lossesHeader = (TextView) findViewById(R.id.lossesHeader);
        TextView pointsHeader = (TextView) findViewById(R.id.pointsHeader);
        positionHeader.setText("Club");
        matchesPlayedHeader.setText("MP");
        winsHeader.setText("W");
        drawsHeader.setText("D");
        lossesHeader.setText("L");
        pointsHeader.setText("Pts");


        listView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onSwipeRight() {
                finish();
            }
        });

        //Allows viewing of matches for individual team
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String teamName = tableTeamList.get(i).getTeamName();
                Toast.makeText(Standings.this, "You have clicked in " +
                        teamName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Standings.this, TeamMatches.class);

                //Passes data to team matches activity so it can handle which team matches to display
                Bundle b = new Bundle();
                b.putString("team", teamName);
                b.putInt("id", tableTeamList.get(i).getId());
                b.putInt("matchday",matchday);

                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    //in onPause/onResume register/deregister accelerometer to prevent activation in other activites
    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(sensorEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    //callback
    @Override
    public void requestFinished(boolean existance)
    {
        //get new data from database
        tableTeamDAO = AppDatabase.getInstance(this).getTableTeamDAO();
        List<TableTeam> tableTeamList = tableTeamDAO.getTableTeams();

        //Assign the new datetime of update, can be any entry from the list ie. first entry .get(0)
        updateHeader.setText("Updated: "+ tableTeamList.get(0).getDateTime());

        //Update the standings table
        StandingsTableView standingsTableView = new StandingsTableView(this, tableTeamList);
        listView.setAdapter(standingsTableView);
    }
}