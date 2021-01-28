import logic.SGLMbot;
import logic.utils.CurrencyParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;


public class Main {

    public static final Logger LOGGER = LogManager.getRootLogger();
    static int seconds = 10;
    static boolean flag = true;
    static boolean marker = false;
    static int count = 0;


    public static void main(String[] args) throws IOException {

/*        System.out.println(CurrencyParser.getUSDprice());
        System.out.println(CurrencyParser.getEURprice());
        System.out.println(CurrencyParser.getBTCPrice());
        System.out.println(CurrencyParser.getETHPrice());
        System.out.println(CurrencyParser.getTSLAprice());*/

        //System.out.println(CurrencyParser.getTSLAprice());

        SGLMbot botInstance = SGLMbot.getInstance();
        startBot(botInstance);

//        HibernateUtil.closeSessionFactory();

        new Thread(() -> {
            try {
                while (flag) {
                    Thread.sleep(seconds * 1_000);
                    if(!marker) {
                        String teslaPrice = CurrencyParser.getTSLAprice();
                        if (Double.parseDouble(teslaPrice) < CurrencyParser.getTslaMarker() - CurrencyParser
                                .getTslaDelta()) {
                            botInstance.sendNotification(teslaPrice);
                            marker = true;
                            flag = false;
                        }
                    }


                    ;
                    count++;
                    if (count > 4) {
                        flag = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

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
