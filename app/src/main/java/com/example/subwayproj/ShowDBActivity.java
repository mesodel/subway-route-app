package com.example.subwayproj;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowDBActivity extends AppCompatActivity {
    private String content;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        type = intent.getStringExtra("type");

        TextView tv = findViewById(R.id.tvContent);
        tv.setText(content);
    }

    public void writeToTxt(View view) throws IOException {
        String fileName = type + ".txt";
        FileOutputStream file = openFileOutput(fileName, MODE_PRIVATE);
        DataOutputStream out = new DataOutputStream(file);

        out.writeUTF(content);
        out.flush();
        file.close();

        Toast.makeText(this, "Report successfully saved to text file.", Toast.LENGTH_LONG).show();
    }

    public void extractFromTxt(View view) throws IOException {
        String fileName = type + ".txt";
        FileInputStream file = openFileInput(fileName);
        DataInputStream in = new DataInputStream(file);

        StringBuilder res = new StringBuilder("");
        String line = in.readUTF();
        try {
            while (true) {
                res.append(line);
                line = in.readUTF();
            }
        } catch (EOFException e) {

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

