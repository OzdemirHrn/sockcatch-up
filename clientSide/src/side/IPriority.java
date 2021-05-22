package side;

public interface IPriority {

    double calculateDifference(double current, double prev);

    double priorityAssigner(float data);

    void setFirstConsistentData(double firstConsistentData);

    default void setTempAnnealing(double tempAnnealing) {

    }

}
