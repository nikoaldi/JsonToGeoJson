package CAT240RESOURCE;


import CAT240.C240;
import CAT240GEOJSON.C240GeoJson;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.print.DocFlavor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Path("C240")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CAT240EndPoint {
    private static final String JSON_FILE_PATH = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\data.json";
    private static final String JSON_FILE_PATH2 = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\TestLowVidioRadar.json";
    @Inject
    Jsonb jsonb;

    @Inject
    C240Handler c240Handler;

    C240 c240 = new C240();
    C240 c2401 = new C240();

    int i = 0;
    @GET
    public String getC240() throws  IOException{
        if (i == 0) {
            String jsonData = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
            c240 = jsonb.fromJson(jsonData, new C240(){}.getClass().getGenericSuperclass());
            c240Handler.setC240(c240);
            i = i+1;
        } else {
            String jsonData1 = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH2)));
            c2401 = jsonb.fromJson(jsonData1, new C240(){}.getClass().getGenericSuperclass());
            c240Handler.setC240(c2401);
            i = 0;
        }

        return c240Handler.getCheck();
    }

    @GET
    @Path("/oke")
    public String getGas(){
        return "Gassss";
    }



//    @GET
//    @Path("/vidio-block")
//    public String getI051(){
//        return  cat240Resource.getVidioBlock();
//    }
//
//    @GET
//    @Path("/hasil-hitung")
//    public List<Double> getHitung(){
//        return cat240Resource.getHasilHitung();
//    }
//
//    @GET
//    @Path("/hasil")
//    public Hasil getHasil(){
//        return cat240Resource.getHasil();
//    }
//
//    @GET
//    @Path("/substring")
//    public int getSubstring(){
//        String hex = "0C";
//        int decimal=Integer.parseInt(hex,16);
////        return cat240Resource.getHasilChar();
//        return decimal;
//    }
//
//    @GET
//    @Path("/coor")
//    public List<double[][]> getCordinate(){
//        return cat240Resource.getTest3();
//    }

//    @GET
//    @Path("/geojson")
//    public C240GeoJson getGeojson(){
//        return cat240Resource.getGeoJson1();
//    }
//
//    @GET
//    @Path("/count")
//    public int getCount(){
//        return cat240Resource.getC240GeoJsonFeaturesList().size();
//    }
//
//
//    @GET
//    @Path("/check")
//    public String getCheck(){
//        return "LatAwal = " + cat240Resource.getLatAwal() + " LonAwal = " + cat240Resource.getLonAwal() + "LatAkhir = " + cat240Resource.getLatAkhir() + "LonAkhir = "+ cat240Resource.getLonAkhir() ;
//    }


}
