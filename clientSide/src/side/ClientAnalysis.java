package side;

public class ClientAnalysis {
    static float start = System.nanoTime();
    static float end;
    static float[] arr = new float[20];



    static void publishersTimer(Counter counter) {

        int interval=50;
        for(int i=0;i<20;i++){
            if (counter.getCounter() == interval) {
                end = System.nanoTime() - start;
                arr[i] = (float) (end / 1e6);
                break;
            }
            interval+=50;
        }
    }

    public void printArr() {
        int i;
        int counterNumber = 0;
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + 50;
            System.out.println("Counter number: " + counterNumber + " time: " + arr[i]);
        }

    }
}
