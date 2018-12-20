import tester.*;                
import javalib.worldimages.*;   
import javalib.funworld.*;      
import java.awt.Color;   
import javalib.worldcanvas.*;

// Designs a mobile and calculates measurements needed for balance, height, weight, etc.
// An IMobile is a:
// Simple Mobile
// Complex Mobile

interface IMobile {

  // computes the total weight of the mobile
  int totalWeight();

  // computes total height of the mobile
  int totalHeight();

  //computes whether a mobile is balanced
  boolean isBalanced();

  //combines this balanced mobile with the given balanced mobile and produces a new mobile
  IMobile buildMobile(int l, int s, IMobile other);

  //uses acc to figure out left and right side balance
  int buildHelper(int s, int acc, IMobile other);

  //computes the current width of a mobile
  int curWidth();

  // count the left side width of a mobile
  int countLeft();

  // count the right side width of a mobile
  int countRight();

  // draw the 2D mobile
  WorldImage drawMobile();

}

class Simple implements IMobile {

  int length;
  int weight;
  Color color;

  /* Fields:
   * this.length...int
   * this.weight...int
   * this.color...Color
   * 
   * Methods:
   * totalWeight() -> int
   * totalHeight() -> int
   * isBalanced() -> boolean
   * buildMobile(int l, int s, IMobile other) -> IMobile
   * buildHelper(int l, int acc, IMobile other) -> IMobile
   * currWidth() -> int
   * countLeft() -> int
   * countRight() -> int
   * drawMobile(posn p) -> Image
   */

  Simple(int length, int weight, Color color) {

    this.length = length;
    this.weight = weight;
    this.color = color;

  }

  // returns simple mobile total weight
  public int totalWeight() {
    return this.weight;
  }

  // returns simple mobile total height
  public int totalHeight() {
    return this.weight / 10 + this.length;
  }

  // a simple mobile is always balanced
  public boolean isBalanced() {
    return true;
  }


  // builds a balanced IMobile
  public IMobile buildMobile(int l, int s, IMobile other) {
    return new Complex(l, this.buildHelper(s, 0, other),
        s - buildHelper(s, 0, other), this, other);
  }


  // helps build complex mobile using an accumulator
  public int buildHelper(int s, int acc, IMobile other) {
    if (new Complex(this.length, s - acc, acc, this, other).isBalanced()) {
      return s - acc;
    }
    else {
      return this.buildHelper(s, acc + 1, other);
    }
  }


  // determines the width of a simple mobile
  public int curWidth() {
    if (this.weight % 10 == 0) {
      return this.weight / 10;
    }
    else {
      return this.weight / 10 + 1;
    }
  }

  // count the left side of a simple mobile
  public int countLeft() {
    return this.curWidth();
  }


  // count the right side of a simple mobile
  public int countRight() {
    return this.curWidth();
  }

  // draws a simple mobile
  public WorldImage drawMobile() {
    return new AboveImage(new LineImage(new Posn(0,this.length * 10), 
        new Color(51,51,51)), 
        new RectangleImage(this.weight / 10 * 15, this.weight / 10 * 15,
            OutlineMode.SOLID, this.color ));
  }
}

class Complex implements IMobile {

  int length;
  int leftside;
  int rightside;
  IMobile left;
  IMobile right;

  /* Fields:
   * this.length...int
   * this.leftside...int
   * this.rightside...int
   * this.left...IMobile
   * this.right...IMobile
   * 
   * Methods:
   * totalWeight() -> int
   * totalHeight() -> int
   * isBalanced() -> boolean
   * buildMobile(int l, int s, IMobile other) -> IMobile
   * buildHelper(int s, int acc, IMobile other) -> IMobile
   * currWidth() -> int
   * countLeft() -> int
   * countRight() ->
   * drawMobile(posn p) -> Image
   * 
   * 
   * Methods for Fields:
   * this.left.totalWeight() -> int
   * this.right.totalWeight() -> int
   * this.left.totalHeight() -> int
   * this.right.totalHeight() -> int
   * this.left.countLeft
   * this.right.countRight
   * this.leftside.countLeft
   * this.rightside.countRight
   */

  Complex(int length, int leftside, int rightside, IMobile left, IMobile right) {

    this.length = length;
    this.leftside = leftside;
    this.rightside = rightside;
    this.left = left;
    this.right = right;
  }

  // returns total Weight of a mobile
  public int totalWeight() {
    return this.left.totalWeight() + this.right.totalWeight();

  }

  // returns total Height of a mobile
  public int totalHeight() {
    return this.length + Math.max(this.left.totalHeight(), this.right.totalHeight());
  }


  // checks if a mobile is balanced
  public boolean isBalanced() {
    return ((this.left.totalWeight() * this.leftside) 
        == (this.right.totalWeight() * this.rightside));
  }


  // builds a complex mobile
  public IMobile buildMobile(int l, int s, IMobile other) {
    return new Complex(l, this.buildHelper(s, 0, other),
        s - this.buildHelper(s, 0, other), this, other);
  }


