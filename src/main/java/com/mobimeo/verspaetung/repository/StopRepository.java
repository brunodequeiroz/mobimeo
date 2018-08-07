package com.mobimeo.verspaetung.repository;

import com.mobimeo.verspaetung.model.Stop;
import io.reactivex.Observable;

public interface StopRepository {

    Observable<Stop> findByIds(final Integer id, final Integer... ids);

    Observable<Stop> findByIdAndInPosition(final Integer id, final Integer x, final Integer y);
}
