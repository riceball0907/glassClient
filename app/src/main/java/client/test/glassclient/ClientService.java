package client.test.glassclient;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;

/**
 * Created by csc on 2015/1/21.
 */
public class ClientService extends Service {
    private static final String LIVE_CARD_TAG = "LiveCardService";

    private LiveCard mLiveCard;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.glass_client);
            mLiveCard.setViews(remoteViews);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, LiveCardMenuActivity.class);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
            mLiveCard.publish(LiveCard.PublishMode.REVEAL);
        } else {
            mLiveCard.navigate();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }

}
