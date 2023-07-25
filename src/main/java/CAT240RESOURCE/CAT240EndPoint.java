package CAT240RESOURCE;


import CAT240.C240;
import CAT240.C240I051;
import CAT240.Hasil;
import CAT240GEOJSON.C240GeoJson;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Path("C240")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CAT240EndPoint {
    private static final String JSON_FILE_PATH = "C:\\Users\\nikoa\\OneDrive\\Documents\\json\\test.json";



    @Inject
    Jsonb jsonb;
    @Inject
    CAT240Resource cat240Resource;

    List<C240I051> listVideo = new ArrayList<>();


    public int count;



    @GET
    public List<C240> getC240() throws IOException {
        cat240Resource.readC240();
        return cat240Resource.getC240List();
    }

    @GET
    @Path("/vidio-block")
    public String getI051(){
        return  cat240Resource.getVidioBlock();
    }

    @GET
    @Path("/hasil-hitung")
    public List<Double> getHitung(){
        return cat240Resource.getHasilHitung();
    }

    @GET
    @Path("/hasil")
    public Hasil getHasil(){
        return cat240Resource.getHasil();
    }

    @GET
    @Path("/substring")
    public int getSubstring(){
        String hex = "0C";
        int decimal=Integer.parseInt(hex,16);
//        return cat240Resource.getHasilChar();
        return decimal;
    }

    @GET
    @Path("/coor")
    public List<double[][]> getCordinate(){
        return cat240Resource.getTest3();
    }

    @GET
    @Path("/geojson")
    public C240GeoJson getGeojson(){
        return cat240Resource.getGeoJson1();
    }

    @GET
    @Path("/count")
    public int getCount(){
        return cat240Resource.getC240GeoJsonFeaturesList().size();
    }


    @GET
    @Path("/check")
    public String getCheck(){
        return "LatAwal = " + cat240Resource.getLatAwal() + " LonAwal = " + cat240Resource.getLonAwal() + "LatAkhir = " + cat240Resource.getLatAkhir() + "LonAkhir = "+ cat240Resource.getLonAkhir() ;
    }


}
