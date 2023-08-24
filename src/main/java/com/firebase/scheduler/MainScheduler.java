package com.firebase.scheduler;

import com.firebase.entity.NotificationMessage;
import com.firebase.entity.Users;
import com.firebase.service.FirebaseMessagingService;
import com.firebase.service.SlackService;
import com.firebase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class MainScheduler {
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private UserService userService;
    @Autowired
    private SlackService slackService;

    @Value("${web.url}")
    private String link;

    @Value("${point.minimum}")
    private Integer pointMinimum;

    @Scheduled(cron= "${jobs.cron.monthly}")
    public void runJobsNotififcationMnthly() throws ExecutionException, InterruptedException, ParseException {
        var notificationMessage = new NotificationMessage();
        Map<String, String> dataPayload = new HashMap<String, String>();
        dataPayload.put("click_action", link);
        notificationMessage.setData(dataPayload);
        notificationMessage.setTitle("DOTI: Monster kamu laper nih");
        notificationMessage.setBody("Yukk, kumpulkan lebih banyak poin dan tingkatkan level kamu di bulan berikutnya.");
        var users = userService.getListUsers();
        for (Users user : users) {
            if (user.getPoint() < pointMinimum){
                notificationMessage.setToken(user.getCloudMessagingToken());
                notificationMessage.setImageUrl("https://www.dot.co.id/images/img-metadata.webp");
                firebaseMessagingService.sendNotificationByToken(notificationMessage);
            }
        }
    }

    @Scheduled(cron= "${jobs.cron.weekly}")
    public void runJobsNotififcationWeekly() throws ExecutionException, InterruptedException, ParseException {
        var notificationMessage = new NotificationMessage();
        Map<String, String> dataPayload = new HashMap<String, String>();
        dataPayload.put("click_action", link);
        notificationMessage.setData(dataPayload);
        notificationMessage.setTitle("DOTI: Monstermu sedang menunggumu");
        notificationMessage.setBody("Segera klaim poin dan beri makan monstermu.");
        var users = userService.getListUsers();
        for (Users user : users) {
            notificationMessage.setToken(user.getCloudMessagingToken());
            notificationMessage.setImageUrl("https://www.dot.co.id/images/img-metadata.webp");
            firebaseMessagingService.sendNotificationByToken(notificationMessage);
        }
    }

    @Scheduled(cron = "${jobs.cron.slack}")
    public void doStuffOnLastDayOfMonth() throws ParseException, ExecutionException, InterruptedException {
        slackService.sendToSlack();
    }

}
