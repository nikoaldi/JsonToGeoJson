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

    String check = "";



    C240 c240 = new C240();
    C240 c2401 = new C240();
    private static final String JSON_FILE_PATH = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\data.json";
    private static final String JSON_FILE_PATH2 = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\TestLowVidioRadar.json";
    double sAZ = 0.0;
    double eAZ = 0.2;

//    List<C240GeoJson> listGeojson = new ArrayList<>();
//    List<Double> test = new ArrayList<>();


//    @Scheduled(every = "1s")
//    public void simulasi()  throws  IOException{
//            String jsonData = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
//            c240 = jsonb.fromJson(jsonData, new C240(){}.getClass().getGenericSuperclass());
//            c240.getI041().setSTART_AZ(sAZ);
//            c240.getI041().setEND_AZ(eAZ);
//            setC240(c240);
//            String jsonData1 = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH2)));
//            c2401 = jsonb.fromJson(jsonData1, new C240(){}.getClass().getGenericSuperclass());
//            c2401.getI041().setSTART_AZ(sAZ);
//            c2401.getI041().setEND_AZ(eAZ);
//            setC240(c2401);
//            sAZ = eAZ + 1;
//            eAZ = sAZ +2;
//    }


    public void setC240(C240 c240) {
        getRangeAndCellPosition(c240);
    }

    // Calculate Range
    public void getRangeAndCellPosition(C240 c240)  {
        GeoCoordinate geoCrdRefStartAZ = new GeoCoordinate(-6.949612491503703, 107.61957049369812);     // lat lon ownunit
        GeoCoordinate geoCrdRefEndtAZ = new GeoCoordinate(-6.949612491503703, 107.61957049369812);      // lat lon ownunit
        double cellStartAZ = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (c240.getI041().getSTART_RG() + 1 -1) * (299792458 / 2);           //  0.0
        double cellEndAZ = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (c240.getI041().getSTART_RG() + 1 -1) * (299792458 / 2);             //  0.0
        double range = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (c240.getI041().getSTART_RG() + 2 -1) * (299792458 / 2) - cellStartAZ;   // 9.765624948527275
        double latAwalAZ = GeoUtil.getInstance().getCrdFromBR(geoCrdRefStartAZ, c240.getI041().getSTART_AZ(),cellStartAZ).getLatitude();            // -6.9496124915037
        double lonAwalAZ = GeoUtil.getInstance().getCrdFromBR(geoCrdRefStartAZ, c240.getI041().getSTART_AZ(),cellStartAZ).getLongitude();           // 107.61957049369812
        double latAkhirAZ = GeoUtil.getInstance().getCrdFromBR(geoCrdRefEndtAZ, c240.getI041().getEND_AZ(), cellEndAZ).getLatitude();               // -6.9496124915037
        double lonAkhirAZ = GeoUtil.getInstance().getCrdFromBR(geoCrdRefEndtAZ, c240.getI041().getEND_AZ(), cellEndAZ).getLongitude();              // 107.61957049369812

//        // check
//        check = "cellStartAZ = "+cellStartAZ+", cellEndAZ = "+cellEndAZ+", range = "+range+", latAwalAZ = "+latAwalAZ+", lonAwalAZ = "+lonAwalAZ+", latAkhirAZ = "+latAkhirAZ+", lonAkhirAZ = "+lonAkhirAZ;
        generateGeoJSON(c240, latAwalAZ, lonAwalAZ, latAkhirAZ, lonAkhirAZ, range);
    }



    // Generate Geojson
    public void generateGeoJSON(C240 c240, double latAwalAZ, double lonAwalAZ, double latAkhirAZ, double lonAkhirAZ, double range)  {


        double latAwalAZ1 = latAwalAZ;          // -6.9496124915037
        double lonAwalAZ1 = lonAwalAZ;          // 107.61957049369812
        double latAkhirAZ1 = latAkhirAZ;        // -6.9496124915037
        double lonAkhirAZ1 = lonAkhirAZ;        // 107.61957049369812
        int substringStart = 0;
        int substringEnd = 0;
        int resolusi = getRes(c240.getI048().getRES());
        String vidioBlock = getVideoBlock(c240); // 2
        List<C240GeoJsonFeature> c240GeoJsonFeaturesList = new ArrayList<>();
        C240GeoJson geoJson = new C240GeoJson();

        // Loop Gambar Polygonn
        for (int i = 0; i <c240.getI049().getNB_CELLS(); i++){

            GeoCoordinate geoCoordinateStart = new GeoCoordinate(latAwalAZ1,lonAwalAZ1); // ownunit
            GeoCoordinate geoCoordinateEnd = new GeoCoordinate(latAkhirAZ1,lonAkhirAZ1); // ownunit

            double latStart = GeoUtil.getInstance().getCrdFromBR(geoCoordinateStart, c240.getI041().getSTART_AZ(),range).getLatitude();
            double lonStart = GeoUtil.getInstance().getCrdFromBR(geoCoordinateStart,c240.getI041().getSTART_AZ(),range).getLongitude();
            double latEndAz = GeoUtil.getInstance().getCrdFromBR(geoCoordinateEnd,  c240.getI041().getEND_AZ(),range).getLatitude();
            double lonEndAz = GeoUtil.getInstance().getCrdFromBR(geoCoordinateEnd, c240.getI041().getEND_AZ(),range).getLongitude();

            double[] cellPoint1 = new double[]{lonAwalAZ1, latAwalAZ1};
            double[] cellPoint2 = new double[]{lonStart,latStart};
            double[] cellPoint3 = new double[]{lonEndAz,latEndAz};
            double[] cellPoint4 = new double[]{lonAkhirAZ1,latAkhirAZ1};


            double[][] polygonCell = new double[][]{cellPoint1, cellPoint2,cellPoint3,cellPoint4};

            lonAwalAZ1 = lonStart;
            latAwalAZ1 = latStart;
            lonAkhirAZ1 = lonEndAz;
            latAkhirAZ1 = latEndAz;

            C240GeoJsonGeometry geoJsonGeometry = new C240GeoJsonGeometry();
            C240GeoJsonProperties geoJsonProperties = new C240GeoJsonProperties();
            C240GeoJsonFeature geoJsonFeature = new C240GeoJsonFeature();

            geoJsonGeometry.setCoordinates(new double[][][]{polygonCell});
            geoJsonGeometry.setType("Polygon");
            substringEnd = substringEnd + resolusi;

            geoJsonProperties.setOpacity(((double) Integer.parseInt(vidioBlock.substring(substringStart,substringEnd),16)) * 255 / 100 /100);
            geoJsonFeature.setType("Feature");
            geoJsonFeature.setGeometry(geoJsonGeometry);
            geoJsonFeature.setProperties(geoJsonProperties);
            substringStart = substringEnd; //2
            c240GeoJsonFeaturesList.add(geoJsonFeature);
        }
        geoJson.setType("FeatureCollection");
        geoJson.setFeatures(c240GeoJsonFeaturesList);
//        webSocketMessageSender.sendMessageToAll(geoJson);
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

    public String getCheck() {
        return check;
    }

}
