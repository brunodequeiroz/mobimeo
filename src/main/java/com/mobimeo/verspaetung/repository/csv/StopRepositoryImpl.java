package com.mobimeo.verspaetung.repository.csv;

import com.mobimeo.verspaetung.csv.CSVConnection;
import com.mobimeo.verspaetung.model.Stop;
import com.mobimeo.verspaetung.repository.StopRepository;
import io.reactivex.Observable;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopRepositoryImpl implements StopRepository {

    private final List<Stop> stops;

    public StopRepositoryImpl(@NonNull final CSVConnection<Stop> client) {
        this.stops = client.load();
    }

    // TODO ask if we need to introduce any radius
    @Override
    public Observable<Stop> findByIdAndInPosition(
        @NonNull final Integer id,
        @NonNull final Integer x,
        @NonNull final Integer y
    ) {
        return Observable.fromIterable(stops)
            .filter(stop -> stop.getId().equals(id) && stop.getX().equals(x) && stop.getY().equals(y));
    }

    @Override
    public Observable<Stop> findByIds(@NonNull final Integer id, final Integer... ids) {
        final List<Integer> allIds = new ArrayList<>();
        allIds.add(id);
        allIds.addAll(Arrays.asList(ids));

        return Observable.fromIterable(stops)
            .filter(stop -> allIds.contains(stop.getId()));
    }
}
