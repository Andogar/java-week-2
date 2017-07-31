package com.company;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Creator {

    public void createWorkOrders() {
        ObjectMapper mapper = new ObjectMapper();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your work orders ID");
        Integer workOrderID = scanner.nextInt();

        scanner.nextLine();
        System.out.println("Enter the description of your work order");
        String senderName = scanner.nextLine();

        System.out.println("Enter your name");
        String description = scanner.nextLine();

        WorkOrder workOrder = new WorkOrder(workOrderID, description, senderName, Status.INITIAL);

        String json;
        try {
            File newJson = new File(workOrder.getId() + ".json");
            json = mapper.writeValueAsString(workOrder);
            FileWriter createFile = new FileWriter(newJson);
            createFile.write(json);
            createFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Creator creator = new Creator();
        creator.createWorkOrders();
    }
}
