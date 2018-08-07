package com.mobimeo.verspaetung.mapper;

import com.mobimeo.verspaetung.csv.CSVMapper;
import com.mobimeo.verspaetung.model.Delay;
import lombok.NonNull;

public class DelayMapper implements CSVMapper<Delay> {

    public Delay mapFromCSV(@NonNull final String line) {
        String[] split = line.split(",");
        return new Delay(split[0], Integer.valueOf(split[1]));
    }
}
