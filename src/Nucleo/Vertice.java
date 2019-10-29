/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Nucleo;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JPanel;

/**
 *
 * @author Samuzero15
 */
public class Vertice implements Comparable{
    private static int CUENTA;
    private String tag;
    private Ellipse2D.Double hit;

    public Vertice() {
        
        String taggu;
        char letra1;
        char letra2;
        int divLetra2 = 0;
        int modLetra1 = 0;
        divLetra2 = (CUENTA / 26);
        modLetra1 = CUENTA % 26;
        
        letra1 = (char) (65 + modLetra1);
        letra2 = (char) (65 + divLetra2-1);
        
        if(divLetra2 > 0)
        taggu = new String(new char[] {letra2, letra1});
        else taggu = new String(new char[] {letra1});

        this.tag = taggu;
        CUENTA++;
    }
    
    public String getTag() {
        return tag;
    }
    
    public static int getCuenta(){return CUENTA;};

    public Point getCords() {
        Point p = new Point((int)hit.x, (int)hit.y);
        return p;
    }
    public void setCords(double x, double y) {
        this.hit.x = x;
        this.hit.y = y;
    }
    

    @Override
    public String toString() {
        return "(" + tag + ")";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
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
        final Vertice other = (Vertice) obj;
        if (!Objects.equals(this.tag, other.tag)) {
            return false;
        }
        return true;
    }

    public Ellipse2D.Double getHit() {
        return hit;
    }

    public void setHit(Ellipse2D.Double hit) {
        this.hit = hit;
    }

    @Override
    public int compareTo(Object o) {
        Vertice v = (Vertice) o;
        return this.tag.compareTo(v.tag);
    }


}
