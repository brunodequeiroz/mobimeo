package com.mobimeo.verspaetung.mapper;

import com.mobimeo.verspaetung.csv.CSVMapper;
import com.mobimeo.verspaetung.model.Time;
import lombok.NonNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeMapper implements CSVMapper<Time> {

    public Time mapFromCSV(@NonNull final String line) {
        String[] split = line.split(",");
        return new Time(
            Integer.valueOf(split[0]),
            Integer.valueOf(split[1]),
            LocalTime.parse(split[2], DateTimeFormatter.ISO_TIME)
        );
    }
}
