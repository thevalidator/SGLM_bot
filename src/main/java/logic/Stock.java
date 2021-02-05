package logic;

public class Stock {

    private String name;
    private Double lowTarget;
    private Double highTarget;
    private boolean notify;

    Stock(String name, Double lowTarget, Double highTarget, boolean notify) {
        this.name = name;
        this.lowTarget = lowTarget;
        this.highTarget = highTarget;
        this.notify = notify;
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

    public boolean hasNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
