import javafx.scene.control.Alert;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        initListeners();
    }

    private void initListeners() {
        view.getSolveButton().setOnAction(e -> {
            try {
                String selectedAlgorithm = view.getAlgorithmComboBox().getValue();
                String arrivalTimesString = view.getArrivalTextField().getText().trim();
                String burstTimesString = view.getBurstTextField().getText().trim();
                
                int quantum1 = 0;
                int quantum2 = 0;

                if ("Round Robin, RR".equals(selectedAlgorithm)) {
                    String quantumString = view.getQuantumTextField().getText().trim();
                    if (quantumString.isEmpty()) {
                        showAlert("Missing Input", "Please enter a time quantum for Round Robin.");
                        return;
                    }
                    quantum1 = Integer.parseInt(quantumString);
                    if (quantum1 <= 0) {
                        showAlert("Invalid Input", "Time quantum must be a positive integer.");
                        return;
                    }
                } else if ("Multilevel Feedback Queue, MLFQ".equals(selectedAlgorithm)) {
                    String quantum1String = view.getQuantumTextField().getText().trim();
                    String quantum2String = view.getQuantum2TextField().getText().trim();
                    if (quantum1String.isEmpty() || quantum2String.isEmpty()) {
                        showAlert("Missing Input", "Please enter time quanta for both Queue 1 and Queue 2.");
                        return;
                    }
                    quantum1 = Integer.parseInt(quantum1String);
                    quantum2 = Integer.parseInt(quantum2String);
                    if (quantum1 <= 0 || quantum2 <= 0) {
                        showAlert("Invalid Input", "Time quanta must be positive integers.");
                        return;
                    }
                }
                
                model.setAlgorithm(selectedAlgorithm);
                model.setArrivalTimes(parseInput(arrivalTimesString));
                model.setBurstTimes(parseInput(burstTimesString));
                
                if (model.getArrivalTimesSize() != model.getBurstTimesSize()) {
                    showAlert("Input Mismatch", "The number of arrival times and burst times must be equal.");
                    return;
                }

                model.initializeProcesses();
                model.reset(); 
                
                // Pass both quanta to the model for MLFQ, or just quantum1 for RR.
                if ("Multilevel Feedback Queue, MLFQ".equals(selectedAlgorithm)) {
                    model.runScheduling(quantum1, quantum2);
                } else {
                    model.runScheduling(quantum1, 0); // Pass quantum1 to RR, 0 for others
                }

                Map<String, Object> results = model.getResults();
                results.put("ganttChartLog", model.getGanttChartLog()); 
                
                view.displayResults(results);

            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for all fields.");
            }
        });
    }

    private Map<Integer, Integer> parseInput(String input) throws NumberFormatException {
        Map<Integer, Integer> dataMap = new HashMap<>();
        String[] values = input.split("[\\s,]+");
        for (int i = 0; i < values.length; i++) {
            if (!values[i].isEmpty()) {
                dataMap.put(i, Integer.parseInt(values[i]));
            }
        }
        return dataMap;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}