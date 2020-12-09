package com.shawn.user.repostitory;

import com.shawn.user.model.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarTypeRepository extends JpaRepository<CarType, Long> {

    Optional<CarType> findByType(String type);

}
