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
import java.util.Map;

/**
 *
 * @author Samuzero15
 */
public class Grafo {
    HashMap<String,Vertice> verticesDelGrafo;
    HashMap<String,Arista> aristasDelGrafo;

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
    
    public void eliminaVertice(String tal){
        Vertice v = getVertice(tal);
        ArrayList<Arista> aBorrar = new ArrayList<>();
        
       Iterator it = aristasDelGrafo.entrySet().iterator(); 
        
        while(it.hasNext()){
            Map.Entry par = (Map.Entry)it.next();
            Arista a = (Arista)par.getValue();
            if(a.contiene(v))
                aBorrar.add(a);
        }
        for(Arista r : aBorrar)
        {
            aristasDelGrafo.remove(r.getNum());
        }
        
        verticesDelGrafo.remove(v.getTag());
    }
    
    public void añadeArista(String v1, String v2, String num)
    {
        Vertice n1 = verticesDelGrafo.get(v1), n2 = verticesDelGrafo.get(v2);
        Arista ari = new Arista(n1,n2, num);
        System.out.println(laAristaIntersecta(ari));
        aristasDelGrafo.put(num, ari);
    }
    
    public boolean laAristaIntersecta(Arista ari)
    {
        Vertice vi = null;
        for(Map.Entry par : aristasDelGrafo.entrySet()){
            Arista aVer = (Arista) par.getValue();
            if(ari.intersecta(aVer)){
                if(vi == null){
                  Point pi = ari.puntoDondeIntersecta(aVer);
                  vi = creaVertice(pi.x, pi.y);
                  añadeVertice(vi);
                  //añadeArista();
                  
                }

                
            return true;
            } 
        }
        return false;
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
                
                if(a.contiene(t)){
                    Vertice v = null;
                    if(a.getvFinal().equals(t)){
                        v = a.getvInicial();
                    }else if(a.getvInicial().equals(t)){
                        v = a.getvFinal();
                    }
                    int inx = indux.get(t.getTag());
                    int iny = indux.get(v.getTag());
                    matAdj[inx][iny] = 1;
                }
            }           
        }
        
        return matAdj;
    }
    
    public int cuantosTriangulosHay()
    { // no exactamente.
        int res = 0;
        int n = verticesDelGrafo.size();
        int[][] matAdj = matrizDeAdjacencia();
        int[][] aux2= new int[n][n]
              , aux3 = new int[n][n];
        for (int i = 0; i < n; ++i) 
        { 
           for (int j = 0; j < n; ++j) 
           { 
               aux2[i][j] = aux3[i][j] = 0; 
           } 
        } 
        
        aux2 = multiplicaMatriz(matAdj, matAdj,n); // M^2
        aux3 = multiplicaMatriz(matAdj, aux2, n); // M^3
        res = trazaDeMatriz(aux3) / 6;
        
        return res;
    }
    
    private int trazaDeMatriz(int[][] a)
    {
        int res = 0;
        
        for(int i = 0; i < a.length; i++)
        {
                res += a[i][i];
        }
        
        return res;
    }
    
    private int[][] multiplicaMatriz(int[][] a, int[][] b, int n)
    {
        
        int[][] res = new int[n][n];
        
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                res[i][j] = 0;
                for(int k = 0; k < n; k++)
                {
                    res[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return res;
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
            if(a.contiene(ver))
            {
                if(a.getvFinal().equals(ver)){
                    a.setvFinal(ver);
                }else if(a.getvInicial().equals(ver)){
                    a.setvInicial(ver);
                }
            }
        }
        this.verticesDelGrafo.replace(v, ver);
    }
    
    public HashMap<String, Vertice> getVerticesDelGrafo() {return verticesDelGrafo;}
    public HashMap<String, Arista> getAristasDelGrafo() {return aristasDelGrafo;}
}
