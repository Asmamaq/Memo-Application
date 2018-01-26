package com.masst.memo.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.masst.memo.Database.DatabaseAccess;
import com.masst.memo.Globals;
import com.masst.memo.Models.AlertDialogManager;
import com.masst.memo.Models.Memo;
import com.masst.memo.Models.SessionManagment;
import com.masst.memo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagment session;

    // for Accessing database function
    private DatabaseAccess databaseAccess;

    Snackbar sb;
    public View snackview;
    boolean BackPressed = false;

    String username,userPass;
    // for recycler view
    private RecyclerView rvMemos;
    private ArrayList<Memo> alMemos;
    public MemoRVAdapter rvaMemos;

    @Override
    protected void onRestart() {
        super.onRestart();
        // if user press back button from login then app close
        if(Globals.Exit_Code.equals("true"))
        {
            this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        // Session class instance
        session = new SessionManagment(getApplicationContext());

        session.checkLogin();

       // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name of user
        username = user.get(SessionManagment.KEY_NAME);
        userPass = user.get(SessionManagment.KEY_PASS);


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
        this.alMemos = databaseAccess.getAllMemos(username);
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
        } else {
            this.finish();
        }
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
        if (id == R.id.action_logOut) {
            // Clear the session data
            // This will clear all session data and
            // redirect user to LoginActivity
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Logout");
            alertDialog.setMessage(username+":   Are You sure you want to Log out :)");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();
                            finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else  if (id == R.id.action_profile) {
            // for checking profile
            AlertDialog palertDialog = new AlertDialog.Builder(this).create();
            palertDialog.setTitle("Profile");
            palertDialog.setMessage("Username:   "+username+" "+ "\n\nPassword:  "+userPass);
            palertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                        }
                    });
            palertDialog.show();

            // if you want to check database then uncomment this code and comment above code
            /*Intent i = new Intent(MainActivity.this,AndroidDatabaseManager.class);
            startActivity(i);*/
        }
        return super.onOptionsItemSelected(item);
    }
}
