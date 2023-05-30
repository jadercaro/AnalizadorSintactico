
package analizadorsint2;

import java.util.ArrayList;

/**
 *
 * @author Jader-Caro
 */
public class SintAscSLR {
    public static final int NOPROD = 10; //numero de producciones
    public final int NODDS = 1000;       //numero de DDS
    public final int NOACTIONS = 1000;   //numero de acciones disponibles 
    public final int NOGOTOS = 1000;     //numero de movimientos gotos que podemos hacer
    public String[] _vts = {"","id","=",";","+","-","*","/","num","(",")","$"}; //(negativos)
    public String[] _vns = {"","A","E","T","F"};
    public int [][] _prod ={{1,4,-1,-2,2,-3},  //A --> id = E  (operación de asignación)
                     {2,3,2,-4,3,0},    //E --> E + T        (operacion de suma)
                     {2,3,2,-5,3,0},    //E --> E - T        (operación de resta)
                     {2,1,3,0,0,0},     //E --> T           (cambio de estado)
                     {3,3,3,-6,4,0},    //T --> T * F       (multiplicacion)
                     {3,3,3,-7,4,0},    //T --> T / T       (división)
                     {3,1,4,0,0,0},     //T --> F           (cambio de estado)
                     {4,1,-1,0,0,0},    //F --> id          (terminal)
                     {4,1,-8,0,0,0},    //F --> num         (terminal)  
                     {4,3,-9,2,-10,0}}; //F --> ( E )       (operacion con parentesis)
    
    public int[][] _sig={{0,0,0,0,0,0,0,0,0,0,0},
                  {1,11,0,0,0,0,0,0,0,0,0},         //SIG(A){  $  }
                  {4,3,4,5,10,0,0,0,0,0,0},         //SIG(E){  ; + - ) }
                  {6,6,7,3,4,5,10,0,0,0,0},         //SIG(T){ * / ; + - ) }
                  {6,6,7,3,4,5,10,0,0,0,0}};        //SIG(T){ * / ; + - ) }
    
