package com.example.msd_project.db;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Dao;

import com.example.msd_project.models.Match;

import java.util.List;

@Dao
public interface MatchDAO {

    @Insert
    public void insert(Match... matches);

    @Update
    public void update(Match... matches);

    @Delete
    public void delete(Match... matches);

    @Query("SELECT * FROM match")
    public List<Match> getMatches();

    @Query("DELETE FROM match")
    public void deleteMatches();

    @Query("SELECT * FROM match where homeTeam like '%' || :team || '%' " +
            "OR awayTeam like '%' || :team || '%'")
    public List<Match> searchMatchesByTeam(String team);
}

