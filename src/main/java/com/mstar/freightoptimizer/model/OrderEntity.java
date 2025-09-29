package com.mstar.freightoptimizer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int demand;

    private double latitude;

    private double longitude;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;
}
