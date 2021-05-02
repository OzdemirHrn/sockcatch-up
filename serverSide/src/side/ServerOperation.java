package side;

public class ServerOperation {





    public void operation(String messageDeviceInfo, int messageOperation) {

        if (messageOperation > 4000) {

            int periot = 0;
            for (int i = 0; i < 7000000; i++) {
                periot++;
                if (periot == 700000) {
                    System.out.print("");
                }
            }
        } else if (messageOperation > 3000) {

            int periot = 0;
            for (int i = 0; i < 700000; i++) {
                periot++;
                if (periot == 70000) {
                    System.out.print("");
                }
            }
        } else if (messageOperation > 2000) {

        } else if (messageOperation > 1000) {

            int periot = 0;
            for (int i = 0; i < 70000000; i++) {
                periot++;
                if (periot == 7000000) {
                    System.out.print("");
                }
            }
        } else {
            System.out.println("case:else buraya girmememiz lazım");
        }

    }
}
