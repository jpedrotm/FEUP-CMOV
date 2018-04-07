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

    public void addUserCoffee(Long id, int amount) {
        metadata.addUserCoffee(id, amount);
        saveMetadata();
    }

    public boolean hasNewFreeCoffeeVoucher(Long id) {
        return metadata.hasNewFreeCoffeeVoucher(id);
    }

    public boolean hasNewDiscountVoucher(Long id) {
        return metadata.hasNewDiscountVoucher(id);
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
