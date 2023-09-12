package com.sm.serversmanagement.controller;

import com.sm.serversmanagement.apiResponse.ApiResponse;
import com.sm.serversmanagement.request.ServerRequest;
import com.sm.serversmanagement.service.ServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllServers (
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean deleted
    ) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return this.serverService.getAllServers(pageNumber, pageSize, sortBy, sortDirection, search, deleted);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getServer (
            @PathVariable Long id
    ) {
        return this.serverService.getServer(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> addServer (
            @RequestBody @Valid ServerRequest serverRequest
    ) {
        return this.serverService.addServer(serverRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateServer (
            @PathVariable Long id,
            @RequestBody @Valid ServerRequest serverRequest
    ) {
        return this.serverService.updateServer(id, serverRequest);
    }

    @PutMapping("/archive/{id}")
    public ResponseEntity<ApiResponse> archiveServer (
            @PathVariable Long id
    ) {
        return this.serverService.archiveServer(id);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<ApiResponse> activateServer (
            @PathVariable Long id
    ) {
        return this.serverService.activateServer(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteServer (
            @PathVariable Long id
    ) {
        return this.serverService.deleteServer(id);
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<ApiResponse> pingServer (@PathVariable("ipAddress") String ipAddress) throws IOException {
        return this.serverService.pingServer(ipAddress);
    }

}
