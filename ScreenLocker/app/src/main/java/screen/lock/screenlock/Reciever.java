package screen.lock.screenlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import static android.content.Context.MODE_PRIVATE;

public class Reciever extends BroadcastReceiver {
    int type;
    boolean serviceCheck,checkEnable;
    public void onReceive(Context context, Intent intent) {

        checkEnable=context.getSharedPreferences("MySharedPref",MODE_PRIVATE).getBoolean("checkenable", false);
        serviceCheck=context.getSharedPreferences("MySharedPref",MODE_PRIVATE).getBoolean("serviceCheck",false);
        type=context.getSharedPreferences("MySharedPref",MODE_PRIVATE).getInt("type",1);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            start_lockscreen(context);
        }
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            if(serviceCheck){
                start_lockscreen(context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, ForegroundService.class));
                } else {
                    context.startService(new Intent(context, ForegroundService.class));
                }
            }else{
                if(checkEnable){
                    start_lockscreen(context);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, ForegroundService.class));
                    } else {
                        context.startService(new Intent(context, ForegroundService.class));
                    }
                }
            }
        }

    }

    private void start_lockscreen(Context context) {
        if(type==1) {
            Intent mIntent = new Intent(context, LockScreenActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }else if(type==2){
            Intent mIntent = new Intent(context, PatternLock.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }
}
