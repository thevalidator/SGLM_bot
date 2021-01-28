package logic.utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CurrencyParser {

    private final static String BTC_URL = "https://www.coindesk.com/price/bitcoin";
    private final static String ETH_URL = "https://www.coindesk.com/price/ethereum";
    private final static String CBR = "https://cbr.ru/currency_base/daily/";
   // private final static String TSLA = "https://finance.yahoo.com/quote/TSLA";
    private final static String TSLA = "https://www.marketwatch.com/investing/stock/tsla";
    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:83.0)" +
            " Gecko/20100101 Firefox/83.0";
    private final static String noData = "no data";

    private static int tslaMarker = 835;
    private static int tslaDelta = 40;


    /* TESLA ends in Tinkoff at 01.45 msk, in world at 04.00 msk */
    public static String getTSLAprice() {
        String tslaPrice;
        try {
            Document webSite = establishInternetConnection(TSLA);
            //tslaPrice = webSite.body().getElementsByClass("Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)").text();
            tslaPrice = webSite.body().select("body > div.container.container--body " +
                    "> div.region.region--intraday > div.column.column--aside > div > div.intraday__data " +
                    "> h3 > bg-quote").text();
        } catch (IOException e) {
            e.printStackTrace();
            tslaPrice = noData;
        }
        return tslaPrice ;
    }

    public static String getEURprice() {
        String eurPrice;
        try {
            Document webSite = establishInternetConnection(CBR);
            Elements element = webSite.select("#content > div > div > div > div.table-wrapper > div " +
                    "> table > tbody > tr:nth-child(13) > td:nth-child(5)");
            eurPrice = element.text();
        } catch (IOException e) {
            e.printStackTrace();
            eurPrice = noData;
        }
        return eurPrice;
    }

    public static String getUSDprice() {
        String usdPrice;
        try {
            Document webSite = establishInternetConnection(CBR);
            Elements element = webSite.select("#content > div > div > div > div.table-wrapper > div > table " +
                    "> tbody > tr:nth-child(12) > td:nth-child(5)");
            usdPrice = element.text();
        } catch (IOException e) {
            e.printStackTrace();
            usdPrice = noData;
        }
        return usdPrice;
    }

    public static String getBTCPrice() {
        String btcPrice;
        try {
            Document webSite = establishInternetConnection(BTC_URL);
            Elements element = webSite.select("#export-chart-element > div > section > div.coin-info-list" +
                    ".price-list > div:nth-child(1) > div.data-definition > div");
            btcPrice = element.text().replaceAll("\\$","");
        } catch (IOException e) {
            e.printStackTrace();
            btcPrice = noData;
        }
        return btcPrice;
    }

    public static String getETHPrice() {
        String ethPrice;
        try {
            Document webSite = establishInternetConnection(ETH_URL);
            Elements element = webSite.select("#export-chart-element > div > section > div" +
                    ".coin-info-list.price-list > div:nth-child(1) > div.data-definition > div");
            ethPrice = element.text().replaceAll("\\$","");
        } catch (IOException e) {
            e.printStackTrace();
            ethPrice = noData;
        }
        return ethPrice;
    }

    private static Document establishInternetConnection(String link) throws IOException {
        return Jsoup.connect(link).userAgent(USER_AGENT).timeout(7000).ignoreHttpErrors(true).get();
    }

    public static int getTslaMarker() {
        return tslaMarker;
    }

    public static void setTslaMarker(int tslaMarker) {
        CurrencyParser.tslaMarker = tslaMarker;
    }

    public static int getTslaDelta() {
        return tslaDelta;
    }
}
