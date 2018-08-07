package com.mobimeo.verspaetung.repository.csv;

import com.mobimeo.verspaetung.csv.CSVConnection;
import com.mobimeo.verspaetung.model.Line;
import com.mobimeo.verspaetung.repository.LineRepository;
import io.reactivex.Single;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class LineRepositoryImpl implements LineRepository {

    private final List<Line> lines;

    public LineRepositoryImpl(@NonNull final CSVConnection<Line> client) {
        this.lines = client.load();
    }

    @Override
    public Single<Optional<Line>> one(@NonNull final Integer id) {
        return Single.just(
            lines
                .stream()
                .filter(line -> line.getId().equals(id))
                .findFirst()
        );
    }

    @Override
    public Single<Optional<Line>> byName(@NonNull final String name) {
        return Single.just(
            lines
                .stream()
                .filter(line -> line.getName().equals(name))
                .findFirst()
        );
    }
}
