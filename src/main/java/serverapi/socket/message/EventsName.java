package serverapi.socket.message;

import lombok.Getter;

@Getter
public class EventsName {
    String UPDATE_SOCKETID = "updateSocketId";
    String NOTIFY_ONLINE = "notifyonline";

    String SEND_OK = "sendmessage_ok";
    String SEND_FAILED = "sendmessage_failed";

    // from client
    String SPECIFIC_USERS = "sendMessageToSpecificUsers_server";
    String ALL_USERS = "sendMessageToAllUsers_server";

    // to client
    String FROM_SERVER_TO_SPECIFIC_USERS = "fromServerToSpecificUsers_client";
    String FROM_SERVER_TO_ALL_USERS = "fromServerToAllUsers_client";
}
