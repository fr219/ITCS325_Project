// Fatema Redha Hamza           202102467
// Sara Mansoor Mahmood         202109880
// Fatima Mohamed Hasan Abdulla 202206839
// Eman rauf                    202009084
// Section: 2

package src;

import java.util.*;

class Process { // class for the data structure
    /*
    This defines a Process class to hold all necessary details for each process:
    pid: Process ID
    arrivalTime: When the process enters the system
    burstTime: Total time required to execute
    remainingTime: Changes as process executes
    priority: Lower value = higher priority
    completionTime, turnaroundTime, waitingTime, responseTime: Used for calculating final performance metrics
    started: Used to check if the process started for the first time (needed for response time)
    */
    int pid, arrivalTime, burstTime, remainingTime, priority;
    int completionTime, turnaroundTime, waitingTime, responseTime;
    boolean started = false;

    // Constructor that initializes a Process with its given data. remainingTime is set to the full burstTime at first.
    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> processList = new ArrayList<>();
        System.out.println("Enter process details: PID ArrivalTime BurstTime Priority");
        System.out.println("Enter 0 0 0 0 to stop input.");

        while (true) {
            int pid = sc.nextInt();
            int at = sc.nextInt();
            int bt = sc.nextInt();
            int pr = sc.nextInt();

            if (pid == 0 && at == 0 && bt == 0 && pr == 0)
                break;

            processList.add(new Process(pid, at, bt, pr));
        }

        // ---- Begin Scheduling Logic ----
        int time = 0;
        int completed = 0;
        int n = processList.size();
        List<String> gantt = new ArrayList<>();

        while (completed < n) {
            Process current = null;

            for (Process p : processList) {
                if (p.arrivalTime <= time && p.remainingTime > 0) {
                    if (current == null ||
                            p.remainingTime < current.remainingTime ||
                            (p.remainingTime == current.remainingTime && p.priority < current.priority)) {
                        current = p;
                    }
                }
            }

            if (current != null) {
                gantt.add("P" + current.pid);
                if (!current.started) {
                    current.responseTime = time - current.arrivalTime;
                    current.started = true;
                }
                current.remainingTime--;
                if (current.remainingTime == 0) {
                    current.completionTime = time + 1;
                    current.turnaroundTime = current.completionTime - current.arrivalTime;
                    current.waitingTime = current.turnaroundTime - current.burstTime;
                    completed++;
                }
            } else {
                gantt.add("Idle");
            }

            time++;
        }

        // ---- Gantt Chart Output ----
        System.out.println("\nGantt Chart:");
        for (String entry : gantt) {
            System.out.print("|" + entry);
        }
        System.out.println("|");

        // ---- Process Table and Averages ----
        double totalTAT = 0, totalWT = 0, totalRT = 0;
        System.out.println("\nPID\tAT\tBT\tPR\tCT\tTAT\tWT\tRT");
        for (Process p : processList) {
            totalTAT += p.turnaroundTime;
            totalWT += p.waitingTime;
            totalRT += p.responseTime;
            System.out.printf("P%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
                    p.pid, p.arrivalTime, p.burstTime, p.priority,
                    p.completionTime, p.turnaroundTime, p.waitingTime, p.responseTime);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTAT / n);
        System.out.printf("Average Waiting Time: %.2f\n", totalWT / n);
        System.out.printf("Average Response Time: %.2f\n", totalRT / n);
    }
}
