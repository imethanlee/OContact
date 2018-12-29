package com.example.gg.ocontact;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DeletionService extends Service {
    private boolean threadDisable = false;

    public DeletionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!threadDisable){
            threadDisable = true;
            new Thread(() -> {
                while (!threadDisable){
                    Intent deletionIntent = new Intent();
                    deletionIntent.setAction("com.example.gg.ocontact.DeletionService");
                    sendBroadcast(deletionIntent);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stopSelf();
                }
            }).start();
            threadDisable = false;
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadDisable = true;
    }
}
