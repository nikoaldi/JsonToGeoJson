package CAT240RESOURCE;

import CAT240.C240;
import CAT240.Hasil;
import CAT240GEOJSON.*;
import Socket.SocketClass;
import Socket.WebSocketMessageSender;
import com.len.ccs.common.kinematics.GeoCoordinate;
import com.len.ccs.common.kinematics.util.GeoUtil;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.websocket.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@ApplicationScoped
public class CAT240Resource {

    @Inject
    Jsonb jsonb;

    @Inject
    private WebSocketMessageSender webSocketMessageSender;


    ///////////////// Variabel Untuk Test

    // List C240
    List<C240> c240List = new ArrayList<>();

    // Vidio Bolock
    public String vidioBlock="";

    // Hasil Hitung Per Cell
    List<Double> hasilHitung = new ArrayList<>();

    // Resume object C240
    Hasil hasil = new Hasil();


    List<String> hasilChar = new ArrayList<>();


    public String substring="";

    public int res = 2;

    public C240GeoJson geoJson;




    // Point Awal
    public double latAwal = -6.949612491503703;
    public double lonAwal = 107.61957049369812;
    public double latAkhir = -6.949612491503703;
    public double lonAkhir = 107.61957049369812;

    public double latStart = 0.0;
    public double lonStart = 0.0;
    public double latEndAz = 0.0;
    public double lonEndAz = 0.0;

    public double startAz = 0.0;
    public double endAz = 0.0;




    // Simulasii
//    public double startAz = 258.9312744140625;
//    public double endAz = 259.1015625;

    public double range = 0.0;


    public double[] listCoordinate;

    public double[][] listCoor;

    public double[][] polygon;

    C240GeoJson geoJson1 = new C240GeoJson();

    List<double[]> test2 = new ArrayList<>();

    List<double[][]> test3 = new ArrayList<>();

    private static final String JSON_FILE_PATH1 = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\TestLowVidioRadar.json";
    private static final String JSON_FILE_PATH = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\test.json";

    int substringStart = 0;
    int substringEnd = 2;

    List<C240GeoJsonFeature> c240GeoJsonFeaturesList = new ArrayList<>();

    C240 c240 = new C240();

    public double start = 258.9312744140625;
    public double end = 259.1015625;


