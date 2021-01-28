package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.HashMap;


public class User {

    private static final Logger USER_LOGGER = LogManager.getLogger("usr");

    private int ordinalNumber;
    private LocalDateTime joinStamp;
    private Integer telegramId;
    private Long chatId;
    private String name;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address address;
    private Subscription subscription;
    private String description;
    private HashMap<String, StockSettings> stocks;
//    private boolean isVIP;
    //.format(DateTimeFormatter.ofPattern("d.MM.yyyy - HH:mm"))

    public User(){
        joinStamp = LocalDateTime.now();
        stocks = new HashMap<>();
    }

    public User(Integer telegramId) {
        this();
        this.telegramId = telegramId;
    }

    public User(Integer telegramId, Long chatId, String name, String firstName, String lastName) {
        this();
        this.telegramId = telegramId;
        this.chatId = chatId;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}