package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.EmployeeEntity;
import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.entity.ScheduleEntity;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.exception.CommonException;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        ScheduleEntity schedule = scheduleService.saveSchedule(convertScheduleDTOToScheduleEntity(scheduleDTO));
        return convertScheduleEntityToScheduleDTO(schedule);
        //throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleEntity> scheduleEntities = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        for (ScheduleEntity schedule : scheduleEntities){
            scheduleDTOS.add(convertScheduleEntityToScheduleDTO(schedule));
        }

        return scheduleDTOS;

        //throw new UnsupportedOperationException();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleEntity> schedule = scheduleService.getSchedulesByPetId(petId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        for (ScheduleEntity  schedule1: schedule){
            scheduleDTOS.add(convertScheduleEntityToScheduleDTO(schedule1));
        }
        return scheduleDTOS;
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleEntity> schedule = scheduleService.getScheduleByEmployee(employeeId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        for (ScheduleEntity  schedule1: schedule){
            scheduleDTOS.add(convertScheduleEntityToScheduleDTO(schedule1));
        }
        return scheduleDTOS;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleEntity> schedule = scheduleService.getScheduleByOwner(customerId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        for (ScheduleEntity  schedule1: schedule){
            scheduleDTOS.add(convertScheduleEntityToScheduleDTO(schedule1));
        }
        return scheduleDTOS;
    }


    private ScheduleDTO convertScheduleEntityToScheduleDTO(ScheduleEntity schedule) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        scheduleDTO.setActivities(schedule.getActivities());

        List<PetEntity> pets = schedule.getPets();
        List<Long> petId = new ArrayList<>();
        for (PetEntity pet : pets) {
            petId.add(pet.getId());
        }
        scheduleDTO.setPetIds(petId);
        List<EmployeeEntity> employees = schedule.getEmployees();
        List<Long> employeeId = new ArrayList<>();
        for (EmployeeEntity employee : employees) {
            employeeId.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeId);
        return scheduleDTO;

    }

    private ScheduleEntity convertScheduleDTOToScheduleEntity(ScheduleDTO scheduleDTO){
        ModelMapper modelMapper = new ModelMapper();
        ScheduleEntity schedule = modelMapper.map(scheduleDTO, ScheduleEntity.class);

        schedule.setActivities(scheduleDTO.getActivities());
        HashMap<Long, EmployeeEntity> employeeMap = new HashMap<>();
        for (Long employeeId : scheduleDTO.getEmployeeIds()) {
            Optional<EmployeeEntity> optionalEmployee = Optional.ofNullable(userService.findEmployeeById(employeeId));
            if (optionalEmployee.isPresent()) {
                employeeMap.put(employeeId, optionalEmployee.get());
            } else {
                throw new CommonException();
            }
        }
        schedule.setEmployees(new ArrayList<EmployeeEntity>(employeeMap.values()));
        HashMap<Long, PetEntity> petMap = new HashMap<>();
        for (Long petId : scheduleDTO.getPetIds()) {
            Optional<PetEntity> optionalPet = Optional.ofNullable(petService.getPetByPetId(petId));
            if (optionalPet.isPresent()) {
                petMap.put(petId, optionalPet.get());
            } else {
                throw new CommonException();
            }
        }
        schedule.setPets(new ArrayList<PetEntity>(petMap.values()));
        return schedule;
    }
}
