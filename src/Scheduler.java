// Fatema Redha Hamza           202102467
// Sara Mansoor Mahmood         202109880
// Fatima Mohamed Hasan Abdulla 202206839
// Eman rauf                    202009084
// Section: 2

package src;

import java.util.*;

class Process{ //class for the data structure
    /*
    his defines a Process class to hold all necessary details for each process:
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

    //Constructor that initializes a Process with its given data. remainingTime is set to the full burstTime at first.
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

//        System.out.println("\nProcesses Entered:");
//        for (Process p : processList) {
//            System.out.printf("PID: %d, Arrival: %d, Burst: %d, Priority: %d\n",
//                    p.pid, p.arrivalTime, p.burstTime, p.priority);
//        }




    }
}
