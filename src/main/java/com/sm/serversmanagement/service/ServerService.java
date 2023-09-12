package com.sm.serversmanagement.service;

import com.sm.serversmanagement.apiResponse.ApiResponse;
import com.sm.serversmanagement.request.ServerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public interface ServerService {

    ResponseEntity<ApiResponse> getAllServers(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String search,
            boolean deleted
    );
    ResponseEntity<ApiResponse> getServer(
            Long id
    );
    ResponseEntity<ApiResponse> addServer(ServerRequest serverRequest);

    ResponseEntity<ApiResponse> updateServer(Long id, ServerRequest serverRequest);

    ResponseEntity<ApiResponse> archiveServer(Long id);

    ResponseEntity<ApiResponse> activateServer(Long id);

    ResponseEntity<ApiResponse> deleteServer(Long id);

    ResponseEntity<ApiResponse> pingServer(String ipAddress) throws IOException;

}
