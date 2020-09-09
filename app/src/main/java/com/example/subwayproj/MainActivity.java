package com.example.subwayproj;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static List<Magistrala> magistrale;
    private Spinner sItems;
    private Spinner sItems2;

    // wanting to

    public static DatabaseInstance database;
    public static final int insertSubwayStationsToDB = 1;
    public static final int showSubwayStationsFromDB = 2;
    public static final int deleteSubwayStationsFromDB = 3;
    public static final int updateSubwayStationsFromDB = 4;
    public static final int insertMagistraleToDB = 5;
    public static final int showMagistraleFromDB = 6;
    public static final int deleteMagistraleFromDB = 7;
    public static final int updateMagistralaFromDB = 8;
    public static final int graph = 9;
    public static final int googleMaps = 10;

    FirebaseDatabase db;
    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = DatabaseInstance.getDatabaseInstance(this);

        magistrale = new ArrayList<>(addStation());

        List<String> strings = makeStationNames(magistrale);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = findViewById(R.id.location_spinner);
        sItems.setAdapter(adapter);

        sItems2 = findViewById(R.id.spinner);
        sItems2.setAdapter(adapter);

        List<Magistrala> magistrale = database.magistralaDao().getAll();
        magistrale.toString();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        /*DatabaseReference myRef = db.getReference("message");
        try {
            myRef.setValue("hello world");
        } catch (Exception ex) {
            Log.e("errFirebase", ex.getMessage());
        }*/

        Button btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Account management")
                            .setMessage("Email: " + user.getEmail())
                            .setNeutralButton("Sign out", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.signOut();
                                    Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_LONG);

                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Show saved routes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    writeToFirebase(null);
                                    Toast.makeText(getApplicationContext(), "Write successful", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                    View loginV = inflater.inflate(R.layout.dialog_login, null);
                    builder.setTitle("Account")
                            .setView(loginV);
                    final AlertDialog dialog = builder.create();

                    final EditText tbEmail = loginV.findViewById(R.id.tbEmail);
                    final EditText tbPassword = loginV.findViewById(R.id.tvPassword);
                    Button btnRegister = loginV.findViewById(R.id.btnRegister);
                    Button btnDismiss = loginV.findViewById(R.id.btnDismiss);
                    Button btnLogin = loginV.findViewById(R.id.btnLogin);

                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = tbEmail.getText().toString();
                            String password = tbPassword.getText().toString();

                            if (email.contains("@")) {
                                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_LONG).show();

                                        dialog.dismiss();
                                    }
                                });

                            } else {
                                Toast.makeText(v.getContext(), "Please make sure that the email address is in a valid form", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    btnDismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = tbEmail.getText().toString();
                            String password = tbPassword.getText().toString();

                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();

                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Auth failed. Please make sure you typed in the correct email and password", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });

                    dialog.show();
                }
            }
        });
    }

    private List<SubwayStation> readFromFirebase() {
        return null;
    }

    private void writeToFirebase(List<SubwayStation> route) {
        DatabaseReference myRef = db.getReference("message");
        myRef.setValue("hello world");
    }

    public void checkWeather(View view) {
        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
        startActivity(intent);
    }

    public void viewInfo(View view) {
        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(intent);

    }

    public void searchRoute(View view) {
        Intent newWindow = new Intent(MainActivity.this, ResultRouteActivity.class);

        SubwayStation from = null;
        SubwayStation to = null;

        for (Magistrala m : magistrale) {
            for (int i = 0; i < m.getStations().size(); i++) {
                if (m.getStations().get(i).getName() == sItems.getSelectedItem().toString()) {
                    from = m.getStations().get(i);
                } else if (m.getStations().get(i).getName() == sItems2.getSelectedItem().toString()) {
                    to = m.getStations().get(i);
                }
            }
        }

        if (sItems.getSelectedItem().toString().equalsIgnoreCase(sItems2.getSelectedItem().toString())) {
            to = from;
        }

        boolean ok = false;

        if (from.getMagistrala() != to.getMagistrala()) {
            for (int i = 0; i < from.getNeighbors().size(); i++) {
                if (from.getNeighbors().get(i).getMagistrala() == to.getMagistrala()) {
                    from = from.getNeighbors().get(i);
                    ok = true;
                    break;
                }
            }
        } else {
            ok = true;
        }

        if (!ok) {
            for (int i = 0; i < to.getNeighbors().size(); i++) {
                if (to.getNeighbors().get(i).getMagistrala() == from.getMagistrala()) {
                    to = to.getNeighbors().get(i);
                    break;
                }
            }
        }

        List<SubwayStation> route = makeRoute(from, to, magistrale);
        newWindow.putExtra("route", (Serializable) route);

        try {
            startActivity(newWindow);
        } catch (Exception e) {
            Log.e("onclickerr", e.getMessage());
        }
    }

    private List<Magistrala> addStation() {
        List<Magistrala> magistrale = new ArrayList<>();
        List<SubwayStation> m1 = new LinkedList<>();
        List<SubwayStation> m2 = new LinkedList<>();
        List<SubwayStation> m3 = new LinkedList<>();
        List<SubwayStation> m4 = new LinkedList<>();

        SubwayStation preciziei = new SubwayStation("Precizei", 3, null, true);
        SubwayStation pacii = new SubwayStation("Pacii", 3, null, false);
        SubwayStation gorjului = new SubwayStation("Gorjului", 3, null, false);
        SubwayStation lujerului = new SubwayStation("Lujerului", 3, null, false);
        SubwayStation politehnica = new SubwayStation("Politehnica", 3, null, false);
        SubwayStation eroilor3 = new SubwayStation("Eroilor", 3, null, false);
        SubwayStation eroilor1 = new SubwayStation("Eroilor", 1, null, false);
        SubwayStation izvor1 = new SubwayStation("Izvor", 1, null, false);
        SubwayStation izvor3 = new SubwayStation("Izvor", 3, null, false);
        SubwayStation unirii1 = new SubwayStation("Piata Unirii", 1, null, false);
        SubwayStation unirii2 = new SubwayStation("Piata Unirii", 2, null, false);
        SubwayStation unirii3 = new SubwayStation("Piata Unirii", 3, null, false);
        SubwayStation timpuri_noi1 = new SubwayStation("Timpuri Noi", 1, null, false);
        SubwayStation timpuri_noi3 = new SubwayStation("Timpuri Noi", 3, null, false);
        SubwayStation bravu1 = new SubwayStation("Mihai Bravu", 1, null, false);
        SubwayStation bravu3 = new SubwayStation("Mihai Bravu", 3, null, false);
        SubwayStation dristor1 = new SubwayStation("Dristor 1", 1, null, false);
        SubwayStation dristor2 = new SubwayStation("Dristor 2", 3, null, false);
        SubwayStation dristor3 = new SubwayStation("Dristor 1", 1, null, false);
        SubwayStation grigorescu1 = new SubwayStation("Nicolae Grigorescu", 1, null, false);
        SubwayStation grigorescu3 = new SubwayStation("Nicolae Grigorescu", 3, null, false);
        SubwayStation decembrie3 = new SubwayStation("1 Decembrie", 3, null, false);
        SubwayStation teclu = new SubwayStation("Nicolae Teclu", 3, null, false);
        SubwayStation saligny = new SubwayStation("Anghel Saligny", 3, null, true);
        SubwayStation pantelimon = new SubwayStation("Pantelimon", 1, null, true);
        SubwayStation republica = new SubwayStation("Republica", 1, null, false);
        SubwayStation cgeorgian = new SubwayStation("Costin Georgian", 1, null, false);
        SubwayStation titan = new SubwayStation("Titan", 1, null, false);
        SubwayStation grozavesti = new SubwayStation("Grozavesti", 1, null, false);
        SubwayStation petrache = new SubwayStation("Petrache Poenaru", 1, null, false);
        SubwayStation crangasi = new SubwayStation("Crangasi", 1, null, false);
        SubwayStation grivita = new SubwayStation("Grivita", 4, null, false);
        SubwayStation basarab1 = new SubwayStation("Basarab", 1, null, false);
        SubwayStation basarab4 = new SubwayStation("Basarab", 4, null, false);
        SubwayStation gara1 = new SubwayStation("Gara de Nord", 1, null, false);
        SubwayStation gara4 = new SubwayStation("Gara de Nord", 4, null, true);
        SubwayStation victoriei1 = new SubwayStation("Piata Victoriei", 1, null, false);
        SubwayStation victoriei2 = new SubwayStation("Piata Victoriei", 2, null, false);
        SubwayStation stefancelmare = new SubwayStation("Stefan cel Mare", 1, null, false);
        SubwayStation obor = new SubwayStation("Obor", 1, null, false);
        SubwayStation iancului = new SubwayStation("Piata Iancului", 1, null, false);
        SubwayStation muncii = new SubwayStation("Piata Muncii", 1, null, false);
        SubwayStation mai1 = new SubwayStation("1 Mai", 4, null, false);
        SubwayStation jiului = new SubwayStation("Jiului", 4, null, false);
        SubwayStation bazilescu = new SubwayStation("Parc Bazilescu", 4, null, false);
        SubwayStation laminorului = new SubwayStation("Laminorului", 4, null, false);
        SubwayStation straulesti = new SubwayStation("Straulesti", 4, null, true);
        SubwayStation pipera = new SubwayStation("Pipera", 2, null, true);
        SubwayStation vlaicu = new SubwayStation("Aurel Vlaicu", 2, null, false);
        SubwayStation aviatorilor = new SubwayStation("Aviatorilor", 2, null, false);
        SubwayStation romana = new SubwayStation("Piata Romana", 2, null, false);
        SubwayStation universitate = new SubwayStation("Universitate", 2, null, false);
        SubwayStation tineretului = new SubwayStation("Tineretului", 2, null, false);
        SubwayStation eroiirev = new SubwayStation("Eroii Revolutiei", 2, null, false);
        SubwayStation ctinbranc = new SubwayStation("Constantin Brancoveanu", 2, null, false);
        SubwayStation sudului = new SubwayStation("Piata Sudului", 2, null, false);
        SubwayStation aparatorii = new SubwayStation("Aparatorii Patriei", 2, null, false);
        SubwayStation dleonida = new SubwayStation("Dimitrie Leonida", 2, null, false);
        SubwayStation berceni = new SubwayStation("Berceni", 2, null, true);

        eroilor1.addNeighbor(eroilor3);
        eroilor3.addNeighbor(eroilor1);
        dristor1.addNeighbor(dristor2);
        dristor1.addNeighbor(dristor3);
        dristor2.addNeighbor(dristor1);
        dristor2.addNeighbor(dristor3);
        dristor3.addNeighbor(dristor1);
        dristor3.addNeighbor(dristor2);
        victoriei1.addNeighbor(victoriei2);
        victoriei2.addNeighbor(victoriei1);
        gara1.addNeighbor(gara4);
        gara4.addNeighbor(gara1);
        basarab1.addNeighbor(basarab4);
        basarab4.addNeighbor(basarab1);
        izvor1.addNeighbor(izvor3);
        izvor3.addNeighbor(izvor1);
        unirii1.addNeighbor(unirii2);
        unirii1.addNeighbor(unirii3);
        unirii2.addNeighbor(unirii1);
        unirii2.addNeighbor(unirii3);
        unirii3.addNeighbor(unirii1);
        unirii3.addNeighbor(unirii2);
        timpuri_noi1.addNeighbor(timpuri_noi3);
        timpuri_noi3.addNeighbor(timpuri_noi1);
        bravu1.addNeighbor(bravu3);
        bravu3.addNeighbor(bravu1);
        grigorescu1.addNeighbor(grigorescu3);
        grigorescu3.addNeighbor(grigorescu1);


        m3.add(preciziei);
        m3.add(pacii);
        m3.add(gorjului);
        m3.add(lujerului);
        m3.add(politehnica);
        m3.add(eroilor3);
        m3.add(izvor3);
        m3.add(unirii3);
        m3.add(timpuri_noi3);
        m3.add(bravu3);
        m3.add(dristor2);
        m3.add(grigorescu3);
        m3.add(decembrie3);
        m3.add(teclu);
        m3.add(saligny);

        m1.add(pantelimon);
        m1.add(republica);
        m1.add(cgeorgian);
        m1.add(titan);
        m1.add(grigorescu1);
        m1.add(dristor1);
        m1.add(bravu1);
        m1.add(timpuri_noi1);
        m1.add(unirii1);
        m1.add(izvor1);
        m1.add(eroilor1);
        m1.add(grozavesti);
        m1.add(petrache);
        m1.add(crangasi);
        m1.add(basarab1);
        m1.add(gara1);
        m1.add(victoriei1);
        m1.add(stefancelmare);
        m1.add(obor);
        m1.add(iancului);
        m1.add(muncii);
        m1.add(dristor3);

        m4.add(straulesti);
        m4.add(laminorului);
        m4.add(bazilescu);
        m4.add(jiului);
        m4.add(mai1);
        m4.add(grivita);
        m4.add(basarab4);
        m4.add(gara4);

        m2.add(pipera);
        m2.add(vlaicu);
        m2.add(aviatorilor);
        m2.add(victoriei2);
        m2.add(romana);
        m2.add(universitate);
        m2.add(unirii2);
        m2.add(tineretului);
        m2.add(eroiirev);
        m2.add(ctinbranc);
        m2.add(sudului);
        m2.add(aparatorii);
        m2.add(dleonida);
        m2.add(berceni);

        List<SubwayStation> possibleM1 = new ArrayList<>();
        possibleM1.add(eroilor3);
        possibleM1.add(dristor2);
        possibleM1.add(dristor3);
        possibleM1.add(dristor1);
        possibleM1.add(unirii2);
        possibleM1.add(unirii3);
        possibleM1.add(basarab4);
        possibleM1.add(gara4);
        possibleM1.add(victoriei2);
        possibleM1.add(grigorescu3);

        List<SubwayStation> possibleM3 = new ArrayList<>();
        possibleM3.add(eroilor1);
        possibleM3.add(unirii2);
        possibleM3.add(unirii1);
        possibleM3.add(dristor1);
        possibleM3.add(dristor3);
        possibleM3.add(grigorescu1);

        List<SubwayStation> possibleM2 = new ArrayList<>();
        possibleM2.add(victoriei1);
        possibleM2.add(unirii1);
        possibleM2.add(unirii3);

        List<SubwayStation> possibleM4 = new ArrayList<>();
        possibleM4.add(basarab1);
        possibleM4.add(gara1);

        magistrale.add(new Magistrala(m1, possibleM1, 1, "Opened on 16 November 1979", "M1"));
        magistrale.add(new Magistrala(m2, possibleM2, 2, "Opened on 25 October 1987", "M2"));
        magistrale.add(new Magistrala(m3, possibleM3, 3, "Opened on 19 August 1983", "M3"));
        magistrale.add(new Magistrala(m4, possibleM4, 4, "Opened on 1 March 2000", "M4"));


        return magistrale;
    }

    public List<SubwayStation> makeRoute(SubwayStation from, SubwayStation to, List<Magistrala> magistrale) {
        List<SubwayStation> route = new LinkedList<>();
        Graph g = convertToGraph(magistrale);

        return djikstra(g, from, to);
//        if (from.getMagistrala() == to.getMagistrala()) {
//            Magistrala magistrala = magistrale.get(from.getMagistrala() - 1);
//
//            int fromIndex = magistrala.indexOf(from);
//            int toIndex = magistrala.indexOf(to);
//
//            if (fromIndex < toIndex) {
//                for (int i = fromIndex; i <= toIndex; i++) {
//                    route.add(magistrala.get(i));
//                }
//            } else {
//                for (int i = fromIndex; i >= toIndex; i--) {
//                    route.add(magistrala.get(i));
//                }
//            }
//
//
//        } else {
//            /*boolean ok = false;
//
//            while (from != to) {
//                Magistrala fromMagistrala = magistrale.get(from.getMagistrala() - 1);
//                Magistrala toMagistrala = magistrale.get(to.getMagistrala() - 1);
//
//                List<SubwayStation> possibleChanges = fromMagistrala.getPossibleChanges();
//
//                List<List<SubwayStation>> routes = new ArrayList<>();
//
//                for (int i = 0; i < possibleChanges.size(); i++) {
//                    if (possibleChanges.get(i).getMagistrala() == to.getMagistrala()) {
//                        ok = true;
//
//                        SubwayStation changeTo = possibleChanges.get(i);
//                        SubwayStation changeFrom = null;
//
//                        try {
//                            for (int j = 0; j < changeTo.getNeighbors().size(); j++) {
//                                if (changeTo.getNeighbors().get(j).getMagistrala() == from.getMagistrala())
//                                    changeFrom = changeTo.getNeighbors().get(j);
//                            }
//                        } catch(Exception e) {
//                            Log.e("exceptionFailMessage", e.getMessage());
//                        }
//
//                        int indexFrom = fromMagistrala.indexOf(from);
//                        int indexChangeFrom = fromMagistrala.indexOf(changeFrom);
//                        int indexChangeTo = toMagistrala.indexOf(changeTo);
//                        int indexTo = toMagistrala.indexOf(to);
//
//                        List<SubwayStation> aRoute = new LinkedList<>();
//
//                        // this adds route to routes list
//                        if (indexFrom < indexChangeFrom) {
//                            for (int k = indexFrom; k < indexChangeFrom; k++) {
//                                aRoute.add(fromMagistrala.get(k));
//                            }
//                        } else {
//                            for (int k = indexFrom; k > indexChangeFrom; k--) {
//                                aRoute.add(fromMagistrala.get(k));
//                            }
//                        }
//
//                        if (indexTo < indexChangeTo) {
//                            for (int k = indexChangeTo; k >= indexTo; k--) {
//                                aRoute.add(toMagistrala.get(k));
//                            }
//                        } else {
//                            for (int k = indexChangeTo; k <= indexTo; k++) {
//                                aRoute.add(toMagistrala.get(k));
//                            }
//                        }
//
//                        routes.add(aRoute);
//                    }
//                }
//
//                List<SubwayStation> bestRoute = routes.get(0);
//
//                for (int i = 1; i < routes.size(); i++) {
//                    if (bestRoute.size() > routes.get(i).size())
//                        bestRoute = routes.get(i);
//                }
//
//                route = bestRoute;
//
//                from = to;
//
//
//            }*/
//
//            route = djikstra(g, from, to);
//        }
    }

    private List<String> makeStationNames(List<Magistrala> magistrale) {
        List<String> strings = new ArrayList<>();
        for (Magistrala m : magistrale) {
            for (int i = 0; i < m.getStations().size(); i++) {
                strings.add(m.getStations().get(i).getName());
            }
        }

        return strings;
    }

    private Graph convertToGraph(List<Magistrala> magistrale) {
        Graph graph = new Graph();
        for (Magistrala m : magistrale) {
            List<SubwayStation> stations = m.getStations();

            graph.addVertex(stations.get(0));

            for (int i = 1; i < stations.size(); i++) {
                graph.addVertex(stations.get(i));
                graph.addEdge(stations.get(i - 1), stations.get(i));
            }
        }

        SubwayStation preciziei = new SubwayStation("Precizei", 3, null, true);
        SubwayStation pacii = new SubwayStation("Pacii", 3, null, false);
        SubwayStation gorjului = new SubwayStation("Gorjului", 3, null, false);
        SubwayStation lujerului = new SubwayStation("Lujerului", 3, null, false);
        SubwayStation politehnica = new SubwayStation("Politehnica", 3, null, false);
        SubwayStation eroilor3 = new SubwayStation("Eroilor", 3, null, false);
        SubwayStation eroilor1 = new SubwayStation("Eroilor", 1, null, false);
        SubwayStation izvor1 = new SubwayStation("Izvor", 1, null, false);
        SubwayStation izvor3 = new SubwayStation("Izvor", 3, null, false);
        SubwayStation unirii1 = new SubwayStation("Piata Unirii", 1, null, false);
        SubwayStation unirii2 = new SubwayStation("Piata Unirii", 2, null, false);
        SubwayStation unirii3 = new SubwayStation("Piata Unirii", 3, null, false);
        SubwayStation timpuri_noi1 = new SubwayStation("Timpuri Noi", 1, null, false);
        SubwayStation timpuri_noi3 = new SubwayStation("Timpuri Noi", 3, null, false);
        SubwayStation bravu1 = new SubwayStation("Mihai Bravu", 1, null, false);
        SubwayStation bravu3 = new SubwayStation("Mihai Bravu", 3, null, false);
        SubwayStation dristor1 = new SubwayStation("Dristor 1", 1, null, false);
        SubwayStation dristor2 = new SubwayStation("Dristor 2", 3, null, false);
        SubwayStation dristor3 = new SubwayStation("Dristor 1", 1, null, false);
        SubwayStation grigorescu1 = new SubwayStation("Nicolae Grigorescu", 1, null, false);
        SubwayStation grigorescu3 = new SubwayStation("Nicolae Grigorescu", 3, null, false);
        SubwayStation decembrie3 = new SubwayStation("1 Decembrie", 3, null, false);
        SubwayStation teclu = new SubwayStation("Nicolae Teclu", 3, null, false);
        SubwayStation saligny = new SubwayStation("Anghel Saligny", 3, null, true);
        SubwayStation pantelimon = new SubwayStation("Pantelimon", 1, null, true);
        SubwayStation republica = new SubwayStation("Republica", 1, null, false);
        SubwayStation cgeorgian = new SubwayStation("Costin Georgian", 1, null, false);
        SubwayStation titan = new SubwayStation("Titan", 1, null, false);
        SubwayStation grozavesti = new SubwayStation("Grozavesti", 1, null, false);
        SubwayStation petrache = new SubwayStation("Petrache Poenaru", 1, null, false);
        SubwayStation crangasi = new SubwayStation("Crangasi", 1, null, false);
        SubwayStation grivita = new SubwayStation("Grivita", 4, null, false);
        SubwayStation basarab1 = new SubwayStation("Basarab", 1, null, false);
        SubwayStation basarab4 = new SubwayStation("Basarab", 4, null, false);
        SubwayStation gara1 = new SubwayStation("Gara de Nord", 1, null, false);
        SubwayStation gara4 = new SubwayStation("Gara de Nord", 4, null, true);
        SubwayStation victoriei1 = new SubwayStation("Piata Victoriei", 1, null, false);
        SubwayStation victoriei2 = new SubwayStation("Piata Victoriei", 2, null, false);
        SubwayStation stefancelmare = new SubwayStation("Stefan cel Mare", 1, null, false);
        SubwayStation obor = new SubwayStation("Obor", 1, null, false);
        SubwayStation iancului = new SubwayStation("Piata Iancului", 1, null, false);
        SubwayStation muncii = new SubwayStation("Piata Muncii", 1, null, false);
        SubwayStation mai1 = new SubwayStation("1 Mai", 4, null, false);
        SubwayStation jiului = new SubwayStation("Jiului", 4, null, false);
        SubwayStation bazilescu = new SubwayStation("Parc Bazilescu", 4, null, false);
        SubwayStation laminorului = new SubwayStation("Laminorului", 4, null, false);
        SubwayStation straulesti = new SubwayStation("Straulesti", 4, null, true);
        SubwayStation pipera = new SubwayStation("Pipera", 2, null, true);
        SubwayStation vlaicu = new SubwayStation("Aurel Vlaicu", 2, null, false);
        SubwayStation aviatorilor = new SubwayStation("Aviatorilor", 2, null, false);
        SubwayStation romana = new SubwayStation("Piata Romana", 2, null, false);
        SubwayStation universitate = new SubwayStation("Universitate", 2, null, false);
        SubwayStation tineretului = new SubwayStation("Tineretului", 2, null, false);
        SubwayStation eroiirev = new SubwayStation("Eroii Revolutiei", 2, null, false);
        SubwayStation ctinbranc = new SubwayStation("Constantin Brancoveanu", 2, null, false);
        SubwayStation sudului = new SubwayStation("Piata Sudului", 2, null, false);
        SubwayStation aparatorii = new SubwayStation("Aparatorii Patriei", 2, null, false);
        SubwayStation dleonida = new SubwayStation("Dimitrie Leonida", 2, null, false);
        SubwayStation berceni = new SubwayStation("Berceni", 2, null, true);

        graph.addEdge(eroilor1, eroilor3);
        graph.addEdge(izvor1, izvor3);
        graph.addEdge(unirii1, unirii3);
        graph.addEdge(unirii1, unirii2);
        graph.addEdge(unirii2, unirii3);
        graph.addEdge(timpuri_noi1, timpuri_noi3);
        graph.addEdge(bravu1, bravu3);
        graph.addEdge(dristor1, dristor3);
        graph.addEdge(dristor1, dristor2);
        graph.addEdge(dristor2, dristor3);
        graph.addEdge(grigorescu1, grigorescu3);

        graph.addEdge(victoriei1, victoriei2);
        graph.addEdge(gara1, gara4);
        graph.addEdge(basarab1, basarab4);

        return graph;
    }

    public List<SubwayStation> djikstra(Graph g, SubwayStation src0, SubwayStation to) {

        // initialization and declaration
        List<Vertex> visited = new ArrayList<>();
        List<Vertex> unvisited = new ArrayList<>();
        int[] dist = new int[g.getAdjVertices().size() + 1];

        // starting from source vertex
        Vertex from = new Vertex(src0);

        // getting the list of vertices and source position
        List<Vertex> vertices = g.getVertices();
        int pos = vertices.indexOf(from);

        // getting a map to store the prev vertices
        Map<Vertex, Vertex> prevVertices = new HashMap<>();

        // populating the visited and unvisited for first time use
        try {
            for (int i = 0; i < vertices.size(); i++) {
                if (i != pos) {
                    unvisited.add(vertices.get(i));
                    dist[i] = 999;
                } else {
                    //visited.add(vertices.get(i));
                    dist[i] = 0;
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage() + "");
        }

        Vertex current = from;

        int noI = 0;

        // djikstra check neighbors for current
        while (true) {
            noI++;
            if (current != from)
                unvisited.remove(current);

            int cPos = vertices.indexOf(current);
            List<Vertex> adj = g.getAdjVertices().get(current);

            // calculating the distance to the neighbors
            for (int i = 0; i < adj.size(); i++) {
                Vertex cAdj = adj.get(i);
                if (!visited.contains(cAdj)) {
                    int indexAdj = vertices.indexOf(cAdj);

                    if (dist[cPos] + 1 < dist[indexAdj]) {
                        dist[indexAdj] = dist[cPos] + 1;
                        prevVertices.put(cAdj, current);
                        //next = cAdj;
                    }
                }
            }

            // adding the current vertex to the visited list
            visited.add(current);

            int min = 999999;
            int minIndex = 999999;

            // searching for the next vertex to visit; the smallest distance not visited = best
            for (int i = 0; i < dist.length; i++) {
                if (dist[i] < min) {
                    Vertex curr = vertices.get(i);
                    if (unvisited.contains(curr)) {
                        min = dist[i];
                        minIndex = i;
                    }

                    Log.e("noDebugger", vertices.get(i).toString());
                }
            }


            // trying the get the next || array out of bounds exception possible
            try {
                current = vertices.get(minIndex);
            } catch (Exception e) {
                //Toast.makeText(this, "Iteration: " + noI, Toast.LENGTH_LONG).show();
                break;
            }
        }

        List<SubwayStation> route = new LinkedList<>();
        Vertex vertexTo = new Vertex(to);
        int index = vertices.indexOf(vertexTo);

        int count = dist[index];

        Vertex c = vertices.get(index);

        for (int i = 0; i <= count; i++) {
            route.add(c.getStation());

            c = prevVertices.get(c);
        }

        Collections.reverse(route);

        return route;
    }

    private String readSharedPreferences(String fileName) {
        // we use a string builder for performance reasons
        // is the equivalent of a mutable string
        StringBuilder res = new StringBuilder("");

        // we open the shared preferences file
        SharedPreferences settings = getSharedPreferences(fileName, MODE_PRIVATE);

        // we take the no of routes in order to cycle through them
        int noRoutes = settings.getInt("noRoutes", 0);

        // we declare and initialize a new variable to keep track of the current station
        int currStation = 0;

        for (int i = 0; i < noRoutes; i++) {
            int noStations = settings.getInt("noStations" + (i + 1), 0);

            res.append("Route no: " + (i + 1) + '\n');
            for (int j = 0; j < noStations; j++) {
                String stationName = settings.getString("stationName" + currStation, "");
                int nrMagistrala = settings.getInt("magistralaNo" + currStation, 0);

                currStation++;
                res.append("Station: " + stationName + '\n' + "Magistrala: " + nrMagistrala + "\n\n");
            }
        }

        return res.toString();
    }

    public void showSavedRoutes(View view) {
        String res = readSharedPreferences("routes");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(res)
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, insertSubwayStationsToDB, 1, "Insert Subway Stations to DB");
        menu.add(0, showSubwayStationsFromDB, 2, "Show Subway Stations from DB");
        menu.add(0, deleteSubwayStationsFromDB, 3, "Delete Subway Stations from DB");
        menu.add(0, updateSubwayStationsFromDB, 4, "Update Subway station name from DB");
        menu.add(0, insertMagistraleToDB, 5, "Insert Magistrale to DB");
        menu.add(0, showMagistraleFromDB, 6, "Show magistrale from DB");
        menu.add(0, deleteMagistraleFromDB, 7, "Delete Magistrale from DB");
        menu.add(0, updateMagistralaFromDB, 8, "Update Magistrala from DB");
        menu.add(0, graph, 9, "Graph distribution of subway stations on lines");
        menu.add(0,googleMaps,10,"Google Maps");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case insertSubwayStationsToDB:
                insertSubwayStationsToDB(magistrale);
                break;
            case showSubwayStationsFromDB:
                Intent intent = new Intent(MainActivity.this, ShowDBActivity.class);
                final String subwayStations = showSubwayStationsFromDB();
                intent.putExtra("content", subwayStations);
                intent.putExtra("type", "subwaystation");
                startActivity(intent);
                break;
            case deleteSubwayStationsFromDB:
                database.subwayStationDao().deleteAllSubwayStations();
                Toast.makeText(this, "Subway Stations successfully deleted.", Toast.LENGTH_LONG).show();
                break;
            case insertMagistraleToDB:
                insertMagistraleToDB(magistrale);
                break;
            case showMagistraleFromDB:
                Intent intentMagistrale = new Intent(MainActivity.this, ShowDBActivity.class);
                String magistrale = showMagistraleFromDB();
                intentMagistrale.putExtra("content", magistrale);
                intentMagistrale.putExtra("type", "magistrala");
                startActivity(intentMagistrale);
                break;
            case deleteMagistraleFromDB:
                database.magistralaDao().deleteAllMagistrale();
                Toast.makeText(this, "Magistrale successfully deleted.", Toast.LENGTH_LONG).show();
                break;
            case updateSubwayStationsFromDB:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View myView = inflater.inflate(R.layout.dialog_change_name, null);
                final Spinner checkInNames = myView.findViewById(R.id.stationsId);
                final List<SubwayStation> subStations = database.subwayStationDao().getAll();
                List<String> stationNames = new ArrayList<>();
                for (SubwayStation s : subStations) {
                    stationNames.add(s.getName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stationNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                checkInNames.setAdapter(dataAdapter);
                final EditText subwayNewName = myView.findViewById(R.id.editTextName);

                builder.setView(myView);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = subwayNewName.getText().toString();
                        String selectedItem = checkInNames.getSelectedItem().toString();
                        int id = -1;
                        for (SubwayStation s : subStations) {
                            if (selectedItem.equalsIgnoreCase(s.getName())) {
                                id = s.getSubwayStationId();
                            }
                        }
                        if (id != -1)
                            database.subwayStationDao().update(newName, id);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case updateMagistralaFromDB:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                View View = layoutInflater.inflate(R.layout.dialog_change_name, null);
                final Spinner checkInMagistrala = View.findViewById(R.id.stationsId);
                final List<Magistrala> magistraleDB = database.magistralaDao().getAll();
                List<String> magistralaNames = new ArrayList<>();
                for (Magistrala m : magistraleDB) {
                    magistralaNames.add(m.getNameMagistrala());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, magistralaNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                checkInMagistrala.setAdapter(adapter);
                final EditText subwayNewDescript = View.findViewById(R.id.editTextName);
                builder2.setView(View);
                builder2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newDescript = subwayNewDescript.getText().toString();
                        String selectedItem = checkInMagistrala.getSelectedItem().toString();
                        int id = -1;
                        for (Magistrala m : magistraleDB) {
                            if (selectedItem.equalsIgnoreCase(m.getNameMagistrala())) {
                                id = m.getNrMagistrala();
                            }
                        }
                        if (id != -1)
                            database.magistralaDao().updateMagistrala(newDescript, id);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.create().show();
                break;
            case graph:
                Intent intent2 = new Intent(this, ActivityGraphic.class);
                startActivity(intent2);
                break;
            case googleMaps:
                Intent intent3 = new Intent(this, MapsActivity.class);
                startActivity(intent3);
                break;
        }
        return true;
    }

    private void insertSubwayStationsToDB(List<Magistrala> magistrale) {
        for (Magistrala m : magistrale) {
            for (int i = 0; i < m.getStations().size(); i++) {
                database.subwayStationDao().insertSubwayStation(m.getStations().get(i));
            }
        }
        Toast.makeText(this, "Subway stations successfully added to database.", Toast.LENGTH_LONG).show();
    }

    private String showSubwayStationsFromDB() {
        StringBuilder res = new StringBuilder("");
        List<SubwayStation> temp = database.subwayStationDao().getAll();

        for (int i = 0; i < temp.size(); i++) {
            res.append(temp.get(i).toString() + "\n");
        }
        return res.toString();
    }

    private void insertMagistraleToDB(List<Magistrala> magistrale) {
        for (Magistrala m : magistrale)
            database.magistralaDao().insertMagistrala(m);
        Toast.makeText(this, "Magistrale successfully added to database.", Toast.LENGTH_LONG).show();
    }

    private String showMagistraleFromDB() {
        StringBuilder res = new StringBuilder("");
        List<Magistrala> temp = database.magistralaDao().getAll();

        for (int i = 0; i < temp.size(); i++) {
            res.append(temp.get(i).toString() + "\n");
        }

        return res.toString();
    }
}