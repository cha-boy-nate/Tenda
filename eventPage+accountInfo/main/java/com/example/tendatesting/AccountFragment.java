package com.example.tendatesting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Account Information");
        View v = inflater.inflate(R.layout.fragment_account, container, false);


        FloatingActionButton button = v.findViewById(R.id.info_OK_Button);
        final EditText editText_name = (EditText) v.findViewById(R.id.info_name);
        final EditText editText_email = (EditText) v.findViewById(R.id.info_email);
        final EditText editText_password = (EditText) v.findViewById(R.id.info_password);
        final EditText editText_confirmPassword = (EditText) v.findViewById(R.id.info_confirmPassword);
        final EditText editText_contactNumber = (EditText) v.findViewById(R.id.info_contactNumber);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String password = "";
                password = editText_password.getText().toString();
                //将文本框1的文本赋给文本框2
                editText_confirmPassword.setText(password.toCharArray(), 0, password.length());
            }
        });


        return v;
    }
}
