package com.example.subwayproj;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MagistralaDao {

    @Query("select * from magistrale")
    List<Magistrala> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertMagistrala(Magistrala magistrala);

    @Query("delete from magistrale")
    void deleteAllMagistrale();

    @Query("update magistrale set description= :description where nrMagistrala= :nrMagistrala")
    void updateMagistrala(String description,int nrMagistrala);
}
