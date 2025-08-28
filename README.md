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
📁 **CPU-Scheduling-Simulator/**
│
├── 📁 **.vscode/**  
│   VS Code workspace settings (optional)
│
├── 📁 **bin/**  
│   Compiled `.class` files
│
├── 📁 **lib/**  
│   External libraries (if any)
│
├── 📁 **src/**  
│   Source code files  
│   ├── App.java — Main entry point  
│   ├── Controller.java — Handles user input and scheduling logic  
│   ├── GanttEntry.java — Represents a unit in the Gantt chart  
│   ├── Model.java — Core scheduling algorithms and data  
│   ├── ProcessData.java — Process structure and attributes  
│   └── View.java — JavaFX UI components and layout
└── README.md  — Setup, usage, and documentation
```