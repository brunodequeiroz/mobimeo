package com.mobimeo.verspaetung.repository;

import com.mobimeo.verspaetung.model.Line;
import io.reactivex.Single;

import java.util.Optional;

public interface LineRepository {

    Single<Optional<Line>> one(final Integer id);

    Single<Optional<Line>> byName(final String name);
}
