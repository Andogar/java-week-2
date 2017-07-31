package com.company;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Processor {
    ObjectMapper mapper = new ObjectMapper();
    Map<Status, Set> workOrderMap = new HashMap<>();
    Set<WorkOrder> assignedSet = new HashSet<>();
    Set<WorkOrder> progressSet = new HashSet<>();
    Set<WorkOrder> doneSet = new HashSet<>();


    public void processWorkOrders() {
        readIt();
      }

    private void readIt() {
        File currentDirectory = new File(".");
        File files[] = currentDirectory.listFiles();
        Set<WorkOrder> orderSet = new HashSet<>();

        for (File f : files) {
            if (f.getName().endsWith(".json")) {
                FileReader jsonFiles;
                try {
                    jsonFiles = new FileReader(f);
                    WorkOrder order = mapper.readValue(jsonFiles, WorkOrder.class);

                    if (order.getStatus() == Status.INITIAL) {
                        orderSet.add(order);
                        System.out.println(order.getId() + ", " + order.getDescription() + ", " + order.getSenderName() + ", " + order.getStatus());
                        f.delete();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        workOrderMap.put(Status.INITIAL, orderSet);
    }

    private void moveIt() {

        // If there are no orders with Initial as their status, this will error out with a null pointer exception

        Set<WorkOrder> initialSet = workOrderMap.get(Status.INITIAL);


        for (WorkOrder order : progressSet) {
            doneSet.add(order);
            System.out.println("moved " + order.getId() + " to done");

            order.setStatus(Status.DONE);
            String json;
            try {
                File newJson = new File(order.getId() + ".json");
                json = mapper.writeValueAsString(order);
                FileWriter createFile = new FileWriter(newJson);
                createFile.write(json);
                createFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        progressSet.clear();
        workOrderMap.putIfAbsent(Status.DONE, doneSet);
        doneSet.clear();

        for (WorkOrder order : assignedSet) {
            progressSet.add(order);
            System.out.println("moved " + order.getId() + " to in progress");
        }

        assignedSet.clear();

        for (WorkOrder order : initialSet) {
            assignedSet.add(order);
            System.out.println("moved  " + order.getId() + " to assigned");
        }

        initialSet.clear();
        workOrderMap.remove(Status.INITIAL, initialSet);
    }

    public static void main(String args[]) {
        Processor processor = new Processor();
        try {
            while (true) {
                processor.processWorkOrders();
                Thread.sleep(5000l);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
