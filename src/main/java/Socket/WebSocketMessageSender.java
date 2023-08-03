package Socket;

import CAT240GEOJSON.C240GeoJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Set;

@ApplicationScoped
public class WebSocketMessageSender {

//    public static void sendMessageToAll(String message) {
//        Set<Session> sessions = SocketClass.getSessions();
//        for (Session session : sessions) {
//            if (session.isOpen()) {
//                try {
//                    session.getBasicRemote().sendText(message);
//                } catch (IOException e) {
//                    // Handle the exception if sending the message fails
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public static void sendMessageToAll(C240GeoJson geoJson) {
        Set<Session> sessions = SocketClass.getSessions();
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(geoJson);
                    session.getBasicRemote().sendText(json);
                } catch (IOException e) {
                    // Handle the exception if sending the message fails
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void sendMessageToAll(String geoJson) {
//        Set<Session> sessions = SocketClass.getSessions();
//        for (Session session : sessions) {
//            if (session.isOpen()) {
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String json = objectMapper.writeValueAsString(geoJson);
//                    session.getBasicRemote().sendText(json);
//                } catch (IOException e) {
//                    // Handle the exception if sending the message fails
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
