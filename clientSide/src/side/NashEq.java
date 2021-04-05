package side;

public class NashEq {

    private double priority;
    private double size;
    private double RTT;
    private double award;
    private double queueOccupancy; // for now Q2=Q
    private double queue2; // for now Q2=Q


    private double[][] nashArray = new double[2][4];

    public NashEq(double size, double RTT, double priority, double award, double queueOccupancy, double queue2) {
        this.size = size;
        this.RTT = RTT;
        this.priority = priority;
        this.award = award;
        this.queueOccupancy = queueOccupancy;
        this.queue2 = queue2;

    }

    /*
        Tamam şimdi sender payoff pozitif çıktı. Ama sender drop edicek.
        Senderın drop edeceğini de mi ben hesaplıyorum. Aslında elimde tüm değerler var.

        O zaman gateway sadece drop accept hesaplicak alıcak.
        İşin çoğu sensorde.


     */
    public boolean action() {

        sendAccept();
        sendDrop();
        notSend();

        if (nashArray[0][0] >= nashArray[0][2] && nashArray[0][0] > 0) {
            if (nashArray[0][1] > 0) { // zaten server side -lisini veriyor. drop ve accept tam tersi
                return true;
            } else if (nashArray[0][1] < 0) {
                return false;
            }
        }

        return false;


    }

    private void sendAccept() {

        nashArray[0][0] = (1 - queue2) * (priority * award - size - RTT) + queue2 * (-size - RTT);
        nashArray[0][1] = (1 - queueOccupancy) * (priority * award - size) + queueOccupancy * ((-award) * (1 - priority) - size);
        System.out.print("( " + nashArray[0][0] + "," + nashArray[0][1] + " )   ");


    }

    private void sendDrop() {

        nashArray[0][2] = -size - RTT;
        nashArray[0][3] = (1 - queueOccupancy) * (-priority * award + size) + queueOccupancy * ((1 - priority) * award + size);
        System.out.println("( " + nashArray[0][2] + "," + nashArray[0][3] + " )   ");


    }

    private void notSend() {

        nashArray[1][0] = 0;
        nashArray[1][1] = 0;
        System.out.println("( " + nashArray[1][0] + "," + nashArray[1][1] + " )   ");
        //0

    }


}