    public Pila _pila;   //pila para almacenar los siguientes estados a donde iremos
    public int[][] _action;  //tupla de acciones que se conforma por accion y estado
    int _noActions;         
    int _noGoTos;           
    public int[][] _goTo;  //matriz de gotos
    int[] _dd;
    int _noDds;
    public Item[] _c;
    int _noItems;
    public ArrayList lista = new ArrayList();
    public ArrayList lista2 = new ArrayList();
    
    
    
    
    //metodos
    //inicializamos los valores desde cero
    public SintAscSLR(){
        _pila = new Pila();
        _dd = new int [NODDS];
        _noDds = 0;
        _action = new int[1000*(_vts.length-1)][4];
        _goTo = new int[1000*(_vns.length-1)][3];
        _noActions = 0;
        _noGoTos =0;
        _noItems=0;
         ArrayList lista = new ArrayList();
    }
    public void Inicia(){
        _pila.Inicia();
        // Inicializa contadores de variables "_noDds", "_noActions" y "_noGoTos" a cero
        _noDds = 0;
        _noActions = 0;
        _noGoTos = 0;
        _c = new Item[1000];
        _noItems = 0;
        
        //
        for(int i=0; i<_c.length;i++)
            _c[i] = new Item();
        
        int[][] arre ={{-1,0}};
        _c[_noItems++] = Cerradura(new Item(arre,1));
        
        int[][] arreItem1 = {{-1,1}};
        _c[_noItems++] = new Item(arreItem1,1);
        
        for(int i=0; i<_noItems; i++)
            if(i!=1)
                AgregarConjItems(i);
        
        _goTo[_noGoTos][0] = 0;
        _goTo[_noGoTos][1] = 1;
        _goTo[_noGoTos++][2] = 1;
        
        for(int i=0; i<1000;i++){
            GeneraCambios(i);
            GeneraReducciones(i);
        }
    }
    //para comprobar hasta que limite llega dicha declaracion 
    public Item Cerradura(Item oItem){
        boolean cambios = true;
        while(cambios){
            for(int i=0; i<oItem.NoItems();i++){
                int noItemsAgregado = AgregarItems(i,oItem);       
                if(noItemsAgregado>0){
                    cambios = true;
                    break;
                }
                else
                    cambios = false; 
            }
        }
        return oItem;
    }
    //son los item o caracteres ya trasnformados que seran evaluados 
    public void AgregarConjItems(int i){
        boolean[] marcaItems = new boolean[NOPROD + 1];
        for(int j=0; j<NOPROD+1;j++)
            marcaItems[j]=false;
        marcaItems[0]=i ==0;
        for(int j=0; j<_c[i].NoItems();j++)
            if(!marcaItems[j]){
                int noProd = _c[i].NoProd(j);
                int posPto = _c[i].PosPto(j);
                System.out.println("hola mundo desde "+ _c[i].PosPto(j));
                if(posPto != _prod[noProd][1]){
                    
                    Item oNuevoItem = new Item();
                    int indSimGoTo = _prod[noProd][posPto+2];
                    for(int k=0;k< _c[i].NoItems();k++)
                        if(!marcaItems[k]){
                            int nP = _c[i].NoProd(k);
                            int pP = _c[i].PosPto(k);
                            try{
                                if(indSimGoTo==_prod[nP][pP+2]){
                                    oNuevoItem.Agregar(nP,pP+1);
                                    marcaItems[k]=true;
                                }
                            }
                            catch(Exception e){
                                continue;
                            }
                        }
                    
                    
                    int[] edoYaExiste ={-1};
                    _goTo[_noGoTos][0] = i;
                    _goTo[_noGoTos][1] = indSimGoTo;
                    oNuevoItem = Cerradura(oNuevoItem);
                    if(!EstaNuevoItem(oNuevoItem,edoYaExiste)){
                        _goTo[_noGoTos++][2] = _noItems;
                        _c[_noItems++] = oNuevoItem;
                    }
                    else
                        _goTo[_noGoTos++][2]=edoYaExiste[0];
                }
            }
    }
    //agregamos un nuevo item a la pila, tomando el numero de produccion
    public int AgregarItems(int i, Item oItem){
        int noItemsAgregado =0;
        int posPto = oItem.PosPto(i);
        int noProd = oItem.NoProd(i);
        int indVns = noProd == -1? 1:(posPto==_prod[noProd][1] ? 0:(_prod[noProd][posPto + 2])<0? 0:_prod[noProd][posPto+2]);
    
        if(indVns>0)
            for(int j=0;j<NOPROD;j++)
                if(indVns==_prod[j][0] && !oItem.ExisteItem(j,0)){
                    oItem.Agregar(j,0);
                    noItemsAgregado++;
                    
                    
                }
        return noItemsAgregado;
    }
    
    //con esta funcion comprobamos si un item es nuevo o no, esto con e fin de no repetir o almacenar un item que ya existe
    public boolean EstaNuevoItem(Item oNuevoItem, int[] edoYaExiste){
        edoYaExiste[0]=-1;
        for(int i=0;i<_noItems;i++)
            if(_c[i].NoItems()==oNuevoItem.NoItems()){
                int aciertos =0;
                for(int j=0;j<_c[i].NoItems();j++)
                    for(int k=0;k<oNuevoItem.NoItems();k++)
                        if(_c[i].NoProd(j)==oNuevoItem.NoProd(k) && _c[i].PosPto(j)==oNuevoItem.PosPto(k)){//claras dudas
                            aciertos++;
                            break;
                        }
                if(aciertos ==_c[i].NoItems()){
                    edoYaExiste[0]=i;
                    return true;
                }
            }
        return false;
    }
    //genera una reduccion, lo cual implicara desapilar un nuevo estado de la pila
    public void GeneraReducciones(int i){
        for(int j=0; j<_c[i].NoItems();j++){
            int noProd = _c[i].NoProd(j);
            int posPto = _c[i].PosPto(j);
            if (i==1){
                _action[_noActions][0]=i;
                _action[_noActions][1]=_vts.length-1;
                _action[_noActions][2]=2;
                _action[_noActions++][3]=-1;
                lista.add("Estoy en "+i+" Meto: "+_vts[(_vts.length-1)]+" Aceptación "+"--> "+ -1);
                
            }
            else
                if(noProd != -1 && posPto == _prod[noProd][1]){
                    int indVns = _prod[noProd][0];
                    for(int k=1;k<=_sig[indVns][0];k++){
                        _action[_noActions][0] = i;
                        _action[_noActions][1] = _sig[indVns][k];
                        _action[_noActions][2] = 1;
                        _action[_noActions++][3] = noProd;
                        lista.add("Estoy en "+i+" Meto: "+_vts[(_sig[indVns][k])]+" Reducción "+"--> "+noProd);
                    }
                }
        }
    }
    //genera un cambio de estado, segun el item que le estemos ingresando
    public void GeneraCambios(int i){
        for(int j=0;j<_c[i].NoItems();j++){
            int noProd = _c[i].NoProd(j);
            int posPto = _c[i].PosPto(j);
            if (noProd != -1){
                if(posPto != _prod[noProd][1]){
                    int indSim = _prod[noProd][posPto+2];
                    if(indSim < 0){
                        int edoTrans =-1;
                        for(int k=0; k<_noGoTos;k++)
                            if(_goTo[k][0]==i && _goTo[k][1]==indSim){
                                edoTrans = _goTo[k][2];
                                break;
                            }
                        
                        _action[_noActions][0]=i;
                        _action[_noActions][1]=-indSim;
                        _action[_noActions][2] = 0;
                        _action[_noActions++][3]=edoTrans;  
                        lista.add("Estoy en "+i+" Meto: "+_vts[-indSim] +" Cambio "+"--> "+edoTrans);
                    }
                }
            }
        }
        
    }
    
