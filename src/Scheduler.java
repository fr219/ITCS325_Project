// Fatema Redha Hamza           202102467
// Sara Mansoor Mahmood         202109880
// Fatima Mohamed Hasan Abdulla 202206839
// Eman rauf                    202009084
// Section: 2

package src;

import java.util.*;

class Process { // class for the data structure
    int pid, arrivalTime, burstTime, remainingTime, priority; //Used for calculating final performance
    int completionTime, turnaroundTime, waitingTime, responseTime;
    boolean started = false; //check if the process started for the first time

    // Constructor that initializes a Process with its given data. RemainingTime is set to the full burstTime at first.
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
        Set<Integer> userPID= new HashSet<>(); // To avoid duplicate PIDs

        // ----------- Input Section -----------
        while (true) {
            System.out.println("Enter processes line-by-line (PID ArrivalTime BurstTime Priority)");
            System.out.println("Type '0 0 0 0' on a new line to finish.\n");

            List<String> inputLines = new ArrayList<>();
            boolean allValid = true;
            processList.clear();
            userPID.clear();

            // Read input lines until termination line
            while (true) {
                String line = sc.nextLine().trim();
                if (line.equals("0 0 0 0")) {
                    break;
                }
                inputLines.add(line);
            }

            // Validate and parse input
            for (String line : inputLines) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 4) {
                    System.out.println("Error: Each line must contain exactly 4 values.");
                    allValid = false;
                    break;
                }

                int[] values = new int[4];
                for (int i = 0; i < 4; i++) {
                    if (!parts[i].matches("-?\\d+")) {
                        System.out.println("Error: Invalid character detected in line: \"" + line + "\"");
                        allValid = false;
                        break;
                    }
                    values[i] = Integer.parseInt(parts[i]);
                }

                if (!allValid) break;

                int pid = values[0], at = values[1], bt = values[2], pr = values[3];

                if (bt <= 0 || at < 0 || pr < 0) {
                    System.out.println("Error: ArrivalTime and Priority must be ≥ 0; BurstTime must be > 0.");
                    allValid = false;
                    break;
                }

                if (userPID.contains(pid)) {
                    System.out.println("Error: Duplicate PID detected (PID " + pid + ")");
                    allValid = false;
                    break;
                }

                processList.add(new Process(pid, at, bt, pr));
                userPID.add(pid);
            }

            if (!allValid) {
                System.out.println("\nOne or more lines were invalid. All data has been cleared.");
                System.out.println("Please re-enter the processes.\n");
                continue;
            } else if (processList.isEmpty()) {
                System.out.println(" No valid processes were entered. Please enter at least one process.\n");
                continue;
            } else {
                break;
            }

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
