package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText psd;
    //public ArrayList<String> originalPassword = new ArrayList<>();
    public static String originalPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("NEW_PASS", MODE_PRIVATE);
        String new_passs = pref.getString("new_pass", "N/A");
        if(new_passs.equals("N/A"))
        {
            originalPassword="123366";
        }
        else
        {
            originalPassword=decode(Base64.getDecoder().decode(new_passs),"test");
        }
        psd=(EditText) findViewById(R.id.psd);
        btn=(Button) findViewById(R.id.btn);

        btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(psd.getText().toString().equals(originalPassword))
                {
                    //Toast.makeText(getApplicationContext(),"Succsess login",Toast.LENGTH_LONG).show();
                    Intent myintent = new Intent(MainActivity.this,LoggedActivity.class);
                    startActivity(myintent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No login",Toast.LENGTH_LONG).show();

                }
            }
        }));
    }
    public static String getter()
    {
        //return originalPassword;
        int length =32;
        String letter = "G";
        String adding="";
        if(originalPassword.length()<=length)
        {
            int how_much=length-originalPassword.length();
            for(int i=0;i<how_much;i++)
            {
                //originalPassword=originalPassword+adding;
                adding=adding.concat(letter);
            }
            return originalPassword+adding;
        }
        return "";
    }
    public static void setter(String new_code)
    {
        originalPassword=new_code;
    }





    public static String decode(byte[] pText, String pKey) {
        byte[] res = new byte[pText.length];
        byte[] key = pKey.getBytes();

        for (int i = 0; i < pText.length; i++) {
            res[i] = (byte) (pText[i] ^ key[i % key.length]);
        }

        return new String(res);
    }




}