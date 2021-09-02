package model.stanica;

import model.kompozicija.Kompozicija;

import java.util.ArrayList;

public class Stanica {

    private ArrayList<Kompozicija> kompozicije;
    private boolean slobodno;
    private String name;
    public Stanica(){

    }
    public String getName(){return name;}
}
