package com.example.oguz.dooropenner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;

public class DoorOpenner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_openner);
        SeekBar seekBar=(SeekBar) findViewById(R.id.seekBar);
        final TextView position=(TextView) findViewById(R.id.position);

        List<ParticleDevice> devices = null;
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                List<ParticleDevice> devices = null;
                final TextView position2=(TextView) findViewById(R.id.position);
                try {
                    devices = ParticleCloudSDK.getCloud().getDevices();
                } catch (ParticleCloudException e) {
                    e.printStackTrace();
                }
                for (ParticleDevice device : devices) {
                    if (device.getName().equals("bayram")) {
                        Const.myDevice=device;
                        Log.e("device:",Const.myDevice.getName());
                        break;
                    }
                }
                int lastPos=-1;
                try {
                     lastPos = Const.myDevice.getIntVariable("getpos");
                } catch (ParticleCloudException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParticleDevice.VariableDoesNotExistException e) {
                    e.printStackTrace();
                }
                return lastPos;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                position.setText(o.toString()+"°");
            }
        }.execute();

        final List<String> poslist = new ArrayList<String>();
        poslist.add(0,"0");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            int progress=seekBar.getProgress();
                String pos= String.valueOf(progress);
                poslist.set(0,pos);


                Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {
                        Object variable;
                        try {
                            variable = Const.myDevice.callFunction("setpos",poslist);
                        } catch (Exception e) {
                            e.printStackTrace();
                            variable=-1;
                        }
                        return variable;
                    }

                    @Override
                    public void onSuccess(Object i) {
                        Log.e("object",i.toString());
                        position.setText(String.valueOf(i)+"°");
                        // this goes on the main thread
                        //     Toast.makeText(DoorOpenner.this,"fonksiyon çagrıldı",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(ParticleCloudException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
