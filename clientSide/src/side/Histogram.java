//package side;
//
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class Histogram {
//
//    public Histogram() throws IOException {
//
//        FileWriter fisrtAttempt = new FileOperations().createInputfile("FirstAttempt");
//        for (double priority:
//                MultipleClients.delayedMessagesPriority.get(0)) {
//            fisrtAttempt.write(priority+"\n");
//        }
//        fisrtAttempt.close();
//
//        FileWriter secondAttempt = new FileOperations().createInputfile("SecondAttempt");
//        for (double priority:
//                MultipleClients.delayedMessagesPriority.get(1)) {
//            secondAttempt.write(priority+"\n");
//        }
//        secondAttempt.close();
//
//        FileWriter thirdAttempt = new FileOperations().createInputfile("ThirdAttempt");
//        for (double priority:
//                MultipleClients.delayedMessagesPriority.get(2)) {
//            thirdAttempt.write(priority+"\n");
//        }
//        thirdAttempt.close();
//
//        FileWriter fourthAttempt = new FileOperations().createInputfile("FourthAttempt");
//        for (double priority:
//                MultipleClients.delayedMessagesPriority.get(3)) {
//            fourthAttempt.write(priority+"\n");
//        }
//        fourthAttempt.close();
//
//        FileWriter DroppedAfter4Attempts = new FileOperations().createInputfile("DroppedAfter4Attempts");
//        for (double priority:
//                MultipleClients.delayedMessagesPriority.get(4)) {
//            DroppedAfter4Attempts.write(priority+"\n");
//        }
//        DroppedAfter4Attempts.close();
//
//    }
//}