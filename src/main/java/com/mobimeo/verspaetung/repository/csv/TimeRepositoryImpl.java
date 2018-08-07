package com.mobimeo.verspaetung.repository.csv;

import com.mobimeo.verspaetung.csv.CSVConnection;
import com.mobimeo.verspaetung.model.Time;
import com.mobimeo.verspaetung.repository.TimeRepository;
import io.reactivex.Observable;
import lombok.NonNull;

import java.time.LocalTime;
import java.util.List;

public class TimeRepositoryImpl implements TimeRepository {

    private final List<Time> times;

    public TimeRepositoryImpl(@NonNull final CSVConnection<Time> client) {
        this.times = client.load();
    }

    @Override
    public Observable<Time> findByTime(@NonNull final LocalTime time) {
        return Observable.fromIterable(times)
            .filter(t -> t.getTime().equals(time));
    }

    @Override
    public Observable<Time> findByStop(@NonNull final Integer stopId) {
        return Observable.fromIterable(times)
            .filter(time -> time.getStopId().equals(stopId));
    }

    @Override
    public Observable<Time> findByLine(@NonNull final Integer lineId) {
        return Observable.fromIterable(times)
            .filter(time -> time.getLineId().equals(lineId));
    }
}
