/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Nucleo;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

/**
 *
 * @author Samuzero15
 */
public class Grafo {
    HashMap<String,Vertice> verticesDelGrafo;
    HashMap<String,Arista> aristasDelGrafo;
    
    private final double MIN_GRADOS_TRIANGULO = 3.5; 

    public Grafo() {
        this.verticesDelGrafo = new HashMap<>();
        this.aristasDelGrafo = new HashMap<>();
    }
    
    public Vertice creaVertice(int x, int y){
        Vertice v = new Vertice();
        v.setHit(new Ellipse2D.Double(x-10, y-10, 20, 20));
        return v;
    }
    
    public void nuevoVertice(int x, int y){
        Vertice v = new Vertice();
        v.setHit(new Ellipse2D.Double(x-10, y-10, 20, 20));
        verticesDelGrafo.put(v.getTag(), v);
    }
    public void añadeVertice(Vertice v){
        verticesDelGrafo.put(v.getTag(), v);
    }
    
    public Vertice getVertice(String tal){
        return verticesDelGrafo.get(tal);
    }
    
    public Arista getArista(String tal){
        return aristasDelGrafo.get(tal);
    }
    
    public Arista getArista(Vertice v1, Vertice v2){
        Arista a = null;
        if(v1.equals(v2)) return null;
        for(Map.Entry par : aristasDelGrafo.entrySet())
        {
            Arista ari = (Arista)par.getValue();
            if(ari.enlazadosEnArista(v1, v2)){ 
                a = ari;
                break;
            }
        }
        return a;
    }
    
    public void eliminaVertice(String tal){
        Vertice v = getVertice(tal);
        ArrayList<Arista> aBorrar = new ArrayList<>();
        
       Iterator it = aristasDelGrafo.entrySet().iterator(); 
        
        while(it.hasNext()){
            Map.Entry par = (Map.Entry)it.next();
            Arista a = (Arista)par.getValue();
            if(a.contieneEnPunta(v))
                aBorrar.add(a);
        }
        for(Arista r : aBorrar)
        {
            aristasDelGrafo.remove(r.getTag());
        }
        
        verticesDelGrafo.remove(v.getTag());
    }
    
    public void añadeArista(String v1, String v2)
    {
        Vertice n1 = verticesDelGrafo.get(v1), n2 = verticesDelGrafo.get(v2);
        Arista ari = new Arista(n1,n2);
        aristasDelGrafo.put(ari.getTag(), ari);
        laAristaIntersecta(ari);
    }
    
    public void laAristaIntersecta(Arista ari)
    {
        Vertice vi = null;
        ArrayList<String> ariveddercci = new ArrayList<>();
        String[][] adicionales = new String[4][2];
        boolean desicion = false;
        for(Map.Entry par : aristasDelGrafo.entrySet()){
            Arista aVer = (Arista) par.getValue();
            if(ari.intersecta(aVer)){
              Point pi = ari.puntoDondeIntersecta(aVer);
                if(pi != null){
                  vi = creaVertice(pi.x, pi.y);
                  añadeVertice(vi);
                  // Coloco los pares que van a crear nuevas aristas aqui.
                  adicionales[0][0] = vi.getTag();
                  adicionales[1][0] = adicionales[0][0];
                  adicionales[2][0] = adicionales[0][0];
                  adicionales[3][0] = adicionales[0][0];
                  
                  adicionales[0][1] = aVer.getvA().getTag();
                  adicionales[1][1] = aVer.getvB().getTag();
                  adicionales[2][1] = ari.getvA().getTag();
                  adicionales[3][1] = ari.getvB().getTag();
                  
                  // Eliminare las viejas aristas que habian antes de la deteccion de intersecciones
                  ariveddercci.add(aVer.getTag());
                  ariveddercci.add(ari.getTag());
                  desicion = true;
                  break;
                }
            } 
        }
        if(desicion){
            for(String s : ariveddercci) eliminaArista(s);
            
            Arista[] nuevas = new Arista[4];
            int bruno = 0;
            
            for(int a = 0; a < adicionales.length; a++){
                System.out.println(adicionales[a][0] + " " + adicionales[a][1]);
                
                Vertice n1 = verticesDelGrafo.get(adicionales[a][0]), n2 = verticesDelGrafo.get(adicionales[a][1]);
                Arista ara = new Arista(n1,n2);
                nuevas[bruno] = ara;
                aristasDelGrafo.put(ara.getTag(), ara);
                bruno-=-1; // sum mem
            }
            
            // Debo revisar si alguna de las aristas que se han creado hay intersecciones...
            for(Arista aris : nuevas){
                laAristaIntersecta(aris);
            }
        }
    }
    
    public void eliminaArista(String nomb)
    {
        Arista ari = aristasDelGrafo.get(nomb);
        aristasDelGrafo.remove(nomb, ari);
    }
    
