package com.masst.memo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.masst.memo.Activities.MainActivity;
import com.masst.memo.Models.SessionManagment;
import com.masst.memo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText edName, edPass;
    Button btsave;
    Context context;

    // Session Manager Class
    SessionManagment session;
    SharedPreferences preferences;

    // for snackbar
    Snackbar sb;
    public View snackview;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.login, container, false);
        context=v.getContext();


        edName = (EditText) v.findViewById(R.id.edloginName);
        edPass = (EditText) v.findViewById(R.id.edloginPass);
        btsave = (Button)   v.findViewById(R.id.btLogin);
        snackview= v.findViewById(android.R.id.content);

        btsave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String user= edName.getText().toString();
                String pass= edPass.getText().toString();

                // Session Manager
                session = new SessionManagment(LoginFragment.this.getActivity());

                preferences = LoginFragment.this.getActivity().getSharedPreferences("MyPref", 0);
                String username = preferences.getString("user","");
                String password= preferences.getString("password","");

                Toast.makeText(LoginFragment.this.getActivity(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(pass)) {
                    edPass.setError(getString(R.string.error_field_required));
                    return;
                }
                if(!TextUtils.isEmpty(user)) {
                    edName.setError(getString(R.string.error_field_required));
                    return;
                }
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) ){
                    // Creating user login session
                    // For testing i am stroing name, email as follow
                    // Use user real data
                    session.createLoginSession(username, password);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user",username);
                    editor.putString("password",password);
                    editor.commit();

                    Intent i = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                    startActivity(i);
                 }
                else
                {
                    sb = Snackbar.make(snackview,"You have No Account! Please Register yourself :)",Snackbar.LENGTH_INDEFINITE);
                    sb.setAction("Exit", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sb.dismiss();
                        }
                    });
                }
            }
        });
        return v;
    }
}
