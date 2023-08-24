package com.firebase.service;

import com.firebase.entity.NotificationMessage;
import com.google.api.client.util.Value;
import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FirebaseMessagingService {

  @Value("${app.notifications.color}")
  private String colorNotif;

  @Value("${app.notifications.sound}")
  private String soundNotif;

  @Value("${web.url}")
  private String link;

  @Value("${icon}")
  private String icon;

  public String sendNotificationByToken(NotificationMessage request) {
    String response = "";
    try {
      Message message = getPreconfiguredMessageToToken(request);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonOutput = gson.toJson(message);
      response = sendAndGetResponse(message);
      log.info(
              "Sent message to token. Device token: " +
                      request.getToken() +
                      ", " +
                      response +
                      " msg " +
                      jsonOutput
      );
    } catch (Exception e) {
      log.error(e.getMessage());
      response = e.getMessage();
    }
    return response;
  }


  public void sendPushNotificationWithoutData(NotificationMessage request) {
    try {
      Message message = getPreconfiguredMessageWithoutData(request);
      String response = sendAndGetResponse(message);
      log.info(
        "Sent message without data. Tittle: " + request.getTitle() + ", " + response
      );
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public String sendPushNotification(NotificationMessage request) {
    String response = "";
    try {
      Message message = getPreconfiguredMessageWithData(request);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String jsonOutput = gson.toJson(message);
      response = sendAndGetResponse(message);
      log.info(
        "Sent message with data. Tittle: " +
        request.getTitle() +
        ", " +
        response +
        " msg " +
        jsonOutput
      );
    } catch (Exception e) {
      log.error(e.getMessage());
      response = e.getMessage();
    }
    return response;
  }

  // ------------------------------------------------------------------------------
  public void sendSingleDevice(String token) throws FirebaseMessagingException {

    // See documentation on defining a message payload.
    Message message = Message.builder()
            .putData("score", "850")
            .putData("time", "2:45")
            .setToken(token)
            .build();

    // Send a message to the device corresponding to the provided
    // registration token.
    String response = FirebaseMessaging.getInstance().send(message);
    // Response is a message ID string.
    System.out.println("Successfully sent message: " + response);
  }

  public void sendMultipleDevice() throws FirebaseMessagingException {
    // Create a list containing up to 500 registration tokens.
  // These registration tokens come from the client FCM SDKs.
    List<String> registrationTokens = Arrays.asList(
            "YOUR_REGISTRATION_TOKEN_1",
            // ...
            "YOUR_REGISTRATION_TOKEN_n"
    );

    MulticastMessage message = MulticastMessage.builder()
            .putData("score", "850")
            .putData("time", "2:45")
            .addAllTokens(registrationTokens)
            .build();
    BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
  // See the BatchResponse reference documentation
  // for the contents of response.
    System.out.println(response.getSuccessCount() + " messages were sent successfully");
  }

  public void sendMessageByTopic(String topic) throws FirebaseMessagingException {
    // See documentation on defining a message payload.
    Message message = Message.builder()
            .putData("score", "850")
            .putData("time", "2:45")
            .setTopic(topic)
            .build();

    // Send a message to the devices subscribed to the provided topic.
    String response = FirebaseMessaging.getInstance().send(message);
    // Response is a message ID string.
    System.out.println("Successfully sent message: " + response);
  }

  // ------------------------------------------------------------------------------

  private String sendAndGetResponse(Message message)
    throws InterruptedException, ExecutionException, FirebaseMessagingException {
    return FirebaseMessaging.getInstance().sendAsync(message).get();
  }

  private Message getPreconfiguredMessageWithoutData(NotificationMessage request) {
    return getPreconfiguredMessageBuilder(request).setTopic(request.getTitle()).build();
  }

  private Message getPreconfiguredMessageToToken(NotificationMessage request) {
    return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
  }

  private Message getPreconfiguredMessageWithData(NotificationMessage request) {
    return getPreconfiguredMessageBuilder(request)
      .putAllData(request.getData())
      .setToken(request.getToken())
      .build();
  }

  // -----------------------------------------------------------------------------

  private Message.Builder getPreconfiguredMessageBuilder(NotificationMessage request) {
    AndroidConfig androidConfig = getAndroidConfig(
      request.getTitle(),
      request.getImageUrl()
    );
    ApnsConfig apnsConfig = getApnsConfig(request.getTitle());
    Notification notification = Notification
      .builder()
      .setTitle(request.getTitle())
      .setBody(request.getBody())
      .setImage(request.getImageUrl())
      .build();
    return Message
      .builder()
      .putAllData(request.getData())
      .setApnsConfig(apnsConfig)
      .setAndroidConfig(androidConfig)
      .setWebpushConfig(WebpushConfig.builder()
              .setNotification(new WebpushNotification(
                      request.getTitle(),
                      request.getBody(),
                      request.getImageUrl()))
              .setFcmOptions(WebpushFcmOptions.withLink("https://pet-monster.studiocloud.dev/"))
              .build())
      .setNotification(notification);
  }

  private AndroidConfig getAndroidConfig(String topic, String imageUrl) {
    return AndroidConfig
      .builder()
      .setTtl(Duration.ofMinutes(2).toMillis())
      .setCollapseKey(topic)
      .setPriority(AndroidConfig.Priority.HIGH)
      .setNotification(
        AndroidNotification
          .builder()
          .setSound(soundNotif)
          .setColor(colorNotif)
          .setImage(imageUrl)
          .setTag(topic)
          .setClickAction("https://pet-monster.studiocloud.dev/")
          .build()
      )
      .build();
  }

  private ApnsConfig getApnsConfig(String topic) {
    return ApnsConfig
      .builder()
      .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build())
      .build();
  }

  // --------------------------------------------------------------------------------

}
