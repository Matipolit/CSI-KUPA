package CSI.Sensor;

public class Location {

    private final String name;

    private final Boolean measuresTemperature;
    private final Boolean measuresHumidity;
    private final Boolean measuresPressure;

    public Location(String name, Boolean measuresTemperature, Boolean measuresHumidity, Boolean measuresPressure) {
        this.name = name;
        this.measuresTemperature = measuresTemperature;
        this.measuresHumidity = measuresHumidity;
        this.measuresPressure = measuresPressure;
    }

    public boolean measuresTemperature(){
        return measuresTemperature;
    }

    public boolean measuresHumidity(){
        return measuresHumidity;
    }

    public boolean measuresPressure(){
        return measuresPressure;
    }

    public boolean measuresDataFromString(String dataType){
        switch(dataType){
            case("temperature") -> {return measuresTemperature;}
            case("humidity") -> {return measuresHumidity;}
            case("pressure") -> {return measuresPressure;}
            default -> {return false;}
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", measuresTemperature=" + measuresTemperature +
                ", measuresHumidity=" + measuresHumidity +
                ", measuresPressure=" + measuresPressure +
                '}';
    }
}
