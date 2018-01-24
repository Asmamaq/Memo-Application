package com.masst.memo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.masst.memo.Activities.MainActivity;
import com.masst.memo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText edName, edPass;
    Button btsave;
    Context context;

    Snackbar sb;
    public View snackview;
    SharedPreferences preferences;
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

        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user= edName.getText().toString();
                String pass= edPass.getText().toString();

               preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String userDetails = preferences.getString(user + pass + "data","");
            if(!TextUtils.isEmpty(userDetails)){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("display", userDetails);
                editor.commit();

                Intent i = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                startActivity(i);
            }
            else
            {
                sb = Snackbar.make(snackview,"Your Login Credentials not working. Please Register yourself:)",Snackbar.LENGTH_INDEFINITE);
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
