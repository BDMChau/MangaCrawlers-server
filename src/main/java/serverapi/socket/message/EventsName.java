package serverapi.socket.message;

import lombok.Getter;

@Getter
public class EventsName {
    String UPDATE_SOCKETID = "updateSocketId";

    // from client
    String SPECIFIC_USERS = "sendMessageToSpecificUsers_server";
    String ALL_USERS = "sendMessageToAllUsers_server";

    // to client
    String FROM_SERVER_TO_SPECIFIC_USERS = "fromServerToSpecificUsers_client";
    String FROM_SERVER_TO_ALL_USERS = "fromServerToAllUsers_client";
}
