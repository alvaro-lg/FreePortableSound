package es.upm.etsiinf.freeportablesound.presentation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;

import es.upm.etsiinf.freeportablesound.R;

public class NotificationHandler extends ContextWrapper {

    private NotificationManager manager;

    // Variable para la creación de canales
    public static final String CHANNEL_HIGH_ID = "1";
    public static final String CHANNEL_LOW_ID = "2";
    private final String CHANNEL_HIGH_NAME = "HIGH CHANNEL";
    private final String CHANNEL_LOW_NAME = "LOW CHANNEL";

    // Variables para la creación del grupo
    private final String GROUP_NAME = "GROUP";
    public static final int GROUP_ID= 111;


    public NotificationHandler(Context base) {
        super(base);
        createChannels();
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public void createChannels() {
        NotificationChannel highChannel = new NotificationChannel(CHANNEL_HIGH_ID, CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);
        // Configuraciones a nivel de canal
        highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationChannel lowChannel = new NotificationChannel(CHANNEL_LOW_ID, CHANNEL_LOW_NAME, NotificationManager.IMPORTANCE_LOW);
        // configuraciones a nivel de canal

        getManager().createNotificationChannel(highChannel);
        getManager().createNotificationChannel(lowChannel);
    }

    public Notification.Builder createNotification(String title, String msg, boolean priority, Uri fileUri) {
        if (priority) {
            return createNotificationChannels(title, msg, CHANNEL_HIGH_ID, fileUri);
        }
        return createNotificationChannels(title, msg, CHANNEL_LOW_ID, fileUri);
    }

    private Notification.Builder createNotificationChannels(String title, String msg, String channelId, Uri fileUri) {
        // Intent to open the file explorer
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(fileUri, "audio/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        // Create PendingIntent to open the file explorer
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 1, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Create notification action to open the file explorer
        Notification.Action action = new Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.ic_launcher_background), "Open File", pendingIntent).build();

        // Convert image to an icon
        Bitmap my_image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);

        return new Notification.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setActions(action)
                .setLargeIcon(my_image)
                .setStyle(new Notification.BigTextStyle().bigText(msg));
    }

    private Notification.Builder createNotificationNoChannels(String title, String msg) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true);
    }

    public void publishGroup (boolean priority) {
        String channel= priority?CHANNEL_HIGH_ID:CHANNEL_LOW_ID;

        Notification groupNotification = new Notification.Builder(getApplicationContext(), channel)
                .setGroup(GROUP_NAME)
                .setGroupSummary(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground).build();
        getManager().notify(GROUP_ID,groupNotification);
    }
}

