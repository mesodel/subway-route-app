package com.example.subwayproj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Vertex, List<Vertex>> adjVertices;
    private List<Vertex> vertices;

    public Graph() {
        adjVertices = new HashMap<>();
        vertices = new ArrayList<>();
    }

    public Map<Vertex, List<Vertex>> getAdjVertices() {
        return adjVertices;
    }

    public void setAdjVertices(Map<Vertex, List<Vertex>> adjVertices) {
        this.adjVertices = adjVertices;
    }

    public void addVertex(SubwayStation station, List<SubwayStation> stations) {
        List<Vertex> vertices = new ArrayList<>();

        for(SubwayStation st : stations) {
            vertices.add(new Vertex(st));
        }

        Vertex s = new Vertex(station);

        adjVertices.put(s, vertices);
        vertices.add(s);
    }

    public void addVertex(SubwayStation station) {
        Vertex s = new Vertex(station);

        adjVertices.put(s, new ArrayList<Vertex>());
        vertices.add(s);
    }

    public void addEdge(SubwayStation from, SubwayStation to) {
        Vertex fromV = new Vertex(from);
        Vertex toV = new Vertex(to);

        adjVertices.get(fromV).add(toV);
        adjVertices.get(toV).add(fromV);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}