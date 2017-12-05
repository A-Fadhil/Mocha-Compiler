package cj;

public class CParseException extends Exception
{
    public CParseException(String text, int begin, int end, int line)
    {
        super(text + "[" + begin + "-" + end + ", " + line + "]");
    }
}