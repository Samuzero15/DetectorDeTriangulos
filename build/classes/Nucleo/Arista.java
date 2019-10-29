/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Nucleo;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import static java.lang.Double.NaN;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Samuzero15
 */
public class Arista {
    private Vertice vInicial;
    private Vertice vFinal;
    //private ArrayList<Vertice> puntosDeArista;
    private double pendiente;
    private Line2D hit;
    private String num;
    
    private final int LINE_OFFSET = 10; 

    public Arista(Vertice vInicial, Vertice vFinal, String num) {
        this.vInicial = vInicial;
        this.vFinal = vFinal;
        this.num = num;
        this.hit = new Line2D.Double(vInicial.getCords().x + LINE_OFFSET, vInicial.getCords().y + LINE_OFFSET,
                                       vFinal.getCords().x + LINE_OFFSET, vFinal.getCords().y + LINE_OFFSET); 
        actualizaPendiente();
    }
    
    private void actualizaPendiente()
    {
        Point pf = this.vFinal.getCords();
        Point pi = this.vInicial.getCords();
        
        double Xn = (pf.x - pi.x);

            pendiente = (pf.y - pi.y) / Xn;
    }

    public String getNum() { return num;}
    
    public Vertice getvInicial() { return vInicial;}
    public Vertice getvFinal() {return vFinal;}
    
    
    public boolean contiene(Vertice v)
    {
        return v.equals(this.vInicial) || v.equals(this.vFinal) /*|| puntosDeArista.contains(v)*/;
    }
    
    public boolean esColinear(Arista conQuien)
    {
        return this.pendiente == conQuien.pendiente;
    }
    
    public boolean intersecta(Arista conQuien)
    {
        return this.hit.intersectsLine(conQuien.getHit());
    }
    
    public Point puntoDondeIntersecta(Arista conQuien)
    {
        Point interseccion = new Point(0,0);
        if(! intersecta(conQuien) || this.esColinear(conQuien)) return null;
        
        double a1 = this.vFinal.getHit().y - this.vInicial.getHit().y; 
        double b1 = this.vInicial.getHit().x - this.vFinal.getHit().x; 
        double c1 = a1*(this.vInicial.getHit().x) + b1*(this.vInicial.getHit().y); 
       
        // Line CD represented as a2x + b2y = c2 
        double a2 = conQuien.vFinal.getHit().y - conQuien.vInicial.getHit().y; 
        double b2 = conQuien.vInicial.getHit().x - conQuien.vFinal.getHit().x; 
        double c2 = a2*(conQuien.vInicial.getHit().x)+ b2*(conQuien.vInicial.getHit().y); 
       
        double determinant = a1*b2 - a2*b1; 
       
        if (determinant == 0) 
        { 
            // The lines are parallel. This is simplified 
            // by returning a pair of FLT_MAX 
            return null; 
        } 
        else
        { 
            double x = (b2*c1 - b1*c2)/determinant; 
            double y = (a1*c2 - a2*c1)/determinant; 
            interseccion.x = (int)x +10;
            interseccion.y = (int)y +10;
        } 
        
        
        return interseccion;
    }

    public Line2D getHit() {return hit;}
    
    public void mueve(double dx, double dy)
    { 
        double x = dx + LINE_OFFSET;
        double y = dy + LINE_OFFSET;
        Point pf = this.vFinal.getCords();
        Point pi = this.vInicial.getCords();
        this.hit.setLine(pi.x + x, pi.y + y, pf.x + x, pf.y + y);
        this.vInicial.setCords(pi.x + dx, pi.y + dy);
        this.vFinal.setCords(pf.x + dx, pf.y + dy);
    }

    private void reconstruye()
    { 
        Point pf = this.vFinal.getCords();
        Point pi = this.vInicial.getCords();
        this.hit.setLine(pi.x + LINE_OFFSET, pi.y + LINE_OFFSET,
                         pf.x + LINE_OFFSET, pf.y + LINE_OFFSET);
    }
    
    
    public void setvInicial(Vertice vInicial) {
        this.vInicial = vInicial;
        actualizaPendiente();
        reconstruye();
    }
    public void setvFinal(Vertice vFinal) {
        this.vFinal = vFinal;
        actualizaPendiente();
        reconstruye();
    }
    public void setPuntosDeArista(ArrayList<Vertice> puntosDeArista) {/*this.puntosDeArista = puntosDeArista;*/}

    @Override
    public String toString() {
        String msg = "";
        
        Double pend = new Double(this.pendiente);
        
        if(pend.isInfinite()) msg = "No existe.";
        else msg = String.valueOf(this.pendiente);
        return "(" + this.num + ") =" + this.vInicial.toString() + " - " + this.vFinal.toString() + " Pend.: " + msg;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Arista other = (Arista) obj;
        if (!Objects.equals(this.num, other.num)) {
            return false;
        }
        return true;
    }
    
    
    
}
