package com.udacity.jdnd.course3.critter.repository;


import com.udacity.jdnd.course3.critter.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> getScheduleByPets_Id(Long id);
    List<ScheduleEntity> getScheduleByEmployees_Id(Long employeeId);
}
