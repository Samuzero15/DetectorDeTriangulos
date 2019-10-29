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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author Samuzero15
 */
public class Arista {
    private Vertice vA;
    private Vertice vB;
    //private ArrayList<Vertice> puntosDeArista;
    private double longitud;
    private double pendiente;
    private double angInclinado;
    private Line2D hit;
    private String tag;
    
    private final int LINE_OFFSET = 10; 
    private static int CUENTA = 1;

    public Arista(Vertice vInicial, Vertice vFinal) {
        
        
        this.vA = vInicial;
        this.vB = vFinal;
        this.tag = Integer.toString(CUENTA);
        this.hit = new Line2D.Double(vInicial.getCords().x + LINE_OFFSET, vInicial.getCords().y + LINE_OFFSET,
                                       vFinal.getCords().x + LINE_OFFSET, vFinal.getCords().y + LINE_OFFSET); 
        actualizaPendiente();
        CUENTA++;
    }
    
    private void actualizaPendiente()
    {
        Point pf = this.vB.getCords();
        Point pi = this.vA.getCords();
        
        double Xn = (pf.x - pi.x);

            this.pendiente = (pf.y - pi.y) / Xn;
            
        
        double angulo = Math.toDegrees(Math.atan(this.pendiente));
        if(angulo < 0) angulo += 180.0;
        this.angInclinado = angulo;
        
        double Xm = (Math.max(pf.x, pi.x) - Math.min(pf.x, pi.x));
        double Ym = (Math.max(pf.y, pi.y) - Math.min(pf.y, pi.y));
        this.longitud = Math.sqrt(Math.pow(Xm, 2) + Math.pow(Ym, 2));
    }
    
    private static double pendienteEntre(Vertice A, Vertice B)
    {
        Point pf = A.getCords();
        Point pi = B.getCords();
        
        double Xn = (pf.x - pi.x);

        return (pf.y - pi.y) / Xn;
    }
    

    public String getTag() { return tag;}
    
    public Vertice getvA() { return vA;}
    public Vertice getvB() {return vB;}
    
    public boolean enlazadosEnArista(Vertice v1, Vertice v2)
    {
        return contieneEnPunta(v1) && contieneEnPunta(v2);
    }
    
    public boolean contieneEnPunta(Vertice v){
        return v.equals(this.vA) || v.equals(this.vB);
    }
    
    public boolean esColinear(Arista conQuien){
        return this.pendiente == conQuien.pendiente || this.entreRangoDeError(conQuien.angInclinado);
    }
    
    private boolean entreRangoDeError(double angulo) {
        angulo = Math.abs(angulo);
        double esteAngulo = Math.abs(this.angInclinado);
        
        double rangoError = 15.0;
        
        double lados[] = {(esteAngulo + rangoError), (esteAngulo - rangoError)};
        boolean revisaVuelta[] = {false, false};
        boolean vuelta = false;
        
            if(lados[0] >= 180.0){ 
                revisaVuelta[0] = true;
                lados[0] -= 180.0; // Pa 0 + lo que sobre.
                vuelta = true;
            }
            else if (lados[1] <= 0){
                lados[1] += 180.0; // Pa 180 - lo que sobre.
                revisaVuelta[1] = true;
                vuelta = true;
            }
        
        
        if(vuelta){
            if(revisaVuelta[0]){
                double[] toTest1 = {0, lados[0]};
                double[] toTest2 = {180, lados[1]};
                boolean test = entreDoubles(angulo, toTest1) || entreDoubles(angulo, toTest2);
                return test;
            }
            if(revisaVuelta[1]){
                double[] toTest1 = {0, lados[0]};
                double[] toTest2 = {180, lados[1]};
                boolean test = entreDoubles(angulo, toTest1) || entreDoubles(angulo, toTest2);
                return test;
            }
        }
        double[] toTest = {lados[0], lados[1]};
        boolean test = entreDoubles(angulo, toTest);
        return test;
        
    }
    
