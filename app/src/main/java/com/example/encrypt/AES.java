package com.example.encrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Locale;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class AES extends AppCompatActivity {


    EditText input_text;
    TextView output_text;
    Button enc, dec,clear;
    String outputstring="";
    String AES = "AES";
    String pwd="atrey23";

    String inptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);

        input_text = (EditText) findViewById(R.id.input_text);

        output_text = (TextView) findViewById(R.id.output_text);
        enc = (Button) findViewById(R.id.encrypt);
        dec = (Button) findViewById(R.id.decrypt);
        clear = (Button) findViewById(R.id.clear_button);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inptext = input_text.getText().toString();
                   // we will get the input
                    outputstring = encrypt(inptext,pwd);
                    // here we send the inputtext and key to encrypt function
                    output_text.setText(outputstring);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inptext = input_text.getText().toString();
                    // pwdtext = password_text.getText().toString();
                    outputstring = decrypt(inptext,pwd);// outputstring was there in place of inptext
                    output_text.setText(outputstring);
                    //make a toast her e to say deccypted successfully
                    //  Toast.makeText(this,"decrypted Successfully!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    input_text.setText(" ");
                    output_text.setText("");

                    //  Toast.makeText( this,"Cleared Successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }


    private String encrypt(String data,String password) throws Exception {
        SecretKeySpec key = generateKey(password);

        Cipher c = Cipher.getInstance(AES);// we use  AES
        c.init(Cipher.ENCRYPT_MODE, key);//initialisation the cryptosystem
        byte[] encVal = c.doFinal(data.getBytes("UTF-8"));
      // we convert plain text to bytes and then due to doFinal encryption happne
        String encryptedvalue = Base64.encodeToString(encVal, Base64.DEFAULT);
      // we want to represent and encode the byte sequence as string
        return encryptedvalue;

    }

    private String decrypt(String data,String password) throws Exception {
        SecretKeySpec key = generateKey(password);

        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedvalue = Base64.decode(data, Base64.DEFAULT);
        // we are getting back from base64
        byte[] decvalue = c.doFinal(decodedvalue);//due to this decoding happend
        String decryptedvalue = new String(decvalue, "UTF-8");//converting bytes into string
        return decryptedvalue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");//for using hash function SHA-256
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);//process kr bytes array ko
        byte[] key = digest.digest();////Completes the hash computation by performing final operations such as padding.
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
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
