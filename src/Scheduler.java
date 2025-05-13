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

public class Scheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> processList = new ArrayList<>();
         Set<Integer> userPID= new HashSet<>(); 
        System.out.println("Enter process details: PID ArrivalTime BurstTime Priority");
        System.out.println("Enter 0 0 0 0 to stop input.");
        

        while (true) {
            String line = sc.nextLine();
            String[] parts = line.trim().split("\\s+");
            if(parts.length != 4) {
        System.out.println("Enter exactly 4 numbers separated by space.");
        continue;
    }

    boolean isValid = true;
    int[] values = new int[4];
    for (int i = 0; i < 4; i++) {
        if (!parts[i].matches("-?\\d+")) {
            isValid = false;
            break;
        }
        values[i] = Integer.parseInt(parts[i]);
    }

    if (!isValid) {
        System.out.println("All inputs must be integer.");
        continue;
    }

    int pid = values[0], at = values[1], bt = values[2], pr = values[3];

            if (pid == 0 && at == 0 && bt == 0 && pr == 0)
                break;
            if (bt <= 0 || at < 0 || pr < 0) {
                System.out.println("Invalid input. Please Make sure that all values are positive and burst time is greater than 0.");
                continue;
            }
            if (userPID.contains(pid)) {
                System.out.println("Duplicate PID detected. Please enter a unique PID.");
                continue;  
            }
           
            processList.add(new Process(pid, at, bt, pr));
            userPID.add(pid);
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
       System.out.print("       ");
       for (int i = 0; i < gantt.size(); i++) {
           System.out.printf("%-3d", i);
       }
       System.out.println();
       System.out.print("       ");
       for (String entry : gantt) {
         System.out.printf("|%-2s", entry);
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
        sc.close();
        // ---- End of Program ----
    }
}
