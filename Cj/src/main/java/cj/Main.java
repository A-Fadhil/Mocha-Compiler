package cj;

import cj.helpers.BinarySearchTree;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

public class Main
{
    public static File root;

    public static void main(String... args) throws Exception
    {
        int arglength = args == null ? 0 : args.length;

        if (arglength > 0)
        {
            final String arguments[] = {"-o", "-r", "-e", "gc=0", "gc=1", "-i", "-n"};
            final boolean ARGS[] = {false, false, false, false, false, false, false};

            boolean close = false;

            File includes = null;

            for (int string = 0; string < args.length - 1; string++)
                for (int i = 0; i < arguments.length; i++)
                    if (args[string].equals(arguments[i]))
                    {
                        ARGS[i] = true;
                        if (args[string].equals("-i")) string++;
                        break;
                    } else
                    {
                        if (i == arguments.length - 1)
                        {
                            System.err.println(args[string] + ": is not a valid Objective J argument.");
                            close = true;
                        }
                    }

            if (close)
            {
                System.out.println("valid Objective J arguments:");
                System.out.println("arguments... path.c");
                System.out.println("-o: output .object files of target.");
                System.out.println("-r: run target.");
                System.out.println("-e: generate executables.");
                System.out.println("gc=#: garbage collection = 0/1.");
                System.out.println("-n: to generate native method stubs.");
                System.exit(0);
            }

            byte object[];

            Preprocessor preprocessor = new Preprocessor(new File(args[args.length - 1]), includes);
            root = new File(new File(args[args.length - 1]).getParent());

            Lexer lexer = new Lexer(preprocessor.text());
            Parser parser = new Parser(lexer);

            BinarySearchTree tree = new BinarySearchTree(17);
            if(ARGS[6]) writeNatives(args[args.length - 1], new AbstractSyntaxTree(parser).generateNativeStub());
            if(ARGS[0]) writeObject(args[args.length - 1], new AbstractSyntaxTree(parser).generateObjectFile());

            tree.add(5);
            tree.add(51);

//            Runtime runtime = new Runtime(256, 1024, false);

//            int maxval = 0;
//            for (int i = 0; i < 17; i++)
//            {
//                maxval = (int) (maxval + Math.pow(Memory.twiddle((i + 1)), 2));
//                System.out.println((i + 1) + " " + Math.pow((i + 1), 2));
//            }

//            System.out.println(tree.get(1) + " " + maxval + " " + Math.pow(8, 2));

//            tree.get(0b01000000000000000000000000000000);

//            System.out.println(new BinarySearchTree(17).total());
        }
    }

    private static void writeObject(String arg, ArrayList<Byte> bytes) throws IOException
    {
        if(bytes == null)
        {
            System.err.println("couldn't create .o file(s), an error occurred.");
            System.exit(0);
        }

        File file = new File(new File(arg).getParent() + "\\" + new File(arg).getName().replaceAll("\\..+", ".o"));

        FileOutputStream writer = new FileOutputStream(file);

        for(Byte b : bytes) writer.write(b);

        writer.flush();
        writer.close();
    }

    private static void writeNatives(String arg, String s) throws IOException
    {
        File file = new File(new File(arg).getParent() + "\\natives.h");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        writer.write(s);

        writer.flush();
        writer.close();
    }
}