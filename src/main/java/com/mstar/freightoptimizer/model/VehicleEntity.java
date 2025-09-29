package com.mstar.freightoptimizer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehicles")
@Data
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacity;

    @Column(name = "shift_start")
    private String shiftStart;

    @Column(name = "shift_end")
    private String shiftEnd;
}
