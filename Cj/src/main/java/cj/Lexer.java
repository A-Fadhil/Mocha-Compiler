package cj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Lexer
{
    static final char __SEPERATORS__[] = {' ', '+', '-', '*', ':', ';', '&', '!', '%', '^', '(', ')', '=', '/', '<', '>', '.', ',', '{', '}', '[', ']', '|', '?', '\n', (char)13};
    //     'and', 'or'

    ArrayList<String> tokens;
    List<Token>       mTokens;

    public Lexer(String code) throws ParseException
    {
        ArrayList<String> tokens = new ArrayList<>();
                          mTokens = new ArrayList<>();

        String string = "";

        boolean quote = false;
        boolean squote = false;
        char lastone = 'a';
        int line = 0;
        int index = 0;

        for(int i = 0; i < code.length(); i++)
        {
            char ati = code.charAt(i);
            if(ati == '\n')
            {
                line ++;
                index = 0;
            }

            if(lastone == '=' & ati == '=')
            {
                tokens.set(tokens.size() - 1, "==");
                continue;
            }

            if(lastone == '-' & ati == '>')
            {
                tokens.set(tokens.size() - 1, "->");
                continue;
            }

            if(lastone == '=' & ati == '>')
            {
                tokens.set(tokens.size() - 1, "=>");
                continue;
            }

            if((lastone + "").matches("\\d") & ati == '.')
            {
//                tokens.remove(tokens.size() - 1);
                string = string + ati;
                continue;
            }

            if(lastone != '\\' & (ati == '"' | ati == '\''))
            {
                if(ati == '"')
                {
                    quote = !quote;
                    if(!quote) string = "\"" + string + "\"";
                    tokens.add(string);
                    Token token = new Token();
                    token.value = string;
                    mTokens.add(token);
                    token.line = line;
                    token.end  = index;
                    string = "";
                } else {
                    squote = !squote;
                    if(!squote) {
                        if(string.replaceAll("\\\\", "").length() > 1) throw new ParseException("parse error: single quotes cannot contain char[]" + " " + errorByLine(line, index - 2), line);
                        else if(string.length() == 0) throw new ParseException("parse error: single quotes are empty" + " " + errorByLine(line, index - 2), line);
                        string = "\'" + string + "\'";
                    }
                    tokens.add(string);
                    Token token = new Token();
                    token.value = string;
                    mTokens.add(token);
                    token.line = line;
                    token.end  = index;
                    string = "";
                }
                continue;
            }

            if(quote | squote) string += ati;
            else {
                boolean found = false;
                for(char chr : __SEPERATORS__) if(ati == chr)
                {
                    tokens.add(string);
                    Token token = new Token();
                    token.value = tokens.get(tokens.size() - 1);
                    mTokens.add(token);
                    token.end = index;
                    token.line = line;
                    string = "";
                    found = true;
                    if(ati != '\n' && ati != ' ')
                    {
                        Token token2 = new Token();
                        token2.value = ati + "";
                        mTokens.add(token2);
                        token2.end = index;
                        token2.line = line;
                        string = "";
                        tokens.add(ati + "");
                    }
                }
                if(!found) string += ati;
            }

            lastone = ati;
            index ++;
        }

        ArrayList<String> tokens0 = new ArrayList<>();
        ArrayList<Token>  tokens1 = new ArrayList<>();

        for(String string1 : tokens) if(string1.length() > 0 && !(string1.length() == 1 & string1.charAt(0) == (char)13)) tokens0.add(string1);
        for(Token  token : mTokens) if(token.value.length() > 0 && !(token.value.length() == 1 & token.value.charAt(0) == (char)13)) tokens1.add(token);
//        for(String string1 : tokens0) System.out.println(string1);
        this.tokens = tokens0;

        mTokens.clear();
        this.mTokens.addAll(tokens1);

//        for(Token token : this.mTokens) System.out.println(token.value + " " + token.end + " " + token.line);
    }

    public static String errorByLine(int line, int index)
    {
        return "[" + line + " : " + index + "]";
    }
}