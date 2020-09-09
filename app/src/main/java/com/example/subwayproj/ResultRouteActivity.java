package com.example.subwayproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ResultRouteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<SubwayStation> route;
    //private RecyclerView.LayoutManager mLayoutManager;
    public static final int sharedPreferences = 0;
    public static final int extractFromFile = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_route);

        Intent intent = getIntent();
        route = (List<SubwayStation>) intent.getSerializableExtra("route");


        recyclerView = findViewById(R.id.recyclerView);
        adapter = new WordListAdapter(route);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, sharedPreferences, 1, "Save Shared Preferences");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case sharedPreferences:
                saveResultRoute(route);
                break;
            case extractFromFile:
                try {
                    extractFromTextFile("route.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    private void saveResultRoute(List<SubwayStation> route) {
        SharedPreferences settings = getSharedPreferences("routes", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // getting the no of routes ( we need this to separate the routes in a nice way for formatting reasons )
        int noRoutes = settings.getInt("noRoutes", 0);

        // getting the no of stations so we can keep track of them in order not to override any value
        int totalNoStations = settings.getInt("totalNoStations", 0);

        // incrementing it to add one more route
        noRoutes++;

        // updating the noRoutes in the shared preferences file to reflect the change
        editor.putInt("noRoutes", noRoutes);

        // putting the no of stations so we know how much to read
        editor.putInt("noStations" + noRoutes, route.size());

        // updating the no of stations by adding the no of stations for the current route
        editor.putInt("totalNoStations", totalNoStations + route.size());

        // we can now cycle through the stations and add them to our list
        // we need to separate them in order avoid overrides
        // in order to do this we use totalNoStations which we increment for each station added

        for (int i = 0; i < route.size(); i++) {
            SubwayStation curr = route.get(i);
            editor.putString("stationName" + totalNoStations, curr.getName());
            editor.putInt("magistralaNo" + totalNoStations, curr.getMagistrala());
            totalNoStations++;
        }

        // in order for the changes made to the shared preferences to be saved, we need to apply them
        editor.apply();

        // now the shared preferences file has one more route added to it
        Toast.makeText(this, "Route succesfully saved", Toast.LENGTH_LONG).show();
    }

    private void extractFromTextFile(String fileName) throws IOException {
        FileInputStream file = openFileInput(fileName);
        DataInputStream in = new DataInputStream(file);

        StringBuilder res = new StringBuilder("");
        String line = in.readUTF();
    try {
        while (true) {
            res.append(line);
            line = in.readUTF();
        }
    } catch(EOFException e) {

    }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Saved route:").setMessage(res.toString()).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
