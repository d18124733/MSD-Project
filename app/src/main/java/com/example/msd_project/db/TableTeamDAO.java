package com.example.msd_project.db;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Dao;

import com.example.msd_project.models.TableTeam;

import java.util.List;

@Dao
public interface TableTeamDAO {

    @Insert
    public void insert(TableTeam... tableTeams);

    @Update
    public void update(TableTeam... tableTeams);

    @Delete
    public void delete(TableTeam... tableTeams);

    @Query("SELECT * FROM table_team ORDER BY position")
    public List<TableTeam> getTableTeams();

    @Query("DELETE FROM table_team")
    public void deleteTableTeams();
}
