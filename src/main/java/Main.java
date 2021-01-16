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

    public static void main(String[] args) throws IOException {


/*        System.out.println(CurrencyParser.getUSDprice());
        System.out.println(CurrencyParser.getEURprice());
        System.out.println(CurrencyParser.getBTCPrice());
        System.out.println(CurrencyParser.getETHPrice());*/

        startBot();

//        HibernateUtil.closeSessionFactory();

    }

    public static void startBot() {
        System.out.println("==== starting SGLM shop BOT ====");
        try {

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(SGLMbot.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
