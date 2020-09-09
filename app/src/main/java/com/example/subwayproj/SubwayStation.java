package com.example.subwayproj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "subwayStations", foreignKeys = @ForeignKey(entity = Magistrala.class,
        parentColumns = "nrMagistrala",childColumns = "magistrala",onDelete = ForeignKey.CASCADE))
public class SubwayStation implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int subwayStationId;
    private String name;
    private int magistrala;

    @Ignore
    private List<SubwayStation> neighbors = null;
    @Ignore
    private boolean isEnd;

    @Ignore
    public SubwayStation(String name, int magistrala, SubwayStation[] neighbors, boolean isEnd) {
        this.name = name;
        this.isEnd = isEnd;
        this.magistrala = magistrala;
        this.neighbors = new ArrayList<>();
        if(neighbors != null) {
            for(int i = 0; i < neighbors.length; i++) {
                this.neighbors.add(neighbors[i]);
            }
        }
    }

    public SubwayStation(int subwayStationId, String name, int magistrala) {
        this.subwayStationId = subwayStationId;
        this.name = name;
        this.magistrala = magistrala;
    }

    public int getSubwayStationId() {
        return subwayStationId;
    }

    public void setSubwayStationId(int subwayStationId) {
        this.subwayStationId = subwayStationId;
    }

    public String getName() {
        return name;
    }

    public int getMagistrala() {
        return magistrala;
    }

    public List<SubwayStation> getNeighbors() {
        return neighbors;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void addNeighbor(SubwayStation neighbor) {
        this.neighbors.add(neighbor);
    }

    public boolean hasNeighbors() {
        if(neighbors.size() > 0)
            return true;

        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMagistrala(int magistrala) {
        this.magistrala = magistrala;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == this)
            return true;
        if(obj == null)
            return false;

        SubwayStation s = (SubwayStation) obj;
        if(this.name != s.name)
            return false;
        if(this.magistrala != s.magistrala)
            return false;
        if(this.isEnd != s.isEnd)
            return false;

        return true;
    }

    @NonNull
    @Override
    public String toString() {
        String res = "\n\nSubway station: " + getName() +  "\nMagistrala: " + getMagistrala();
        return res;
    }
}
