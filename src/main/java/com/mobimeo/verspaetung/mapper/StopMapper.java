package com.mobimeo.verspaetung.mapper;

import com.mobimeo.verspaetung.csv.CSVMapper;
import com.mobimeo.verspaetung.model.Stop;
import lombok.NonNull;

public class StopMapper implements CSVMapper<Stop> {

    public Stop mapFromCSV(@NonNull final String line) {
        String[] split = line.split(",");
        return new Stop(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
    }
}
