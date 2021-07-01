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

public class ChangePassActivity extends AppCompatActivity {

    Button accept;
    EditText new_key;
    String new_key_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        accept=(Button)findViewById(R.id.accept_button);
        new_key=(EditText)findViewById(R.id.editTextTextPersonName8);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new_key_string=new_key.getText().toString();
                if(new_key_string.length()>=32)
                {
                    Toast.makeText(getApplicationContext(),"Too long pass, choose shorter one",Toast.LENGTH_LONG).show();
                    return;
                }
                //MainActivity.setter(new_key_string);

                SharedPreferences sPref = getSharedPreferences("SAVED_PASSWORD", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("new_password",new_key_string);
                ed.commit();

                Intent intent= new Intent();
                intent.putExtra("result",4);
                setResult(RESULT_OK,intent);
                finish();



            }
        });






    }
}