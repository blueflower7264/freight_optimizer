package com.mstar.freightoptimizer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "route_stops")
@Data
public class RouteStopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stop_sequence")
    private int stopSequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private RouteEntity route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
}
