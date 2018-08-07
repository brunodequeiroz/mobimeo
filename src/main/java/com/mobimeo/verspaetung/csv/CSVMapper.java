package com.mobimeo.verspaetung.csv;

public interface CSVMapper<T> {

    T mapFromCSV(final String line);
}
