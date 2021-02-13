package logic;

import logic.utils.CurrencyParser;


public enum Stocks {
    TSLA {
        @Override
        public String parsePrice()
            {
                return CurrencyParser.getTSLAprice();
            }
        },
    BTC {
        @Override
        public String parsePrice()
        {
            return CurrencyParser.getBTCPrice();
        }
    },
    ETH {
        @Override
        public String parsePrice()
        {
            return CurrencyParser.getETHPrice();
        }
    },
    USD {
        @Override
        public String parsePrice()
        {
            return CurrencyParser.getUSDprice();
        }
    },
    EUR {
        @Override
        public String parsePrice()
        {
            return CurrencyParser.getEURprice();
        }
    };

    public abstract String parsePrice();
}
