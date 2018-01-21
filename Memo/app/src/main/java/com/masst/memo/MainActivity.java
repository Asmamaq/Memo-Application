package com.masst.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // for Accessing database function
    private DatabaseAccess databaseAccess;

    Snackbar sb;
    public View snackview;
    boolean BackPressed = false;

    // for recycler view
    private RecyclerView rvMemos;
    private ArrayList<Memo> alMemos;
    public MemoRVAdapter rvaMemos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        snackview= findViewById(android.R.id.content);
        this.databaseAccess = DatabaseAccess.getInstance(this);
        rvMemos = (RecyclerView) findViewById(R.id.rvStudents);

        databaseAccess.open();
        this.alMemos = databaseAccess.getAllMemos();
        databaseAccess.close();
        rvaMemos = new MemoRVAdapter(this, alMemos);
        rvMemos.setAdapter(rvaMemos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(null);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(MainActivity.this,EditMemoActivity.class);
                startActivity(i);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMemos.setLayoutManager(linearLayoutManager);

        sb = Snackbar.make(snackview,"Click exit to exit :)",Snackbar.LENGTH_INDEFINITE);
        sb.setAction("Exit", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackPressed=true;
                onBackPressed();
                sb.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (!BackPressed) {
            sb.show();
        } else
            super.onBackPressed();
    }

    public void onAddClicked()
    {
            Intent intent = new Intent(this, EditMemoActivity.class);
            startActivity(intent);
    }
}
