import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.cell.PropertyValueFactory;

public class View extends HBox {
    private ComboBox<String> algorithmComboBox;
    private TextField arrivalTextField;
    private TextField burstTextField;
    private TextField quantumTextField;
    private TextField quantum2TextField;
    private Button solveButton;
    private VBox outputPanel;
    private VBox quantumBox;
    private VBox quantum2Box;

    public View() {
        super(24);
        this.setPadding(new Insets(24));
        this.setStyle("-fx-background-color: #f0f2f5;");

        VBox inputPanel = new VBox(12);
        inputPanel.setPadding(new Insets(24));
        inputPanel.setPrefWidth(300);
        inputPanel.setStyle(
                "-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label inputTitle = new Label("Input");
        inputTitle.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label algorithmLabel = new Label("Algorithm");
        algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll("First Come First Serve, FCFS", "Shortest Job First, SJF",
                "Shortest Remaining Time First, SRTF", "Round Robin, RR", "Multilevel Feedback Queue, MLFQ");
        algorithmComboBox.getSelectionModel().selectFirst();
        algorithmComboBox.setPrefWidth(Double.MAX_VALUE);

        Label quantumLabel = new Label("Time Quantum (Queue 1)");
        quantumTextField = new TextField();
        quantumTextField.setPromptText("e.g. 4");
        quantumBox = new VBox(12, quantumLabel, quantumTextField);
        quantumBox.managedProperty().bind(quantumBox.visibleProperty());
        quantumBox.setVisible(false);

        Label quantum2Label = new Label("Time Quantum (Queue 2)");
        quantum2TextField = new TextField();
        quantum2TextField.setPromptText("e.g. 8");
        quantum2Box = new VBox(12, quantum2Label, quantum2TextField);
        quantum2Box.managedProperty().bind(quantum2Box.visibleProperty());
        quantum2Box.setVisible(false);

        algorithmComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isRR = "Round Robin, RR".equals(newVal);
            boolean isMLFQ = "Multilevel Feedback Queue, MLFQ".equals(newVal);

            quantumBox.setVisible(isRR || isMLFQ);
            quantum2Box.setVisible(isMLFQ);
        });

        Label arrivalLabel = new Label("Arrival Times");
        arrivalTextField = new TextField();
        arrivalTextField.setPromptText("e.g. 0 2 4 6 8");

        Label burstLabel = new Label("Burst Times");
        burstTextField = new TextField();
        burstTextField.setPromptText("e.g. 2 4 6 8 10");

        solveButton = new Button("Solve");
        solveButton.setPrefSize(Double.MAX_VALUE, 50);
        solveButton.setStyle(
                "-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");

        inputPanel.getChildren().addAll(
                inputTitle, new Region() {
                    {
                        setPrefHeight(12);
                    }
                },
                algorithmLabel, algorithmComboBox, new Region() {
                    {
                        setPrefHeight(12);
                    }
                },
                quantumBox,
                quantum2Box,
                arrivalLabel, arrivalTextField, new Region() {
                    {
                        setPrefHeight(12);
                    }
                },
                burstLabel, burstTextField, new Region() {
                    {
                        setPrefHeight(24);
                    }
                },
                solveButton);

        outputPanel = createInitialOutputPanel();

