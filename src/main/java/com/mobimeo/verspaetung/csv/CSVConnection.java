package com.mobimeo.verspaetung.csv;

import lombok.NonNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class CSVConnection<T> {

    private final String file;

    private final CSVMapper<T> mapper;

    public CSVConnection(@NonNull final String file, @NonNull final CSVMapper<T> mapper) {
        this.file = file;
        this.mapper = mapper;
    }

    public List<T> load() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputFS = classLoader.getResourceAsStream(this.file);
        final BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
        return br.lines().skip(1).map(mapper::mapFromCSV).collect(Collectors.toList());
    }
}
