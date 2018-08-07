package com.mobimeo.verspaetung.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LineResponse {

    private final Integer id;

    private final String name;

    private final List<LineStopResponse> stops;

    private final List<Integer> delays;
}
