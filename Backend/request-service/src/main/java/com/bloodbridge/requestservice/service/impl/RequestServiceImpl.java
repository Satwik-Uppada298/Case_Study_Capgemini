package com.bloodbridge.requestservice.service.impl;

import com.bloodbridge.requestservice.dto.ApiResponse;
import com.bloodbridge.requestservice.dto.RequestDTO;
import com.bloodbridge.requestservice.entity.Request;
import com.bloodbridge.requestservice.repository.RequestRepository;
import com.bloodbridge.requestservice.service.RequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {
    
    @Autowired
    private RequestRepository requestRepository;
    
    @Override
    public List<RequestDTO> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public RequestDTO getRequestById(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        return convertToDTO(request);
    }
    
    @Override
    public List<RequestDTO> getRequestsByUserId(Long userId) {
        List<Request> requests = requestRepository.findByUserId(userId);
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RequestDTO> getRequestsByStatus(String status) {
        Request.RequestStatus requestStatus = Request.RequestStatus.valueOf(status.toUpperCase());
        List<Request> requests = requestRepository.findByStatus(requestStatus);
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RequestDTO> getRequestsByBloodType(String bloodType) {
        List<Request> requests = requestRepository.findByBloodType(bloodType);
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public RequestDTO createRequest(RequestDTO requestDTO) {
        Request request = convertToEntity(requestDTO);
        request.setStatus(Request.RequestStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());
        Request savedRequest = requestRepository.save(request);
        return convertToDTO(savedRequest);
    }
    
    @Override
    public RequestDTO updateRequest(Long id, RequestDTO requestDTO) {
        Request existingRequest = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        
        // Preserve some fields
        Request.RequestStatus currentStatus = existingRequest.getStatus();
        LocalDateTime requestedAt = existingRequest.getRequestedAt();
        LocalDateTime fulfilledAt = existingRequest.getFulfilledAt();
        
        BeanUtils.copyProperties(requestDTO, existingRequest, "id", "status", "requestedAt", "fulfilledAt");
        
        existingRequest.setStatus(currentStatus);
        existingRequest.setRequestedAt(requestedAt);
        existingRequest.setFulfilledAt(fulfilledAt);
        
        Request updatedRequest = requestRepository.save(existingRequest);
        return convertToDTO(updatedRequest);
    }
    
    @Override
    public ApiResponse approveRequest(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        
        request.setStatus(Request.RequestStatus.APPROVED);
        request.setFulfilledAt(LocalDateTime.now());
        requestRepository.save(request);
        
        return new ApiResponse(true, "Request approved successfully");
    }
    
    @Override
    public ApiResponse rejectRequest(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        
        request.setStatus(Request.RequestStatus.REJECTED);
        requestRepository.save(request);
        
        return new ApiResponse(true, "Request rejected successfully");
    }
    
    @Override
    public ApiResponse deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found with id: " + id);
        }
        
        requestRepository.deleteById(id);
        return new ApiResponse(true, "Request deleted successfully");
    }
    
    private RequestDTO convertToDTO(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        BeanUtils.copyProperties(request, requestDTO);
        requestDTO.setStatus(request.getStatus().name());
        return requestDTO;
    }
    
    private Request convertToEntity(RequestDTO requestDTO) {
        Request request = new Request();
        BeanUtils.copyProperties(requestDTO, request);
        if (requestDTO.getStatus() != null) {
            request.setStatus(Request.RequestStatus.valueOf(requestDTO.getStatus()));
        }
        return request;
    }
}
