/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Nucleo.Arista;
import Nucleo.Grafo;
import Nucleo.Vertice;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author Samuzero15
 */
public class Lienzo extends JPanel{
    public Grafo figura = new Grafo();
    private ArrayList<Vertice> vertSel;
    private ArrayList<Arista> arisSel;
    private boolean verticeVisible;


    public Lienzo() {
        super();
        vertSel = new ArrayList<>();
        arisSel = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        

        Graphics2D g2 = (Graphics2D) g;
        
        prueba(g2);
        
        dibujaAristasSeleccionados(g2);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(1.0f));
        
        Iterator itera1 = figura.getAristasDelGrafo().entrySet().iterator(); 
        
        while(itera1.hasNext()){
            Map.Entry par = (Map.Entry)itera1.next();
            
            dibujaEnlaces((Arista) par.getValue(), g2);
        }
        
        
        Iterator itera2 = figura.getVerticesDelGrafo().entrySet().iterator(); 
        
        while(itera2.hasNext()){
            Map.Entry vert = (Map.Entry)itera2.next();

            if(verticeVisible)
            {
             dibujaPunto((Vertice) vert.getValue(), g2);
             dibujaLetras((Vertice) vert.getValue(), g2);
            } else 
            dibujaPunto2((Vertice) vert.getValue(), g2);
        }
        
        dibujaVerticesSeleccionados(g2);

         g2.setStroke(new BasicStroke(1.0f));
    }
    
    public void seleccionarA(Vertice v){
        vertSel.add(v);
    }
    
    public void seleccionarA(Arista v){
        arisSel.add(v);
    }
    public void desSeleccionarVertices(){vertSel.clear();}
    
    public void desSeleccionarAristas(){arisSel.clear(); }
    
    public void desSeleccionarTodo(){
        vertSel.clear();
        arisSel.clear();
    }
    
    private void dibujaPunto(Vertice v, Graphics2D g2)
    {
        g2.setStroke(new BasicStroke(1.0f));
        g2.setColor(Color.blue);
        g2.fill(v.getHit());
    }
    private void dibujaPunto2(Vertice v, Graphics2D g2)
    {
        Ellipse2D.Double e = new Ellipse2D.Double(v.getHit().x+7.5, v.getHit().y+7.5 , 5, 5);
        g2.setStroke(new BasicStroke(1.0f));
        g2.fill(e);
    }
    
    private void dibujaLetras(Vertice v, Graphics2D g2)
    {
        
        g2.setColor(Color.WHITE);
        g2.drawChars(v.getTag().toCharArray(), 0, v.getTag().toCharArray().length,
                    (int)v.getHit().getX() + 5,(int)v.getHit().getY() + 15);
    }
    
    private void dibujaEnlaces(Arista a, Graphics2D g2)
    {
              g2.draw(a.getHit());
    }
    
    private void dibujaVerticesSeleccionados(Graphics2D g2){
        if(!vertSel.isEmpty())
            for(Vertice e : this.vertSel)
            {
                g2.setStroke(new BasicStroke(3.0f));
                g2.setColor(Color.white);
                g2.draw(e.getHit());
            }
    }
    
    private void dibujaAristasSeleccionados(Graphics2D g2)
    {
        if(!arisSel.isEmpty())
            for(Arista r : this.arisSel)
            {
                g2.setStroke(new BasicStroke(4.0f));
                g2.setColor(Color.white);
                g2.draw(r.getHit());
            }
    }

    Vertice getVerticeEnElLienzo(Point p) {
        Iterator itera = figura.getVerticesDelGrafo().entrySet().iterator(); 
        Vertice v = null;
        
        while(itera.hasNext())
        {
            Map.Entry vert = (Map.Entry)itera.next();
            v = (Vertice) vert.getValue();
            
            if(v.getHit().contains(p)) return v;
        }
        return null;
    }
    
    private final int CUADRIN_CLICK = 10;
    
    Arista getAristaEnElLienzo(Point p) {
        Iterator itera = figura.getAristasDelGrafo().entrySet().iterator(); 
        Arista a = null;
        
        int x = p.x - CUADRIN_CLICK / 2;
        int y = p.y - CUADRIN_CLICK / 2;
        
        int put = CUADRIN_CLICK;
        
        while(itera.hasNext())
        {
            Map.Entry par = (Map.Entry)itera.next();
            a = (Arista) par.getValue();
            
            if(a.getHit().intersects(x,y,put,put)) return a;
        }
        return null;
    }

    public void setVerticesVisibles(boolean selected) {
        verticeVisible = selected;
    }

    private void prueba(Graphics2D g2)
    {
       //Line2D line = new Line2D.Double(200, 200, 100, 100);
       
       //g2.draw(line);
    }
}
