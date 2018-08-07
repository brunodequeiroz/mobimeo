package com.mobimeo.verspaetung.service;

import com.mobimeo.verspaetung.csv.CSVConnection;
import com.mobimeo.verspaetung.exception.NotFoundException;
import com.mobimeo.verspaetung.mapper.DelayMapper;
import com.mobimeo.verspaetung.mapper.LineMapper;
import com.mobimeo.verspaetung.mapper.StopMapper;
import com.mobimeo.verspaetung.mapper.TimeMapper;
import com.mobimeo.verspaetung.output.LineResponse;
import com.mobimeo.verspaetung.repository.csv.DelayRepositoryImpl;
import com.mobimeo.verspaetung.repository.csv.LineRepositoryImpl;
import com.mobimeo.verspaetung.repository.csv.StopRepositoryImpl;
import com.mobimeo.verspaetung.repository.csv.TimeRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LineServiceImplTest {

    private LineServiceImpl service;

    @Before
    public void setUp() {
        final LineMapper lineMapper = new LineMapper();
        final DelayMapper delayMapper = new DelayMapper();
        final StopMapper stopMapper = new StopMapper();
        final TimeMapper timeMapper = new TimeMapper();

        this.service = new LineServiceImpl(
            new LineRepositoryImpl(new CSVConnection<>("db/lines.csv", lineMapper)),
            new DelayRepositoryImpl(new CSVConnection<>("db/delays.csv", delayMapper)),
            new StopRepositoryImpl(new CSVConnection<>("db/stops.csv", stopMapper)),
            new TimeRepositoryImpl(new CSVConnection<>("db/times.csv", timeMapper)),
            lineMapper
        );
    }

    @Test
    public void search_timeNotFound() {
        List<LineResponse> lineResponses = service.search(LocalTime.MAX, 0, 0)
            .toList()
            .toSingle()
            .blockingGet();

        assertThat(lineResponses, is(empty()));
    }

    @Test
    public void search_foundTimeButNotStops() {
        List<LineResponse> lineResponses = service.search(LocalTime.parse("10:02:00"), 0, 0)
            .toList()
            .toSingle()
            .blockingGet();

        assertThat(lineResponses, is(empty()));
    }

    @Test
    public void search_withResults() {
        List<LineResponse> lineResponses = service.search(LocalTime.parse("10:02:00"), 1, 4)
            .toList()
            .toSingle()
            .blockingGet();

        assertThat(lineResponses, hasSize(1));
        assertThat(lineResponses, hasItem(
            allOf(
                hasProperty("name", is("M4")),
                hasProperty("delays", hasSize(1)),
                hasProperty("stops", hasSize(5))
            )
        ));
    }

    @Test(expected = NotFoundException.class)
    public void byName_notFound() {
        service.byName("notFound").blockingGet();
    }

    @Test
    public void byName_found() {
        final LineResponse response = service.byName("M4").blockingGet();

        assertThat(response, allOf(
            hasProperty("name", is("M4")),
            hasProperty("delays", hasSize(1)),
            hasProperty("stops", hasSize(5))
        ));
    }
}
