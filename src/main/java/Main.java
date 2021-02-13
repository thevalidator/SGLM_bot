import logic.Core;
import logic.DTO.UserDTO;
import logic.SGLMbot;
import logic.Stock;
import logic.User;
import logic.utils.CurrencyParser;
import logic.utils.HibernateUtil;
import logic.utils.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class Main {

    public static final Logger LOGGER = LogManager.getRootLogger();
    static int seconds = 60;
    static boolean flag = true;
    //static boolean notification = true;
    static int count = 0;


    public static void main(String[] args) throws IOException {

/*        System.out.println(CurrencyParser.getUSDprice());
        System.out.println(CurrencyParser.getEURprice());
        System.out.println(CurrencyParser.getBTCPrice());
        System.out.println(CurrencyParser.getETHPrice());
        System.out.println(CurrencyParser.getTSLAprice());*/

        //System.out.println(CurrencyParser.getTSLAprice());

        // starting bot
        SGLMbot botInstance = SGLMbot.getInstance();
        startBot(botInstance);

/*        SGLMbot.loadUsers();
        User user = Core.getInstance().getUserList().get(312898894);
        System.out.println(user.getStocks().size());
        for (Map.Entry<String, Stock> e: user.getStocks().entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue().getUser().getTelegramId());
        }*/

/*        User user = new User(44444, 22222L, "String name", "String firstName", "String lastName");
        user.addStock("TSLA");
        System.out.println(user.getStocks().get("TSLA").getUser().getName());
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);
        System.out.println(userDTO.getStocks().get("TSLA").getUserDTO().getName());*/


        /*System.out.println(Core.getInstance().getUserList().size());
        User user = Core.getInstance().getUserList().get(312898894);
        user.getStocks().forEach((k,v) -> System.out.println(k));
        System.out.println(user.getId() + " " + user.getTelegramId() + " " + user.getStocks().size());*/
        //user.getStocks().forEach((k,v) -> System.out.println(k + " " + v.getName()));

        //System.out.println(Core.getInstance().getUserList().get(312898894).getStocks().get("TSLA").getHighTarget());
        //Core.getInstance().getUserList().get(312898894).getStocks().forEach((k,v) -> System.out.println(k + " " + v.getHighTarget()));
        //System.out.println(Core.getInstance().getUserList().get(312898894).getStocks().size());

//        HibernateUtil.closeSessionFactory();

        //stocks monitoring
        new Thread(() -> {
            try {
                while (flag) {
                    Thread.sleep(seconds * 1_000);

                    if (true) {
                        String tsla = CurrencyParser.getTsla();
                        String btc = CurrencyParser.getBtc();
                        Core.getInstance().getUserList().forEach((k,v) -> {v.getStocks().forEach((key,value) -> {
                            String price = CurrencyParser.noData;
                            if (value.hasNotify()) {
                                if (value.getName().equals("TSLA")) {
                                    price = tsla;
                                } else if (value.getName().equals("BTC")) {
                                    price = btc;
                                }
                                //String price = value.getStockPrice();
                                if (!price.equals(CurrencyParser.noData)) {
                                    if (Double.parseDouble(price) < value.getLowTarget()) {
                                        botInstance.sendNotification(value.getName() + " DOWN: " + price,
                                                String.valueOf(v.getChatId()));
                                        value.setNotify(false);
                                    }
                                    if (Double.parseDouble(price) > value.getHighTarget()) {
                                        botInstance.sendNotification(value.getName() + " UP: " + price,
                                                String.valueOf(v.getChatId()));
                                        value.setNotify(false);
                                    }
                                }


                            }
                        });});
                    }

                    /*if(CurrencyParser.hasNotification()) {
                        String teslaPrice = CurrencyParser.getTSLAprice();
                        if (!teslaPrice.equals(CurrencyParser.noData)) {
                            if (Double.parseDouble(teslaPrice) < CurrencyParser.getTslaLow()) {
                                botInstance.sendNotification("TSLA DOWN: " + teslaPrice);
                                CurrencyParser.setNotification(false);
                            }
                            if (Double.parseDouble(teslaPrice) > CurrencyParser.getTslaHigh()) {
                                botInstance.sendNotification("TSLA UP: " + teslaPrice);
                                CurrencyParser.setNotification(false);
                            }
                        }
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // testing
        /*try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User test = new User(1111, 22222L, "TEST-TEST", "TESTNAME", "TESTLASTNAME");
            test.addStock("TSLA");
            test.getStocks().get("TSLA").setHighTarget(890.);
            test.getStocks().get("TSLA").setLowTarget(800.);
            test.getStocks().get("TSLA").setNotify(true);
            test.addStock("BTC");

            UserDTO u = UserMapper.INSTANCE.userToUserDTO(test);
            session.save(u);

            transaction.commit();
        }*/


    }

    public static void startBot(SGLMbot botInstance) {
        System.out.println("==== starting SGLM shop BOT ====");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(botInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
