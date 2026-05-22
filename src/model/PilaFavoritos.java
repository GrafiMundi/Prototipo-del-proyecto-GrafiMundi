package model;

public class PilaFavoritos {

    public static int totalNodos;
    public static nodo tope;
    public static int pos;

    public PilaFavoritos() {
        totalNodos = 0;
        pos=0;
        tope = null;
    }

    public static void agregar(nodoGraficas n) {
            nodo nuevo = new nodo();
            nuevo.nG=n;
            nuevo.sig = tope;
            tope = nuevo;
            totalNodos++; 
    }

    public static void eliminar() {
            tope = tope.sig;
            totalNodos--;    
    }

    public static void limpiarPila() {  // Vaciar lista de favoritos
        while (totalNodos !=0) {
            eliminar();
        }
    }
    
    public static void quitar(String c){  // Quita una gráfica seleccionada de la lista de favoritos
     
        if(tope.nG.codigo.equals(c)){
            tope=tope.sig;
            totalNodos--;  
            return;
        }
       
         nodo p=tope;
        while(p.sig!=null ){
           if(p.sig.nG.codigo.equals(c)) {
               p.sig=p.sig.sig; totalNodos--;   return;
           }
            p=p.sig;
        }
    }
    
    public static nodoGraficas mostrar(){
        nodo p=tope;
        int i=1;
        while(i<pos){ p=p.sig; i++;}
        return p.nG;
    }
    
    public static boolean repetido(String cod){
        nodo p=tope;
        while(p!=null){
            if(cod.equals(p.nG.codigo)) return true;
            p=p.sig;
        }
        return false;
    }

}

