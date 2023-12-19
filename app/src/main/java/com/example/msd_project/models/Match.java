package com.example.msd_project.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "match")
public class Match {

    @PrimaryKey
    @NonNull
    private int id;
    private String homeTeam;
    private String homeTeamShort;
    private String homeTeamImageName;
    private String awayTeam;
    private String awayTeamShort;
    private String awayTeamImageName;
    private String date;
    private String time;
    private int matchday;
    private int homeTeamGoals;
    private int awayTeamGoals;
    private String status;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getHomeTeamShort() { return homeTeamShort; }

    public void setHomeTeamShort(String homeTeamShort) { this.homeTeamShort = homeTeamShort; }

    public String getHomeTeamImageName() { return homeTeamImageName; }

    public void setHomeTeamImageName(String homeTeamImageName) { this.homeTeamImageName = homeTeamImageName; }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getAwayTeamShort() { return awayTeamShort; }

    public void setAwayTeamShort(String awayTeamShort) {this.awayTeamShort = awayTeamShort;}

    public String getAwayTeamImageName() { return awayTeamImageName; }

    public void setAwayTeamImageName(String awayTeamImageName) { this.awayTeamImageName = awayTeamImageName; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMatchday() {
        return matchday;
    }

    public void setMatchday(int matchday) {
        this.matchday = matchday;
    }

    public int getHomeTeamGoals() { return homeTeamGoals; }

    public void setHomeTeamGoals(int homeTeamGoals) { this.homeTeamGoals = homeTeamGoals; }

    public int getAwayTeamGoals() { return awayTeamGoals; }

    public void setAwayTeamGoals(int awayTeamGoals) { this.awayTeamGoals = awayTeamGoals; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
