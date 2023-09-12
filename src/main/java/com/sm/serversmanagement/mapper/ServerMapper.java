package com.sm.serversmanagement.mapper;

import com.sm.serversmanagement.model.Server;
import com.sm.serversmanagement.request.ServerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapper {

    private final ModelMapper modelMapper;

    public ServerRequest convertToRequest(Server server) {
        return this.modelMapper.convertToRequest(server);
    }
}
