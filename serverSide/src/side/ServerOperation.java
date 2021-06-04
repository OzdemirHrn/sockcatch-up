package side;

public class ServerOperation {

    public void operation(String messageDeviceInfo, int messageOperation,float messageSize) throws InterruptedException {

        Thread.sleep((long) (messageSize*120));

        /*
        if (messageOperation > 4000) { //7m
            Thread.sleep(20);

        } else if (messageOperation > 3000) { //700k
            Thread.sleep(50);

        } else if (messageOperation > 2000) {

        } else if (messageOperation > 1000) { //70m
            Thread.sleep(100);

        } else {
            System.out.println("case:else buraya girmememiz lazım");
        }
*/
    }
}
