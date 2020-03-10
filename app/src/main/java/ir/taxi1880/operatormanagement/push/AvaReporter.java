package ir.taxi1880.operatormanagement.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ir.taxi1880.operatormanagement.receiver.PushReceiver;

import static android.content.Context.ALARM_SERVICE;

public class AvaReporter {
  public static void Message(Context context, int type, String msg) {
    if (context == null) return;
    try {
        Intent intent = new Intent(context, PushReceiver.class);
        intent.putExtra(Keys.KEY_MESSAGE, msg);
        intent.putExtra(Keys.KEY_BROADCAST_TYPE, type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), Keys.ALARM_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    } catch (Exception e1) {
      AvaCrashReporter.send(e1, 109);
    }
  }



  public static void MessageLog(String msg) {
//    try {
//      Intent intent = new Intent(AvaFactory.getContext().getPackageName() + "." + Keys.KEY_ACTION_RECEIVE_MESSAGE);
//      intent.putExtra(Keys.KEY_MESSAGE, msg);
//      intent.putExtra(Keys.KEY_BROADCAST_TYPE, 1);
//      AvaFactory.getContext().sendBroadcast(intent);
//    } catch (Exception e1) {
//      AvaCrashReporter.send(e1,110);
//    }
  }

}