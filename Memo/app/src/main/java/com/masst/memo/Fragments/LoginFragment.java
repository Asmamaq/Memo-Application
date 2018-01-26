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

import com.masst.memo.Activities.MainActivity;
import com.masst.memo.Models.AlertDialogManager;
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

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

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

        btsave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String user= edName.getText().toString();
                String pass= edPass.getText().toString();

                // Session Manager
                session = new SessionManagment(LoginFragment.this.getActivity());
               /* if(!session.isLoggedIn())
                {
                    alert.showAlertDialog(LoginFragment.this.getActivity(), "Login Required","Please Login for creating Memos", false);
                }*/

                preferences = LoginFragment.this.getActivity().getSharedPreferences("MyPref", 0);
                String username = preferences.getString("user","");
                String password= preferences.getString("password","");

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(pass)) {
                    edPass.setError(getString(R.string.error_field_required));
                }
                if(TextUtils.isEmpty(user)) {
                    edName.setError(getString(R.string.error_field_required));
                }
                if(user.equals(username) && pass.equals(password) ){
                    // Creating user login session
                    session.createLoginSession(username, password);
                    Intent i = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                    startActivity(i);
                 }
                else
                {
                    alert.showAlertDialog(LoginFragment.this.getActivity(), "Login failed..", "Username/Password is incorrect", false);
                }
            }
        });
        return v;
    }
}
