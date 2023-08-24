package com.firebase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.entity.Users;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private static final String COLECTION_NAME = "users";

    public Users getUsers(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLECTION_NAME).document(email);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        Users user = null;
        if (document.exists()) {
            user = document.toObject(Users.class);
            return user;
        } else {
            return null;
        }
    }

    public List<Users> getListUsers() throws ExecutionException, InterruptedException, ParseException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReferences = dbFirestore.collection(COLECTION_NAME).listDocuments();
        Iterator<DocumentReference> iterator = documentReferences.iterator();
        List<Users> users = new ArrayList<Users>();
        Users user = null;
        while (iterator.hasNext()){
            DocumentReference documentReference = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            user = document.toObject(Users.class);
            user.setPoint(getPoints(user.getAttendData()));
            users.add(user);
        }
        return users;
    }

    public Integer getPoints(List<HashMap<String, Object>> attendData) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int result = 0;
        Date current = new Date();
        for (int i = 0; i < attendData.size(); i++){
            Date parseDate = sdf.parse(attendData.get(i).get("attendAt").toString());
            int point = Integer.parseInt(attendData.get(i).get("point").toString());
            if (parseDate.getMonth() == current.getMonth()) {
                result += point;
            }
        }
        return result;
    }
}
