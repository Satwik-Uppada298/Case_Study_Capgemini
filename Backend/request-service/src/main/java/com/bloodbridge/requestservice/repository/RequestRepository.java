package com.bloodbridge.requestservice.repository;

import com.bloodbridge.requestservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserId(Long userId);
    List<Request> findByStatus(Request.RequestStatus status);
    List<Request> findByBloodType(String bloodType);
}
