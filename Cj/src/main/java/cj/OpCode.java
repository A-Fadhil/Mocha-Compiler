package cj;

import com.sun.org.omg.CORBA.Initializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class OpCode
{
    public static char
    push = 0,
    pop  = 1,
    //signed numeral types
    byt_load = 2, //load byte to stack
    chr_load = 3, //load char to stack
    sht_load = 4, //load short to stack
    u_sht_ld = 5, //load u short to stack
    i_load   = 6, //load int to stack
    l_load   = 7, //load long to stack
    lowpf_ld = 8, //load lowp f to stack
    f_load   = 9, //load float to stack
    u_int_ld = 10, //load u int to stack
    u_lng_ld = 11, //load u long to stack
    uni_c_ld = 12, //load u char to stack
    stringld = 13, //load string to stack
    ptr___ld = 14, //load pointer (new object) to heap and reference it on the stack
    ref___ld = 15, //load reference (new object) to stack and reference it on the stack

    print    = 16,
    //move object from native memory to the stack
    nativ_mv = 17,
    //move object from the stack to native memory
    stack_mv = 18,

    //move object from the stack to another location on the stack, then pop it.
    mov      = 19,
    call     = 20,
    native_  = 21,
    go_to    = 22,
    if_      = 23,
    if_else  = 24,
    else_    = 25,
    arg_load = 26, //arg to ref
    ref_load = 27, //ref to stack
    stk_cpy  = 28, //make a copy of object from the heap to stack
    crt_cpy  = 29, //make a copy of object from stack to the stack
    println  = 30,
    add      = 31,
    sub      = 32,
    mul      = 33,
    div      = 34,
    lshft    = 35,
    rshft    = 36,
    pow      = 37,
    ref_set  = 38,
    ptr_set  = 39,
    ref_setv = 40,
    ptr_setv = 41,
    mallc    = 42,

    byt_load0 = 43, //load byte to stack
    chr_load0 = 44, //load char to stack
    sht_load0 = 45, //load short to stack
    u_sht_ld0 = 46, //load u short to stack
    i_load0   = 47, //load int to stack
    l_load0   = 48, //load long to stack
    lowpf_ld0 = 49, //load lowp f to stack
    f_load0   = 50, //load float to stack
    u_int_ld0 = 51, //load u int to stack
    u_lng_ld0 = 52, //load u long to stack
    uni_c_ld0 = 53, //load u char to stack

    push_byt = 43, //load byte to stack
    push_chr = 44, //load char to stack
    push_sht = 45, //load short to stack
    push_u_sht = 46, //load u short to stack
    push_i   = 47, //load int to stack
    push_l   = 48, //load long to stack
    push_lowpf = 49, //load lowp f to stack
    push_f   = 50, //load float to stack
    push_u_int_ld0 = 51, //load u int to stack
    push_u_lng_ld0 = 52, //load u long to stack
    push_uni_c_ld0 = 53, //load u char to stack

    imul      = 54,
    uimul     = 55,
    iadd      = 56,
    uiadd     = 57,
		/*int_load= 2,
		lwf_load= 3,
		flt_load= 4,
		byt_load= 5,
		chr_load= 6,
		u_chr_load= 7,*/

    enable_gc  = 254,
    halt_prcss = 255;

    ArrayList<Byte> mOpCode = new ArrayList<>();
    String scope;
    StringBuilder   mOpCodeHumanReadable = new StringBuilder();

    public OpCode(String name)
    {
        scope = name;
        mOpCodeHumanReadable.append("scope" + space(15) + ":" + space(20 - name.length()) + name + "\n");
    }

    public String humanReadable() { return mOpCodeHumanReadable.toString(); }

    public static String space(int amount)
    {
        String space = "";

        for(int i = 0; i < amount; i ++) space = space + " ";

        return space;
    }

    public void newHeapObject(int size)
    {
        mOpCodeHumanReadable.append("ptr___ld" + space(20 - "ptr___ld".length()) + ":" + space(20 - (size + "").length()) + "\n");
        mOpCode.add((byte) ptr___ld);

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(size).flip();
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
    }

    public void newStackObject(int size)
    {
        mOpCodeHumanReadable.append("ref___ld" + space(20 - "ref___ld".length()) + ":" + space(20 - (size + "").length()) + "\n");
        mOpCode.add((byte) ref___ld);

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(size).flip();
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
    }

    public void argLoad(int size)
    {
        mOpCodeHumanReadable.append("arg_load" + space(20 - "arg_load".length()) + ":" + space(20 - (size + "").length()) + "\n");
        mOpCode.add((byte) arg_load);

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(size).flip();
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
    }

    public void append(OpCode opcode)
    {
        for (Byte b : opcode.mOpCode) mOpCode.add(b);
        mOpCodeHumanReadable.append(opcode.mOpCodeHumanReadable.toString());
    }

    public void call(int id)
    {
        mOpCodeHumanReadable.append("call" + space(20 - "call".length()) + ":" + space(20 - (id + "").length()) + "\n");
        mOpCode.add((byte) call);

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(id).flip();
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
        mOpCode.add((byte) b.get());
    }

    public void print()
    {
        mOpCodeHumanReadable.append("print" + space(20 - "print".length()) + ":" + space(20) + "\n");
        mOpCode.add((byte) print);
    }

    public void println()
    {
        mOpCodeHumanReadable.append("println" + space(20 - "println".length()) + ":" + space(20) + "\n");
        mOpCode.add((byte) println);
    }

    public void pshString(String value)
    {
        value = new StringBuilder(value).reverse().toString();
        mOpCodeHumanReadable.append("stringld" + space(20 - "stringld".length()) + ":" + space(20 - value.length()) + value + "\n");
        mOpCode.add((byte) stringld);

        mOpCode.add((byte) (value.length() >> 24));
        mOpCode.add((byte) (value.length() >> 16));
        mOpCode.add((byte) (value.length() >> 8));
        mOpCode.add((byte) (value.length()));

        int length = value.length();

        mOpCodeHumanReadable.append((byte) (length >> 24) + space(20 - ((byte) ((length >> 24)) + "").length()) + ":" + space(20 - "length".length()) + "length" + "\n");
        mOpCodeHumanReadable.append((byte) (length >> 16) + space(20 - ((byte) ((length >> 16)) + "").length()) + ":" + space(20 - "length".length()) + "length" + "\n");
        mOpCodeHumanReadable.append((byte) (length >> 8)  + space(20 - ((byte) ((length >> 8)) + "").length()) + ":" + space(20 - "length".length()) + "length" + "\n");
        mOpCodeHumanReadable.append((byte) (length)       + space(20 - ((byte) ((length)) + "").length()) + ":" + space(20 - "length".length()) + "length" + "\n");

        for(int i = 0; i < length; i ++)
        {
            mOpCode.add((byte) (value.charAt(i) >> 8));
            mOpCode.add((byte) (value.charAt(i)));

            mOpCodeHumanReadable.append((byte) (value.charAt(i) >> 8) + space(20 - ((byte) ((value.charAt(i) >> 8)) + "").length()) + ":" + space(20 - "char ' '".length()) + "char '" + value.charAt(i) + "'\n");
            mOpCodeHumanReadable.append((byte) (value.charAt(i)) + "\n");
        }
    }

    public void push_i(int val)
    {
        mOpCodeHumanReadable.append("push_i" + space(20 - "push_i".length()) + ":" + space(20 - (val + "").length()) + val + "\n");
        mOpCode.add((byte)push_i);
    }

    public void uint_ld(Number size)
    {
        if(size != null)
        {
            mOpCodeHumanReadable.append("u_int_ld" + space(20 - "u_int_ld".length()) + ":" + space(20 - (size + "").length()) + size + "\n");
            mOpCode.add((byte) u_int_ld);
            mOpCode.add((byte) (size.longValue() >> 24));
            mOpCode.add((byte) (size.longValue() >> 16));
            mOpCode.add((byte) (size.longValue() >>  8));
            mOpCode.add((byte) (size.longValue()));

            Number number = size;

            mOpCodeHumanReadable.append((byte) (number.longValue() >> 24) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 16) + space(20 - ((byte) (number.longValue() >> 16) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >>  8) + space(20 - ((byte) (number.longValue() >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue()) + space(20 - ((byte) (number.longValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        } else
        {
            mOpCodeHumanReadable.append("u_int_ld0" + space(20 - "u_int_ld0".length()) + ":" + space(20 - (0 + "").length()) + 0 + "\n");
            mOpCode.add((byte) u_int_ld0);
        }
    }

    /**
     * set value of reference to other
     */
    public void ref_set()
    {
        mOpCodeHumanReadable.append("ref_set" + space(20 - 7) + ":" + space(20) + "\n");
        mOpCode.add((byte) ref_set);
    }

    /**
     * set value of reference to other
     */
    public void ref_set_value()
    {
        mOpCodeHumanReadable.append("ref_setv" + space(20 - 8) + ":" + space(20) + "\n");
        mOpCode.add((byte) ref_setv);
    }

    public void imul()
    {
        mOpCodeHumanReadable.append("imul" + space(16) + ":" + space(20) + "\n");
        mOpCode.add((byte)imul);
    }

    public void uimul()
    {
        mOpCodeHumanReadable.append("uimul" + space(15) + ":" + space(20) + "\n");
        mOpCode.add((byte)uimul);
    }

    public void iadd()
    {
        mOpCodeHumanReadable.append("iadd" + space(16) + ":" + space(20) + "\n");
        mOpCode.add((byte)iadd);
    }

    public void uiadd()
    {
        mOpCodeHumanReadable.append("uiadd" + space(15) + ":" + space(20) + "\n");
        mOpCode.add((byte)uiadd);
    }

    public void pshInt(Number number)
    {
        /** int with value **/
        if(number != null)
        {
            mOpCodeHumanReadable.append("i_load" + space(20 - 6) + ":" + space(20) + "\n");
            mOpCode.add((byte) i_load);

            mOpCode.add((byte) (number.intValue() >> 24));
            mOpCode.add((byte) (number.intValue() >> 16));
            mOpCode.add((byte) (number.intValue() >>  8));
            mOpCode.add((byte) (number.intValue()));

            mOpCodeHumanReadable.append((byte) (number.intValue() >> 24) + space(20 - ((byte) (number.intValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.intValue() >> 16) + space(20 - ((byte) (number.intValue() >> 16) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.intValue() >>  8) + space(20 - ((byte) (number.intValue() >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.intValue()) + space(20 - ((byte) (number.intValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        } else
        {
            mOpCodeHumanReadable.append("i_load0" + space(20 - 7) + ":" + space(20) + "\n");
            mOpCode.add((byte) i_load0);
        }
    }

    public void pshLong(Number number)
    {
        /** int with value **/
        if(number != null)
        {
            mOpCodeHumanReadable.append("l_load" + space(20 - 6) + ":" + space(20) + "\n");
            mOpCode.add((byte) l_load);

            mOpCode.add((byte) (number.longValue() >> 56));
            mOpCode.add((byte) (number.longValue() >> 48));
            mOpCode.add((byte) (number.longValue() >> 40));
            mOpCode.add((byte) (number.longValue() >> 32));
            mOpCode.add((byte) (number.longValue() >> 24));
            mOpCode.add((byte) (number.longValue() >> 16));
            mOpCode.add((byte) (number.longValue() >>  8));
            mOpCode.add((byte) (number.longValue()));

            mOpCodeHumanReadable.append((byte) (number.longValue() >> 56) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 48) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 40) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 32) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 24) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 16) + space(20 - ((byte) (number.longValue() >> 16) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >>  8) + space(20 - ((byte) (number.longValue() >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue()) + space(20 - ((byte) (number.longValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        } else
        {
            mOpCodeHumanReadable.append("l_load0" + space(20 - 7) + ":" + space(20) + "\n");
            mOpCode.add((byte) l_load0);
        }
    }

    public void pshUnsignedLong(Number number)
    {
        /** int with value **/
        if(number != null)
        {
            mOpCodeHumanReadable.append("u_lng_ld" + space(20 - 8) + ":" + space(20) + "\n");
            mOpCode.add((byte) u_lng_ld);

            mOpCode.add((byte) (number.longValue() >> 56));
            mOpCode.add((byte) (number.longValue() >> 48));
            mOpCode.add((byte) (number.longValue() >> 40));
            mOpCode.add((byte) (number.longValue() >> 32));
            mOpCode.add((byte) (number.longValue() >> 24));
            mOpCode.add((byte) (number.longValue() >> 16));
            mOpCode.add((byte) (number.longValue() >>  8));
            mOpCode.add((byte) (number.longValue()));

            mOpCodeHumanReadable.append((byte) (number.longValue() >> 56) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 48) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 40) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 32) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 24) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >> 16) + space(20 - ((byte) (number.longValue() >> 16) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue() >>  8) + space(20 - ((byte) (number.longValue() >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.longValue()) + space(20 - ((byte) (number.longValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        } else
        {
            mOpCodeHumanReadable.append("l_load0" + space(20 - 7) + ":" + space(20) + "\n");
            mOpCode.add((byte) l_load0);
        }
    }

    public void pshFloat(Number number)
    {
        /** int with value **/
        if(number != null)
        {
            mOpCodeHumanReadable.append("f_load" + space(20 - 6) + ":" + space(20) + "\n");
            mOpCode.add((byte) f_load);

            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 56));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 48));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 40));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 32));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 16));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >>  8));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue())));

            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 56) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 48) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 40) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 32) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 16) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 16) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >>  8) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue())) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue())) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        } else
        {
            mOpCodeHumanReadable.append("f_load0" + space(20 - 7) + ":" + space(20) + "\n");
            mOpCode.add((byte) f_load0);
        }
    }

    public void pshLowp_f(Number number)
    {
        /** int with value **/
        if(number != null)
        {
            mOpCodeHumanReadable.append("lowpf_ld" + space(20 - 8) + ":" + space(20) + "\n");
            mOpCode.add((byte) lowpf_ld);

            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 16));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue()) >>  8));
            mOpCode.add((byte) (Double.doubleToRawLongBits(number.doubleValue())));

            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 16) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >> 16) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue()) >>  8) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue()) >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (Double.doubleToRawLongBits(number.doubleValue())) + space(20 - ((byte) (Double.doubleToRawLongBits(number.doubleValue())) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        } else
        {
            mOpCodeHumanReadable.append("lowpf_ld0" + space(20 - 9) + ":" + space(20) + "\n");
            mOpCode.add((byte) lowpf_ld0);
        }
    }

    public void pshByte(Number number)
    {
        if(number != null)
        {
            mOpCodeHumanReadable.append("chr_load" + space(20 - 8) + ":" + space(20) + "\n");
            mOpCode.add((byte) chr_load);

            mOpCode.add((byte) (number.byteValue()));

            mOpCodeHumanReadable.append((byte) (number.byteValue()) + space(20 - ((byte) (number.intValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        } else
        {
            mOpCodeHumanReadable.append("chr_load0" + space(20 - 9) + ":" + space(20) + "\n");
            mOpCode.add((byte) lowpf_ld0);
        }
    }

    public void pshChar(Number number)
    {
        if(number != null)
        {
            mOpCodeHumanReadable.append("chr_load" + space(20 - 8) + ":" + space(20) + "\n");
            mOpCode.add((byte) chr_load);

            mOpCode.add((byte) (number.byteValue()));

            mOpCodeHumanReadable.append((byte) (Byte.toUnsignedInt(number.byteValue())) + space(20 - ((byte) (number.intValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        }
    }

    public void pshShort(Number number)
    {
        /** int with value **/
        if(number != null)
        {
            mOpCodeHumanReadable.append("sht_load" + space(20 - 8) + ":" + space(20) + "\n");
            mOpCode.add((byte) sht_load);

            mOpCode.add((byte) (number.shortValue() >>  8));
            mOpCode.add((byte) (number.shortValue()));

            mOpCodeHumanReadable.append((byte) (number.shortValue() >>  8) + space(20 - ((byte) (number.intValue() >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.shortValue()) + space(20 - ((byte) (number.intValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        }
    }

    public void pshUnsignedShort(Number number)
    {
        /** int with value **/
        if(number != null)
        {
            mOpCodeHumanReadable.append("u_sht_ld" + space(20 - 8) + ":" + space(20) + "\n");
            mOpCode.add((byte) u_sht_ld);

            mOpCode.add((byte) (number.shortValue() >>  8));
            mOpCode.add((byte) (number.shortValue()));

            mOpCodeHumanReadable.append((byte) (number.shortValue() >>  8) + space(20 - ((byte) (number.intValue() >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
            mOpCodeHumanReadable.append((byte) (number.shortValue()) + space(20 - ((byte) (number.intValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        }
    }

    public void pshBool(Boolean bool)
    {
        if(bool != null)
        {
            mOpCodeHumanReadable.append("bool" + space(16) + ":" + space(20));
        }
    }

    public void pshReference(int size, Method constructor)
    {
        if(size <= 0) size = 1;

        mOpCodeHumanReadable.append("ref___ld" + space(20 - "ref___ld".length()) + ":" + space(20) + "\n");
        mOpCode.add((byte) ref___ld);
        mOpCode.add((byte) (size >> 24));
        mOpCode.add((byte) (size >> 16));
        mOpCode.add((byte) (size >>  8));
        mOpCode.add((byte) (size));

        Number number = size;

        mOpCodeHumanReadable.append((byte) (number.longValue() >> 24) + space(20 - ((byte) (number.longValue() >> 24) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        mOpCodeHumanReadable.append((byte) (number.longValue() >> 16) + space(20 - ((byte) (number.longValue() >> 16) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        mOpCodeHumanReadable.append((byte) (number.longValue() >>  8) + space(20 - ((byte) (number.longValue() >>  8) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");
        mOpCodeHumanReadable.append((byte) (number.longValue()) + space(20 - ((byte) (number.longValue()) + "").length()) + ":" + space(20 - "value".length()) + "ivalu\n");

        inline(constructor);
    }

    public void inline(Method method)
    {
        mOpCode.addAll(method.opCode.mOpCode);
        mOpCodeHumanReadable.append(method.opCode.mOpCodeHumanReadable);
    }

    public void get(String value)
    {
        mOpCodeHumanReadable.append("ref_load" + space(12) + ":" + space(20 - value.length()) + value + "\n");
        mOpCode.add((byte) ref_load);
    }
}