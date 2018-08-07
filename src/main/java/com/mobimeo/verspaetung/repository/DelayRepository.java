package com.mobimeo.verspaetung.repository;

import com.mobimeo.verspaetung.model.Delay;
import io.reactivex.Observable;

public interface DelayRepository {

    Observable<Delay> findByLineNames(final String name, final String... names);
}
