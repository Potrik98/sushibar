package sushi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sushi.StreamUtils.uncheckRun;


public class SushiBar {

    //SushiBar settings
    private static int waitingAreaCapacity = 5;
    private static int waitressCount = 2;
    private static int duration = 4;
    public static int maxOrder = 10;
    public static int waitressWait = 50; // Used to calculate the time the waitress spends before taking the order
    public static int customerWait = 2000; // Used to calculate the time the customer spends eating
    public static int doorWait = 1; // Used to calculate the interval at which the door tries to create a customer
    public static boolean isOpen = true;

    //Creating log file
    private static File log;
    private static String path = "./";

    //Variables related to statistics
    public static SynchronizedInteger customerCounter;
    public static SynchronizedInteger servedOrders;
    public static SynchronizedInteger takeawayOrders;
    public static SynchronizedInteger totalOrders;


    public static void main(String[] args) {
        log = new File(path + "log.txt");

        //Initializing shared variables for counting number of orders
        customerCounter = new SynchronizedInteger(0);
        totalOrders = new SynchronizedInteger(0);
        servedOrders = new SynchronizedInteger(0);
        takeawayOrders = new SynchronizedInteger(0);

        final Clock c = new Clock(duration);
        final WaitingArea w = new WaitingArea(waitingAreaCapacity);
        final Door door = new Door(w);

        Door.doorThread = new Thread(door, "Door");
        Door.doorThread.start();

        final List<Thread> waitressThreads = IntStream.range(0, waitressCount)
                .mapToObj(i -> new Waitress(w, i))
                .map(x -> new Thread(x, x.toString()))
                .collect(Collectors.toList());
        waitressThreads.forEach(Thread::start);

        waitressThreads.forEach(t -> uncheckRun(t::join));
        uncheckRun(Door.doorThread::join);

        SushiBar.write("Bar closed");
        SushiBar.write("Total orders: " + totalOrders.get());
        SushiBar.write("Served orders: " + servedOrders.get());
        SushiBar.write("Takeaway orders: " + takeawayOrders.get());
    }

    //Writes actions in the log file and console
    public static void write(String str) {
        try {
            FileWriter fw = new FileWriter(log.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            final String text = "[" + Thread.currentThread().getName() + "] " + Clock.getTime() + ", " + str;
            bw.write(text + "\n");
            bw.close();
            System.out.println(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
