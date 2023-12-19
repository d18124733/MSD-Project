package com.example.msd_project.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msd_project.R;
import com.example.msd_project.models.Match;

import java.util.List;

//Something other than an arrayAdapter might be better suited
public class MatchesListView extends ArrayAdapter {

    private Activity context;
    private List<Match> matchesList;
    private final String SCHEDULED_TIME = "SCHEDULED";
    private final String POSTPONED_TIME = "POSTPONED";
    private final String FULL_TIME = "FINISHED";

    public MatchesListView(Activity context, List<Match> matchesList) {
        super(context, R.layout.matches_row_item, matchesList);
        this.context = context;
        this.matchesList = matchesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView==null) {
            row = inflater.inflate(R.layout.matches_row_item, null, true);
        }
        ImageView imageViewTeam1 = (ImageView) row.findViewById(R.id.imageViewTeam1);
        ImageView imageViewTeam2 = (ImageView) row.findViewById(R.id.imageViewTeam2);
        TextView textViewTeam1 = (TextView) row.findViewById(R.id.textViewTeam1);
        TextView textViewTeam2 = (TextView) row.findViewById(R.id.textViewTeam2);
        TextView textViewHomeGoals = (TextView) row.findViewById(R.id.textViewHomeGoals);
        TextView textViewAwayGoals = (TextView) row.findViewById(R.id.textViewAwayGoals);
        TextView textViewDate = (TextView) row.findViewById(R.id.textViewDate);
        TextView textViewTime = (TextView) row.findViewById(R.id.textViewTime);

        imageViewTeam1.setImageResource(context.getResources().getIdentifier(matchesList.get(position).getHomeTeamImageName(), "drawable", context.getPackageName()));
        imageViewTeam2.setImageResource(context.getResources().getIdentifier(matchesList.get(position).getAwayTeamImageName(), "drawable", context.getPackageName()));
        textViewTeam1.setText(matchesList.get(position).getHomeTeamShort());
        textViewTeam2.setText(matchesList.get(position).getAwayTeamShort());

        String homeGoals = String.valueOf(matchesList.get(position).getHomeTeamGoals());
        String awayGoals = String.valueOf(matchesList.get(position).getAwayTeamGoals());

        String date = matchesList.get(position).getDate();
        textViewDate.setText(date);

        //following determines whether time textView displays FT, TBD, or just the time
        //also determines whether to display score for the match (if match yet to occur don't display score)
        String timeDisplay;
        String status = matchesList.get(position).getStatus();

        if(status.equals(SCHEDULED_TIME) || status.equals(POSTPONED_TIME)) {
            timeDisplay = "TBD";
            textViewHomeGoals.setText("");
            textViewAwayGoals.setText("");
        }
        else if(status.equals(FULL_TIME)) {
            timeDisplay = "FT";
            textViewHomeGoals.setText(homeGoals);
            textViewAwayGoals.setText(awayGoals);
        }
        else {
            timeDisplay = matchesList.get(position).getTime();
            textViewHomeGoals.setText("");
            textViewAwayGoals.setText("");
        }
        textViewTime.setText(timeDisplay);

        return row;
    }
}
