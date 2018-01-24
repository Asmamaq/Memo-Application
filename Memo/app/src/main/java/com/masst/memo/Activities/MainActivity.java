package com.masst.memo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.masst.memo.Adapters.MemoRVAdapter;
import com.masst.memo.AndroidDatabaseManager;
import com.masst.memo.Database.DatabaseAccess;
import com.masst.memo.Models.Memo;
import com.masst.memo.R;

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
      /*  SharedPreferences preferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String display = preferences.getString("display","");
        if(display==null)
        {
            return;
        }*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        snackview= findViewById(android.R.id.content);
        this.databaseAccess = DatabaseAccess.getInstance(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(null);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(MainActivity.this,EditMemoActivity.class);
                startActivity(i);
            }
        });

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
    protected void onResume() {
        super.onResume();
        rvMemos = (RecyclerView) findViewById(R.id.rvStudents);
        databaseAccess.open();
        this.alMemos = databaseAccess.getAllMemos();
        databaseAccess.close();
        rvaMemos = new MemoRVAdapter(this, alMemos);
        rvMemos.setAdapter(rvaMemos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMemos.setLayoutManager(linearLayoutManager);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Db) {
            Intent intent = new Intent(MainActivity.this, AndroidDatabaseManager.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
