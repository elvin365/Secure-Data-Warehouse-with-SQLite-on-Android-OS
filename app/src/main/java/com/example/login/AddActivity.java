package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Parcelable;
import javax.crypto.SecretKey;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;


public class AddActivity extends AppCompatActivity {
    EditText Resource_name,Login,Pass,Notes;
    Button ok;
    String Res,Log_in,secret,notes;
    //byte[] a;
    final AES aes=new AES();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //final AES aes=new AES();
        Resource_name =(EditText) findViewById(R.id.Resource);
        Login=(EditText)findViewById(R.id.editTextTextPersonName10);
        Pass=(EditText)findViewById(R.id.editTextTextPassword2);
        Notes=(EditText)findViewById(R.id.editTextTextPersonName11);
        ok=(Button)findViewById(R.id.buttonok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Res=Resource_name.getText().toString();
                //Res=encrypt_string(Res);
                if(LoggedActivity.ifDBHasThis(Res))
                {
                    Toast.makeText(getApplicationContext(),"This name already exists, choose anotherone",Toast.LENGTH_LONG).show();
                    //ok.setEnabled(false);
                    return;
                }
                else
                {
                    ok.setEnabled(true);
                }
                Log_in=Login.getText().toString();
                if(Log_in.isEmpty())
                {
                    Log_in="Left_blank";
                }
                secret=Pass.getText().toString();
                notes=Notes.getText().toString();
                //

                //String temp=encrypt_string(Res);
                //String temp1=encrypt_string(Res);
                //
                SharedPreferences sPref = getSharedPreferences("SAVED_TEXT", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("resource", encrypt_string(Res));
                ed.putString("login", encrypt_string(Log_in));
                ed.putString("password", encrypt_string(secret));
                ed.putString("notes", encrypt_string(notes));

                ed.commit();



                Intent intent= new Intent();
                intent.putExtra("result",1);
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














    /*public void encrypt(String arg)
    {

        try {

            a=aes.encrypt(Res,"123366sghparoigharipjeoighvjoiER","CFB");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String s = Base64.getEncoder().encodeToString(a);
        byte[] decode = Base64.getDecoder().decode(s);

        byte[] iv = new byte[16];
        byte[] encoded=null;
        try {
            encoded=read(a,"CFB",iv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String test= aes.decrypt(encoded,"123366sghparoigharipjeoighvjoiER","CFB",iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


    }



    private static byte[] read(byte[] encode, String mode, byte[] iv) throws IOException {
        byte[] encoded = encode;
        if ("CFB".equals(mode)) {
            int y = 0;
            for (int i = encoded.length - 16; y < 16; i++) {
                iv[y] = encoded[i];
                y++;
            }

            byte[] encoded_shrinked = new byte[encoded.length-16];
            System.arraycopy(encoded, 0, encoded_shrinked, 0, encoded.length-16);

            return encoded_shrinked;
        }
        return encoded;
    }*/



}