package com.mstar.freightoptimizer.repository;

import com.mstar.freightoptimizer.model.RouteStopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStopEntity, Long> {
}
