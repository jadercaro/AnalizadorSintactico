package analizadorsint2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Automata {
   Pattern[] afd = new Pattern[9];
   
//creamos los simbolos aceptados por cada AFD
   public Automata(){
       afd[0]= Pattern.compile("\\s+");
       afd[1]= Pattern.compile("([A-Za-z]|_)([A-Za-z]|_|[0-9])*");
       afd[2]= Pattern.compile("[\\+\\-\\*/]=|=");
       afd[3]= Pattern.compile("[\\+\\-\\*/]");
       afd[8]= Pattern.compile("[0-9]+");
       afd[4]= Pattern.compile("[0-9]+\\.[0-9]+");
       afd[7]= Pattern.compile("[0-9]+\\.");
       afd[5]= Pattern.compile("\\(|\\)");
       afd[6]= Pattern.compile("\\;");
       
   }
   
   //reconoce el tipo de automata en el cual nos encontramos ubicados y el tipo de automata que es 
   public boolean Reconoce(String texto, int iniToken,int[] i,int noAuto){
       Matcher m = afd[noAuto].matcher(texto);
       if(m.find(iniToken)){
           if(m.start()==iniToken){
               i[0]=m.end();
               return true;
           }
           else{
               return false;
           }
       }
       else{
           return false;
       }
   }
}
