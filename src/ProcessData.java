import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class ProcessData {
    // Use JavaFX Properties for bindable data
    private final StringProperty job;
    private final IntegerProperty arrivalTime;
    private final IntegerProperty burstTime;
    private final IntegerProperty finishTime;
    private final IntegerProperty turnaroundTime;
    private final IntegerProperty waitingTime;
    private final IntegerProperty responseTime; // New property for response time

    public ProcessData(String job, int arrivalTime, int burstTime, int finishTime, int turnaroundTime, int waitingTime, int responseTime) {
        // Initialize the properties with the given values
        this.job = new SimpleStringProperty(job);
        this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
        this.burstTime = new SimpleIntegerProperty(burstTime);
        this.finishTime = new SimpleIntegerProperty(finishTime);
        this.turnaroundTime = new SimpleIntegerProperty(turnaroundTime);
        this.waitingTime = new SimpleIntegerProperty(waitingTime);
        this.responseTime = new SimpleIntegerProperty(responseTime); // Initialize new property
    }

    // Public getter methods for the Property objects themselves
    public StringProperty jobProperty() { return job; }
    public IntegerProperty arrivalTimeProperty() { return arrivalTime; }
    public IntegerProperty burstTimeProperty() { return burstTime; }
    public IntegerProperty finishTimeProperty() { return finishTime; }
    public IntegerProperty turnaroundTimeProperty() { return turnaroundTime; }
    public IntegerProperty waitingTimeProperty() { return waitingTime; }
    public IntegerProperty responseTimeProperty() { return responseTime; } // Getter for the new property

    // Provide simple getter/setter methods for convenience, though not required for the TableView
    public String getJob() { return job.get(); }
    public void setJob(String value) { job.set(value); }

    public int getArrivalTime() { return arrivalTime.get(); }
    public void setArrivalTime(int value) { arrivalTime.set(value); }

    public int getBurstTime() { return burstTime.get(); }
    public void setBurstTime(int value) { burstTime.set(value); }

    public int getFinishTime() { return finishTime.get(); }
    public void setFinishTime(int value) { finishTime.set(value); }

    public int getTurnaroundTime() { return turnaroundTime.get(); }
    public void setTurnaroundTime(int value) { turnaroundTime.set(value); }

    public int getWaitingTime() { return waitingTime.get(); }
    public void setWaitingTime(int value) { waitingTime.set(value); }
    
    public int getResponseTime() { return responseTime.get(); } // Simple getter for the new property
    public void setResponseTime(int value) { responseTime.set(value); }
}
