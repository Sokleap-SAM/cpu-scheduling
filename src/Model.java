import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private String algorithm;
    private Map<Integer, Integer> arrivalTimes;
    private Map<Integer, Integer> burstTimes;
    private Map<Integer, Integer> completionTime = new HashMap<>();
    private Map<Integer, Integer> turnAroundTime = new HashMap<>();
    private Map<Integer, Integer> waitingTime = new HashMap<>();
    private Map<Integer, Integer> responseTimes = new HashMap<>();
    private Map<Integer, Integer> firstCpuAllocationTime = new HashMap<>();
    private List<Integer> processIndexList;
    private List<GanttEntry> ganttChartLog = new ArrayList<>();
    private int currentTime = 0;
    private static final int AGING_THRESHOLD = 15; // Time threshold for aging

    private Queue<Integer> queue1;
    private Queue<Integer> queue2;
    private Queue<Integer> queue3;
    private int quantum1; // Make these fields dynamic
    private int quantum2;

    public Model() {
        this.arrivalTimes = new HashMap<>();
        this.burstTimes = new HashMap<>();
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setArrivalTimes(Map<Integer, Integer> arrivalTimes) {
        this.arrivalTimes = arrivalTimes;
    }

    public void setBurstTimes(Map<Integer, Integer> burstTimes) {
        this.burstTimes = burstTimes;
    }

    public Map<String, Object> getResults() {
        Map<String, Object> results = new HashMap<>();
        results.put("algorithm", this.algorithm);
        results.put("arrivalTimes", this.arrivalTimes);
        results.put("burstTimes", this.burstTimes);
        results.put("completionTimes", this.completionTime);
        results.put("turnAroundTimes", this.turnAroundTime);
        results.put("waitingTimes", this.waitingTime);
        results.put("responseTimes", this.responseTimes);
        results.put("processOrder", this.processIndexList);
        results.put("firstCpuAllocationTime", this.firstCpuAllocationTime);
        return results;
    }

    public void reset() {
        completionTime.clear();
        turnAroundTime.clear();
        waitingTime.clear();
        responseTimes.clear();
        firstCpuAllocationTime.clear();
        ganttChartLog.clear();
        currentTime = 0;
        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
        queue3 = new LinkedList<>();
    }

    public void initializeProcesses() {
        processIndexList = new ArrayList<>(arrivalTimes.keySet());
        processIndexList.sort((a, b) -> arrivalTimes.get(a) - arrivalTimes.get(b));
    }

    public String runScheduling(int timeQuantum1, int timeQuantum2) {
        this.quantum1 = timeQuantum1;
        this.quantum2 = timeQuantum2;

        switch (algorithm) {
            case "First Come First Serve, FCFS":
                return firstComeFirstServeOperation();
            case "Shortest Job First, SJF":
                return shortestJobFirstOperation();
            case "Shortest Remaining Time First, SRTF":
                return shortestRemainingTimeFirst();
            case "Round Robin, RR":
                return roundRobinOperation(timeQuantum1);
            case "Multilevel Feedback Queue, MLFQ":
                return multilevelFeedbackQueueOperation();
            default:
                return "Algorithm not recognized.";
        }
    }

    private String firstComeFirstServeOperation() {
        // ... (existing FCFS implementation - no change needed here)
        for (Integer process : processIndexList) {
            int arrival = arrivalTimes.get(process);
            int burst = burstTimes.get(process);
            if (currentTime < arrival) {
                currentTime = arrival;
            }
            if (!firstCpuAllocationTime.containsKey(process)) {
                firstCpuAllocationTime.put(process, currentTime);
            }
            ganttChartLog.add(new GanttEntry(process, currentTime, currentTime + burst));

            int completion = currentTime + burst;
            completionTime.put(process, completion);
            int tat = completion - arrival;
            turnAroundTime.put(process, tat);
            int wt = tat - burst;
            waitingTime.put(process, wt);
            int rt = firstCpuAllocationTime.get(process) - arrival;
            responseTimes.put(process, rt);
            currentTime = completion;
        }
        return "FCFS algorithm executed. Gantt chart and table will be shown here.";
    }

    private String shortestJobFirstOperation() {
        // ... (existing SJF implementation - no change needed here)
        if (arrivalTimes.isEmpty() || burstTimes.isEmpty() || arrivalTimes.size() != burstTimes.size()) {
            return "Invalid input: The maps are empty or have different sizes.";
        }
        int currentTime = 0;
        int completedCount = 0;

        while (completedCount < processIndexList.size()) {
            List<Integer> availableProcesses = new ArrayList<>();
            for (Integer process : processIndexList) {
                if (arrivalTimes.get(process) <= currentTime && !completionTime.containsKey(process)) {
                    availableProcesses.add(process);
                }
            }
            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }
            availableProcesses.sort((a, b) -> burstTimes.get(a) - burstTimes.get(b));
            Integer nextProcess = availableProcesses.get(0);
            int arrival = arrivalTimes.get(nextProcess);
            int burst = burstTimes.get(nextProcess);

            if (!firstCpuAllocationTime.containsKey(nextProcess)) {
                firstCpuAllocationTime.put(nextProcess, currentTime);
            }

            ganttChartLog.add(new GanttEntry(nextProcess, currentTime, currentTime + burst));

            int completion = currentTime + burst;
            completionTime.put(nextProcess, completion);
            int tat = completion - arrival;
            turnAroundTime.put(nextProcess, tat);
            int wt = tat - burst;
            waitingTime.put(nextProcess, wt);
            int rt = firstCpuAllocationTime.get(nextProcess) - arrival;
            responseTimes.put(nextProcess, rt);
            currentTime = completion;
            completedCount++;
        }
        return "SJF algorithm executed. Gantt chart and table will be shown here.";
    }

    private String shortestRemainingTimeFirst() {
        // ... (existing SRTF implementation)
        if (arrivalTimes.isEmpty() || burstTimes.isEmpty() || arrivalTimes.size() != burstTimes.size()) {
            return "Invalid input: The maps are empty or have different sizes.";
        }

        Map<Integer, Integer> remainingBurstTimes = new HashMap<>(burstTimes);
        int completedCount = 0;
        int totalProcesses = processIndexList.size();
        int lastExecutedProcess = -1;
        currentTime = 0;

        while (completedCount < totalProcesses) {
            int nextProcessToExecute = -1;
            int minRemainingTime = Integer.MAX_VALUE;

            for (Integer process : processIndexList) {
                if (arrivalTimes.get(process) <= currentTime && remainingBurstTimes.get(process) > 0) {
                    if (remainingBurstTimes.get(process) < minRemainingTime) {
                        minRemainingTime = remainingBurstTimes.get(process);
                        nextProcessToExecute = process;
                    } else if (remainingBurstTimes.get(process).equals(minRemainingTime)) {
                        if (arrivalTimes.get(process) < arrivalTimes.get(nextProcessToExecute)) {
                            nextProcessToExecute = process;
                        }
                    }
                }
            }

            if (nextProcessToExecute != -1) {
                if (!firstCpuAllocationTime.containsKey(nextProcessToExecute)) {
                    firstCpuAllocationTime.put(nextProcessToExecute, currentTime);
                }

                if (lastExecutedProcess == -1 || lastExecutedProcess != nextProcessToExecute) {
                    ganttChartLog.add(new GanttEntry(nextProcessToExecute, currentTime, currentTime + 1));
                } else {
                    GanttEntry lastEntry = ganttChartLog.get(ganttChartLog.size() - 1);
                    lastEntry.endTime++;
                }
                lastExecutedProcess = nextProcessToExecute;
                remainingBurstTimes.put(nextProcessToExecute, remainingBurstTimes.get(nextProcessToExecute) - 1);

                if (remainingBurstTimes.get(nextProcessToExecute) == 0) {
                    int originalBurst = burstTimes.get(nextProcessToExecute);
                    completionTime.put(nextProcessToExecute, currentTime + 1);
                    int tat = completionTime.get(nextProcessToExecute) - arrivalTimes.get(nextProcessToExecute);
                    turnAroundTime.put(nextProcessToExecute, tat);
                    int wt = tat - originalBurst;
                    waitingTime.put(nextProcessToExecute, wt);
                    int rt = firstCpuAllocationTime.get(nextProcessToExecute) - arrivalTimes.get(nextProcessToExecute);
                    responseTimes.put(nextProcessToExecute, rt);
                    completedCount++;
                }
                currentTime++;
            } else {
                lastExecutedProcess = -1;
                currentTime++;
            }
        }
        return "STRF algorithm executed. Gantt chart and table will be shown here.";
    }

    private String roundRobinOperation(int timeQuantum) {
        // ... (existing RR implementation)
        if (arrivalTimes.isEmpty() || burstTimes.isEmpty() || arrivalTimes.size() != burstTimes.size()) {
            return "Invalid input: The maps are empty or have different sizes.";
        }

        Map<Integer, Integer> remainingBurstTimes = new HashMap<>(burstTimes);
        Queue<Integer> readyQueue = new LinkedList<>();
        int completedCount = 0;
        int totalProcesses = processIndexList.size();
        int processIndex = 0;

        while (completedCount < totalProcesses) {
            while (processIndex < totalProcesses
                    && arrivalTimes.get(processIndexList.get(processIndex)) <= currentTime) {
                int currentProcess = processIndexList.get(processIndex);
                if (!readyQueue.contains(currentProcess)) {
                    readyQueue.add(currentProcess);
                }
                processIndex++;
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            int currentProcess = readyQueue.poll();
            int arrival = arrivalTimes.get(currentProcess);
            int originalBurst = burstTimes.get(currentProcess);
            if (!firstCpuAllocationTime.containsKey(currentProcess)) {
                firstCpuAllocationTime.put(currentProcess, currentTime);
            }
            int executeTime = Math.min(timeQuantum, remainingBurstTimes.get(currentProcess));
            ganttChartLog.add(new GanttEntry(currentProcess, currentTime, currentTime + executeTime));
            currentTime += executeTime;
            remainingBurstTimes.put(currentProcess, remainingBurstTimes.get(currentProcess) - executeTime);
            while (processIndex < totalProcesses
                    && arrivalTimes.get(processIndexList.get(processIndex)) <= currentTime) {
                int newProcess = processIndexList.get(processIndex);
                if (!readyQueue.contains(newProcess)) {
                    readyQueue.add(newProcess);
                }
                processIndex++;
            }
            if (remainingBurstTimes.get(currentProcess) > 0) {
                readyQueue.add(currentProcess);
            } else {
                completionTime.put(currentProcess, currentTime);
                int tat = completionTime.get(currentProcess) - arrival;
                turnAroundTime.put(currentProcess, tat);
                int wt = tat - originalBurst;
                waitingTime.put(currentProcess, wt);
                int rt = firstCpuAllocationTime.get(currentProcess) - arrival;
                responseTimes.put(currentProcess, rt);
                completedCount++;
            }
        }
        return "Round Robin algorithm executed. Gantt chart and table will be shown here.";
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Implements the Multilevel Feedback Queue (MLFQ) scheduling algorithm.
     * This is a basic 3-level queue with different time quanta per queue and aging
     * logic.
     *
     * @return A status message.
     */
    private String multilevelFeedbackQueueOperation() {
        if (arrivalTimes.isEmpty() || burstTimes.isEmpty() || arrivalTimes.size() != burstTimes.size()) {
            return "Invalid input: The maps are empty or have different sizes.";
        }

        Map<Integer, Integer> remainingBurstTimes = new HashMap<>(burstTimes);
        Map<Integer, Integer> processQueueLevel = new HashMap<>();
        Map<Integer, Integer> waitTimesInLowerQueue = new HashMap<>();

        int completedCount = 0;
        int totalProcesses = processIndexList.size();
        currentTime = 0;
        int processIndex = 0;

        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
        queue3 = new LinkedList<>();
        for (Integer process : processIndexList) {
            processQueueLevel.put(process, 1);
            waitTimesInLowerQueue.put(process, 0);
        }

        while (completedCount < totalProcesses) {
            while (processIndex < totalProcesses
                    && arrivalTimes.get(processIndexList.get(processIndex)) <= currentTime) {
                int newProcess = processIndexList.get(processIndex);
                if (!queue1.contains(newProcess) && !queue2.contains(newProcess) && !queue3.contains(newProcess)) {
                    queue1.add(newProcess);
                }
                processIndex++;
            }

            for (Integer process : processQueueLevel.keySet()) {
                if (processQueueLevel.get(process) > 1 && !completionTime.containsKey(process)) {
                    waitTimesInLowerQueue.put(process, waitTimesInLowerQueue.get(process) + 1);
                    if (waitTimesInLowerQueue.get(process) >= AGING_THRESHOLD) {
                        waitTimesInLowerQueue.put(process, 0);

                        int currentLevel = processQueueLevel.get(process);
                        if (currentLevel == 2) {
                            queue2.remove(process);
                            queue1.add(process);
                            processQueueLevel.put(process, 1);
                        } else if (currentLevel == 3) {
                            queue3.remove(process);
                            queue2.add(process);
                            processQueueLevel.put(process, 2);
                        }
                    }
                }
            }

            int currentProcess = -1;
            int timeQuantum = 0;
            if (!queue1.isEmpty()) {
                currentProcess = queue1.poll();
                timeQuantum = this.quantum1; // Use the dynamic quantum
            } else if (!queue2.isEmpty()) {
                currentProcess = queue2.poll();
                timeQuantum = this.quantum2; // Use the dynamic quantum
            } else if (!queue3.isEmpty()) {
                currentProcess = queue3.poll();
                timeQuantum = Integer.MAX_VALUE;
            } else {
                currentTime++;
                continue;
            }

            if (!firstCpuAllocationTime.containsKey(currentProcess)) {
                firstCpuAllocationTime.put(currentProcess, currentTime);
            }

            int executeTime = Math.min(timeQuantum, remainingBurstTimes.get(currentProcess));
            ganttChartLog.add(new GanttEntry(currentProcess, currentTime, currentTime + executeTime));
            currentTime += executeTime;
            remainingBurstTimes.put(currentProcess, remainingBurstTimes.get(currentProcess) - executeTime);

            while (processIndex < totalProcesses
                    && arrivalTimes.get(processIndexList.get(processIndex)) <= currentTime) {
                int newProcess = processIndexList.get(processIndex);
                if (!queue1.contains(newProcess) && !queue2.contains(newProcess) && !queue3.contains(newProcess)) {
                    queue1.add(newProcess);
                }
                processIndex++;
            }

            if (remainingBurstTimes.get(currentProcess) > 0) {
                if (processQueueLevel.get(currentProcess) == 1) {
                    queue2.add(currentProcess);
                    processQueueLevel.put(currentProcess, 2);
                } else if (processQueueLevel.get(currentProcess) == 2) {
                    queue3.add(currentProcess);
                    processQueueLevel.put(currentProcess, 3);
                } else {
                    queue3.add(currentProcess);
                }
            } else {
                completionTime.put(currentProcess, currentTime);
                int tat = completionTime.get(currentProcess) - arrivalTimes.get(currentProcess);
                turnAroundTime.put(currentProcess, tat);
                int wt = tat - burstTimes.get(currentProcess);
                waitingTime.put(currentProcess, wt);
                int rt = firstCpuAllocationTime.get(currentProcess) - arrivalTimes.get(currentProcess);
                responseTimes.put(currentProcess, rt);
                completedCount++;
            }
        }
        return "MLFQ algorithm executed. Gantt chart and table will be shown here.";
    }

    public int getArrivalTimesSize() {
        return arrivalTimes.size();
    }

    public int getBurstTimesSize() {
        return burstTimes.size();
    }

    public List<GanttEntry> getGanttChartLog() {
        return ganttChartLog;
    }
}