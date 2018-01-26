package com.masst.memo.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.masst.memo.Fragments.LoginFragment;
import com.masst.memo.Fragments.RegisterFragment;
import com.masst.memo.Globals;
import com.masst.memo.R;

public class LoginRegisterActivity extends AppCompatActivity {

    Button btlogin, btRegister;
    FragmentManager manager;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);
        manager = getSupportFragmentManager();

        btlogin = (Button) findViewById(R.id.btfLogin);
        btRegister = (Button) findViewById(R.id.btfRegister);
        LoginFragment loginFragment= new LoginFragment();
        manager.beginTransaction().replace(R.id.relative_layout_for_fragment,loginFragment,loginFragment.getTag()).commit();

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment= new LoginFragment();
                manager.beginTransaction().replace(R.id.relative_layout_for_fragment,loginFragment,loginFragment.getTag()).commit();
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                manager.beginTransaction().replace(R.id.relative_layout_for_fragment, registerFragment, registerFragment.getTag()).commit();
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginRegisterActivity.this).create();
        alertDialog.setTitle("Exit");
        alertDialog.setMessage("Are You sure you want to Exit");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        Globals.Exit_Code="true";
                        dialog.dismiss();
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

}
