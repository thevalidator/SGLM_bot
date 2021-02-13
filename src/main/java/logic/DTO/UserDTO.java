package logic.DTO;

import logic.Address;
import logic.Subscription;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "USERS")
public class UserDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ORDINAL_NUMBER")
    private int ordinalNumber;
    @Column(name = "JOINED")
    private LocalDateTime joinStamp;
    @Column(name = "TELEGRAM_ID")
    private Integer telegramId;
    @Column(name = "CHAT_ID")
    private Long chatId;
    @Column(name = "USER_NAME")
    private String name;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Transient
    private Address address;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Transient
    private String description;
    @Transient
    private Subscription subscription;
    @OneToMany(mappedBy = "userDTO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name = "name")
    private Map<String, StockDTO> stocks = new HashMap<>();
/*    @Transient
    private boolean isVIP;*/

    public UserDTO(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public LocalDateTime getJoinStamp() {
        return joinStamp;
    }

    public void setJoinStamp(LocalDateTime joinStamp) {
        this.joinStamp = joinStamp;
    }

    public Integer getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Map<String, StockDTO> getStocks() {
        return stocks;
    }

    public void setStocks(Map<String, StockDTO> stocks) {
        this.stocks = stocks;
    }

    public void umerge(UserDTO userDTO) {
        id = userDTO.getId();
        ordinalNumber = userDTO.getOrdinalNumber();
        joinStamp = userDTO.getJoinStamp();
        telegramId = userDTO.getTelegramId();
        chatId = userDTO.getChatId();
        name = userDTO.getName();
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        address = userDTO.getAddress();
        description = userDTO.getDescription();
        subscription = userDTO.getSubscription();
        stocks = userDTO.getStocks();
    }


}
