package com.sm.serversmanagement.serviceImplementation;

import com.sm.serversmanagement.apiResponse.ApiResponse;
import com.sm.serversmanagement.exceptions.PaginationException;
import com.sm.serversmanagement.exceptions.ServerException;
import com.sm.serversmanagement.mapper.ServerMapper;
import com.sm.serversmanagement.model.Server;
import com.sm.serversmanagement.repository.ServerRepository;
import com.sm.serversmanagement.request.ServerRequest;
import com.sm.serversmanagement.response.ServerResponse;
import com.sm.serversmanagement.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sm.serversmanagement.enumerations.Status.SERVER_DOWN;
import static com.sm.serversmanagement.enumerations.Status.SERVER_UP;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final ServerMapper serverMapper;

    @Override
    public ResponseEntity<ApiResponse> getAllServers(int pageNumber, int pageSize, String sortBy, String sortDirection, String search, boolean deleted) {

        log.info("Fetching all servers from database");
        List<Server> servers;
        if (pageNumber < 0)
            throw new PaginationException("Page number cannot be less than 0");
        if (pageSize < 0 || pageSize > 100)
            throw new PaginationException("Page size cannot be less than 0 and greater than 100");
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, direction, sortBy);
        if (search != null) {
            servers = this.serverRepository.findAllServersBySearch(search, deleted);
        } else {
            servers = this.serverRepository.findAllServers(deleted);
        }
        return this.responseApi(
                "Servers retrieved successfully",
                this.paginationResponse(serversPage(servers, pageable), pageSize),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getServer(Long id) {
        log.info("Fetching a server from database with id = {}", id);
        try {
            Server server = this.serverRepository.findById(id).orElseThrow(
                    () -> new ServerException("Cannot find a server with this id")
            );
            ServerResponse serverResponse = this.serverMapper.convertToResponse(server);
            return this.responseApi(
                    "Server retrieved successfully",
                    Map.of("server", serverResponse),
                    HttpStatus.OK
            );
        } catch (ServerException ex) {
            throw new ServerException("Error encountering while extracting a server with this id");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addServer(ServerRequest serverRequest) {
        log.info("Adding a server to database with data = {}", serverRequest);
        try {
            Server server = this.serverMapper.convertToEntity(serverRequest);
            this.serverRepository.save(server);
            ServerResponse serverResponse = this.serverMapper.convertToResponse(server);
            return this.responseApi(
                    "Server added successfully",
                    Map.of("server", serverResponse),
                    HttpStatus.CREATED
            );
        } catch (ServerException ex) {
            throw new ServerException("Error encountering while saving this server");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateServer(Long id, ServerRequest serverRequest) {
        log.info("Updating the server with id = {}", id);
        try {
            Server existingServer = this.serverRepository.findById(id).orElseThrow(
                    () -> new ServerException("Cannot find a server with this id")
            );
            Server server = this.serverMapper.convertToEntity(serverRequest);
            if (serverRequest.getIpAddress() == null) {
                server.setIpAddress(existingServer.getIpAddress());
            }
            if (serverRequest.getType() == null) {
                server.setType(existingServer.getType());
            }
            if (serverRequest.getName() == null) {
                server.setName(existingServer.getName());
            }
            if (serverRequest.getMemory() == null) {
                server.setMemory(existingServer.getMemory());
            }
            if (serverRequest.getStatus() == null) {
                server.setStatus(existingServer.getStatus());
            }
            if (serverRequest.getImageUrl() == null) {
                server.setImageUrl(existingServer.getImageUrl());
            }
            server.setId(id);
            server.setCreationDate(existingServer.getCreationDate());
            log.info("The updated server with id = {} is = {}", id, server);
            this.serverRepository.save(server);
            ServerResponse serverResponse = this.serverMapper.convertToResponse(server);
            return this.responseApi(
                    "Server updated successfully",
                    Map.of("server", serverResponse),
                    HttpStatus.OK
            );
        } catch (ServerException ex) {
            throw new ServerException("Error encountering while updating this server");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> archiveServer(Long id) {

        log.info("Archiving the server with id = {}", id);
        try {
            Server server = this.serverRepository.findById(id).orElseThrow(
                    () -> new ServerException("Cannot find a server with this id")
            );
            server.setId(id);
            server.setDeleted(true);
            this.serverRepository.save(server);
            ServerResponse serverResponse = this.serverMapper.convertToResponse(server);
            return this.responseApi(
                    "Server archived successfully",
                    Map.of("server", serverResponse),
                    HttpStatus.OK
            );
        } catch (ServerException ex) {
            throw new ServerException("Error encountering while archiving this server");
        }

    }

    @Override
    public ResponseEntity<ApiResponse> activateServer(Long id) {

        log.info("Activating the server with id = {}", id);
        try {
            Server server = this.serverRepository.findById(id).orElseThrow(
                    () -> new ServerException("Cannot find a server with this id")
            );
            server.setId(id);
            server.setDeleted(false);
            this.serverRepository.save(server);
            ServerResponse serverResponse = this.serverMapper.convertToResponse(server);
            return this.responseApi(
                    "Server activated successfully",
                    Map.of("server", serverResponse),
                    HttpStatus.OK
            );
        } catch (ServerException ex) {
            throw new ServerException("Error encountering while activating this server");
        }

    }

    @Override
    public ResponseEntity<ApiResponse> deleteServer(Long id) {

        log.info("Deleting the server with id = {}", id);
        try {
            Server server = this.serverRepository.findById(id).orElseThrow(
                    () -> new ServerException("Cannot find a server with this id")
            );
            ServerResponse serverResponse = this.serverMapper.convertToResponse(server);
            this.serverRepository.delete(server);
            return this.responseApi(
                    "Server deleted successfully",
                    Map.of("server", serverResponse),
                    HttpStatus.OK
            );
        } catch (ServerException ex) {
            throw new ServerException("Error encountering while deleting this server from database");
        }

    }

    @Override
    public ResponseEntity<ApiResponse> pingServer(String ipAddress) throws IOException {

        log.info("Pinging the server with ip address = {}", ipAddress);
        Server server = this.serverRepository.findServerByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        this.serverRepository.save(server);
        ServerResponse serverResponse = this.serverMapper.convertToResponse(server);
        return this.responseApi(
                serverResponse.getStatus() == SERVER_UP ? "Server pinged successfully":"Server didn't ping",
                Map.of("server", serverResponse),
                serverResponse.getStatus() == SERVER_UP ? HttpStatus.OK : HttpStatus.GATEWAY_TIMEOUT
        );

    }

    private Map<String, Object> paginationResponse(Page<Server> page, int size) {

        List<Server> listData = page.getContent();
        Map<String, Object> response = new HashMap<>();
        if (listData.isEmpty())
            response.put("message", "There is nothing to display");
        response.put("servers", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("pageSize", size);
        return response;

    }

    private Page<Server> serversPage (List<Server> servers, Pageable pageable) {

        List<Server> listServers;
        int startItem = pageable.getPageNumber() * pageable.getPageSize();
        if (servers.size() < startItem) {
            listServers = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageable.getPageSize(), servers.size());
            listServers = servers.subList(startItem, toIndex);
        }
        return new PageImpl<>(listServers, pageable, servers.size());

    }

    private ResponseEntity<ApiResponse> responseApi(String message, Map<?, ?> data, HttpStatus status) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .time(LocalDateTime.now())
                        .data(data)
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );

    }
}
