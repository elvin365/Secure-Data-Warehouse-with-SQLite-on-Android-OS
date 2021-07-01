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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.NoSuchPaddingException;

public class DeleteActivity extends AppCompatActivity {
    EditText Resourse;
    Button ok_del;
    String res;
    final AES aes=new AES();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        Resourse=(EditText)findViewById(R.id.editTextTextPersonName);
        ok_del=(Button) findViewById(R.id.ok_button_del);

        ok_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res=Resourse.getText().toString();
                if(!LoggedActivity.ifDBHasThis(res))
                {
                    Toast.makeText(getApplicationContext(),"Nothing to delete in DATA_BASE",Toast.LENGTH_LONG).show();
                    //ok.setEnabled(false);
                    return;
                }


                SharedPreferences sPref = getSharedPreferences("DEL_TEXT", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                //res=encrypt_string(res);
                ed.putString("resource_deletable", res);

                ed.commit();



                Intent intent= new Intent();
                intent.putExtra("result",2);
                setResult(RESULT_OK,intent);
                finish();

            }
        });




    }



    private String encrypt_string(String arg)
    {
        byte[] a=null;
        String key=MainActivity.getter();// получаем свежий ключ
        try {
            a=aes.encrypt(arg,key,"CFB");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(Base64.getEncoder().encodeToString(a));
        return (Base64.getEncoder().encodeToString(a));




    }













}