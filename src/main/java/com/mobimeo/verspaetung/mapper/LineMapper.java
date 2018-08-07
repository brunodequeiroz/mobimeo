package com.mobimeo.verspaetung.mapper;

import com.mobimeo.verspaetung.csv.CSVMapper;
import com.mobimeo.verspaetung.model.Delay;
import com.mobimeo.verspaetung.model.Line;
import com.mobimeo.verspaetung.model.Stop;
import com.mobimeo.verspaetung.output.LineResponse;
import com.mobimeo.verspaetung.output.LineStopResponse;
import lombok.NonNull;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineMapper implements CSVMapper<Line> {

    public Line mapFromCSV(@NonNull final String line) {
        String[] split = line.split(",");
        return new Line(Integer.valueOf(split[0]), split[1]);
    }

    public LineResponse mapToResponse(
        @NonNull final Line line,
        @NonNull final List<LineStopResponse> stops,
        final List<Delay> delays
    ) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            stops,
            delays.stream().map(Delay::getDelay).collect(Collectors.toList())
        );
    }

    public LineStopResponse mapToLineStopResponse(
        @NonNull final Integer lineId,
        @NonNull final Stop stop,
        @NonNull final LocalTime time
    ) {
        return new LineStopResponse(
            lineId,
            stop.getId(),
            time,
            stop.getX(),
            stop.getY()
        );
    }
}
