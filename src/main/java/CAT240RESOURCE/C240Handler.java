package CAT240RESOURCE;

import CAT240.C240;
import CAT240GEOJSON.C240GeoJson;
import CAT240GEOJSON.C240GeoJsonFeature;
import CAT240GEOJSON.C240GeoJsonGeometry;
import CAT240GEOJSON.C240GeoJsonProperties;
import Socket.WebSocketMessageSender;
import com.len.ccs.common.kinematics.GeoCoordinate;
import com.len.ccs.common.kinematics.util.GeoUtil;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class C240Handler {

    @Inject
    Jsonb jsonb;

    // Inject Websokcet Class
    @Inject
    public WebSocketMessageSender webSocketMessageSender;



    C240 c240 = new C240();
    C240 c2401 = new C240();
    private static final String JSON_FILE_PATH = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\data.json";
    private static final String JSON_FILE_PATH2 = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\TestLowVidioRadar.json";
    double sAZ = 0.0;
    double eAZ = 0.17;

    int cellTest = 0;

    GeoCoordinate ownUnitStartAz = new GeoCoordinate(-6.949612491503703, 107.61957049369812);     // lat lon ownunit
    GeoCoordinate ownUnitEndtAZ = new GeoCoordinate(-6.949612491503703, 107.61957049369812);      // lat lon ownunit



//    @Scheduled(every = "1s")
//    public void simulasi()  throws  IOException{
//            String jsonData = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
//            c240 = jsonb.fromJson(jsonData, new C240(){}.getClass().getGenericSuperclass());
//            c240.getI041().setSTART_AZ(sAZ);
//            c240.getI041().setEND_AZ(eAZ);
//            generateGeoJSON(c240);
////            String jsonData1 = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH2)));
////            c2401 = jsonb.fromJson(jsonData1, new C240(){}.getClass().getGenericSuperclass());
////            c2401.getI041().setSTART_AZ(sAZ);
////            c2401.getI041().setEND_AZ(eAZ);
////            setC240(c2401);
//            sAZ = eAZ;
//            eAZ = eAZ + 0.17;
//    }



    // Generate Geojson
    public void generateGeoJSON(C240 c240)  {

        String vidioBlock = getVideoBlock(c240);
        List<C240GeoJsonFeature> c240GeoJsonFeaturesList = new ArrayList<>();
        C240GeoJson geoJson = new C240GeoJson();
        int resolusi = getRes(c240.getI048().getRES());
        int substringStart = 0;
        int substringEnd = resolusi;
        int cell = 0;
        double distanceCellStart = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (c240.getI041().getSTART_RG() + 1 -1) * (299792458 / 2);                     // Jarak (meter) cell mulai digambar berdasarkan bearing
        double getLatCrdRefStartAZ = GeoUtil.getInstance().getCrdFromBR(ownUnitStartAz, c240.getI041().getSTART_AZ(),distanceCellStart).getLatitude();              // lat point 1
        double getLonCrdRefStartAZ = GeoUtil.getInstance().getCrdFromBR(ownUnitStartAz, c240.getI041().getSTART_AZ(),distanceCellStart).getLongitude();             // lon point 1
        double getLatCrdRefEndAZ = GeoUtil.getInstance().getCrdFromBR(ownUnitEndtAZ, c240.getI041().getEND_AZ(), distanceCellStart).getLatitude();                  // lat point 4
        double getLonCrdRefEndAZ = GeoUtil.getInstance().getCrdFromBR(ownUnitEndtAZ, c240.getI041().getEND_AZ(), distanceCellStart).getLongitude();                 // lon point 4

        for (int k=0; k < (vidioBlock.length() / resolusi); k++){

            double getResolusi = ((double) Integer.parseInt(vidioBlock.substring(substringStart,substringEnd),16)) * 255 / 100 /100;  // substring 1 - 2 (11)

            if (getResolusi > 0.5){
                GeoCoordinate geoCrdRefStartAZ = new GeoCoordinate(getLatCrdRefStartAZ, getLonCrdRefStartAZ);
                GeoCoordinate geoCrdRefEndtAZ = new GeoCoordinate(getLatCrdRefEndAZ, getLonCrdRefEndAZ);

                double StartCell = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (k + 1 -1) * (299792458 / 2);  // Distance Meter from ownUnit
                double latPoint1 = GeoUtil.getInstance().getCrdFromBR(geoCrdRefStartAZ, c240.getI041().getSTART_AZ(),StartCell).getLatitude();            // lat point 1
                double lonPoint1 = GeoUtil.getInstance().getCrdFromBR(geoCrdRefStartAZ, c240.getI041().getSTART_AZ(),StartCell).getLongitude();           // lon point 1
                double latPoint4 = GeoUtil.getInstance().getCrdFromBR(geoCrdRefEndtAZ, c240.getI041().getEND_AZ(), StartCell).getLatitude();              // lat point 4
                double lonPoint4 = GeoUtil.getInstance().getCrdFromBR(geoCrdRefEndtAZ, c240.getI041().getEND_AZ(), StartCell).getLongitude();             // lon point 4

                GeoCoordinate nextPointStartAZ = new GeoCoordinate(latPoint1,lonPoint1); // ownunit
                GeoCoordinate nextPointEndAZ = new GeoCoordinate(latPoint4,lonPoint4);

                double range = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (0 + 2 -1) * (299792458 / 2);
                double latPoint2 = GeoUtil.getInstance().getCrdFromBR(nextPointStartAZ, c240.getI041().getSTART_AZ(),range).getLatitude();
                double lonPoint2 = GeoUtil.getInstance().getCrdFromBR(nextPointStartAZ, c240.getI041().getSTART_AZ(),range).getLongitude();
                double latPoint3 = GeoUtil.getInstance().getCrdFromBR(nextPointEndAZ,  c240.getI041().getEND_AZ(),range).getLatitude();
                double lonPoint3 = GeoUtil.getInstance().getCrdFromBR(nextPointEndAZ,  c240.getI041().getEND_AZ(),range).getLongitude();

                double[] cellPoint1 = new double[]{lonPoint1, latPoint1};
                double[] cellPoint2 = new double[]{lonPoint2, latPoint2};
                double[] cellPoint3 = new double[]{lonPoint3, latPoint3};
                double[] cellPoint4 = new double[]{lonPoint4, latPoint4};
                double[][] polygonCell = new double[][]{cellPoint1, cellPoint2,cellPoint3,cellPoint4};

                C240GeoJsonGeometry geoJsonGeometry = new C240GeoJsonGeometry();
                C240GeoJsonProperties geoJsonProperties = new C240GeoJsonProperties();
                C240GeoJsonFeature geoJsonFeature = new C240GeoJsonFeature();

                geoJsonGeometry.setCoordinates(new double[][][]{polygonCell});
                geoJsonGeometry.setType("Polygon");
                geoJsonProperties.setOpacity(getResolusi);
                geoJsonProperties.setColor("#00ff33");
                geoJsonFeature.setType("Feature");
                geoJsonFeature.setGeometry(geoJsonGeometry);
                geoJsonFeature.setProperties(geoJsonProperties);
                c240GeoJsonFeaturesList.add(geoJsonFeature);
            }
            substringStart = substringEnd;
            substringEnd = substringEnd + resolusi;
            cell = cell +1;
        }
        geoJson.setType("FeatureCollection");
        geoJson.setFeatures(c240GeoJsonFeaturesList);
        webSocketMessageSender.sendMessageToAll(geoJson);
    }

    // Get Vidio Block
    public String getVideoBlock(C240 c240){
        String vidioBlock = "";
        if (c240.getI050() != null){
            vidioBlock = c240.getI050().getVIDEO();
        } else if (c240.getI051() != null){
            vidioBlock = c240.getI051().getVIDEO();
        } else if (c240.getI052() != null){
            vidioBlock = c240.getI052().getVIDEO();
        }
        return vidioBlock;
    }


    // Get Vidio Resolution
    public int getRes(int res){
        int resolusi = 0;
        if (res == 3){
            resolusi =  1;
        } else if (res == 4){
            resolusi =  2;
        } else if (res == 5){
            resolusi =  4;
        } else if (res == 6){
            resolusi =  8;
        }
        return resolusi;
    }


    public int getCellTest() {
        return cellTest;
    }
}
