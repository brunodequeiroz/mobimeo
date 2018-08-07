package com.mobimeo.verspaetung.service;

import com.mobimeo.verspaetung.output.LineResponse;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.time.LocalTime;

public interface LineService {

    Observable<LineResponse> search(final LocalTime time, final Integer x, final Integer y);

    Single<LineResponse> byName(final String name);
}
