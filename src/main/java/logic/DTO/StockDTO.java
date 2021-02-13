package logic.DTO;


import javax.persistence.*;

@Entity
@Table(name = "STOCKS")
public class StockDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private UserDTO userDTO;
    @Column(name = "NAME")
    private String name;
    @Column(name = "LOW")
    private Double lowTarget;
    @Column(name = "HIGH")
    private Double highTarget;
    @Column(name = "NOTIFY")
    private boolean notify;

    public StockDTO() {
    }

    public StockDTO(long id, UserDTO userDTO, String name, Double lowTarget, Double highTarget, boolean notify) {
        this.id = id;
        this.userDTO = userDTO;
        this.name = name;
        this.lowTarget = lowTarget;
        this.highTarget = highTarget;
        this.notify = notify;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
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
