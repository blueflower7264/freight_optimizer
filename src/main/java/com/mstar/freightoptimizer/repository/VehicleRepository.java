package com.mstar.freightoptimizer.repository;

import com.mstar.freightoptimizer.model.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
}
