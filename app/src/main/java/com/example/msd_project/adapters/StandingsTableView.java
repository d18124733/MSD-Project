package com.example.msd_project.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msd_project.R;
import com.example.msd_project.models.TableTeam;


import java.util.List;

public class StandingsTableView extends ArrayAdapter {

    private Activity context;
    private List<TableTeam> tableTeamList;

    public StandingsTableView(Activity context, List<TableTeam> tableTeamList) {
        super(context, R.layout.standings_table_row_item, tableTeamList);
        this.context =context;
        this.tableTeamList = tableTeamList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView==null) {
            row = inflater.inflate(R.layout.standings_table_row_item, null, true);
        }

        TextView textViewPosition = (TextView) row.findViewById(R.id.textViewPosition);
        TextView textViewTeam = (TextView) row.findViewById(R.id.textViewTeam);
        ImageView imageViewClub = (ImageView) row.findViewById(R.id.imageViewClub);
        TextView textViewMatchesPlayed = (TextView) row.findViewById(R.id.textViewMatchesPlayed);
        TextView textViewWins = (TextView) row.findViewById(R.id.textViewWins);
        TextView textViewDraws = (TextView) row.findViewById(R.id.textViewDraws);
        TextView textViewLosses = (TextView) row.findViewById(R.id.textViewLosses);
        TextView textViewPoints = (TextView) row.findViewById(R.id.textViewPoints);

        // getResources().getIdentifier from string learned from: https://stackoverflow.com/questions/3042961/how-do-i-get-the-resource-id-of-an-image-if-i-know-its-name
        String imageName = tableTeamList.get(position).getImageURL();
        int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        /*Log.d("ImageName", imageName);
        Log.d("Id:", String.valueOf(resID));*/

        textViewPosition.setText(String.valueOf(tableTeamList.get(position).getPosition()));
        textViewTeam.setText(String.valueOf(tableTeamList.get(position).getTeamNameShort()));
        imageViewClub.setImageResource(resID);
        textViewMatchesPlayed.setText(String.valueOf(tableTeamList.get(position).getMatchesPlayed()));
        textViewWins.setText(String.valueOf(tableTeamList.get(position).getWins()));
        textViewDraws.setText(String.valueOf(tableTeamList.get(position).getDraws()));
        textViewLosses.setText(String.valueOf(tableTeamList.get(position).getLosses()));
        textViewPoints.setText(String.valueOf(tableTeamList.get(position).getPoints()));

        return row;
    }
}
