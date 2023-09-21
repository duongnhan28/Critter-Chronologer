package com.udacity.jdnd.course3.critter.service;


import com.udacity.jdnd.course3.critter.entity.OwnerEntity;
import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.entity.ScheduleEntity;
import com.udacity.jdnd.course3.critter.exception.CommonException;
import com.udacity.jdnd.course3.critter.repository.OwnerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;

    public ScheduleEntity saveSchedule(ScheduleEntity schedule){
        return this.scheduleRepository.save(schedule);
    }

    public List<ScheduleEntity> getAllSchedules(){
        return this.scheduleRepository.findAll();
    }

    public List<ScheduleEntity> getSchedulesByPetId(Long petId){

        return this.scheduleRepository.getScheduleByPets_Id(petId);
    }

    // finding the customer with the given id, and if it's not already recorded in DB throw proper exception
    public List<ScheduleEntity> getScheduleByOwner(Long ownerId) {
        Optional<OwnerEntity> optionalOwner = this.ownerRepository.findById(ownerId);

        if (!optionalOwner.isPresent()) {
            throw new CommonException("not found Schedule: "+ ownerId);
        }
        else{
            OwnerEntity customer = optionalOwner.get();
            List<PetEntity> pets = customer.getPets();
            List<ScheduleEntity> schedules = new ArrayList<>();

            for (PetEntity pet : pets) {
                schedules.addAll(scheduleRepository.getScheduleByPets_Id(pet.getId()));
            }
            return schedules;
        }
    }

    public List<ScheduleEntity> getScheduleByEmployee(Long employeeId) {
        return scheduleRepository.getScheduleByEmployees_Id(employeeId);
    }
}
