package side;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.Frequency;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.text.DecimalFormat;
import java.util.*;

import static org.apache.commons.math3.util.Precision.round;

public class Histogram {

    private Map distributionMap;
    private double classWidth;
    private static DecimalFormat df = new DecimalFormat("#.00");


    public Histogram() {

        distributionMap = new TreeMap();
        classWidth = 0.1;
        Map distributionMap = processRawData(MultipleClients.delayedMessagesPriority.get(4));
        List yData = new ArrayList();
        yData.addAll(distributionMap.values());
        List xData = Arrays.asList(distributionMap.keySet().toArray());
        for (int i = 0; i < xData.size(); i++) {
            System.out.println(xData.get(i));
        }

        CategoryChart chart = buildChart(xData, yData);
        new SwingWrapper<>(chart).displayChart();

    }

    private CategoryChart buildChart(List xData, List yData) {

        // Create Chart
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600)
                .title("Priortiy Distribution")
                .xAxisTitle("Priority")
                .yAxisTitle("Frequency")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(0.99);
        chart.getStyler().setOverlapped(true);

        chart.addSeries("Priority Group", xData, yData);

        return chart;
    }

    private Map processRawData( ArrayList<Double> arrayList) {

        Frequency frequency = new Frequency();
        arrayList.forEach(d -> frequency.addValue(Double.parseDouble(d.toString())));

        arrayList.stream()
                .map(d -> Double.parseDouble(d.toString()))
                .distinct()
                .forEach(observation -> {
                    long observationFrequency = frequency.getCount(observation);
                    double upperBoundary =  findUpperBound(observation);
                    double lowerBoundary = findLowerBound(observation);
                    String bin = lowerBoundary + "-" + upperBoundary;

                    updateDistributionMap(lowerBoundary, bin, observationFrequency);

                });

        return distributionMap;
    }

    private double findLowerBound(Double observation) {
        if(observation>=0.9)return 0.9;
        else if(observation>=0.8)return 0.8;
        else if(observation>=0.7)return 0.7;
        else if(observation>=0.6)return 0.6;
        else if(observation>=0.5)return 0.5;
        else if(observation>=0.4)return 0.4;
        else if(observation>=0.3)return 0.3;
        else if(observation>=0.2)return 0.2;
        else if(observation>=0.1)return 0.1;
        else if(observation>=0.0)return 0.0;
        else return 0;
    }

    private double findUpperBound(Double observation) {
        if(observation>=0.9)return 1;
        else if(observation>=0.8)return 0.9;
        else if(observation>=0.7)return 0.8;
        else if(observation>=0.6)return 0.7;
        else if(observation>=0.5)return 0.6;
        else if(observation>=0.4)return 0.5;
        else if(observation>=0.3)return 0.4;
        else if(observation>=0.2)return 0.3;
        else if(observation>=0.1)return 0.2;
        else if(observation>=0.0)return 0.1;
        else return 0.1;
    }


    private void updateDistributionMap(double lowerBoundary, String bin, long observationFrequency) {

        double prevLowerBoundary = Double.parseDouble(df.format(
                (lowerBoundary > classWidth) ? lowerBoundary - classWidth : 0
        ).replaceAll(",", "."));
        String prevBin = prevLowerBoundary + "-" + lowerBoundary;
        if(!distributionMap.containsKey(prevBin))
            distributionMap.put(prevBin, 0);

        if(!distributionMap.containsKey(bin)) {
            distributionMap.put(bin, observationFrequency);
        }
        else {
            long oldFrequency = Long.parseLong(distributionMap.get(bin).toString());
            distributionMap.replace(bin, oldFrequency + observationFrequency);
        }
    }

//    public static void main(String[] args) {
//
//    }

}