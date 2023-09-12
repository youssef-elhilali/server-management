package com.sm.serversmanagement.mapper;

import com.sm.serversmanagement.model.Server;
import com.sm.serversmanagement.request.ServerRequest;
import com.sm.serversmanagement.response.ServerResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ServerMapper {

    private final ModelMapper modelMapper;

    public ServerRequest convertToRequest(Server server) {
        return this.modelMapper.map(server, ServerRequest.class);
    }

    public Server convertToEntity(ServerRequest serverRequest) {
        return this.modelMapper.map(serverRequest, Server.class);
    }

    public ServerResponse convertToResponse(Server server) {
        return this.modelMapper.map(server, ServerResponse.class);
    }

}
