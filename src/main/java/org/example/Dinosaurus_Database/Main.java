package org.example.Dinosaurus_Database;



import java.sql.SQLException;
import java.util.ArrayList;

import static org.example.Dinosaurus_Database.DB.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Dino Trex = new Dino("Trex", 25, false,"Africa");
        Dino Raptor = new Dino("Raptor", 10, false, "Africa");
        Dino Brontosaurus = new Dino("Brontosaurus", 30, true, "Africa");
        Dino Velociraptor = new Dino("Velociraptor", 5, true, "Africa");
        Dino dina=new Dino("Ptero", 10, false, "All of the past world");
        ArrayList<Dino> dinos = DB.read();

        for (Dino dino : dinos) {
            System.out.println(dino.getType());
            System.out.println(dino.getH());
            System.out.println(dino.isStatus());
            System.out.println(dino.getLocation());
            System.out.println("--------------------");

        }
    }
}