  // helps build a complex mobile using an acc
  public int buildHelper(int s, int acc, IMobile other) {
    if (new Complex(this.length, s - acc, acc, this, other).isBalanced()) {
      return s - acc;
    }
    else {
      return this.buildHelper(s, acc + 1, other);
    }
  }


  // determines width of a mobile laid flat
  public int curWidth() {
    return this.leftside + this.left.countLeft() + this.rightside + this.right.countRight();
  }

  // returns left side width
  public int countLeft() {
    return this.leftside + this.left.countLeft();
  }

  // returns right side width
  public int countRight() {
    return this.rightside + this.right.countRight();


  }

  // draws a complex mobile
  public WorldImage drawMobile() {
    return new LineImage(new Posn(0, this.length * 10),
        new Color(51,51,51)).movePinhole(- this.leftside * 5, this.length * 5)
        .overlayImages(new LineImage(new Posn(this.leftside * 10, 0), 
            new Color(51,51,51))).movePinhole((this.leftside * 5) 
                + (this.rightside * 5), 0)
        .overlayImages(new LineImage(new Posn(this.rightside * 10, 0), 
                    new Color(51,51,51))).movePinhole(0,0).overlayImages(left.drawMobile()
                        .movePinhole(0, 0).overlayImages(this.right.drawMobile()));



  }
}

class ExamplesMobiles {

  Color blue = new Color(0,0,255);
  Color red = new Color(255, 0, 0);
  Color green = new Color(0, 255, 0);
  IMobile exampleSimple = new Simple(2, 20, this.blue);
  IMobile exampleComplex = new Complex(1, 9, 3, new Simple(1, 36, this.blue), 
      new Complex(2, 8, 1, new Simple(1, 12, this.red),
          new Complex(2, 5, 3, new Simple(2, 36, this.red), 
              new Simple(1, 60, this.green))));
  IMobile otherComplex = new Complex(2, 8, 8, new Simple(1, 79, this.green),
      new Complex(3, 7, 7, new Simple(3, 34, this.red), new Complex(4, 3, 9, 
          new Simple(2, 25, this.blue), new Simple(3, 15, this.red))));
  IMobile example3 = new Complex(3, 8, 8, this.exampleComplex, this.otherComplex);
  IMobile example4 = new Complex(4, 8, 8, new Simple(1, 32, this.red),
      new Complex(2, 4, 4, new Simple(1, 16, this.green), new Simple(1, 16, this.blue)));

  // tests totalWeight()
  boolean testtotalWeight(Tester t) {
    return t.checkExpect(this.exampleSimple.totalWeight(), 20)
        && t.checkExpect(this.exampleComplex.totalWeight(), 144)
        && t.checkExpect(this.example3.totalWeight(), 297);
  }

  // tests totalHeight()
  boolean testtotalHeight(Tester t) {
    return t.checkExpect(this.exampleSimple.totalHeight(), 4)
        && t.checkExpect(this.exampleComplex.totalHeight(), 12)
        && t.checkExpect(this.example3.totalHeight(), 16);

  }

  // tests isBalanced()
  boolean testisBalanced(Tester t) {
    return t.checkExpect(this.exampleSimple.isBalanced(), true)
        && t.checkExpect(this.exampleComplex.isBalanced(), true)
        && t.checkExpect(this.example3.isBalanced(), false)
        && t.checkExpect(this.example4.isBalanced(), true);
  }

  // tests buildMobile()
  boolean testbuildMobile(Tester t) {
    return t.checkExpect(this.exampleSimple.buildMobile(2, 6, this.exampleSimple), 
        new Complex(2, 3, 3, this.exampleSimple, this.exampleSimple))
        && t.checkExpect(this.exampleComplex.buildMobile(2, 13, this.example4), 
            new Complex(2, 4, 9, this.exampleComplex, this.example4));
  }
  
  // tests buildHelper()
  boolean testbuildHelper(Tester t) {
    return t.checkExpect(this.exampleComplex.buildHelper(12, 3, this.exampleComplex), 6);
  }
  

  // tests curWidth()
  boolean testcurWidth(Tester t) {
    return t.checkExpect(this.exampleSimple.curWidth(), 2)
        && t.checkExpect(this.exampleComplex.curWidth(), 26)
        && t.checkExpect(this.otherComplex.curWidth(), 42);
  }

  // tests countLeft
  boolean testcountLeft(Tester t) {
    return t.checkExpect(this.exampleSimple.countLeft(), 2)
        && t.checkExpect(this.exampleComplex.countLeft(), 13);
  }

  // tests countRight
  boolean testcountRight(Tester t) {
    return t.checkExpect(this.exampleSimple.countRight(), 2)
        && t.checkExpect(this.exampleComplex.countRight(), 13);
  }

  // draws the simple mobile
  boolean testDrawMobile(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(exampleSimple.drawMobile(), 250, 250))
        && c.show();
  }

  // draws the complex mobile
  boolean testDrawMobile2(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(exampleComplex.drawMobile(), 250, 250))
        && c.show();
  }

}









