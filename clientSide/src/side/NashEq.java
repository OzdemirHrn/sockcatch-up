package side;

public class NashEq {

    private double priority;
    private double size;
    private double RTT;
    private double award;
    private int queueOccupancy; // for now Q2=Q

    public NashEq(double size, double RTT, double priority, double award, int queueOccupancy) {
        this.size = size;
        this.RTT = RTT;
        this.priority = priority;
        this.award = award;
        this.queueOccupancy = queueOccupancy;

    }

    /*
        Tamam şimdi sender payoff pozitif çıktı. Ama sender drop edicek.
        Senderın drop edeceğini de mi ben hesaplıyorum. Aslında elimde tüm değerler var.

        O zaman gateway sadece drop accept hesaplicak alıcak.
        İşin çoğu sensorde.


     */
    public double action() {

        double retVal = Double.MIN_VALUE;
        if (sendAccept() > retVal) retVal = sendAccept();
        if (sendDrop() > retVal) retVal = sendDrop();
        if (notSend() > retVal) retVal = notSend();

        return retVal;
    }

    private double sendAccept() {
        return (1 - queueOccupancy) * (priority * award - size - RTT) + queueOccupancy * (-size - RTT);
    }

    private double sendDrop() {
        return -size - RTT;
    }

    private double notSend() {
        return 0;
    }


}
