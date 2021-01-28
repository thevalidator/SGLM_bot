package logic;

public class StockSettings {

    private String name;
    private Double lowTarget;
    private Double highTarget;
    private boolean marker;

    StockSettings(String name, Double lowTarget, Double highTarget, boolean marker) {
        this.name = name;
        this.lowTarget = lowTarget;
        this.highTarget = highTarget;
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLowTarget() {
        return lowTarget;
    }

    public void setLowTarget(Double lowTarget) {
        this.lowTarget = lowTarget;
    }

    public Double getHighTarget() {
        return highTarget;
    }

    public void setHighTarget(Double highTarget) {
        this.highTarget = highTarget;
    }

    public boolean isMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }
}
