package com.example.msd_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msd_project.R;
import com.example.msd_project.adapters.TeamListView;
import com.example.msd_project.db.AppDatabase;
import com.example.msd_project.db.TableTeamDAO;
import com.example.msd_project.models.TableTeam;

import java.util.List;

//displays list of teams and can launch matches for team from selection
public class Teams extends AppCompatActivity {

    private List<TableTeam> tableTeamList;
    private int matchday;
    private final int OFFSET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("Teams");

        ListView listView = (ListView)findViewById(R.id.teams_activity_list);
        listView.addHeaderView(textView);

        TableTeamDAO tableTeamDAO = AppDatabase.getInstance(this).getTableTeamDAO();
        tableTeamList = tableTeamDAO.getTableTeams();
        matchday = tableTeamList.get(0).getMatchday();

        TeamListView teamListView = new TeamListView(this, tableTeamList);
        listView.setAdapter(teamListView);

        //Allows viewing of matches for individual team
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    String teamName = tableTeamList.get(i - OFFSET).getTeamName();
                    Toast.makeText(Teams.this, "You have clicked in " +
                            teamName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Teams.this, TeamMatches.class);

                    //Passes data to team matches activity so it can handle which team matches to display
                    Bundle b = new Bundle();
                    b.putString("team", teamName);
                    b.putInt("id", tableTeamList.get(i - OFFSET).getId());
                    b.putInt("matchday", matchday);

                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });
    }

}