package com.bloodbridge.requestservice.service;

import com.bloodbridge.requestservice.dto.ApiResponse;
import com.bloodbridge.requestservice.dto.RequestDTO;

import java.util.List;

public interface RequestService {
    List<RequestDTO> getAllRequests();
    RequestDTO getRequestById(Long id);
    List<RequestDTO> getRequestsByUserId(Long userId);
    List<RequestDTO> getRequestsByStatus(String status);
    List<RequestDTO> getRequestsByBloodType(String bloodType);
    RequestDTO createRequest(RequestDTO requestDTO);
    RequestDTO updateRequest(Long id, RequestDTO requestDTO);
    ApiResponse approveRequest(Long id);
    ApiResponse rejectRequest(Long id);
    ApiResponse deleteRequest(Long id);
}
