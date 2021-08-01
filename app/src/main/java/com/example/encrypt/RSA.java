package com.example.encrypt;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSA extends AppCompatActivity {

    String temp;
    TextView output;
    EditText input;
    Button enc,dec,clear;
    String tosend="";
    private PrivateKey privateKey;
    private  PublicKey publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa);

        output=(TextView) findViewById(R.id.output_text);
        input=(EditText) findViewById(R.id.input_text);
        enc=(Button) findViewById(R.id.encrypt);
        dec=(Button) findViewById(R.id.decrypt);
        clear=(Button) findViewById(R.id.clear_button);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // generate a new public/private key pair to test with (note. you should only do this once and keep them!)


        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
         privateKey = pair.getPrivate();
         publicKey = pair.getPublic();
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp=input.getText().toString();
                String encrypted = null;
                try {
                    encrypted = encrypt(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  Log.d("NIKHIL", "encrypt key:" +encrypted);
                output.setText(encrypted);
                tosend=encrypted;
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp=input.getText().toString();
                String decrypted = null;
                try {
                    decrypted = decrypt(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //    Log.d("NIKHIL", "encrypt key:" +decrypted);
                output.setText(decrypted);
                tosend=decrypted;
            }
        });

        clear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    input.setText(" ");
                    output.setText("");
                    //  Toast.makeText( this,"Cleared Successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }


    public String encrypt(String message) throws Exception{
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }
    private String encode(byte[] data){
        String data2 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          data2 = Base64.getEncoder().encodeToString(data);
        }
        return  data2;
    }

    public String decrypt(String encryptedMessage) throws Exception{
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage,"UTF8");
    }
    private byte[] decode(String data){
        byte [] data1 = new byte[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           data1= Base64.getDecoder().decode(data);
        }
        return data1;
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
                    input.setText(res.get(0));
                }
                break;
        }
    }
}
