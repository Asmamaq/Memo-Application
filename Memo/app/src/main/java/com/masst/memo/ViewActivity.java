package com.masst.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    TextView tvText,tvDate;
    RelativeLayout RView;
    String text,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        tvText = (TextView) findViewById(R.id.tvtext);
        tvDate = (TextView) findViewById(R.id.tvdate);
        RView = (RelativeLayout) findViewById(R.id.relativelayoutview);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            text = (String)  bundle.get("memo_text");
            date = (String)  bundle.get("memo_date");
            if(text != null) {
                this.tvText.setText(text);
                this.tvDate.setText("Last modified: "+date);
            }
        }
        else
        {
            this.tvText.setText("");
            this.tvDate.setText("");
        }

       // tvText.setText("hiiiiiiiiiiii");
       // tvDate.setText("last modified"+" 19 jan 2017");
        RView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewActivity.this, EditMemoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("memo_text",tvText.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



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
        if (id == R.id.action_Delete) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
