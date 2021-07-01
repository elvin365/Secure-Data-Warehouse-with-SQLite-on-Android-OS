package com.example.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.os.IResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.NoSuchPaddingException;

public class LoggedActivity extends AppCompatActivity {
    static String key;
    private MainActivity pass;
    String Resourse,Login,password,notes;
    String delete_from_database;
   static  SQLiteDatabase database;
    final AES aes=new AES();


    public  DBHelper dbHelper;

    Button add_info;
    Button del_button;
    Button change_button;
    Button change_pass;
    Button export_button;
    Button import_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
         key=MainActivity.getter();//получаем код-пароль

        dbHelper = new DBHelper(this);
        database= dbHelper.getWritableDatabase();







        add_info=(Button) findViewById(R.id.add_info);
        add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                //Toast.makeText(getApplicationContext(),"Adding in DataBase",Toast.LENGTH_LONG).show();
                Intent myintent = new Intent(LoggedActivity.this,AddActivity.class);
                //myintent.putExtra("EXTRA_DATA_BASE", (Parcelable) dbHelper);
                startActivityForResult(myintent,1);
            }
        });


        del_button=(Button)findViewById(R.id.del_button);
        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"Deleteing from DataBase",Toast.LENGTH_LONG).show();
                Intent myintent = new Intent(LoggedActivity.this,DeleteActivity.class);
                //myintent.putExtra("EXTRA_DATA_BASE", (Parcelable) dbHelper);
                startActivityForResult(myintent,2);

            }
        });

        change_button=(Button)findViewById(R.id.change_button);
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getApplicationContext(),"Changing or lookng in DataBase",Toast.LENGTH_LONG).show();
                Intent myintent = new Intent(LoggedActivity.this,ChangeActivity.class);
                //myintent.putExtra("EXTRA_DATA_BASE", (Parcelable) dbHelper);
                startActivityForResult(myintent,3);

            }
        });
        change_pass=(Button)findViewById(R.id.button2);
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Changing key-logon password",Toast.LENGTH_LONG).show();
                Intent myintent = new Intent(LoggedActivity.this,ChangePassActivity.class);
                //myintent.putExtra("EXTRA_DATA_BASE", (Parcelable) dbHelper);
                startActivityForResult(myintent,4);
            }
        });
        export_button=(Button)findViewById(R.id.ex_button);
        export_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Cursor cursor2 = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);


                if (cursor2.moveToFirst())
                {
                    int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
                    int resourceIndex = cursor2.getColumnIndex(DBHelper.KEY_RESOURCE);
                    int loginIndex = cursor2.getColumnIndex(DBHelper.KEY_LOGIN);
                    int passIndex=cursor2.getColumnIndex(DBHelper.KEY_PASSWORD);
                    int notesIndex=cursor2.getColumnIndex(DBHelper.KEY_NOTES);
                    int id=0;
                    /*SharedPreferences sPref = getSharedPreferences("EXPORT_DB", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();*/
                    final String LOG_TAG = "myLogs";

                    final String FILENAME = "file";

                    final String DIR_SD = "MyFiles";
                    final String FILENAME_SD = "fileSD";
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED))
                    {
                        Toast.makeText(getApplicationContext(),"CD-card not valid",Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
                        return;
                    }
                    // получаем путь к SD
                    File sdPath = Environment.getExternalStorageDirectory();
                    // добавляем свой каталог к пути
                    sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
                    // создаем каталог
                    sdPath.mkdirs();
                    // формируем объект File, который содержит путь к файлу
                    File sdFile = new File(sdPath, FILENAME_SD);
                    BufferedWriter bw = null;
                    try {
                        bw = new BufferedWriter(new FileWriter(sdFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    do {
                       /* id=cursor2.getInt(idIndex);
                       ed.putInt("export_id"+String.valueOf(id),cursor2.getInt(idIndex));
                       ed.putString("export_resource"+String.valueOf(id),cursor2.getString(resourceIndex));
                       ed.putString("export_login"+String.valueOf(id),cursor2.getString(loginIndex));
                       ed.putString("export_pass"+String.valueOf(id),cursor2.getString(passIndex));
                       ed.putString("export_notes"+String.valueOf(id),cursor2.getString(notesIndex));
                        ed.commit();*/

                        try {
                            // открываем поток для записи

                            // пишем данные
                            bw.write("1 "+cursor2.getString(resourceIndex));
                            bw.write("\n");
                            bw.write("2 "+cursor2.getString(loginIndex));
                            bw.write("\n");
                            bw.write("3 "+cursor2.getString(passIndex));
                            bw.write("\n");
                            bw.write("4 "+cursor2.getString(notesIndex));
                            bw.write("\n");


                            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } while (cursor2.moveToNext());
                    // закрываем поток
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"DB exported successfully",Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(getApplicationContext(),"DB is empty",Toast.LENGTH_LONG).show();

                }


                cursor2.close();



            }
        });

        import_button=(Button)findViewById(R.id.im_button);
        import_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = dbHelper.getWritableDatabase();
                ContentValues contentValues =new ContentValues();

                final String LOG_TAG = "myLogs";
                final String FILENAME = "file";
                final String DIR_SD = "MyFiles";
                final String FILENAME_SD = "fileSD";
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
                    return;
                }
                // получаем путь к SD
                File sdPath = Environment.getExternalStorageDirectory();
                // добавляем свой каталог к пути
                sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
                // формируем объект File, который содержит путь к файлу
                File sdFile = new File(sdPath, FILENAME_SD);
                try
                {
                    // открываем поток для чтения
                    BufferedReader br = new BufferedReader(new FileReader(sdFile));
                    String str = "";
                    int id=0;
                    String id_str="";
                    // читаем содержимое
                    while ((str = br.readLine()) != null)
                    {
                        id_str=str.substring(0,1);
                        id=Integer.parseInt((id_str));

                        if(id==1)
                        {
                            str=str.substring(2);
                            contentValues.put(DBHelper.KEY_RESOURCE, str);
                        }
                        if(id==2)
                        {
                            str=str.substring(2);
                            contentValues.put(DBHelper.KEY_LOGIN, str);
                        }
                        if(id==3)
                        {
                            str=str.substring(2);
                            contentValues.put(DBHelper.KEY_PASSWORD, str);
                        }
                        if(id==4)
                        {
                            str=str.substring(2);
                            contentValues.put(DBHelper.KEY_NOTES, str);
                            database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                            Toast.makeText(getApplicationContext(),"Succsesfully imported",Toast.LENGTH_LONG).show();

                        }

                    }
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }



            }
        });








    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // here AcivityB is finished. Call your method



        super.onActivityResult(requestCode, resultCode, data);

         database = dbHelper.getWritableDatabase();

        ContentValues contentValues =new ContentValues();


        if(requestCode==1)
        {
            // meaning that it is add activity ended

            SharedPreferences pref = getSharedPreferences("SAVED_TEXT", MODE_PRIVATE);
            Resourse = pref.getString("resource", "N/A");
            Login = pref.getString("login", "N/A");
            password = pref.getString("password", "N/A");
            notes = pref.getString("notes", "N/A");


            contentValues.put(DBHelper.KEY_RESOURCE, Resourse);
            contentValues.put(DBHelper.KEY_LOGIN, Login);
            contentValues.put(DBHelper.KEY_PASSWORD, password);
            contentValues.put(DBHelper.KEY_NOTES, notes);

            database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);




            pref.edit().clear().commit();



        }
        Cursor cursor2 = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor2.moveToFirst()) {
            int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
            int resourceIndex = cursor2.getColumnIndex(DBHelper.KEY_RESOURCE);
            int loginIndex = cursor2.getColumnIndex(DBHelper.KEY_LOGIN);
            do {
                Log.d("mLog", "ID = " + cursor2.getInt(idIndex) +
                        ", resource name = " + cursor2.getString(resourceIndex) +
                        ", login = " + cursor2.getString(loginIndex));
            } while (cursor2.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor2.close();
        if(requestCode==2)
        {
            SharedPreferences pref = getSharedPreferences("DEL_TEXT", MODE_PRIVATE);
            delete_from_database = pref.getString("resource_deletable", "N/A");
            int id_del=0;


            Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int resourceIndex = cursor.getColumnIndex(DBHelper.KEY_RESOURCE);
                do {
                    String decrypted=decrypt(cursor.getString(resourceIndex));
                    if(decrypted.equals(delete_from_database))
                    {
                        id_del=cursor.getInt(idIndex);
                        break;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();



            int delCount = database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "=" + id_del, null);

            Log.d("mLog", "deleted rows count = " + delCount);



        }



        Cursor cursor3 = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor3.moveToFirst()) {
            int idIndex = cursor3.getColumnIndex(DBHelper.KEY_ID);
            int resourceIndex = cursor3.getColumnIndex(DBHelper.KEY_RESOURCE);
            int loginIndex = cursor3.getColumnIndex(DBHelper.KEY_LOGIN);
            do {
                Log.d("mLog", "ID = " + cursor3.getInt(idIndex) +
                        ", resource name = " + cursor3.getString(resourceIndex) +
                        ", login = " + cursor3.getString(loginIndex));
            } while (cursor3.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor3.close();
        if(requestCode==3)
        {
            SharedPreferences pref = getSharedPreferences("CHANGE_TEXT", MODE_PRIVATE);
            int id=pref.getInt("identificator",0);
            Resourse=pref.getString("resource_change","N/A");
            Login = pref.getString("login_change", "N/A");
            password = pref.getString("password_change", "N/A");
            notes = pref.getString("notes_change", "N/A");
            contentValues.put(DBHelper.KEY_RESOURCE, Resourse);
            contentValues.put(DBHelper.KEY_LOGIN, Login);
            contentValues.put(DBHelper.KEY_PASSWORD, password);
            contentValues.put(DBHelper.KEY_NOTES, notes);
            int updCount = database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "= ?", new String[] {String.valueOf(id)});

        }




        Cursor cursor4 = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor4.moveToFirst()) {
            int idIndex = cursor4.getColumnIndex(DBHelper.KEY_ID);
            int resourceIndex = cursor4.getColumnIndex(DBHelper.KEY_RESOURCE);
            int loginIndex = cursor4.getColumnIndex(DBHelper.KEY_LOGIN);
            do {
                Log.d("mLog", "ID = " + cursor4.getInt(idIndex) +
                        ", resource name = " + cursor4.getString(resourceIndex) +
                        ", login = " + cursor4.getString(loginIndex));
            } while (cursor4.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor4.close();


        if(requestCode==4)
        {
            SharedPreferences pref = getSharedPreferences("SAVED_PASSWORD", MODE_PRIVATE);
            String new_key;
            new_key = pref.getString("new_password", "N/A");


            Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                String resource;
                String login;
                String pass;
                String notes;
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int resourceIndex = cursor.getColumnIndex(DBHelper.KEY_RESOURCE);
                int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                int passIndex= cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                int notesIndex= cursor.getColumnIndex(DBHelper.KEY_NOTES);
                int id=0;
                String old_key=MainActivity.getter();
                do {
                    MainActivity.setter(old_key);
                    id=cursor.getInt(idIndex);
                    resource=decrypt(cursor.getString(resourceIndex));
                    login=decrypt(cursor.getString(loginIndex));
                    pass=decrypt(cursor.getString(passIndex));
                    notes=decrypt(cursor.getString(notesIndex));

                    SharedPreferences sPref = getSharedPreferences("NEW_PASS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("new_pass", encode(new_key,"test"));
                    ed.commit();

                    MainActivity.setter(new_key);
                    resource=encrypt_string(resource);
                    login=encrypt_string(login);
                    pass=encrypt_string(pass);
                    notes=encrypt_string(notes);

                    contentValues.put(DBHelper.KEY_RESOURCE, resource);
                    contentValues.put(DBHelper.KEY_LOGIN, login);
                    contentValues.put(DBHelper.KEY_PASSWORD, pass);
                    contentValues.put(DBHelper.KEY_NOTES, notes);
                    int updCount = database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "= ?", new String[] {String.valueOf(id)});




                } while (cursor.moveToNext());
            } else
                Log.d("mLog","0 rows");

            cursor.close();







        }











    }

    static boolean ifDBHasThis(String resource)
    {
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int resourceIndex = cursor.getColumnIndex(DBHelper.KEY_RESOURCE);
            do {
                String decrypted=decrypt(cursor.getString(resourceIndex));
                if(decrypted.equals(resource))
                {
                    cursor.close();

                    return true;
                }
            } while (cursor.moveToNext());
        } else


        cursor.close();
        return false;
    }





    static ArrayList<String> GetInfoFromDB(String resource)
    {
        ArrayList<String> info=new ArrayList<>();

        Cursor cursor2 = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor2.moveToFirst()) {
            int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
            int resourceIndex = cursor2.getColumnIndex(DBHelper.KEY_RESOURCE);
            int loginIndex = cursor2.getColumnIndex(DBHelper.KEY_LOGIN);
            int passIndex = cursor2.getColumnIndex(DBHelper.KEY_PASSWORD);
            int notesIndex = cursor2.getColumnIndex(DBHelper.KEY_NOTES);
            do {
                String decrypted=decrypt(cursor2.getString(resourceIndex));
                if(decrypted.equals(resource))
                {
                    info.add(decrypt(cursor2.getString(resourceIndex)));
                    info.add(decrypt(cursor2.getString(loginIndex)));
                    info.add(cursor2.getString(passIndex));
                    info.add(decrypt(cursor2.getString(notesIndex)));
                    cursor2.close();
                    return info;
                }

            } while (cursor2.moveToNext());
        } else

        cursor2.close();
        return info;
    }


    static int GetIdFromDB(String resource)
    {
        int id=0;
        Cursor cursor2 = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor2.moveToFirst()) {
            int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
            int resourceIndex = cursor2.getColumnIndex(DBHelper.KEY_RESOURCE);
            do {
                String decrypted=decrypt(cursor2.getString(resourceIndex));
                if(decrypted.equals(resource))
                {
                    id=cursor2.getInt(idIndex);
                    cursor2.close();
                    return id;
                }

            } while (cursor2.moveToNext());
        } else

            cursor2.close();
        return id;

    }

    static String getter()
    {
        key=MainActivity.getter();//получаем код-пароль
        return key;
    }

    private static String decrypt(String arg)
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






    public static String encode(String pText, String pKey) {
        byte[] txt = pText.getBytes();
        byte[] key = pKey.getBytes();
        byte[] res = new byte[pText.length()];

        for (int i = 0; i < txt.length; i++) {
            res[i] = (byte) (txt[i] ^ key[i % key.length]);
        }

        return Base64.getEncoder().encodeToString(res);
    }






}