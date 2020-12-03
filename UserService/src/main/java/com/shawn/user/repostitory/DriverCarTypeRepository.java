package com.shawn.user.repostitory;

import com.shawn.user.model.DriverCarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverCarTypeRepository extends JpaRepository<DriverCarType, Long> {

    DriverCarType findByDriver(long id);

}
