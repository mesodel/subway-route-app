package com.example.subwayproj;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "magistrale")
public class Magistrala {
    @PrimaryKey
    private int nrMagistrala;
    private String description;
    private String nameMagistrala;
    @Ignore
    private List<SubwayStation> stations;
    @Ignore
    private List<SubwayStation> possibleChanges;


    public Magistrala(int nrMagistrala, String description, String nameMagistrala) {
        this.nrMagistrala = nrMagistrala;
        this.nameMagistrala = nameMagistrala;
        this.description = description;

        stations = MainActivity.database.subwayStationDao().getAllStations(nrMagistrala);
    }

    @Ignore
    public Magistrala(List<SubwayStation> stations, List<SubwayStation> possibleChanges, int nrMagistrala, String description, String nameMagistrala) {
        this.stations = stations;
        this.possibleChanges = possibleChanges;
        this.nrMagistrala = nrMagistrala;
        this.description = description;
        this.nameMagistrala = nameMagistrala;
    }

    public int indexOf(SubwayStation from) {
        return stations.indexOf(from);
    }

    public SubwayStation get(int i) {
        return stations.get(i);
    }

    public List<SubwayStation> getStations() {
        return stations;
    }

    public List<SubwayStation> getPossibleChanges() {
        return possibleChanges;
    }

    public int getNrMagistrala() {
        return nrMagistrala;
    }

    public void setNrMagistrala(int nrMagistrala) {
        this.nrMagistrala = nrMagistrala;
    }

    public void setStations(List<SubwayStation> stations) {
        this.stations = stations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameMagistrala() {
        return nameMagistrala;
    }

    public void setNameMagistrala(String nameMagistrala) {
        this.nameMagistrala = nameMagistrala;
    }

    @NonNull
    @Override
    public String toString() {

        return "\nMagistrala: " + this.nrMagistrala + "\nNumber of stations: " + this.stations.size() + "\n" + "Description: "+this.description +"\n";
    }
}
