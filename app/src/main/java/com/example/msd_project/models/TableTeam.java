package com.example.msd_project.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_team")
public class TableTeam {

    @PrimaryKey
    @NonNull
    private int id;
    private int position;
    private String teamName;
    private String teamNameShort;
    private String imageURL;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int points;
    private String dateTime;
    private int matchday;

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamNameShort() {
        return teamNameShort;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getPoints() {
        return points;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDateTime() { return dateTime; }

    public int getMatchday() { return matchday; }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamNameShort(String teamNameShort) {
        this.teamNameShort = teamNameShort;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public void setMatchday(int matchday) { this.matchday = matchday; }
}
