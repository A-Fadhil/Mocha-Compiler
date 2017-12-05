#include "/examples/class.c"

////#define TUT0
////
////#ifdef TUT0
////
////int main()
////{
////  println("hello world");
////}
////
////#endif
//
//struct Berry
//{
//  float sweetness;
//  float weight;
//  float ripeness;
//
//  //forward definitions work
//  //however stack allocated
//  //objects can only exist
//  //in one of the forward declared
//  //structs.
//  //i.e if Berry* cherry in struct Cherry is not a pointer, the compiler would throw an error.
//  Cherry berry;
//
//  virtual inline void ripen()
//  {
//    ripeness = 100;
//  }
//
//  inline void print()
//  {
//    println(sizeof(this));
//  }
//}
//
//struct Cherry
//{
//  float sweetness;
//  float weight;
//  float ripeness;
//
//  Berry* cherry;
//}
//
/////** inheritance doesn't need to be declared **/
//struct StrawBerry ///* : Berry */
//{
//  //as long as the first declared fields are
//  //in the same order as the base class, and have the same names
//  //the compiler will recognize this class, as a subset of Berry.
//    float sweetness;
//    float weight;
//    float ripeness;
//
//    Cherry berry;
//
//    StrawBerry* red;
//
//    ///** because all the fields are in the correct order
//    //and all the virtual methods are the same, then this is a subclass of
//    //Berry
//    //**/
//    virtual inline void ripen()
//    {
//      ripeness = 50;
//    }
//}
//
//
//class hello_world {
//  //body is called once
//
//  //types
//  byte b; //8bit signed integer type [-127 to 127]
//  char c; //8bit unsigned integer type [0 to 256]
//  short s; //16bit signed integer type [-]
//  u_short us; //16 unsigned integer type
//  int i; //32bit signed integer type
//  u_int ui; //32bit unsigned integer type
//  long l; //64bit signed integer type
//  u_long ul; //64bit unsigned integer type 0 to a really big number.
//
//  unicode_c uc; //16bit unicode character
//  String    st; //a string consisting of a unicode_c array
//  // string    ls; //a string consisting of an 8bit unicode array
//
//  float f; //64bit floating point type
//  lowp_f lf; //32bit floating point type
//
////main is called by default if it exists.
//  static void main()
//  {
//    println("hello world");
//
//    Berry berry;
//
//    // the compiler will allow the conversion of StrawBerry to Berry
//    Berry b = StrawBerry();
//
//    berry.print();
//    b.print();
//  }
//
//  Berry* bPointer;
//  Berry  localBerry;
//
//  native void native_function(long a, long b);
//  static native Berry native_berry(float sweetness, float ripeness);
//
//  void scope_influence()
//  {
//    //this berry gets deleted once the method is finished.
//    Berry berry;
//    Berry sweetBerry;// <- this berry would get deleted as well, however. We lengthen it's lifespan by
//    //by assigning a local variable to it's value.
//    //this operation will copy the contents of 'sweetBerry' from the stack
//    //to native memory, and 'bPointer' will point to that information.
//
//    int a = b * c;
//    int a = b + c;
//
//    Berry& BB = sweetBerry;
//
//    bPointer = &sweetBerry;
//    bPointer = *sweetBerry;
//    //any modification to sweetBerry will not affect the content of bPointer
//
//    Berry lBerry;
//    localBerry = lBerry;
//    //in here, lBerry takes a new memory block in stack.
//    //for the sake of simplicity, let's say that 'localBerry's' stack index is 8
//    //and 'lBerry's' stack index is 20
//    //when we assign localBerry to lBerry the stack copy's lBerry's information
//    //to localBerry's index and then pops lBerry, therefore any reference to lBerry
//    //will be a reference to localBerry.
//  }
//}
