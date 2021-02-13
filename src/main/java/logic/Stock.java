package logic;

import logic.utils.CurrencyParser;


public class Stock {

    private long id;
    private User user;
    private String name;
    private Double lowTarget;
    private Double highTarget;
    private boolean notify;

    public Stock() {
        name = "blank";
        lowTarget = 0.;
        highTarget = 0.;
        notify = false;
    }

    public Stock(long id, User user, String name, Double lowTarget, Double highTarget, boolean notify) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.lowTarget = lowTarget;
        this.highTarget = highTarget;
        this.notify = notify;
    }

    public Stock(String name, User user) {
        this();
        this.name = name;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStockPrice() {
        return CurrencyParser.getPrice(name);
    }

}
