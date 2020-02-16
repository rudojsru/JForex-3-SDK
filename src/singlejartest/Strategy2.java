package singlejartest;

import com.dukascopy.api.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Strategy2 implements IStrategy {
    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IIndicators indicators;
    private IUserInterface userInterface;


    List<Double> mass = new ArrayList<Double>();
    int[] massInt = new int[10];


    @Override
    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;
        this.indicators = context.getIndicators();
        this.userInterface = context.getUserInterface();
        System.out.println("Start Strategy2 ///////////////////////////////");

        ITick tick = history.getLastTick(Instrument.EURUSD);
        System.out.println(tick);
        System.out.println(tick.getAsk());
        System.out.println(tick.getBid());
        System.out.println(tick.getAskVolume());
        System.out.println(tick.getTotalAskVolume());
        System.out.println(tick.getTime());
        System.out.println(context.getHistory().getTicks(Instrument.EURUSD,0,0));
        System.out.println(history+"///////////////////////////");

//        double priceAsk = tick.getAsk() + 10 * Instrument.EURUSD.getPipValue();
//        System.out.println(priceAsk);
//        System.out.println(mass.add(priceAsk));
//        if((mass.size()>=4) ){
//            double t1= mass.get(mass.size()-1);
//            double t2= mass.get(mass.size()-2);
//            double t3= mass.get(mass.size()-3);
//            if ((t1>t2 & t2>t3)&&((t1-t2)-(t2-t3)<0.0006)){
//                double price2 = tick.getAsk() + 10 * Instrument.EURUSD.getPipValue();
//                double stopLoss =history.getLastTick(Instrument.EURUSD).getAsk()+10 * Instrument.EURUSD.getPipValue();
//                System.out.println(stopLoss);
//                IOrder order2 = this.engine.submitOrder("sell", Instrument.EURUSD, IEngine.OrderCommand.SELL, 0.001, price2, 1, 0, 0);
//                order2.waitForUpdate(3000, IOrder.State.OPENED);
//            }
//        }
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
//        System.out.println("Start Strategy2 ///////////////////////////////");
//        IContext context = null;
//        this.history = context.getHistory();
//        System.out.println(Instrument.values());
//
//        double priceAsk = tick.getAsk() + 10 * Instrument.EURUSD.getPipValue();
//        System.out.println(mass.add(priceAsk));
//        if((mass.size()>=4) ){
//               double t1= mass.get(mass.size()-1);
//               double t2= mass.get(mass.size()-2);
//               double t3= mass.get(mass.size()-3);
//               if ((t1>t2 & t2>t3)&&((t1-t2)-(t2-t3)<0.0006)){
//                   double price2 = tick.getAsk() + 10 * Instrument.EURUSD.getPipValue();
//                   double stopLoss =history.getLastTick(Instrument.EURUSD).getAsk()+10 * Instrument.EURUSD.getPipValue();
//                   System.out.println(stopLoss);
//                   IOrder order2 = this.engine.submitOrder("sell", Instrument.EURUSD, IEngine.OrderCommand.SELL, 0.001, price2, 1, 0, 0);
//                   order2.waitForUpdate(3000, IOrder.State.OPENED);
//               }
//        }


    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {

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