    public void readC240()throws  IOException {
//        if (c240List.size() > 0){
//            c240List.remove(0);
//        }

        String jsonData = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH1)));
        c240 = jsonb.fromJson(jsonData, new C240(){}.getClass().getGenericSuperclass());
        c240List.add(jsonb.fromJson(jsonData, new C240(){}.getClass().getGenericSuperclass()));
        c240.getI041().setSTART_AZ(start);
        c240.getI041().setEND_AZ(end);
        setC240(c240);
        start = start + 0.5;
        end = end + 0.5;
    }


    public void setC240(C240 c240) throws IOException {
        startAz = c240.getI041().getSTART_AZ();
        endAz =  c240.getI041().getEND_AZ();
        latAwal = -6.949612491503703;
        lonAwal = 107.61957049369812;
        latAkhir = -6.949612491503703;
        lonAkhir  = 107.61957049369812;
        if (c240.getI041().getSTART_RG() != 0){
            GeoCoordinate geoCoordinateStart = new GeoCoordinate(latAwal,lonAwal);
            GeoCoordinate geoCoordinateEnd = new GeoCoordinate(latAkhir,lonAkhir);
            latAwal = GeoUtil.getInstance().getCrdFromBR(geoCoordinateStart, startAz,(hitungRange(c240))* c240.getI041().getSTART_RG()).getLatitude();
            lonAwal = GeoUtil.getInstance().getCrdFromBR(geoCoordinateStart, startAz,(hitungRange(c240))* c240.getI041().getSTART_RG()).getLongitude();
            latAkhir = GeoUtil.getInstance().getCrdFromBR(geoCoordinateEnd, endAz,(hitungRange(c240))* c240.getI041().getSTART_RG()).getLatitude();
            lonAkhir = GeoUtil.getInstance().getCrdFromBR(geoCoordinateEnd, endAz,(hitungRange(c240))* c240.getI041().getSTART_RG()).getLongitude();
        }




        calculateCellRange(c240);
        calculateRange(c240, startAz, endAz);
//        //Untuk simulasi

    }

    public void calculateRange(C240 c240,double startAzimut, double endAzimut ) throws IOException {
        range = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (0 + 2 -1) * (299792458 / 2);
        createGeoJosn(c240.getI049().getNB_CELLS(), startAzimut,endAzimut);
    }

    public void createGeoJosn(int jumlahCell, double startAzimut, double endAzimut)  {

        // Gambar Polygon
//

        if (c240GeoJsonFeaturesList.size() > 0){
            c240GeoJsonFeaturesList.remove(0);
        }
        for (int i = 0; i <jumlahCell; i++){

            GeoCoordinate geoCoordinateStart = new GeoCoordinate(latAwal,lonAwal);
            GeoCoordinate geoCoordinateEnd = new GeoCoordinate(latAkhir,lonAkhir);
            latStart = GeoUtil.getInstance().getCrdFromBR(geoCoordinateStart, startAzimut,range).getLatitude();
            lonStart = GeoUtil.getInstance().getCrdFromBR(geoCoordinateStart, startAzimut,range).getLongitude();
            latEndAz = GeoUtil.getInstance().getCrdFromBR(geoCoordinateEnd, endAzimut,range).getLatitude();
            lonEndAz = GeoUtil.getInstance().getCrdFromBR(geoCoordinateEnd, endAzimut,range).getLongitude();


            double[] point1 = new double[]{lonAwal,latAwal};
            double[] point2 = new double[]{lonStart,latStart};
            double[] point3 = new double[]{lonEndAz,latEndAz};
            double[] point4 = new double[]{lonAkhir,latAkhir};
            lonAwal = lonStart;
            latAwal = latStart;
            latAkhir = latEndAz;
            lonAkhir = lonEndAz;

            polygon = new double[][]{point1, point2,point3,point4};


            C240GeoJsonGeometry geoJsonGeometry = new C240GeoJsonGeometry();
            C240GeoJsonProperties geoJsonProperties = new C240GeoJsonProperties();
            C240GeoJsonFeature geoJsonFeature = new C240GeoJsonFeature();




            geoJsonGeometry.setCoordinates(new double[][][]{polygon});
            geoJsonGeometry.setType("Polygon");
            geoJsonProperties.setOpacity(((double) Integer.parseInt(vidioBlock.substring(substringStart,substringEnd),16)) * 255 / 100 /100);
//            geoJsonProperties.setOpacity(1);
            geoJsonFeature.setType("Feature");
            geoJsonFeature.setGeometry(geoJsonGeometry);
            geoJsonFeature.setProperties(geoJsonProperties);


            c240GeoJsonFeaturesList.add(geoJsonFeature);
            geoJson = geoJson1;
            substringStart = substringEnd;
            substringEnd = substringEnd +2;
        }
        geoJson1.setType("FeatureCollection");
        geoJson1.setFeatures(c240GeoJsonFeaturesList);
        webSocketMessageSender.sendMessageToAll(geoJson1);
    }

    public double hitungRange(C240 c240) {
     return (c240.getI041().getCELL_DUR()  * Math.pow(10,-15)) * (0 + 2 -1) * (299792458 / 2);
    }


    public void calculateCellRange(C240 c240){
        vidioBlock = "";
        hasil.setJumlahCell(c240.getI049().getNB_CELLS());
        hasil.setCellDur(c240.getI041().getCELL_DUR());
        hasil.setStartRG(c240.getI041().getSTART_RG());

        for (int i=0; i < c240.getI051().length; i++){
            vidioBlock = vidioBlock.concat( c240.getI051()[i].getVIDEO());
        }
//        for (int j =1; j < c240.getI049().getNB_CELLS()+1; j++){
//            double hitung = (c240.getI041().getCELL_DUR() * Math.pow(10,-15)) * (0 + j -1) * (299792458 / 2);
//            range =  hitung ;
//            hasilHitung.add(hitung);
//        }
//
//        for (int k=0; k < vidioBlock.length(); k=k+2){
//            hasilChar.add(vidioBlock.substring(k,k+2));
//        }
        substring = vidioBlock.substring(2,4);
        hasil.setRangeCell((hasil.getCellDur() * Math.pow(10,-15)) * (0 + 2 -1) * (299792458 / 2));
        hasil.setTotalRange(range);
        hasil.setStartAZ(c240.getI041().getSTART_AZ());
        hasil.setEndAZ(c240.getI041().getEND_AZ());


    }



    // Function to get the specific characterr
    public static char
    getCharFromString(String str, int index)
    {
        return str.charAt(index);
    }




//
//    @Scheduled(every = "10s")
//    public void simulasi() throws IOException {
//        readC240();
//    }





    // GETTER VALUE

    public List<C240> getC240List() {
        return c240List;
    }

    public String getVidioBlock() {
        return vidioBlock;
    }

    public List<Double> getHasilHitung() {
        return hasilHitung;
    }
    public Hasil getHasil() {
        return hasil;
    }


    public C240GeoJson getGeoJson1() {
        return geoJson1;
    }

    public String getSubstring() {
        return substring;
    }

    public List<String> getHasilChar() {
        return hasilChar;
    }

    public double getLatStart() {
        return latStart;
    }

    public double getLonStart() {
        return lonStart;
    }

    public double[] getListCoordinate() {
        return listCoordinate;
    }

    public double[][] getListCoor() {
        return listCoor;
    }

    public List<double[]> getTest2() {
        return test2;
    }


    public List<double[][]> getTest3() {
        return test3;
    }

    public C240GeoJson getGeoJson() {
        return geoJson;
    }

    public List<C240GeoJsonFeature> getC240GeoJsonFeaturesList() {
        return c240GeoJsonFeaturesList;
    }

    public double getLatAwal() {
        return latAwal;
    }

    public double getLonAwal() {
        return lonAwal;
    }

    public double getLatAkhir() {
        return latAkhir;
    }

    public double getLonAkhir() {
        return lonAkhir;
    }
}
