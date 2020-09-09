package com.example.subwayproj;

import java.util.Objects;

public class Vertex {
    private SubwayStation station;

    public Vertex(SubwayStation station) {
        this.station = station;
    }

    public SubwayStation getStation() {
        return station;
    }

    public void setStation(SubwayStation station) {
        this.station = station;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return station.equals(vertex.station);
    }

    @Override
    public int hashCode() {
        return station.getName().length() + station.getMagistrala();
    }
}