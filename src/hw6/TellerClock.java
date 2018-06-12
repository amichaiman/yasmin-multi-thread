package hw6;

public class TellerClock extends Clock{
    private Teller teller;

    public TellerClock(int activeTime, Teller teller) {
        super(activeTime);

        this.teller = teller;
    }

    @Override
    public void run() {
        super.run();

        synchronized (teller) {
            teller.notify();
        }
    }
}
