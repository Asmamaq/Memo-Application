package com.masst.memo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.masst.memo.Activities.LoginRegisterActivity;
import com.masst.memo.Models.AlertDialogManager;
import com.masst.memo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    EditText edrName, edrPass ,edremail;
    Button btreg;
    Context context;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
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

        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = RegisterFragment.this.getActivity().getSharedPreferences("MyPref", 0);
                String newUser = edrName.getText().toString();
                String newPass = edrPass.getText().toString();
                String newEmail = edremail.getText().toString();

                // Check for a Valid Username
                if (TextUtils.isEmpty(newUser))
                {
                    edrName.setError(getString(R.string.error_field_required));
                    return;
                }
                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(newPass))
                {
                    edrPass.setError(getString(R.string.error_field_required));
                    return;
                }
                else if(!isPasswordValid(newPass))
                {
                    edrPass.setError(getString(R.string.error_invalid_password));
                    return;
                }
                // Check for a valid email address.
                if (TextUtils.isEmpty(newEmail))
                {
                    edremail.setError(getString(R.string.error_field_required));
                    return;
                }
                else if (!isEmailValid(newEmail))
                {
                    edremail.setError(getString(R.string.error_invalid_email));
                    return;
                }

                String uname = preferences.getString("user","");
                if(uname.equals(newUser))
                {
                    alert.showAlertDialog(RegisterFragment.this.getActivity(), "Registeration Failed", "Username is already used", false);
                    return;
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user",newUser);
                editor.putString("password",newPass);
                editor.putString("email",newEmail);
                Boolean flag = editor.commit();

                if(flag)
                {
                    Toast.makeText(RegisterFragment.this.getActivity(), "Successfully Registered", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterFragment.this.getActivity(), LoginRegisterActivity.class);
                    startActivity(i);
               }
               else
               {
                   Toast.makeText(RegisterFragment.this.getActivity(), "Please Try again,You are not registered ", Toast.LENGTH_LONG).show();
               }
            }
        });


        return v;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
