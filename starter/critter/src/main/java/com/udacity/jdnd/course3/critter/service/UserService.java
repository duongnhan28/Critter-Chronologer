package com.udacity.jdnd.course3.critter.service;


import com.udacity.jdnd.course3.critter.entity.EmployeeEntity;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.OwnerEntity;
import com.udacity.jdnd.course3.critter.exception.CommonException;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.OwnerRepository;
import com.udacity.jdnd.course3.critter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    OwnerRepository ownerRepository;

    public OwnerEntity saveCustomer(OwnerEntity owner){
        return this.ownerRepository.save(owner);
    }

    public EmployeeEntity saveEmployee(EmployeeEntity employee){
    return this.employeeRepository.save(employee);
    }


    // finding the employee with the given id, and if it's not already recorded in DB throw proper exception
    public EmployeeEntity findEmployeeById(Long id){
        Optional<EmployeeEntity> optionalEmployee = this.employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            EmployeeEntity employee = optionalEmployee.get();
            return employee;
        }
         return optionalEmployee.orElseThrow(() -> new CommonException("Not found Employee" + id));
    }

    public OwnerEntity findOwnerById(Long id){
        Optional<OwnerEntity> optionalOwner = this.ownerRepository.findById(id);
        if(optionalOwner.isPresent()){
            OwnerEntity owner = optionalOwner.get();
            return owner;
        }
        return optionalOwner.orElseThrow(CommonException::new);
    }

    public List<OwnerEntity> findAllOwners(){
        return this.ownerRepository.findAll();
    }


    public void updateDaysAvailable(Set<DayOfWeek> daysAvailable, Long id){
        EmployeeEntity employee = this.findEmployeeById(id);

        employee.setDaysAvailable(daysAvailable);
        this.employeeRepository.save(employee);
    }

    public List<EmployeeEntity> findEmployeesForService(Set<EmployeeSkill> skills, LocalDate date){
        List<EmployeeEntity> availableEmployees = new ArrayList<>();
        List<EmployeeEntity> employees = this.employeeRepository.findEmployeesByDaysAvailable(date.getDayOfWeek());

        for (EmployeeEntity employee : employees) {
            if (employee.getSkills().containsAll(skills)) {
                availableEmployees.add(employee);
            }
        }
        return availableEmployees;
    }
}
