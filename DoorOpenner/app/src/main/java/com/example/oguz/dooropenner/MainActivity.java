package com.example.oguz.dooropenner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParticleCloudSDK.init(this);
        setContentView(R.layout.activity_main);
        final EditText PassTxt=(EditText)findViewById(R.id.pass);
        final EditText MailTxt=(EditText)findViewById(R.id.mail);
        Button loginBtn=(Button)findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Mail=MailTxt.getText().toString();
                final  String pass=PassTxt.getText().toString();

                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            ParticleCloudSDK.getCloud().logIn(Mail, pass);
                            Log.e("baglandı","ok");

                        } catch (final ParticleCloudException e) {
                            e.printStackTrace();
                            return "0";
                        }
                        return "1";
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(o.toString().equals("1")){
                        Intent i=new Intent(MainActivity.this,DoorOpenner.class);
                        startActivity(i);}
                    }
                }.execute();


                }

        });
    }
}
