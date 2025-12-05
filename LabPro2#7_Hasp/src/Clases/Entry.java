/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author HP
 */
public class Entry {
    private String username;
    private long posicion;
    private Entry siguiente;
    
    public Entry(String username, long posicion){
        this.username = username;
        this.posicion = posicion;
        this.siguiente = null;
    }
    
    public String getUsername(){
        return username;
    }
    
    public long getPosicion(){
        return posicion;
    }
    
    public Entry getSiguiente(){
        return siguiente;
    }
    
    public void setSiguiente(Entry siguiente){
        this.siguiente = siguiente;
    }
}
