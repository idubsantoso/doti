package com.firebase.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NotificationMessage {

  private String title;
  private String body;
  private String token;
  private String imageUrl = "https://www.dot.co.id/images/img-metadata.webp";
  private Map<String, String> data;
}
