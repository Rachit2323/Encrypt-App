package com.example.encrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Locale;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class DES extends AppCompatActivity {


    EditText input_text, password_text;
    TextView output_text;
    Button enc, dec,clear;
    String outputstring;

    //alll the stuff for the key generation
    private static final String UNICODE_FORMAT="UTF8";
    public static final String DES_ENCRYPTION_SCHEME="DES";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] KeyAsBytes;
    private String myEncryptionKey;
    private String getMyEncryptionScheme;
    SecretKey key;
    String myEncKey="rachit atrey";


    String ans="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);

        input_text = (EditText) findViewById(R.id.input_text);
        //  password_text = (EditText) findViewById(R.id.password_text);
        output_text = (TextView) findViewById(R.id.output_text);
        enc = (Button) findViewById(R.id.encrypt);
        dec = (Button) findViewById(R.id.decrypt);
        clear = (Button) findViewById(R.id.clear_button);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialising key generation part
        myEncryptionKey=myEncKey;
        getMyEncryptionScheme=DES_ENCRYPTION_SCHEME;
        try {
            KeyAsBytes=myEncryptionKey.getBytes(UNICODE_FORMAT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            myKeySpec=new DESKeySpec(KeyAsBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            mySecretKeyFactory=SecretKeyFactory.getInstance(getMyEncryptionScheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            cipher=Cipher.getInstance(getMyEncryptionScheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            key=mySecretKeyFactory.generateSecret(myKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp=input_text.getText().toString();
                ans=encrypt(temp);

                output_text.setText(ans);

            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=input_text.getText().toString();
                ans=decrypt(temp);

                output_text.setText(ans);
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    input_text.setText(" ");
                    output_text.setText("");

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }


    public String encrypt(String unencryptedString)
    {
        String encryptedString =null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);

            encryptedString=Base64.encodeToString(encryptedText, Base64.DEFAULT);
        }
        catch (InvalidKeyException|UnsupportedEncodingException|IllegalBlockSizeException |BadPaddingException e) {

        }

        return encryptedString;
    }

    public String decrypt(String encrytedString)
    { String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(encrytedString, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        }catch(InvalidKeyException|IllegalBlockSizeException|BadPaddingException e) {

        }
        return decryptedText;
    }

    //convert a byte[] into a string
    private static String bytes2String(byte[] bytes)
    {
        StringBuilder stringBuffer=new StringBuilder();
        for(int i=0;i<bytes.length;i++)
        {
            stringBuffer.append((char) bytes[i]);

        }
        return stringBuffer.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1001:
                if(resultCode==RESULT_OK&&data!=null)
                {
                    ArrayList<String> res=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    input_text.setText(res.get(0));
                }
                break;
        }
    }
}
