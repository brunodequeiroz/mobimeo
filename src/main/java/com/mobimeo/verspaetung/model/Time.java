package com.mobimeo.verspaetung.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Time {

    private Integer lineId;

    private Integer stopId;

    private LocalTime time;
}
