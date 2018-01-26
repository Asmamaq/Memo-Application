package com.masst.memo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.masst.memo.Database.DatabaseAccess;
import com.masst.memo.Models.Memo;
import com.masst.memo.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewActivity extends AppCompatActivity {

    TextView tvText,tvDate,tvTitle;
    RelativeLayout RView;
    String text,title;
    String date;
    long time;
    int id;
    public Memo vmemo;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        tvTitle = (TextView) findViewById(R.id.tvtitle);
        tvText = (TextView) findViewById(R.id.tvtext);
        tvDate = (TextView) findViewById(R.id.tvdate);
        RView = (RelativeLayout) findViewById(R.id.relativelayoutview);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            title = (String) bundle.get("memo_title");
            text = (String)  bundle.get("memo_text");
            date = (String)  bundle.get("memo_date");
            id=Integer.valueOf((String) bundle.get("memo_id"));

            DateFormat df = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");
            Date startDate = null;
            try {
                startDate = df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time=startDate.getTime();

            if(text != null) {
                this.tvText.setText(text);
                this.tvTitle.setText(title);
                this.tvDate.setText("Last modified: "+date);
            }
        }
        else
        {
            this.tvText.setText("");
            this.tvDate.setText("");
        }

        RView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewActivity.this, EditMemoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("memo_text",tvText.getText().toString());
                bundle.putString("memo_date",date);
                bundle.putString("memo_title",tvTitle.getText().toString());
                bundle.putString("memo_id", String.valueOf(id));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemid = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (itemid == R.id.action_Delete) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            vmemo = new Memo(id,title,time,text);
            databaseAccess.delete(vmemo);
            databaseAccess.close();
            onBackPressed();
            return true;
        }
        else if(itemid == R.id.menu_item_share)
        {
            String body = tvTitle.getText().toString()+" : " +tvText.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(Intent.createChooser(sendIntent,"Share via" ));
        }
        else if(itemid == R.id.menu_item_weather)
        {
            Intent i = new Intent(ViewActivity.this,weatherActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
