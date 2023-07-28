package UDPHandler;

import CAT240.C240;
import CAT240RESOURCE.C240Handler;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

@ApplicationScoped
@RegisterForReflection
public class UdpMessageHandler {

    @Inject
    C240Handler c240Handler;

    public void startUdpServer() {
        try {
            DatagramSocket socket = new DatagramSocket(8888); // Port to listen on
            byte[] buffer = new byte[10000];

            System.out.println("UDP Server started and listening on port 5000...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());

                Jsonb jsonb = JsonbBuilder.create();
                C240 c240 = jsonb.fromJson(message, C240.class);
                c240Handler.setC240(c240);


                System.out.println(message);
                // Process the message or send it to other components as needed.

                // Clear the buffer for the next message
                buffer = new byte[10000];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
