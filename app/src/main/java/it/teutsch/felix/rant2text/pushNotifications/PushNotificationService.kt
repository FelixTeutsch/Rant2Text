package it.teutsch.felix.rant2text.pushNotifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("PushNotificationService", "Refreshed token: $token")
        // Update Server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("PushNotificationService", "From: ${message.from}")
        // Respond to notifications (Custom push notification behaviour)
    }
}