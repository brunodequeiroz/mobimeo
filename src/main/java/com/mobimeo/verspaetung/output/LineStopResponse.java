package com.mobimeo.verspaetung.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class LineStopResponse {

    private final Integer lineId;

    private final Integer stopId;

    private final LocalTime time;

    private final Integer x;

    private final Integer y;
}
