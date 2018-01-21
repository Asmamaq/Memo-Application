package com.masst.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditMemoActivity extends AppCompatActivity {

    private EditText etText;
    private Button btnSave;
    private Button btnCancel;
    private Memo memo;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        this.etText = (EditText) findViewById(R.id.etText);
        this.btnSave = (Button) findViewById(R.id.btnSave);
        this.btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
           text = (String) bundle.get("memo_text");
            if(text != null) {
                this.etText.setText(text);
                int position = etText.length();
                Editable etext = etText.getText();
                Selection.setSelection(etext, position);
            }
        }
        else
        {
            this.etText.setText("");
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
            databaseAccess.save(temp);
        }
        else {
            // Update the memo
            memo=new Memo();
            memo.setText(etText.getText().toString());
            databaseAccess.update(memo);
        }
        databaseAccess.close();
        /*Intent intent = new Intent();
        intent.putExtra("key", etText.getText().toString());
        setResult(200, intent);*/
        Intent intent = new Intent(EditMemoActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onCancelClicked() {
        this.finish();
    }
}

