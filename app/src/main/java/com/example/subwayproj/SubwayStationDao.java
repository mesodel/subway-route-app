package com.example.subwayproj;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubwayStationDao {
    @Query("select * from subwayStations")
    List<SubwayStation> getAll();

    @Query("select * from subwayStations where magistrala = :nrMagistrala")
    List<SubwayStation> getAllStations(int nrMagistrala);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertSubwayStation(SubwayStation station);

    @Query("delete from subwayStations")
    void deleteAllSubwayStations();

    @Query("update subwayStations set name = :name where subwayStationId= :subwayStationId")
    void update(String name, int subwayStationId);

}
