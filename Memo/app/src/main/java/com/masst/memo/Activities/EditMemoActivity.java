package com.masst.memo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.masst.memo.Database.DatabaseAccess;
import com.masst.memo.Models.Memo;
import com.masst.memo.R;

public class EditMemoActivity extends AppCompatActivity {

    private EditText etText,etTitle;
    private Button btnSave;
    private Button btnCancel;
    private Memo memo;
    String text,date,title;
    int id;
    long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        this.etText = (EditText) findViewById(R.id.etText);
        this.etTitle=(EditText) findViewById(R.id.etTitle);
        this.btnSave = (Button) findViewById(R.id.btnSave);
        this.btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
           text = (String) bundle.get("memo_text");
           title = (String) bundle.get("memo_title");
           id=Integer.valueOf((String) bundle.get("memo_id"));
          /* date =  (String) bundle.get("memo_date");
            DateFormat df = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");
            Date startDate = null;
            try {
                startDate = df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time=startDate.getTime();*/

            if(text != null) {
                this.etText.setText(text);
                int position = etText.length();
                Editable etext = etText.getText();
                Selection.setSelection(etext, position);
            }
            if(title!= null)
            {
                this.etTitle.setText(title);
                int position = etTitle.length();
                Editable etext = etTitle.getText();
                Selection.setSelection(etext, position);
            }
        }

        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
    }

    public void onSaveClicked() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        if(text == null){
            // Add new memo
            Memo temp = new Memo();
            temp.setText(etText.getText().toString());
            temp.setTitle(etTitle.getText().toString());
            databaseAccess.save(temp);
            onBackPressed();
        }
        else {
            // Update the memo
            memo=new Memo();
            memo.setText(etText.getText().toString());
            memo.setTitle(etTitle.getText().toString());
            memo.setId(id);
            databaseAccess.update(memo);
        }
        databaseAccess.close();
        Intent i = new Intent(EditMemoActivity.this,MainActivity.class);
        startActivity(i);
        this.finish();
    }
    public void onCancelClicked() {
        this.finish();
    }
}

