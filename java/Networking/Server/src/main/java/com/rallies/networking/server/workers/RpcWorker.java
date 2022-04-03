package com.rallies.networking.server.workers;

import com.rallies.business.api.AuthenticationException;
import com.rallies.business.api.RalliesObserver;
import com.rallies.business.api.RallyApplicationServices;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;
import com.rallies.exceptions.ExceptionBaseClass;
import com.rallies.networking.protocols.dto.*;
import com.rallies.networking.protocols.rcpprotocol.RpcRequest;
import com.rallies.networking.protocols.rcpprotocol.RpcRequestType;
import com.rallies.networking.protocols.rcpprotocol.RpcResponse;
import com.rallies.networking.protocols.rcpprotocol.RpcResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcWorker implements Runnable, RalliesObserver {
    private boolean clientIsConnected;
    private Socket clientSocket;
    private ObjectInputStream clientRequestStream;
    private ObjectOutputStream serverResponseStream;
    private RallyApplicationServices services;

    private Logger logger = LogManager.getLogger(RpcWorker.class);

    public RpcWorker(Socket clientSocket, RallyApplicationServices services) {
        this.clientIsConnected = true;
        this.services = services;

        try {
            this.clientSocket = clientSocket;
            serverResponseStream = new ObjectOutputStream(clientSocket.getOutputStream());
            serverResponseStream.flush();
            clientRequestStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.error("Couldn't create streams for worker: " + e);
        }
    }

    @Override
    public void run() {
        while (clientIsConnected) {
            try {
                RpcRequest request = (RpcRequest) clientRequestStream.readObject();
                RpcResponse response = handleClientRequest(request);
                sendResponseToClient(response);
            } catch (ClassNotFoundException exception) {
                logger.error("Object read from input stream is not found: " + exception);
            } catch (InvalidRpcRequestException exception) {
                logger.error("Request had invalid type");
            } catch (IOException exception) {
                logger.error("Error occurred while reading request: " + exception);
            }
        }

        closeConnectionStreams();
    }

    private void closeConnectionStreams() {
        try {
            clientRequestStream.close();
            serverResponseStream.flush();
            serverResponseStream.close();
            clientSocket.close();
        } catch (IOException e) {
            logger.error("Error occurred while closing connection streams: " + e);
        }
    }

    private static final RpcResponse OK_LOGIN_RESPONSE = new RpcResponse
            .RpcResponseBuilder()
            .setResponseType(RpcResponseType.OK_LOGIN)
            .build();

    private static final RpcResponse OK_LOGOUT_RESPONSE = new RpcResponse
            .RpcResponseBuilder()
            .setResponseType(RpcResponseType.OK_LOGOUT)
            .build();

    private RpcResponse handleClientRequest(RpcRequest clientRequest) {
        RpcResponse response = null;

        if (clientRequest.getRequestType() == RpcRequestType.LOGIN) {
            response = handleLoginRequest(clientRequest);
        }

        if (clientRequest.getRequestType() == RpcRequestType.LOGOUT) {
            return handleLogoutRequest();
        }

        if (clientRequest.getRequestType() == RpcRequestType.GET_ALL_MEMBERS_OF_TEAM) {
            return handleGetAllMembersOfTeamRequest(clientRequest);
        }

        if (clientRequest.getRequestType() == RpcRequestType.GET_ALL_TEAMS)
            return handleGetAllTeamsRequest(clientRequest);

        if (clientRequest.getRequestType() == RpcRequestType.GET_ALL_RALLIES)
            return handleGetAllRalliesRequest(clientRequest);

        if (clientRequest.getRequestType() == RpcRequestType.GET_PARTICIPANT_BY_NAME)
            return handleGetParticipantByNameRequest(clientRequest);

        if (clientRequest.getRequestType() == RpcRequestType.ADD_PARTICIPANT)
            return handleAddParticipantRequest(clientRequest);

        if (clientRequest.getRequestType() == RpcRequestType.ADD_RALLY)
            return handleAddRallyRequest(clientRequest);

        if (clientRequest.getRequestType() == RpcRequestType.ADD_TEAM)
            return handleAddTeamRequest(clientRequest);

        if (clientRequest.getRequestType() == RpcRequestType.GET_TEAM_BY_NAME)
            return handleGetTeamByNameRequest(clientRequest);

        if (response == null)
            throw new InvalidRpcRequestException();
        return response;
    }

    private RpcResponse handleGetTeamByNameRequest(RpcRequest clientRequest) {
        try {
            var dto = (TeamDto)clientRequest.getData();
            var teamName = dto.getTeamName();
            var addedTeamOptional = services.getTeamByName(teamName);
            TeamDto responseDto = null;

            if (addedTeamOptional.isPresent())
                responseDto = DtoUtils.getDtoFromModel(addedTeamOptional.get());

            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(responseDto)
                    .build();

        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleAddTeamRequest(RpcRequest clientRequest) {
        try {
            var dto = (TeamDto) clientRequest.getData();
            String teamName = dto.getTeamName();
            var addedTeam = services.addTeam(teamName);

            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(DtoUtils.getDtoFromModel(addedTeam))
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleAddRallyRequest(RpcRequest clientRequest) {
        try {
            var dto = (RallyDto)clientRequest.getData();
            int engineCap = Integer.parseInt(dto.getEngineCapacity());
            var rally = services.addRally(engineCap);

            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(DtoUtils.getDtoFromModel(rally))
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleAddParticipantRequest(RpcRequest clientRequest) {
        try {
            ParticipantDto dto = (ParticipantDto) clientRequest.getData();
            Long teamId = Long.valueOf(dto.getTeamId());
            String teamName = dto.getTeamName();

            Long rallyId = Long.valueOf(dto.getRallyId());
            int engCapacity = Integer.parseInt(dto.getEngineCapacity());

            Team team = new Team(teamName);
            team.setId(teamId);

            Rally rally = new Rally(engCapacity);
            rally.setId(rallyId);

            Participant addedParticipant = services.addParticipant(team, rally, dto.getParticipantName());

            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(DtoUtils.getDtoFromModel(addedParticipant))
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleGetParticipantByNameRequest(RpcRequest clientRequest) {
        try {
            var participant = services.getParticipantByName(clientRequest.getData().toString());

            ParticipantDto dtoResponse = null;
            if (participant.isPresent())
                dtoResponse = DtoUtils.getDtoFromModel(participant.get());
            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(DtoUtils.getDtoFromModel(participant))
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleGetAllRalliesRequest(RpcRequest clientRequest) {
        try {
            var rallies = services.getAllRallies();
            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(DtoUtils.getDtoFromModel(rallies.toArray(new Rally[rallies.size()])))
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleGetAllTeamsRequest(RpcRequest clientRequest) {
        try {
            var teams = services.getAllTeams();
            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(DtoUtils.getDtoFromModel(teams.toArray(new Team[teams.size()])))
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleGetAllMembersOfTeamRequest(RpcRequest clientRequest) {
        logger.info("Searching for team members");
        String teamName = clientRequest.getData().toString();

        try {
            var membersOfTeam = services.getAllMembersOfTeam(teamName);
            return new RpcResponse
                    .RpcResponseBuilder()
                    .setData(DtoUtils.getDtoFromModel(membersOfTeam.toArray(new Participant[membersOfTeam.size()])))
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private RpcResponse handleLogoutRequest() {
        logger.info("User logging out...");
        services.logout();
        clientIsConnected = false;
        return OK_LOGOUT_RESPONSE;
    }

    private RpcResponse handleLoginRequest(RpcRequest clientRequest) {
        logger.info("Handling login request...");

        UserDto dto = (UserDto) clientRequest.getData();
        String username = dto.getUsername();
        String password = dto.getPassword();

        try {
            services.login(username, password);
            logger.info("User authentication succeeded for " + username + " " + password);
            return OK_LOGIN_RESPONSE;
        } catch (AuthenticationException exception) {
            clientIsConnected = false;
            logger.error("Authentication exception thrown for user " + username + " " + password);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.LOGIN_ERROR)
                    .build();
        } catch (ExceptionBaseClass exception) {
            clientIsConnected = false;
            logger.error("Server error occurred: " + exception);
            return new RpcResponse.RpcResponseBuilder()
                    .setData(exception.getMessage())
                    .setResponseType(RpcResponseType.SERVER_ERROR)
                    .build();
        }
    }

    private void sendResponseToClient(RpcResponse response) {
        try {
            serverResponseStream.writeObject(response);
            serverResponseStream.flush();
        } catch (IOException e) {
            logger.error("Error occurred while sending response to client: " + e);
        }
    }

    @Override
    public void updateTeamWasAdded(Team addedTeam) {
        RpcResponse response = new RpcResponse
                .RpcResponseBuilder()
                .setResponseType(RpcResponseType.UPDATE_TEAMS)
                .setData(DtoUtils.getDtoFromModel(addedTeam))
                .build();

        sendResponseToClient(response);
    }

    @Override
    public void updateParticipantWasAdded(Participant addedParticipant) {
        RpcResponse response = new RpcResponse
                .RpcResponseBuilder()
                .setResponseType(RpcResponseType.UPDATE_PARTICIPANTS)
                .setData(DtoUtils.getDtoFromModel(addedParticipant))
                .build();

        sendResponseToClient(response);
    }

    @Override
    public void updateRallyWasAdded(Rally addedRally) {
        RpcResponse response = new RpcResponse
                .RpcResponseBuilder()
                .setResponseType(RpcResponseType.UPDATE_RALLIES)
                .setData(DtoUtils.getDtoFromModel(addedRally))
                .build();

        sendResponseToClient(response);
    }
}
