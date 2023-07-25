package CAT240GEOJSON;

import java.util.List;

public class C240GeoJsonFeature {
    private String type;
    private C240GeoJsonGeometry geometry;
    private C240GeoJsonProperties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public C240GeoJsonGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(C240GeoJsonGeometry geometry) {
        this.geometry = geometry;
    }

    public C240GeoJsonProperties getProperties() {
        return properties;
    }

    public void setProperties(C240GeoJsonProperties properties) {
        this.properties = properties;
    }
}
