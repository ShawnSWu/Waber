package com.shawn.user.repostitory;

import com.shawn.user.model.entity.DriverCarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverCarTypeRepository extends JpaRepository<DriverCarType, Long> {

    Optional<DriverCarType> findByDriver(long id);

}
