package com.example.subwayproj;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SubwayStation.class,Magistrala.class}, version = 3)
public abstract class DatabaseInstance extends RoomDatabase {
    private static DatabaseInstance instance = null;

    public abstract SubwayStationDao subwayStationDao();
    public abstract MagistralaDao magistralaDao();

    public static DatabaseInstance getDatabaseInstance(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context,DatabaseInstance.class,"my-db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        return instance;
    }
}