    public int[][] matrizDeAdjacencia()
    {
        int[][] matAdj = new int[verticesDelGrafo.size()][verticesDelGrafo.size()];
        
        int i = 0;
        Iterator it1 = verticesDelGrafo.entrySet().iterator(); 
        
        HashMap<String, Integer> indux = new HashMap();
        while(it1.hasNext()){ // Indexa cada vertice.
            Map.Entry vert = (Map.Entry)it1.next();
            Vertice v = (Vertice)vert.getValue();
            indux.put(v.getTag(), i);
            i++;
        }

        for (Map.Entry par : verticesDelGrafo.entrySet()) {
            // Indexa cada vertice.
            Vertice t = (Vertice)par.getValue();
            
            for(Map.Entry ariPar : aristasDelGrafo.entrySet())
            {
                Arista a = (Arista) ariPar.getValue();
                
                if(a.contieneEnPunta(t)){
                    Vertice v = null;
                    v = a.ladoExtremoDe(t);
                    int inx = indux.get(t.getTag());
                    int iny = indux.get(v.getTag());
                    matAdj[inx][iny] = 1;
                }
            }           
        }
        
        return matAdj;
    }
    
    public ArrayList<SortedSet<Vertice>> cuantosTriangulosHay()
    { // no exactamente.
        int res = 0;
        int turno = 0;
        Vertice tmp;
        Arista atmp;
        ArrayList<SortedSet<Vertice>> misTriangulos = new ArrayList<>();
        for(Map.Entry verts : verticesDelGrafo.entrySet())
        {
            Vertice v = (Vertice)verts.getValue();
            System.out.println("Ver R: "+ v);
            ArrayList<Vertice> vAdj = buscaVerticesAdjacentes(v);
            for(Vertice e : vAdj){
                for(Vertice r : vAdj){
                 if(getArista(e,r) != null){
                     SortedSet<Vertice> triangulo = new TreeSet<>();
                     triangulo.add(v); // Raiz de la busqueda.
                     triangulo.add(e); // El adjacente de la raiz.
                     triangulo.add(r); // El otro adjacente de la raiz unido con el anterior.
                     if(!misTriangulos.contains(triangulo) &&
                         compruebaTriangulo(v, e, r)){
                         misTriangulos.add(triangulo);
                     }
                 }
                }
                atmp = getArista(v,e);
                
                for(Map.Entry aris : aristasDelGrafo.entrySet()){
                    Arista ari = (Arista) aris.getValue();
                    if(atmp.esColinear(ari) && !atmp.equals(ari)){
                       // System.out.println(ari.toString());
                       Vertice tmp2 = ari.ladoExtremoDe(e);
                       if(tmp2 != null)
                       {
                           System.out.println("Ari 1: "+ atmp);
                           System.out.println("Ari 2: "+ ari);
                           System.out.println("Lado Ex: "+tmp2);
                       }
                    }
                }
                
            }
            
            
            
            //System.out.println(explorado.toString());
        }
        
        /*
        for(Set<Vertice> setV : misTriangulos) System.out.println(setV.toString());
        System.out.println("Okay");
        */

        
        return misTriangulos;
    }
    
    private boolean compruebaTriangulo(Vertice a, Vertice b, Vertice c)
    {
        boolean[] angulosValidos = { false, false, false};
        angulosValidos[0] = anguloEntre2Aristas(getArista(a,b),getArista(a,c)) >= MIN_GRADOS_TRIANGULO;
        angulosValidos[1] = anguloEntre2Aristas(getArista(b,a),getArista(b,c)) >= MIN_GRADOS_TRIANGULO;
        angulosValidos[2] = anguloEntre2Aristas(getArista(c,b),getArista(c,a)) >= MIN_GRADOS_TRIANGULO;
        
        for (int i = 0; i < angulosValidos.length; i++) {
            System.out.println(angulosValidos[i]);
        }
        
        return angulosValidos[0] && angulosValidos[1] && angulosValidos[2];
    }
    
    private double anguloEntre2Aristas(Arista a, Arista b){
        double res = 0.0;
        
        if(a.mayorPendiente(b) == b)
        {
            Arista tmp;
            tmp = a;
            a = b;
            b = tmp;
        } 
        
        res = Math.atan((a.getPendiente() - b.getPendiente()) / (1+(a.getPendiente() * b.getPendiente())));
        res = Math.floor(Math.toDegrees(res));
        if(res < 0) res += 180.0;
        System.out.println("El area de este triangulo es: " + res);
        return res;
    }
    
    public ArrayList<Vertice> buscaVerticesAdjacentes(Vertice v){
        ArrayList<Vertice> adj = new ArrayList<>();
        
        for(Map.Entry ariPar : getAristasDelGrafo().entrySet())
            {
                Arista a = (Arista) ariPar.getValue();
                
                if(a.contieneEnPunta(v)){
                    Vertice e = null;
                    e = a.ladoExtremoDe(v);
                    adj.add(e);
                }
            }  
        return adj;
    }
    
    
    public void actualizarArista(String a, Arista ari)
    {
        this.aristasDelGrafo.replace(a, ari);
    }
    public void actualizarVertice(String v, Vertice ver)
    {
        for (Map.Entry vert : aristasDelGrafo.entrySet()) {
            // Indexa cada vertice.
            Arista a = (Arista)vert.getValue();
            if(a.contieneEnPunta(ver))
            {
                if(a.getvB().equals(ver)){
                    a.setvB(ver);
                }else if(a.getvA().equals(ver)){
                    a.setvA(ver);
                }
            }
        }
        this.verticesDelGrafo.replace(v, ver);
    }
    
    public HashMap<String, Vertice> getVerticesDelGrafo() {return verticesDelGrafo;}
    public HashMap<String, Arista> getAristasDelGrafo() {return aristasDelGrafo;}
}
