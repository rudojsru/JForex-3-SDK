package singlejartest;

import com.dukascopy.api.*;

import java.util.Arrays;
import java.util.Date;

import static com.dukascopy.api.IOrder.State.FILLED;


public class Strategy2 implements IStrategy {

    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IIndicators indicators;
    private IUserInterface userInterface;

    double[] barsOpen = new double[4];
    double[] barsClose = new double[4];
    Date date = new Date();
    boolean sellExist=false;
    boolean buyExist=false;

    @Override
    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;
        this.indicators = context.getIndicators();
        this.userInterface = context.getUserInterface();

        console.getOut().println("Active orders: " + engine.getOrders());
//        console.getOut().println("Pending orders: " + getPendingOrders());
//        console.getOut().println("Orders in profit: " + getProfitOrders());
//        console.getOut().println("Orders in loss: " + getLossOrders());

    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
        //   System.out.println(" Tick..........."+history.getTick(instrument,1));


    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        instrument = Instrument.EURUSD;
        IOrder order;
        System.out.println("..............sellExist:"+sellExist+",   buyExist:"+buyExist+"..............");

        for (int i = 0; i < 4; i++) {
            barsOpen[i] = history.getBar(instrument, Period.ONE_MIN, OfferSide.BID, i).getOpen();
            barsClose[i] = history.getBar(instrument, Period.ONE_MIN, OfferSide.BID, i).getClose();
        }
        System.out.println(Arrays.toString(barsOpen));
        if (//barsOpen[0] < barsOpen[1] && barsOpen[1] < barsOpen[2]
               // && barsClose[0] < barsClose[1] && barsClose[1] < barsClose[2]
                barsOpen[0] < barsOpen[1]) {
            console.getOut().println("........................Order   was created  SELL......................................" + date.toString());
           for (IOrder orderLabel : engine.getOrders()) {
                if (orderLabel.getLabel() == "sell") {
                  sellExist=true;
                }
            }
            if (sellExist==false){
                ITick tick = history.getLastTick(Instrument.EURUSD);
                double price = tick.getAsk() + 10 * Instrument.EURUSD.getPipValue();
                order = engine.submitOrder("sell", instrument, IEngine.OrderCommand.SELL, 0.001,price,1,(price-5),(price+5));
                order.waitForUpdate(2000, FILLED);
               // sellExist=true;
            }


            console.getOut().println("S..............................................................S");
        }sell:
        if (//barsOpen[0] > barsOpen[1] && barsOpen[1] > barsOpen[2]
              //  && barsClose[0] > barsClose[1] && barsClose[1] > barsClose[2]
                barsOpen[0] > barsOpen[1] ) {
            console.getOut().println("........................Order  was created  BUY......................................" + date.toString());
            for (IOrder orderLabel : engine.getOrders()) {
                if (orderLabel.getLabel() == "buy") {
                    buyExist=true;
                }
            }
            if (buyExist==false){
                order = engine.submitOrder("buy", instrument, IEngine.OrderCommand.BUY, 0.001);
                order.waitForUpdate(2000, FILLED);
               // buyExist=true;
            }


            console.getOut().println("..............................................................");
        }


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
