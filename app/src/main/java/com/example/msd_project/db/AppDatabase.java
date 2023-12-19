package com.example.msd_project.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.msd_project.models.Match;
import com.example.msd_project.models.TableTeam;

@Database(entities = {TableTeam.class, Match.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
     private static AppDatabase instance;
     public abstract TableTeamDAO getTableTeamDAO();
     public abstract MatchDAO getMatchDAO();

     //singleton
     public static AppDatabase getInstance(Context context) {

          if (instance == null) {
               instance = Room.databaseBuilder((context.getApplicationContext()),
                       AppDatabase.class, "app_database").allowMainThreadQueries().build();
          }
          return instance;
     }
}
