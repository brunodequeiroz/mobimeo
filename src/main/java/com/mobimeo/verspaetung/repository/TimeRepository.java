package com.mobimeo.verspaetung.repository;

import com.mobimeo.verspaetung.model.Time;
import io.reactivex.Observable;

import java.time.LocalTime;

public interface TimeRepository {

    Observable<Time> findByTime(final LocalTime time);

    Observable<Time> findByStop(final Integer stopId);

    Observable<Time> findByLine(final Integer lineId);
}
