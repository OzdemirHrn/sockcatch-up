package side;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultipleClients {


    public static void main(String... args) {
		/*
		args[1]="500";
		args[2]="1000";
		args[3]="100";
         /*
         Burada bazÄ± sÄ±kÄ±ntÄ±lar var.
         EÅŸ zamanlÄ± farklÄ± parametrelerle Ã§alÄ±ÅŸtÄ±ramÄ±yorum.
         Mesajlar birbirine giriyor ve random deÄŸer hep aynÄ± oluyor.
         */


        Runnable temperatureComedorSensor = new Irun("Temperature/ComedorSensor", "1", "1", "10000000", "27");
        Thread threadTempComSen = new Thread(temperatureComedorSensor);
        threadTempComSen.start();

        Runnable temperatureHabitacionSensor = new Irun("Temperature/HabitacionSensor", "1", "1", "10000000", "28");
        Thread threadTempHabit = new Thread(temperatureHabitacionSensor);
        threadTempHabit.start();

        Runnable weatherTemperature = new Irun("Weather/Temperature", "1", "1", "10000000", "29");
        Thread threadWeatherTemp = new Thread(weatherTemperature);
        threadWeatherTemp.start();

        Runnable co2ComedorSensor = new Irun("CO2/ComedorSensor", "1", "1", "10000000", "30");
        Thread threadCo2Comedor = new Thread(co2ComedorSensor);
        threadCo2Comedor.start();

        Runnable co2HabitacionSensor = new Irun("CO2/HabitacionSensor", "1", "1", "10000000", "31");
        Thread threadCo2HabitSen = new Thread(co2HabitacionSensor);
        threadCo2HabitSen.start();

    }
}

class Irun implements Runnable {
    private String topic;
    String createObjectSleep;
    String sendObjectSleep;
    String capacityOfQueue;
    String datasetRow;

    public Irun(String topic, String createObjectSleep, String sendObjectSleep, String capacityOfQueue, String datasetRow) {
        this.topic = topic;
        this.createObjectSleep = createObjectSleep;
        this.sendObjectSleep = sendObjectSleep;
        this.capacityOfQueue = capacityOfQueue;
        this.datasetRow = datasetRow;
    }

    @Override
    public void run() {
        try {
            ClientSide.main(topic, createObjectSleep, sendObjectSleep, capacityOfQueue, datasetRow);
        } catch (Exception ex) {
            Logger.getLogger(Irun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
