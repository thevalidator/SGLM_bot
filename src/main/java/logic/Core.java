package logic;

import java.util.HashMap;


public class Core {

    private static Core core;
    private static long userCount;
    private static HashMap<Integer, User> userList;

    private Core() {
        setUserCount(0L);
        userList = new HashMap<>();
    }

    public static Core getInstance() {
        if (core == null) {
            core = new Core();
            System.out.println("... core instance created");
        }
        return core;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        Core.userCount = userCount;
    }

    public void addUser(Integer id, User user) {
        userList.put(id, user);
        userCount = userList.size();
    }

    public HashMap<Integer, User> getUserList() {
        return userList;
    }
}
