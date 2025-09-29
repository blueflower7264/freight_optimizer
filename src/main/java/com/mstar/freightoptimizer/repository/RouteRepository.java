package com.mstar.freightoptimizer.repository;

import com.mstar.freightoptimizer.model.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Long> {
}
