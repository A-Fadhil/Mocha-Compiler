struct A
{
  int a;
  int b;
  int c;

  virtual inline void calculate()
  {
    println(a * b + c);
  }
}

//sizeof(A) will return 12
//to create a child type of A,
//we simply need to reimplement the class, with more features if neccessary.

//B is a greate example of that, the fields of A align with B's fields
//therefore the compiler will recognize it as a subclass of A.
struct B
{
  int a;
  int b;
  int c;

  float G;

  //matching methods with A is not required, however we can override them like so
  //the override keyword is not neccessary, but could be used to make things more
  //clear for sharing code between programmers.
  override inline void calculate()
  {
  }
}

//C is not a subclass of A
struct C
{
  int a;
  int b;
}

//G is not a subclass of A
struct G
{
  int a;
  int b;
  lowp_f c;
}
