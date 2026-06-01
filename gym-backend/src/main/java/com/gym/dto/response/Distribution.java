package com.gym.dto.response;

import lombok.Data;

@Data
public class Distribution {

    private Long groupClass;

    private Long privateClass;

    private Long activeCourse;

    private Long cancelledCourse;

    private Long completedOrder;

    private Long noShowOrder;
}
