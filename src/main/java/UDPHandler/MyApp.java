//package UDPHandler;
//
//import io.quarkus.runtime.QuarkusApplication;
//import io.quarkus.runtime.Startup;
//import io.quarkus.runtime.annotations.QuarkusMain;
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//
//@QuarkusMain
//public class MyApp implements QuarkusApplication {
//    @Inject
//    UdpMessageHandler udpMessageHandler;
//
//    @Override
//    public int run(String... args) throws Exception {
//        udpMessageHandler.startUdpServer();
//        return 0;
//    }
//}
