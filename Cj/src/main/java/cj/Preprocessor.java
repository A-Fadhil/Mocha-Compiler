package cj;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.HashMap;

public class Preprocessor
{
    HashMap<String, String> files = new HashMap<>();

    String text;

    public Preprocessor(File file, File includes) throws ParseException
    {
        File directory = file.getParentFile();

        text = "";

        addFiles(directory);

        File list[] = directory.listFiles();

//        for (File f : list) if (f.isFile())
//        {
////            String txt = files.get(f.toString().replace(file.toString(), ""));
////            Lexer lexer = new Lexer(txt);
////            Parser parser = new Parser(lexer);
////            if(parser);
//
//            text = text + get(f) + "\n";
//        }

        text = text + get(file) + "\n";
    }

    private void addFiles(File file)
    {
        File list[] = file.listFiles();
        for (File fle : list)
        {
            if (fle.isDirectory()) addFiles(fle);
            files.put(fle.toString().replace(file.toString(), ""), get(fle) + "\n");
        }
    }

    private String get(File file)
    {
        return read(file);
    }

    private static String read(File file)
    {
        try
        {
            FileInputStream stream = new FileInputStream(file);
            byte array[] = new byte[(int) file.length()];
            stream.read(array);
            stream.close();
            String text = new String(array, "UTF-8");
            return text;
        } catch (Exception e)
        {
//            Log.e("cannot read file '" + file.toString() + "'");
        }

        return "b''";
    }

    public String text()
    {
        return text.replaceAll("\\/\\/.*", "");
    }
}