//package org.acme;
//
//
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import jakarta.inject.Inject;
//
//import jakarta.json.bind.Jsonb;
//
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Path("/persons")
//public class PersonResource {
//
//    private static final String JSON_FILE_PATH = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\test.json";
//
//    @Inject
//    Jsonb jsonb;
//
//
//
//
//
//    Object object = new Object();
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<JsonObject> getPeople() throws IOException {
//        String jsonData = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
//
//
//        String test = jsonData.toLowerCase();
//
//        JsonArray jsonArray = new JsonArray(jsonData);
//        JsonObject I010 = jsonArray.getJsonObject(0).getJsonObject("I048");
//        JsonObject cuk = jsonArray.getJsonObject(0);
//        JsonArray cuk2 = cuk.getJsonArray("I051");
//
//        Integer check = jsonArray.getJsonObject(0).getJsonObject("I020").getJsonObject("MSG_INDEX").getInteger("val");
//
//        List<JsonObject> test2 = new ArrayList<>();
//        for (int i=0; i < cuk2.size(); i++){
//            Integer pos = i;
//            JsonObject cuk3 = cuk2.getJsonObject(pos);
//            test2.add(cuk3);
//        }
//
//
//
////        String test = String.valueOf(JsonArray.of("I010"));
////        String  test = String.valueOf(jsonObject.getJsonObject("I010"));
////        String nama = jsonObject.getString("category");
//
////
//        Person person = new Person();
//        Cat240Item010 cat240Item010 = new Cat240Item010();
//        Cat240Item000 cat240Item000 = new Cat240Item000();
//        Cat240Item020 cat240Item020 = new Cat240Item020();
//        Cat240Iten040 cat240Iten040 = new Cat240Iten040();
//        Cat240Item048 cat240Item048 = new Cat240Item048();
//        Cat240Item051 cat240Item051 = new Cat240Item051();
//
//
//
//        person = jsonb.fromJson(test, new Person(){}.getClass().getGenericSuperclass());
//
//
//        cat240Item000 = jsonb.fromJson(test, new Cat240Item000(){}.getClass().getGenericSuperclass());
//        cat240Item010 = jsonb.fromJson(test, new Cat240Item010(){}.getClass().getGenericSuperclass());
//        cat240Item020 = jsonb.fromJson(test, new Cat240Item020(){}.getClass().getGenericSuperclass());
//        cat240Iten040 = jsonb.fromJson(test, new Cat240Iten040(){}.getClass().getGenericSuperclass());
//
//
//
//        person.setI000(cat240Item000);
//        person.setI101(cat240Item010);
//        person.setI020(cat240Item020);
//        person.setI040(cat240Iten040);
//
//////
////
////        ORANG orang = new ORANG();
////        orang = jsonb.fromJson(jsonData, new ORANG(){}.getClass().getGenericSuperclass());
////
////        C240I249 c240I249 = new C240I249();
////        c240I249 = jsonb.fromJson(jsonData, new C240I249(){}.getClass().getGenericSuperclass());
//
//
//
//
//
//
//        return test2;
//    }
//}