        this.getChildren().addAll(inputPanel, outputPanel);
        HBox.setHgrow(inputPanel, Priority.ALWAYS);
        HBox.setHgrow(outputPanel, Priority.ALWAYS);
    }

    private VBox createInitialOutputPanel() {
        VBox panel = new VBox(24);
        panel.setPadding(new Insets(24));
        panel.setStyle(
                "-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label title = new Label("Output");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label placeholder = new Label("Gantt chart and table will be shown here");
        placeholder.setFont(Font.font("System", 16));
        placeholder.setTextFill(Color.GREY);

        VBox contentBox = new VBox();
        contentBox.getChildren().add(placeholder);
        contentBox.setPrefHeight(Double.MAX_VALUE);
        contentBox.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(title, contentBox);
        return panel;
    }

    public void displayResults(Map<String, Object> results) {
        outputPanel.getChildren().clear();

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);
        Label outputTitle = new Label("Output");
        outputTitle.setFont(Font.font("System", FontWeight.BOLD, 28));
        Label algoLabel = new Label((String) results.get("algorithm"));
        algoLabel.setStyle(
                "-fx-background-color: #F0FFF0; -fx-text-fill: #3CB371; -fx-border-color: #3CB371; -fx-border-width: 1; -fx-border-radius: 4; -fx-padding: 4;");
        header.getChildren().addAll(outputTitle, algoLabel);
        outputPanel.getChildren().add(header);

        VBox ganttChart = createGanttChart(results);
        outputPanel.getChildren().add(ganttChart);

        TableView<ProcessData> table = createTable(results);
        outputPanel.getChildren().add(table);
    }

    @SuppressWarnings("unchecked")
    private VBox createGanttChart(Map<String, Object> results) {
        VBox ganttContainer = new VBox(10);
        ganttContainer.setAlignment(Pos.CENTER_LEFT);

        Label ganttTitle = new Label("Gantt Chart");
        ganttTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        ganttTitle.setPadding(new Insets(0, 0, 10, 0));

        HBox chart = new HBox(0);
        HBox timeline = new HBox(0);

        List<GanttEntry> ganttLog = (List<GanttEntry>) results.get("ganttChartLog");

        if (ganttLog == null || ganttLog.isEmpty()) {
            System.err.println("Gantt chart log data is incomplete or empty.");
            return ganttContainer;
        }

        int firstEventTime = ganttLog.get(0).startTime;
        if (firstEventTime > 0) {
            double idleWidth = firstEventTime * 30;

            StackPane idleBlock = new StackPane();
            idleBlock.setPrefWidth(idleWidth);
            idleBlock.setPrefHeight(40);
            idleBlock.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: black; -fx-border-width: 1;");
            chart.getChildren().add(idleBlock);
        }

        Label initialTimeLabel = new Label("0");
        timeline.getChildren().add(initialTimeLabel);
        int lastEndTime = firstEventTime;

        for (GanttEntry entry : ganttLog) {
            if (entry.startTime > lastEndTime) {
                double idleDuration = entry.startTime - lastEndTime;
                double idleWidth = idleDuration * 30;

                StackPane idleBlock = new StackPane();
                idleBlock.setPrefWidth(idleWidth);
                idleBlock.setPrefHeight(40);
                idleBlock.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: black; -fx-border-width: 1;");
                chart.getChildren().add(idleBlock);

                Label idleTimeLabel = new Label(String.valueOf(entry.startTime));
                timeline.getChildren().add(idleTimeLabel);
            }

            double blockWidth = (entry.endTime - entry.startTime) * 30;
            StackPane processBlock = new StackPane();
            processBlock.setPrefWidth(blockWidth);
            processBlock.setPrefHeight(40);
            processBlock.setStyle("-fx-background-color: #87CEFA; -fx-border-color: black; -fx-border-width: 1;");

            Label processLabel = new Label(String.valueOf((char) ('A' + entry.processId)));
            processLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            processBlock.getChildren().add(processLabel);

            chart.getChildren().add(processBlock);

            Region timeSpacer = new Region();
            HBox.setHgrow(timeSpacer, Priority.ALWAYS);
            timeSpacer.setPrefWidth(blockWidth);
            timeline.getChildren().add(timeSpacer);

            Label timeLabel = new Label(String.valueOf(entry.endTime));
            timeline.getChildren().add(timeLabel);

            lastEndTime = entry.endTime;
        }

        ganttContainer.getChildren().addAll(ganttTitle, chart, timeline);
        return ganttContainer;
    }

    @SuppressWarnings("unchecked")
    private TableView<ProcessData> createTable(Map<String, Object> results) {
        TableView<ProcessData> table = new TableView<>();

        TableColumn<ProcessData, String> jobCol = new TableColumn<>("Job");
        jobCol.setCellValueFactory(new PropertyValueFactory<>("job"));

        TableColumn<ProcessData, Integer> arrivalCol = new TableColumn<>("Arrival Time");
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));

        TableColumn<ProcessData, Integer> burstCol = new TableColumn<>("Burst Time");
        burstCol.setCellValueFactory(new PropertyValueFactory<>("burstTime"));

        TableColumn<ProcessData, Integer> finishCol = new TableColumn<>("Finish Time");
        finishCol.setCellValueFactory(new PropertyValueFactory<>("finishTime"));

        TableColumn<ProcessData, Integer> tatCol = new TableColumn<>("Turnaround Time");
        tatCol.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));

        TableColumn<ProcessData, Integer> waitCol = new TableColumn<>("Waiting Time");
        waitCol.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        TableColumn<ProcessData, Integer> responseCol = new TableColumn<>("Response Time");
        responseCol.setCellValueFactory(new PropertyValueFactory<>("responseTime"));

        table.getColumns().addAll(jobCol, arrivalCol, burstCol, finishCol, tatCol, waitCol, responseCol);

        Map<Integer, Integer> arrivalTimes = (Map<Integer, Integer>) results.get("arrivalTimes");
        Map<Integer, Integer> burstTimes = (Map<Integer, Integer>) results.get("burstTimes");
        Map<Integer, Integer> completionTimes = (Map<Integer, Integer>) results.get("completionTimes");
        Map<Integer, Integer> turnAroundTimes = (Map<Integer, Integer>) results.get("turnAroundTimes");
        Map<Integer, Integer> waitingTimes = (Map<Integer, Integer>) results.get("waitingTimes");
        Map<Integer, Integer> responseTimes = (Map<Integer, Integer>) results.get("responseTimes");
        List<Integer> processOrder = (List<Integer>) results.get("processOrder");

        if (arrivalTimes == null || burstTimes == null || completionTimes == null || turnAroundTimes == null
                || waitingTimes == null || responseTimes == null || processOrder == null) {
            System.err.println("Table data is incomplete.");
            return table;
        }

        double totalTAT = 0;
        double totalWT = 0;
        double totalRT = 0;

        for (int processIndex : processOrder) {
            ProcessData data = new ProcessData(
                    String.valueOf((char) ('A' + processIndex)),
                    arrivalTimes.get(processIndex),
                    burstTimes.get(processIndex),
                    completionTimes.get(processIndex),
                    turnAroundTimes.get(processIndex),
                    waitingTimes.get(processIndex),
                    responseTimes.get(processIndex));
            table.getItems().add(data);
            totalTAT += data.getTurnaroundTime();
            totalWT += data.getWaitingTime();
            totalRT += data.getResponseTime();
        }

        if (!table.getItems().isEmpty()) {
            int numberOfProcesses = table.getItems().size();
            double avgTAT = totalTAT / numberOfProcesses;
            double avgWT = totalWT / numberOfProcesses;
            double avgRT = totalRT / numberOfProcesses;

            table.getItems().add(new ProcessData("Average", 0, 0, 0, (int) Math.round(avgTAT), (int) Math.round(avgWT),
                    (int) Math.round(avgRT)));
        }

        return table;
    }

    public ComboBox<String> getAlgorithmComboBox() {
        return algorithmComboBox;
    }

    public TextField getArrivalTextField() {
        return arrivalTextField;
    }

    public TextField getBurstTextField() {
        return burstTextField;
    }

    public Button getSolveButton() {
        return solveButton;
    }

    public TextField getQuantumTextField() {
        return quantumTextField;
    }

    public TextField getQuantum2TextField() {
        return quantum2TextField;
    }
}