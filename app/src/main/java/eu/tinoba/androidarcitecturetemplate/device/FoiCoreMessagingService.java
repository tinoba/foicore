package eu.tinoba.androidarcitecturetemplate.device;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.ui.discount.DiscountActivity;

public class FoiCoreMessagingService extends FirebaseMessagingService {

    private static final String TAG = FoiCoreMessagingService.class.getSimpleName();

    Random rand = new Random();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FROM:" + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("text"), remoteMessage.getData().get("title"));
        }
    }

    /**
     * Dispay the notification
     */
    private void sendNotification(String body, String title) {
        int randomNum = rand.nextInt((9999999 - 1) + 1) + 1;

        Intent intent = new Intent(this, DiscountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, randomNum, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cart_rfid_icon_white)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(randomNum, notifiBuilder.build());
    }
}
