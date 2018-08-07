package com.mobimeo.verspaetung.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Delay {

    private String lineName;

    private Integer delay;
}
