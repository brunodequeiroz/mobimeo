package com.mobimeo.verspaetung.repository.csv;

import com.mobimeo.verspaetung.csv.CSVConnection;
import com.mobimeo.verspaetung.model.Delay;
import com.mobimeo.verspaetung.repository.DelayRepository;
import io.reactivex.Observable;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DelayRepositoryImpl implements DelayRepository {

    private final List<Delay> delays;

    public DelayRepositoryImpl(@NonNull final CSVConnection<Delay> client) {
        this.delays = client.load();
    }

    @Override
    public Observable<Delay> findByLineNames(@NonNull final String name, final String... names) {
        final List<String> allNames = new ArrayList<>();
        allNames.add(name);
        allNames.addAll(Arrays.asList(names));

        return Observable.fromIterable(delays)
            .filter(delay -> allNames.contains(delay.getLineName()));
    }
}