    //verifica el actual estado, comprueba si es necesario un cambio o una reduccion y 
    //luego apila o desapila segun la accion 
    //correspondiente
    public int Analiza(Lexico oAnalex){
        int ae =0;
        oAnalex.Anade("$","$");
        _pila.Push(new SimbGram("0"));
        while (true){
            String s = _pila.Tope().Elem();
            String a = oAnalex.Tokens()[ae];
            String accion = Accion(s,a);
            switch(accion.charAt(0)){
                case 's':_pila.Push(new SimbGram(a));
                    _pila.Push(new SimbGram(accion.substring(1)));
                    ae++;
                    break;
                case 'r':
                    SacarDosBeta(accion);
                    MeterAGoTo(accion);
                    _dd[_noDds++] = Integer.parseInt(accion.substring(1));
                    break;
                case 'a':
                    return 0;
                case'e':
                    return 1;
            }
        }
    }
    //movimiento generado de un estado a otro sea reduccion o traslado o aceptacion junto con el estado hacia donde va
    public String Accion(String s, String a){
        int tipo = -1 , no =-1;
        int edo = Integer.parseInt(s);
        int inda = 0;
        boolean enc = false;
        for(int i =1; i< _vts.length;i++){
            if(_vts[i].equals(a)){
                inda = i;
                break;
            }
        }
        for(int i=0; i<_noActions;i++){
            if(_action[i][0] == edo && _action[i][1]==inda){
                tipo = _action[i][2];
                no=_action[i][3];
                enc = true;
                lista2.add("tipo de cambio--> "+tipo +" de--> "+edo+" hacia-->" + no + "    "+"accion hecha"+inda);
            }
        }
        if(!enc){
            return "error";
        } else{
            switch(tipo){
                case 0:
                    return "s" + Integer.toString(no);
                case 1:
                    return "r" + Integer.toString(no);
                case 2:
                    return "acc";
                default:
                    return "error";
            }
        }
    }
    //elimina un estado y una accion de la pila 
    public void SacarDosBeta(String accion){
        int noProd = Integer.parseInt(accion.substring(1));
        int noVeces = _prod[noProd][1] *2;
        for (int i=1; i<=noVeces;i++){
            _pila.Pop();
        }
    }
    
    //seleciona el siguiente estado que entrara s¡junto con su accion correspondiente
    public void MeterAGoTo(String accion){
        int sPrima = Integer.parseInt(_pila.Tope().Elem()); 
        int noProd = Integer.parseInt(accion.substring(1));
        _pila.Push(new SimbGram(_vns[_prod[noProd][0]]));
        for (int i=0; i<_noGoTos;i++){
            if(sPrima == _goTo[i][0] && _prod[noProd][0]==_goTo[i][1]){
                _pila.Push(new SimbGram(Integer.toString(_goTo[i][2])));
                break;
            }
        }
    }
    
    public ArrayList imprimirListas(){
        return lista;
    }
    public ArrayList imprimirLista2(){
    return lista2;
    }
}


