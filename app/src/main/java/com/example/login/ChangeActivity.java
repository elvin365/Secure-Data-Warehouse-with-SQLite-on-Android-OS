package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.NoSuchPaddingException;

public class ChangeActivity extends AppCompatActivity {


    EditText search;
    String search_text;
    Button look;
    final AES aes=new AES();

    //info about element from database
    ArrayList<String> arrayList = new ArrayList<>();
    String resource_name,login,password,notes;
    String resource_name_copy;
    int id;


    EditText resource_string,login_string,en_password_string,de_password_string,notes_string;
    Button end_button1;
    Button decryprt;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        search=(EditText)findViewById(R.id.editTextTextPersonName2);
        look=(Button)findViewById(R.id.look_button);


        resource_string=(EditText)findViewById(R.id.editTextTextPersonName7);
        login_string=(EditText)findViewById(R.id.editTextTextPersonName3);
        en_password_string=(EditText)findViewById(R.id.editTextTextPersonName4);
        de_password_string=(EditText)findViewById(R.id.editTextTextPersonName5);
        notes_string=(EditText)findViewById(R.id.editTextTextPersonName6);
        decryprt=(Button) findViewById(R.id.decrypt_button);
        end_button1=(Button) findViewById(R.id.end_button);
        end_button1.setEnabled(false);
        decryprt.setEnabled(false);
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_text=search.getText().toString();
                if(!LoggedActivity.ifDBHasThis(search_text))
                {
                    Toast.makeText(getApplicationContext(),"There is no such resource name",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                   arrayList= LoggedActivity.GetInfoFromDB(search_text);
                    id=LoggedActivity.GetIdFromDB(search_text);
                    resource_name =arrayList.get(0);
                    resource_name_copy=resource_name;
                    login=arrayList.get(1);
                    password=arrayList.get(2);
                    notes=arrayList.get(3);

                    resource_string.setText(resource_name);
                    login_string.setText(login);
                    en_password_string.setText(password);
                    notes_string.setText(notes);
                    //end_button1.setEnabled(true);
                    decryprt.setEnabled(true);
                    de_password_string.setText("");

                }

            }
        });
        decryprt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // расшифрование
                password=decrypt1(password);
                de_password_string.setText(password);
                decryprt.setEnabled(false);
                end_button1.setEnabled(true);

            }
        });


        end_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //теперь проверяем , то что набрали в строчке(могли и не менять)                 Res=Resource_name.getText().toString();
                resource_name=resource_string.getText().toString();
                login=login_string.getText().toString();
                password=de_password_string.getText().toString();
                notes=notes_string.getText().toString();
                if(LoggedActivity.ifDBHasThis(resource_string.getText().toString()) && !resource_name.equals(resource_name_copy))
                {
                    Toast.makeText(getApplicationContext(),"This name already exists, choose anotherone",Toast.LENGTH_LONG).show();
                    //ok.setEnabled(false);
                    return;
                }
                else
                {
                    SharedPreferences sPref = getSharedPreferences("CHANGE_TEXT", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("identificator",id);
                    ed.putString("resource_change",encrypt_string( resource_name));
                    ed.putString("login_change",encrypt_string( login));
                    ed.putString("password_change", encrypt_string(password));
                    ed.putString("notes_change", encrypt_string(notes));

                    ed.commit();



                    Intent intent= new Intent();
                    intent.putExtra("result",3);
                    setResult(RESULT_OK,intent);
                    finish();

                }

            }
        });





    }




    public static String decrypt1(String arg)
    {
        byte[] encode = Base64.getDecoder().decode(arg);//
        byte[] iv = new byte[16];
        byte[] encoded=null;
        String test="";
        try {
            encoded=read(encode,"CFB",iv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final AES aes=new AES();
        try {
            test= aes.decrypt(encoded,MainActivity.getter(),"CFB",iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return test;


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