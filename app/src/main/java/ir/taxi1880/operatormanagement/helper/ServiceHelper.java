package ir.taxi1880.operatormanagement.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ir.taxi1880.operatormanagement.app.MyApplication;
import ir.taxi1880.operatormanagement.push.AvaCrashReporter;
import ir.taxi1880.operatormanagement.push.AvaLog;

public class ServiceHelper {

    public static final String TAG = ServiceHelper.class.getSimpleName();

    // Method to start the service
    public static void start(Context activity, Class<?> serviceClass) {
        try {
            if (activity != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!isRunning(activity, serviceClass))
                        activity.startForegroundService(new Intent(activity, serviceClass));
                } else {
                    if (!isRunning(activity, serviceClass))
                        activity.startService(new Intent(activity, serviceClass));
                }
        } catch (Exception e) {
            e.printStackTrace();
            AvaCrashReporter.send(e, TAG + " class, start method");
        }
    }

    // Method to stop the service
    public static void stop(Context activity, Class<?> serviceClass) {
        try {
            if (activity != null)
                activity.stopService(new Intent(activity, serviceClass));
            AvaLog.e("must close service " + activity.getClass().getSimpleName() + " GoodBye :'(");
        } catch (Exception e) {
            e.printStackTrace();
            AvaCrashReporter.send(e, TAG + " class, stop method");
        }
    }

    // Method to stop the service
    public static void restart(Context context, Class<?> serviceClass) {
        stop(context, serviceClass);
        //you must set delay between stop and start because maybe conflict together
        MyApplication.handler.postDelayed(() -> start(context, serviceClass), 500);
    }

    //Check the service is running
    public static boolean isRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}