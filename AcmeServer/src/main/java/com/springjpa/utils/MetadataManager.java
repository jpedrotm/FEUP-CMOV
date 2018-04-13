package com.springjpa.utils;

import java.io.*;

public class MetadataManager {

    private Metadata metadata;

    private static MetadataManager instance = null;

    public MetadataManager() {
            try {
                if(!(new File("metadata.ser")).exists()) {
                    metadata = new Metadata();
                    saveMetadata();
                } else {
                    readMetadata();
                }
            } catch (ClassNotFoundException | IOException e ) {
                e.printStackTrace();
            }
    }

    public static MetadataManager getInstance() {
        if(instance == null) {
            instance = new MetadataManager();
        }
        return instance;
    }

    public boolean addUserCoffee(Long id, int amount) {
        boolean addCoffeeVoucher = metadata.addUserCoffee(id, amount);
        saveMetadata();
        return addCoffeeVoucher;
    }

    public boolean addUserDiscount(Long id, double price){

        boolean addDiscountVoucher = metadata.addCustomerMoney(id,price);
        saveMetadata();
        return addDiscountVoucher;
    }

    private void saveMetadata() {
        try {FileOutputStream fileOut = new FileOutputStream("metadata.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(metadata);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMetadata() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("metadata.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        metadata = (Metadata) in.readObject();
        in.close();
        fileIn.close();
    }

    @Override
    public String toString() {
        return "MetadataManager{" +
                "metadata=" + metadata +
                '}';
    }

}
