# 🧠 Project: CPU Scheduling Algorithm Simulator

## 🌟  Project Goal

To simulate and analyze various CPU scheduling algorithms, helping users understand their behavior and performance in an operating system context.

---

## 📘 Introduction
This project is a console-based simulator that models how different CPU scheduling strategies manage process execution. It allows users to input custom processes and observe how algorithms like FCFS, SJF, SRT, RR, and MLFQ handle scheduling. The simulator visualizes execution using Gantt charts and provides key performance metrics for comparison.

---

## 🎯 Objectives
- Understand the mechanics of CPU scheduling in OS
- Implement and simulate multiple scheduling algorithms
- Compare algorithm performance using real-time metrics
- Provide intuitive visual and tabular outputs for analysis

---

## ✨ Features
- ⚙️ Supports five scheduling algorithms:
  - FCFS (First Come First Serve)
  - SJF (Shortest Job First - Non-preemptive)
  - SRT (Shortest Remaining Time - Preemptive)
  - RR (Round Robin with configurable quantum)
  - MLFQ (Multilevel Feedback Queue with aging and promotion/demotion)
- 📊 Gantt chart visualization of scheduling
- 📈 Performance metrics:
  - Waiting Time
  - Turnaround Time
  - Response Time
  - Averages for all metrics

---

## 🧰 Technology Stack

- **🖥️ Language**: Java ☕  
  Used for implementing core scheduling logic and managing process data structures.

- **🎨 UI Framework**: JavaFX  
  Provides a responsive and interactive graphical user interface for input, control, and output visualization.

- **📊 Visualization**: Gantt Chart in JavaFX  
  Displays process execution timelines using dynamic Gantt charts rendered directly in the JavaFX UI.

---

## 🗂️ Project Structure
```Plaintext
📁 CPU-Scheduling-Simulator/
│
├── 📁 .vscode/ 
│   VS Code workspace settings (optional)
│
├── 📁 bin/ 
│   Compiled .class files
│
├── 📁 lib/ 
│   Libraries
├── 📁 src/ 
│   Source code files  
│   ├── App.java — Main entry point  
│   ├── Controller.java — Handles user input and scheduling logic  
│   ├── GanttEntry.java — Represents a unit in the Gantt chart  
│   ├── Model.java — Core scheduling algorithms and data  
│   ├── ProcessData.java — Process structure and attributes  
│   └── View.java — JavaFX UI components and layout
└── README.md  — Setup, usage, and documentation
```
---
## ⚙️ Implemented Algorithms

| Algorithm | Preemptive | Description                                                       |
|----------|------------|--------------------------------------------------------------------|
| FCFS     | No         | Processes are executed in the order they arrive in the ready queue |
| SJF      | No         | The process with the shortest burst time is selected to run next   |
| SRT      | Yes        | The preemptive version of SJF. If a new process arrives with a shorter remaining burst time, the current process is interrupted |
| RR       | Yes        |  Each process is given a small, fixed time slice to run in a circular order. |
| MLFQ     | Yes        | Processes move between 3 queues with different priorities and scheduling algorithms based on their behavior |

---

##  🛠️  How to Setup

### 1. Check `settings.json`

Ensure your `.vscode/settings.json` file contains the correct project structure and library references:

```json
{
    "java.project.sourcePaths": [
        "src"
    ],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": [
        "lib/libs/**/*.jar"
    ]
}
```

### 2. Check Java Runtime Version

Ensure you're using the correct JDK version.

- Open the command palette or run `Ctrl+Shift+P`, then search for **Java: Configure Java Runtime**
- Confirm the runtime is set to:  
  `JavaSE-24`

### 3. Check `launch.json` Configuration


1. Check your .vscode/launch.json before running App.java, you should see something like this:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "App",
            "projectName": "cpu-scheduling_40fcacf6",
            "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml"
        }
    ]
}
```

2. Run App.java. Then check your launch. json again, you should see something like this:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "App",
            "projectName": "cpu-scheduling_5909c15f"
        },
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "App",
            "projectName": "cpu-scheduling_40fcacf6",
            "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml"
        }
    ]
}
```

3. You have to copy the projectName of the first configuration "cpu-scheduling_5909c15f" and paste it into the projectName of the second configuration. Then delete the first configuration, you should see something like this:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "App",
            "projectName": "cpu-scheduling_5909c15f",
            "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml"
        }
    ]
}
```

---

##  ✅  How to use

1. After setting up the project, run App.java.

2. On the left screen, select the algorithm you want.

3. Then input arrival times, burst times, and quantum times (if RR or MLFQ).

4. Finally, press Solve, and then you will receive the Gantt chart and tables on the right screen.