    private boolean entreDoubles(double p, double[] a)
    {
        boolean test = false;
        
        DecimalFormat df = new DecimalFormat("#.##");
        
        if(a[0] < a[1])       test = (p >= a[0] && p <= a[1]);
        else if (a[1] < a[0]) test = (p >= a[1] && p <= a[0]);
        else test = true;
        /*System.out.println(
            "p = " + df.format(p) + 
            " a[0]= "+ df.format(a[0]) + 
            " a[1]= " + df.format(a[1]) + 
            " ("+ test +")");*/
        
        return test;
    }
    
    public boolean intersecta(Arista conQuien)
    {
        return this.hit.intersectsLine(conQuien.getHit());
    }
    
    public Vertice ladoExtremoDe(Vertice v){
        if(v.equals(this.vA)) return this.vB;
        else if(v.equals(this.vB)) return this.vA;
        else return null; // En caso de que no pertenezca a ninguno de los dos extremos.
    }
    
    
    public Point puntoDondeIntersecta(Arista conQuien)
    {
        Point interseccion = new Point(0,0);
        if(! intersecta(conQuien) || this.esColinear(conQuien)) return null;
        if(contieneEnPunta(conQuien.vA) || contieneEnPunta(conQuien.vB)) return null;
        
        double a1 = this.vB.getHit().y - this.vA.getHit().y; 
        double b1 = this.vA.getHit().x - this.vB.getHit().x; 
        double c1 = a1*(this.vA.getHit().x) + b1*(this.vA.getHit().y); 
       
        // Line CD represented as a2x + b2y = c2 
        double a2 = conQuien.vB.getHit().y - conQuien.vA.getHit().y; 
        double b2 = conQuien.vA.getHit().x - conQuien.vB.getHit().x; 
        double c2 = a2*(conQuien.vA.getHit().x)+ b2*(conQuien.vA.getHit().y); 
       
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
    
    public Arista mayorPendiente(Arista a)
    {
        if(a.pendiente > this.pendiente) return a;
        else return this;
    }

    public Line2D getHit() {return hit;}
    
    public void mueve(double dx, double dy)
    { 
        double x = dx + LINE_OFFSET;
        double y = dy + LINE_OFFSET;
        Point pf = this.vB.getCords();
        Point pi = this.vA.getCords();
        this.hit.setLine(pi.x + x, pi.y + y, pf.x + x, pf.y + y);
        this.vA.setCords(pi.x + dx, pi.y + dy);
        this.vB.setCords(pf.x + dx, pf.y + dy);
    }

    private void reconstruye()
    { 
        Point pf = this.vB.getCords();
        Point pi = this.vA.getCords();
        this.hit.setLine(pi.x + LINE_OFFSET, pi.y + LINE_OFFSET,
                         pf.x + LINE_OFFSET, pf.y + LINE_OFFSET);
    }
    
    
    public void setvA(Vertice vA) {
        this.vA = vA;
        actualizaPendiente();
        reconstruye();
    }
    public void setvB(Vertice vB) {
        this.vB = vB;
        actualizaPendiente();
        reconstruye();
    }

    public double getLongitud() {
        return this.longitud;
    }

    public double getPendiente() {
        return this.pendiente;
    }
    
    
    

    @Override
    public String toString() {
        String msg = "";
        
        Double pend = new Double(this.pendiente);

        DecimalFormat df = new DecimalFormat("#.##");
        
        if(pend.isInfinite()) msg = "No existe.";
        else msg = df.format(pend);
        return "(" + this.tag + ") =" 
                + this.vA.toString() + " - " 
                + this.vB.toString() + 
                " Pend.: " + msg + 
                " a " + df.format(this.angInclinado) + "º" + " Long:" + df.format(this.longitud)+ " Unid.";
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
        if (!Objects.equals(this.tag, other.tag)) {
            return false;
        }
        return true;
    }
    
    
    
}
