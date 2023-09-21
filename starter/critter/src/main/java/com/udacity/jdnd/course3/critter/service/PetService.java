package com.udacity.jdnd.course3.critter.service;


import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.exception.CommonException;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    public PetEntity savePet(PetEntity pet){
        return petRepository.save(pet);
    }

    public PetEntity getPetByPetId(Long petId){
        Optional<PetEntity> optionalPet = this.petRepository.findById(petId);
        if(optionalPet.isPresent()){
            return optionalPet.get();
        }
        return optionalPet.orElseThrow(() -> new CommonException("Not found pet" + petId));
    }

    public List<PetEntity> getPetsOfAnOwner(Long ownerId){
        return this.petRepository.findPetsByOwnerId(ownerId);

    }

    public List<PetEntity> findAllPets(){
        return this.petRepository.findAll();
    }
}
