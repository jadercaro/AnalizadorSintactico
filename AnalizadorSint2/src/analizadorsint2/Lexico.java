
package analizadorsint2;

public class Lexico {
    final int TOKREC = 9;
    final int MAXTOKENS = 500;
    String[] _lexemas;
    String[] _tokens;
    String _lexema;
    int _noTokens;
    int[] _i = {0};
    int _iniToken;
    Automata oAFD; 
    
    public int NoTokens(){
        return _noTokens;
    }
    
    public String[] Tokens(){
        return _tokens;
    }
    
    public String[] Lexemas() {
        return _lexemas;
    }
    public void Anade(String tok, String lex){
        _tokens[_noTokens]= tok;
        _lexemas[_noTokens++]=lex;
    }
    
    public Lexico(){
        _lexemas = new String[MAXTOKENS];
        _tokens = new String[MAXTOKENS];
        oAFD = new Automata(); 
        _i[0] = 0;
        _iniToken = 0;
        _noTokens = 0;
        
    }
    
    private boolean EsId(){
        String[] palRes = {"if","else","while","public","break","int","final","switch","double","for","int","String","case","boolean","byte"};
        for(int i=0;i<palRes.length;i++)
            if(_lexema.equals(palRes[i]))
                return false;
        return true;
        
    }
    public void Inicia(){
        _i[0]=0;
        _iniToken = 0;
        _noTokens = 0;
    }
    public boolean Analiza(String texto){
        boolean recAuto;
        int noAuto;
        while(_i[0] < texto.length()){
            recAuto = false;
            noAuto=0;
            for(;noAuto<TOKREC&&!recAuto;)
                 if(oAFD.Reconoce(texto,_iniToken,_i,noAuto)){
                    recAuto=true;}
                 else{
                    noAuto++;}
            if(recAuto){
                _lexema = texto.substring(_iniToken,_i[0]);
                switch(noAuto){
                    case 0:
                       // _tokens[_noTokens] ="delim";
                        break;
                    case 1: if (EsId()){
                                _tokens[_noTokens]="id";}
                    else{
                            _tokens[_noTokens]=_lexema;}
                            break;
                    case 2:_tokens[_noTokens] = _lexema;
                        break;
                    case 3: _tokens[_noTokens] =_lexema;
                        break;
                    case 4: _tokens[_noTokens]="num";
                        break;
                    case 7: _tokens[_noTokens]="num";
                        break;
                    case 8: _tokens[_noTokens]="num";
                        break;
                    case 5: _tokens[_noTokens]=_lexema;
                        break;
                    case 6: _tokens[_noTokens]=_lexema;
                        break;
                    }
                if(noAuto>0){
                _lexemas[_noTokens++]=_lexema;
                }
            }
            else
                return false;
            _iniToken=_i[0];
        }
        return true; //Analisis lexico exitoso
    }
}
