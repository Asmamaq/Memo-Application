package com.masst.memo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.masst.memo.Activities.LoginRegisterActivity;
import com.masst.memo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    EditText edrName, edrPass ,edremail;
    Button btreg;
    Context context;
    Snackbar sb;
    public View snackview;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.register, container, false);

        context=v.getContext();
        edrName = (EditText) v.findViewById(R.id.edName);
        edrPass = (EditText) v.findViewById(R.id.edPass);
        edremail = (EditText) v.findViewById(R.id.edEmail);
        btreg =     (Button)   v.findViewById(R.id.btRegister);
        snackview= v.findViewById(android.R.id.content);

        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String newUser = edrName.getText().toString();
                String newPass = edrPass.getText().toString();
                String newEmail = edremail.getText().toString();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(newUser + newPass + "data",newUser + "\n" +newEmail);
                editor.commit();
                Toast.makeText(RegisterFragment.this.getActivity(),"Successfully Registered",Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegisterFragment.this.getActivity(), LoginRegisterActivity.class);
                startActivity(i);
            }
        });


        return v;
    }

}
