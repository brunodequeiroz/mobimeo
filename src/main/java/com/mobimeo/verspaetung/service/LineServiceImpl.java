package com.mobimeo.verspaetung.service;

import com.mobimeo.verspaetung.exception.NotFoundException;
import com.mobimeo.verspaetung.exception.ServiceException;
import com.mobimeo.verspaetung.mapper.LineMapper;
import com.mobimeo.verspaetung.model.Line;
import com.mobimeo.verspaetung.output.LineResponse;
import com.mobimeo.verspaetung.output.LineStopResponse;
import com.mobimeo.verspaetung.repository.DelayRepository;
import com.mobimeo.verspaetung.repository.LineRepository;
import com.mobimeo.verspaetung.repository.StopRepository;
import com.mobimeo.verspaetung.repository.TimeRepository;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.NonNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class LineServiceImpl implements LineService {

    private final LineRepository lineRepository;

    private final DelayRepository delayRepository;

    private final StopRepository stopRepository;

    private final TimeRepository timeRepository;

    private final LineMapper mapper;

    public LineServiceImpl(
        @NonNull final LineRepository lineRepository,
        @NonNull final DelayRepository delayRepository,
        @NonNull final StopRepository stopRepository,
        @NonNull final TimeRepository timeRepository,
        @NonNull final LineMapper lineMapper
    ) {
        this.lineRepository = lineRepository;
        this.delayRepository = delayRepository;
        this.stopRepository = stopRepository;
        this.timeRepository = timeRepository;
        this.mapper = lineMapper;
    }

    @Override
    public Observable<LineResponse> search(
        @NonNull final LocalTime time,
        @NonNull final Integer x,
        @NonNull final Integer y
    ) {
        return timeRepository
            .findByTime(time)
            .flatMap(entry ->
                stopRepository
                    .findByIdAndInPosition(entry.getStopId(), x, y)
                    .map(stop -> mapper.mapToLineStopResponse(entry.getLineId(), stop, entry.getTime()))
            )
            .groupBy(LineStopResponse::getLineId)
            .flatMap(group ->
                group
                    .collect(ArrayList<LineStopResponse>::new, ArrayList::add)
                    .flatMap(lineStops ->
                        lineRepository
                            .one(group.getKey())
                            .toObservable()
                            .filter(Optional::isPresent)
                            .switchIfEmpty(Observable.error(
                                new ServiceException("Line not found for id: " + group.getKey())
                            ))
                            .map(Optional::get)
                            .toSingle()
                            .flatMap(this::populate)
                            .toObservable()
                    )
            );
    }

    @Override
    public Single<LineResponse> byName(@NonNull final String name) {
        return
            lineRepository
                .byName(name)
                .toObservable()
                .filter(Optional::isPresent)
                .switchIfEmpty(Observable.error(
                    new NotFoundException("Line not found for name: " + name)
                ))
                .map(Optional::get)
                .toSingle()
                .flatMap(this::populate);
    }

    private Single<LineResponse> populate(final Line line) {
        return Single.zip(
            delayRepository
                .findByLineNames(line.getName())
                .toList()
                .toSingle(),
            timeRepository
                .findByLine(line.getId())
                .flatMap(time ->
                    stopRepository
                        .findByIds(time.getStopId())
                        .map(stop -> mapper.mapToLineStopResponse(line.getId(), stop, time.getTime()))
                )
                .toList()
                .toSingle(),
            (delays, stops) -> mapper.mapToResponse(line, stops, delays)
        );
    }
}
