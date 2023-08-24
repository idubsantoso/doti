package com.firebase.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {
    private List<HashMap<String, Object>> attendData;
    private String cloudMessagingToken;
    private String displayName;
    private String email;
    private Object foods;
    private Object monster;
    private Integer point;
}
