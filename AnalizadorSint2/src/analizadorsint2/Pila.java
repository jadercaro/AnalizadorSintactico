/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorsint2;

public class Pila {
    final int MAX = 5000;
    SimbGram[] _elems;
    int _tope;
    
    //crear una pila nueva con el numero maximo de caracteres
    public Pila(){
        _elems = new SimbGram[MAX];
        for (int i=0; i<_elems.length;i++)
            _elems[i] = new SimbGram("");
        _tope=0;
    }
    //comprobar si la pila esta vacia
    public boolean Empty(){
        return _tope==0;
    }
    //comprobar si la pila esta completamente llena
    public boolean Full(){
        return _tope==_elems.length;
    }
    //almacenar un nuevo elemento a la pila
    public void Push(SimbGram oElem){
        _elems[_tope++]=oElem;
    }
    //tomar el tamaño de la pila
    public int Length(){
        return _tope;
    }
    //sacar un elemento de la pila, mas especificamente el tope
    public SimbGram Pop(){
        return _elems[--_tope];
    }
    //Inicializamos la pila en tope=0
    public void Inicia(){
        _tope=0;
    }
    
    public SimbGram Tope(){
        return _elems[_tope-1];
    }
    
}
