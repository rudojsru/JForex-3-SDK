package singlejartest;

import com.dukascopy.api.*;

import java.util.Arrays;
import java.util.Date;

import static com.dukascopy.api.IOrder.State.FILLED;

public class StrategyUSDCHF implements IStrategy {

    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IIndicators indicators;
    private IUserInterface userInterface;

    double[] barsOpen = new double[4];
    double[] barsClose = new double[4];
    Date date = new Date();
    boolean sellExist = false;
    boolean buyExist = false;
    private final static String sell = "sellUSDCHF";
    private final static String buy = "buyUSDCHF";

    @Override
    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;
        this.indicators = context.getIndicators();
        this.userInterface = context.getUserInterface();

        console.getOut().println("Active orders: " + engine.getOrders());
        for (IOrder orderLabel : engine.getOrders()) { // показывает открыт ли ордер на продажу
            if (orderLabel.getLabel().equals("sellUSDCHF")) {
                sellExist = true;
                System.out.println("Sell!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! exist order Sell- " + sellExist);
            }
            if (orderLabel.getLabel().equals("buyUSDCHF")) {
                buyExist = true;
                System.out.println("Buy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! exist order Buy- " + buyExist);
            }
        }

    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {

    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        instrument = Instrument.USDCHF;
        IOrder order;

        for (int i = 0; i < 4; i++) {
            barsOpen[i] = history.getBar(instrument, Period.ONE_MIN, OfferSide.BID, i).getOpen();
            barsClose[i] = history.getBar(instrument, Period.ONE_MIN, OfferSide.BID, i).getClose(); // array ( open i close ostatniech 4-rech barow)
        }
        buyExist = existPositionOrNotFlag(buy, buyExist);
        sellExist = existPositionOrNotFlag(sell, sellExist);


        if (barsOpen[0] < barsOpen[1] && barsOpen[1] < barsOpen[2]
                && barsClose[0] < barsClose[1] && barsClose[1] < barsClose[2]
                && sellExist == false) {

            openOrder(instrument, sell);
        }
        if (barsOpen[0] > barsOpen[1] && barsOpen[1] > barsOpen[2]
                && barsClose[0] > barsClose[1] && barsClose[1] > barsClose[2]
                && buyExist == false) {

            openOrder(instrument, buy);
        }
        stopLossUpdateForExistOrder();

    }

    private void stopLossUpdateForExistOrder() throws JFException {
        IOrder order;
        System.out.println("......1........sellExist:" + sellExist + ",   buyExist:" + buyExist + "..............");
        buyExist = existPositionOrNotFlag(buy, buyExist);
        sellExist = existPositionOrNotFlag(sell, sellExist);
        System.out.println(".......2.......sellExist:" + sellExist + ",   buyExist:" + buyExist + "..............");

        if (existPositionOrNotFlag(sell, sellExist) == true && (barsOpen[0] < barsOpen[1] && barsOpen[1] < barsOpen[2])) {
            System.out.println("update");
            order = engine.getOrder(sell);
            order.setStopLossPrice(barsOpen[2]);
        }
        if (existPositionOrNotFlag(buy, buyExist) == true && (barsOpen[0] > barsOpen[1] && barsOpen[1] > barsOpen[2])) {
            System.out.println("update");
            order = engine.getOrder(buy);
            order.setStopLossPrice(barsOpen[2]);
        }
    }

    private void openOrder(Instrument instrument, String labelSellOrBuy) throws JFException {
        IOrder order;
        double slPrise = 0;
        double tpPrise = 0;
        double price = 0;
        IEngine.OrderCommand sellOrBuy;
        System.out.println(Arrays.toString(barsOpen));
        console.getOut().println("........................Order !!!" + labelSellOrBuy + "!!! was created ................." + date.toString());
        ITick tick = history.getLastTick(instrument);

        if (labelSellOrBuy.equals("sellUSDCHF")) {
            // price = tick.getAsk() + instrument.getPipValue();
            slPrise = barsOpen[2];// + instrument.getPipValue() * 2;
            // tpPrise = price - instrument.getPipValue() * 2;
            sellExist = true;
            sellOrBuy = IEngine.OrderCommand.SELL;
        } else if (labelSellOrBuy.equals("buyUSDCHF")) {
            // price = tick.getBid() + instrument.getPipValue();
            slPrise = barsOpen[2]; // - instrument.getPipValue() * 2;
            // tpPrise = price + instrument.getPipValue() * 2;
            buyExist = true;
            sellOrBuy = IEngine.OrderCommand.BUY;
        } else {
            System.out.println("!!!!!!!!!  Label for order doesn't exist, please check orderLabel  !!!!!!!!");
            return;
        }
        order = engine.submitOrder(labelSellOrBuy, instrument, sellOrBuy, 0.001, 0, 20, slPrise, 0);
        System.out.println("price: " + price + ", slPrice: " + slPrise + ", pPrice: " + 0);
        order.waitForUpdate(2000, FILLED);
        System.out.println("..............sellExist:" + sellExist + ",   buyExist:" + buyExist + "..............");
        console.getOut().println(labelSellOrBuy + ".............................................................." + labelSellOrBuy);

    }

    private boolean existPositionOrNotFlag(String orderLabel, boolean flag) throws JFException {
        for (IOrder orderL : engine.getOrders()) { // показывает открыт ли ордер на покупку или продажу
            if (orderL.getLabel().equals(orderLabel)) {
                flag = true;
                break;
            } else {
                flag = false;
            }
        }
        return flag;
    }


    @Override
    public void onMessage(IMessage message) throws JFException {

    }

    @Override
    public void onAccount(IAccount account) throws JFException {

    }

    @Override
    public void onStop() throws JFException {

    }
}