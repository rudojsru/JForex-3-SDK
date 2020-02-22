package singlejartest;

import com.dukascopy.api.*;
import com.dukascopy.api.feed.*;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.util.*;
import java.util.*;

import static com.dukascopy.api.IOrder.State.FILLED;

public class Strategy implements IStrategy {
    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IIndicators indicators;
    private IUserInterface userInterface;

    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;
        this.indicators = context.getIndicators();
        this.userInterface = context.getUserInterface();


    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {

    }

    public void onStop() throws JFException {
    }
boolean flag = false;
    public void onTick(Instrument instrument, ITick tick) throws JFException {
        if (flag == false) {
            IOrder order;
            instrument = Instrument.EURUSD;
            console.getOut().println("........................Order   was created  SELL.................");
            tick = history.getLastTick(Instrument.EURUSD);
            double price = tick.getAsk() + instrument.getPipValue();
            double slPrise =   (price + instrument.getPipValue() * 10);
            double tpPrise =   (price - instrument.getPipValue() * 10);
             System.out.println("Prise: " + price + ", stopLoss: " + slPrise + ", takeProfit:" + tpPrise);
            //order = engine.submitOrder("hhhh", instrument, IEngine.OrderCommand.SELL, 0.001,0,40,1.0865,1.0830);//,price,10,(price+0.03),tpPrise);
            order = engine.submitOrder("hhhh", instrument, IEngine.OrderCommand.SELL, 0.001,0,40,slPrise,tpPrise);//,price,10,(price+0.03),tpPrise);

            order.waitForUpdate(2000, FILLED);
            console.getOut().println("S...............................................................S");
            flag = true;
        }
    }
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }
}