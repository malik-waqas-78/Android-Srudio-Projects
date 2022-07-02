package screen.lock.screenlock;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;


public class ForegroundService extends Service {
    public static String CHANNEL_ID = "channel1";
    private BroadcastReceiver mReceiver;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "FirstNotify",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("first channel created for notification");
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
        }
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        Reciever reciever = new Reciever();
        this.mReceiver = reciever;
        registerReceiver(reciever, filter);
        startForeground();
        return START_STICKY;
    }

    private void startForeground() {
        startForeground(9999,
                new NotificationCompat.Builder((Context) this, CHANNEL_ID)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setTicker(getResources().getString(R.string.app_name))
                        .setContentText("Running")
                        .setSmallIcon((int) R.drawable.lockicons)
                        .setContentIntent((PendingIntent) null)
                        .setOngoing(true)
                        .setNotificationSilent().build());
    }

    public void onDestroy() {
        boolean checkEnable=getSharedPreferences("MySharedPref",MODE_PRIVATE).getBoolean("serviceCheck",false);
        if(checkEnable){
            Intent intent=new Intent(getApplicationContext(),ForegroundService.class);
            PendingIntent pendingIntent=PendingIntent.getService(this,1,intent,PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 60*1000, pendingIntent);
            super.onDestroy();
        } else {
            super.onDestroy();
            unregisterReceiver(this.mReceiver);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

            Intent intent = new Intent(getApplicationContext(), ForegroundService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
            super.onTaskRemoved(rootIntent);

    }
}
