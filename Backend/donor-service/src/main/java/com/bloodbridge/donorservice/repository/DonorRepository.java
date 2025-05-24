package com.bloodbridge.donorservice.repository;

import com.bloodbridge.donorservice.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    Optional<Donor> findByUserId(Long userId);
    List<Donor> findByBloodType(String bloodType);
    List<Donor> findByIsApproved(Boolean isApproved);
}
