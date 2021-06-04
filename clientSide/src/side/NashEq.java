package side;


import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class NashEq {


    private final double[][] nashArray = new double[2][4];

    public NashEq() {

    }

    /**
     * Tamam şimdi sender payoff pozitif çıktı. Ama sender drop edicek.
     * Senderın drop edeceğini de mi ben hesaplıyorum. Aslında elimde tüm değerler var.
     * O zaman gateway sadece drop accept hesaplicak alıcak.
     * İşin çoğu sensorde.
     */

    public boolean action(double size, double RTT, double priority, double award, double queueOccupancy, double queue2, FileWriter fw) throws IOException {

        DecimalFormat df = new DecimalFormat("#.00");
        String information = "S=" + size +
                ", R=" + Double.parseDouble(df.format(RTT).replaceAll(",", ".")) +
                ", P=" + priority +
                ", A=" + award +
                ", Q=" + queueOccupancy + " ===>   ";

       // System.out.print(information);
        fw.write(information);

        sendAccept(size, RTT, priority, award, queueOccupancy, queue2);
        sendDrop(size, RTT, priority, award, queueOccupancy, queue2);
        notSend();



        String nashStr = Double.parseDouble(df.format(nashArray[0][0]).replaceAll(",", ".")) + ", "
                + Double.parseDouble(df.format(nashArray[0][1]).replaceAll(",", "."))
                + "    " + Double.parseDouble(df.format(nashArray[0][2]).replaceAll(",", "."))
                + ", " + Double.parseDouble(df.format(nashArray[0][3]).replaceAll(",", "."));

     //   System.out.println(nashStr);
        fw.write(nashStr+"\n");

        if (nashArray[0][0] >= nashArray[0][2] && nashArray[0][0] > 0) {
            if (nashArray[0][1] > 0) { // zaten server side -lisini veriyor. drop ve accept tam tersi
                return true;
            } else if (nashArray[0][1] < 0) {
                return false;
            }
        }

        return false;


    }

    private void sendAccept(double size, double RTT, double priority, double award, double queueOccupancy, double queue2) {

        nashArray[0][0] = (1 - queue2) * (priority * award - size - RTT) + queue2 * (-size - RTT);
        nashArray[0][1] = (1 - queueOccupancy) * (priority * award - size) + queueOccupancy * ((-award) * (1 - priority) - size);
        //System.out.print("( " + nashArray[0][0] + "," + nashArray[0][1] + " )   ");


    }

    private void sendDrop(double size, double RTT, double priority, double award, double queueOccupancy, double queue2) {

        nashArray[0][2] = -size - RTT;
        nashArray[0][3] = (1 - queueOccupancy) * (-priority * award + size) + queueOccupancy * ((1 - priority) * award + size);
        //System.out.print("( " + nashArray[0][2] + "," + nashArray[0][3] + " )   ");


    }

    private void notSend() {

        nashArray[1][0] = 0;
        nashArray[1][1] = 0;
        // System.out.println("( " + nashArray[1][0] + "," + nashArray[1][1] + " )   ");
        //0

    }

}