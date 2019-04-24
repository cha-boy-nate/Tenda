package com.example.tendatesting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ReportFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getActivity().setTitle("Report");
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        Button button1 = (Button) v.findViewById(R.id.report_button1);
        Button button2 = (Button) v.findViewById(R.id.report_button2);
        final EditText editText1 = (EditText) v.findViewById(R.id.report_editText1);
        final EditText editText2 = (EditText) v.findViewById(R.id.report_editText2);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String str = "";
                str = editText1.getText().toString();
                //将文本框1的文本赋给文本框2
                editText2.setText(str.toCharArray(), 0, str.length());
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                str = editText2.getText().toString();
                //将文本框2的文本赋给文本框1
                editText1.setText(str.toCharArray(), 0, str.length());
            }
        });

        return v;
    }


}
