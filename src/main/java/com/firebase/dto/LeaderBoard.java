package com.firebase.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LeaderBoard {
    private Integer ranking;
    private String name;
    private Integer point;
}
