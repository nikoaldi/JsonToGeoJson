package CAT240GEOJSON;

import java.util.List;

public class C240GeoJson {
    private String type;
    private List<C240GeoJsonFeature> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<C240GeoJsonFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<C240GeoJsonFeature> features) {
        this.features = features;
    }
}
