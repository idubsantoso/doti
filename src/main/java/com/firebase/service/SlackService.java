package com.firebase.service;

import com.firebase.dto.LeaderBoard;
import com.firebase.entity.Users;
import com.firebase.dto.SlackRequest;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class SlackService {
    @Value("${slack.url}")
    private String url;

    @Value("${length.leaderboard}")
    private Integer leader;


    @Autowired
    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String sendToSlack() throws ExecutionException, InterruptedException, ParseException {
        SlackRequest request = new SlackRequest();
        StringBuilder stringBuilder = new StringBuilder();
        Date date = new Date();
        stringBuilder.append(":sparkles:*DOTI LEADERBOARD: BULAN ").append(getMonth(date.getMonth())).append("*:sparkles:\n");
        var users = userService.getListUsers();
        String list = sortList(users);
        stringBuilder.append(list).append("\n");
        stringBuilder.append("*_Congratulation!!!_*");
        request.setText(stringBuilder.toString());
        RestTemplate restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        var entity = new HttpEntity<>(request,headers);
        try {
            var response = restTemplate.postForEntity(url, entity, String.class);

            var data = response.getBody();
            if (
                    response.getStatusCode() != HttpStatus.OK ||
                            data == null
            ) return null;

            return "Success";
        } catch (final HttpClientErrorException e) {
            log.error(
                    "Error access, status code:{},body:{}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString(),
                    e
            );
            return null;
        }
    }

    private String sortList(List<Users> list) {
        StringBuilder stringBuilder = new StringBuilder();
        list.sort(new Comparator<Users>() {
            public int compare(Users userVal1, Users userVal2) {
                Integer user1 = userVal1.getPoint();
                Integer user2 = userVal2.getPoint();
                return user2.compareTo(user1);
            }
        });
        if ( list.size() <= leader ){
            for (int i = 0; i < list.size(); i++){
                String format = String.format("%-20s %-1s",list.get(i).getDisplayName(),list.get(i).getPoint());
                String fixFormat = format.replaceAll(" ","\t");
                stringBuilder.append(i + 1).append(".\t").append(fixFormat).append(" pts\n");
            }
        } else {
            for (int i = 0; i < leader; i++) {
                String format = String.format("%-20s %-1s",list.get(i).getDisplayName(),list.get(i).getPoint());
                String fixFormat = format.replaceAll(" ","\t");
                stringBuilder.append(i + 1).append(".\t").append(fixFormat).append(" pts\n");
            }
        }
        return stringBuilder.toString();

    }

    private String getMonth(int month){
        String monthID = "";
        switch (month){
            case 0:
                monthID = "JANUARY";
                break;
            case 1:
                monthID = "FEBRUARI";
                break;
            case 2:
                monthID = "MARET";
                break;
            case 3:
                monthID = "APRIL";
                break;
            case 4:
                monthID = "MEI";
                break;
            case 5:
                monthID = "JUNI";
                break;
            case 6:
                monthID = "JULI";
                break;
            case 7:
                monthID = "AGUSTUS";
                break;
            case 8:
                monthID = "SEPTEMBER";
                break;
            case 9:
                monthID = "OKTOBER";
                break;
            case 10:
                monthID = "NOVEMBER";
                break;
            case 11:
                monthID = "DESEMBER";
                break;
        }
        return monthID;
    }
}
