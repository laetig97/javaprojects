import java.awt.Color;
import java.util.ArrayList;
import javalib.impworld.*;
import javalib.worldimages.*;
import tester.*;
import java.util.Random;

//Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // main constructor
  Cell(int x, int y, Color color, boolean flooded, Cell left, Cell top, Cell right, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  /*
   * Fields:
   * x...int
   * y...int
   * color...Color
   * flooded...boolean
   * left...Cell
   * top...Cell
   * right...Cell
   * bottom...Cell
   * 
   * Methods:
   * drawCell()-> WorldImage
   * changeColorAndFloodColor(Color) -> void
   * setLeft(ArrayList<Cell> int) -> void
   * setRight(ArrayList<Cell> int) -> void
   * setTop(ArrayList<Cell> int) -> void
   * setBottom(ArrayList<Cell> int) -> void
   */

  // starting constructor, takes an int sets a color
  public Cell(Integer x, Integer y, int intCol) {
    if (intCol == 0) {
      this.x = x;
      this.y = y;
      this.color = Color.ORANGE;
      this.flooded = false;
      this.left = null;
      this.top = null;
      this.right = null;
      this.bottom = null;
    }
    else if (intCol == 1) {
      this.x = x;
      this.y = y;
      this.color = Color.magenta;
      this.flooded = false;
      this.left = null;
      this.top = null;
      this.right = null;
      this.bottom = null;
    }
    else if (intCol == 2) {
      this.x = x;
      this.y = y;
      this.color = Color.blue;
      this.flooded = false;
      this.left = null;
      this.top = null;
      this.right = null;
      this.bottom = null;
    }
    else {
      this.x = x;
      this.y = y;
      this.color = Color.green;
      this.flooded = false;
      this.left = null;
      this.top = null;
      this.right = null;
      this.bottom = null;
    }
  }

  // draws the cell
  public WorldImage drawCell() {
    return new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, this.color), Color.black);
  }

  // changes the color and floods the cell
  public void changeColorAndFlood(Color newCol) {
    this.color = newCol;
    this.flooded = true;
  }

  // Effect : sets the left neighbor
  public void setLeft(ArrayList<Cell> board, int size) {
    if (this.y == 1) {
      this.left = this;
    }
    else {
      this.left = board.get(board.indexOf(this) - 1);
    }
  }

  // effect: sets the right neighbor
  public void setRight(ArrayList<Cell> board, int size) {
    if (this.y == size) {
      this.right = this;
    }
    else {
      this.right = board.get(board.indexOf(this) + 1);
    }
  }

  // effect: sets the top neighbor
  public void setTop(ArrayList<Cell> board, int size) {
    if (this.x == 1) {
      this.top = this;
    }
    else {
      this.top = board.get(board.indexOf(this) - size);
    }
  }

  // effect: sets the bottom neighbor
  public void setBottom(ArrayList<Cell> board, int size) {
    if (this.x >= size) {
      this.bottom = this;
    }
    else {
      this.bottom = board.get(board.indexOf(this) + size);
    }
  }
}

class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> board;
  Random rand;
  int clickCount;
  int boardSize;

  // constructor - takes a random and starts with the set board size & empty array
  // list
  FloodItWorld(Random rand) {
    this.board = new ArrayList<Cell>();
    this.rand = rand;
    this.clickCount = 20;
    this.boardSize = BOARD_SIZE;
  }

  /*
   * Fields
   * board...ArrayList<Cell>
   * rand...Random
   * clickCount...int
   * 
   * Methods: 
   * makeScene() -> WorldScene
   * makeBoard() -> FloodItWorld
   * setNeighbors() -> void
   * lastScene(String) -> WorldScene
   * onMouseClicked(posn) -> void
   * cellChangeList(ArrayList<cell>, "", Color) ->ArrayList<cell>
   * onKeyEvent(String) -> void
   * worldEnds() -> WorldEnd
   * isAllFlooded() -> boolean
   */


  // constructor - takes nothing, starts with a random, the set board size & empty
  // array list
  FloodItWorld() {
    this.board = new ArrayList<Cell>();
    this.rand = new Random();
    this.clickCount = 20;
    this.boardSize = BOARD_SIZE;
  }

  // constructor - takes a random and array list and starts with the set board
  // size
  FloodItWorld(ArrayList<Cell> board, Random rand) {
    this.board = board;
    this.rand = rand;
    this.clickCount = 20;
    this.boardSize = BOARD_SIZE;
  }

  // constructor - takes a array list and starts with the set board size & random
  FloodItWorld(ArrayList<Cell> board) {
    this.board = board;
    this.rand = new Random();
    this.clickCount = 20;
    this.boardSize = BOARD_SIZE;
  }

  // constructor - takes a random, board size and array list
  FloodItWorld(ArrayList<Cell> board, Random rand, int bSize) {
    this.board = board;
    this.rand = rand;
    this.clickCount = 20;
    this.boardSize = bSize;
  }

  // constructor - takes a random and board size and starts with empty array list
  FloodItWorld(Random rand, int bSize) {
    this.board = new ArrayList<Cell>();
    this.rand = rand;
    this.clickCount = 20;
    this.boardSize = bSize;
  }

  // constructor - takes a board size and array list and starts with the random
  FloodItWorld(ArrayList<Cell> board, int bSize) {
    this.board = board;
    this.rand = new Random();
    this.clickCount = 20;
    this.boardSize = bSize;
  }

  // constant for the board size
  static final int BOARD_SIZE = 15;

  // draws the scene
  public WorldScene makeScene() {
    // sets empty scene
    WorldScene sceneSoFar = new WorldScene(this.boardSize * 100, this.boardSize * 100);
    // places each cell on the board
    for (Cell c : board) {
      sceneSoFar.placeImageXY(c.drawCell(), c.x * 30, c.y * 30);
    }
    // places the click count message
    sceneSoFar.placeImageXY(new TextImage("Moves Left: " + clickCount, 20, Color.black), 80, 480);
    // places the instruction message
    sceneSoFar.placeImageXY(new TextImage("Mouseclick or use o, p, g, or b keys", 15, Color.black),
        350, 480);
    return sceneSoFar;
  }

  // makes the array list board
  public FloodItWorld makeBoard() {
    // creates empties
    ArrayList<Cell> board = this.board;
    ArrayList<Integer> xs = new ArrayList<Integer>();
    FloodItWorld wrld = this;
    // makes a list the size of the board (length and height size)
    for (int i = 0; i < this.boardSize; i += 1) {
      xs.add(i + 1);
    }
    // for this size board adds a new cell for each item in the board, going to the
    // new row at end of row
    for (int l = 0; l < xs.size(); l += 1) {
      for (int k = 0; k < xs.size(); k += 1) {
        board.add(new Cell(xs.get(l), xs.get(k), rand.nextInt(4)));
      }
    }
    wrld.board = board;
    // sets neighbors
    wrld.setNeighbors();
    // adds the first cell to a list of starting cells and sets it and all adjacent
    // cells of same color to flooded
    ArrayList<Cell> starting = new ArrayList<Cell>();
    starting.add(wrld.board.get(0));
    ArrayList<Cell> startFlood = new ArrayUtils().floodNeighbors(starting, wrld.board.get(0).color);
    for (Cell c : startFlood) {
      c.flooded = true;
    }
    // returns the world
    return wrld;
  }

  // effect: sets the neighbors
  public void setNeighbors() {
    for (Cell c : this.board) {
      c.setLeft(this.board, this.boardSize);
      c.setRight(this.board, this.boardSize);
      c.setBottom(this.board, this.boardSize);
      c.setTop(this.board, this.boardSize);
    }
  }

  // creates the end scene
  public WorldScene lastScene(String s) {
    WorldScene endScene = new WorldScene(this.boardSize * 100, this.boardSize * 100);
    endScene.placeImageXY(new TextImage(s, 20, Color.red), 250, 250);
    return endScene;
  }

  // effect: mouse click method
  public void onMouseClicked(Posn loc) {
    int clickLoc = new ArrayUtils().find(this.board, new SameCellLoc(), loc.x, loc.y);
    if (clickLoc == -1) {
      this.makeScene();
      this.clickCount -= 1;
    }
    else {
      Cell cellClicked = this.board.get(clickLoc);
      Color newCol = cellClicked.color;
      ArrayList<Cell> cellsToChange = new ArrayList<Cell>();
      cellsToChange = this.cellChangeList(this.board, cellsToChange, newCol);
      for (Cell c : cellsToChange) {
        c.changeColorAndFlood(newCol);
      }
      this.makeScene();
      this.clickCount -= 1;
    }
    this.makeScene();
  }

  // makes the list of all cells that should be changed each click
  public ArrayList<Cell> cellChangeList(ArrayList<Cell> allCells, ArrayList<Cell> toChange,
      Color newCol) {
    // adds all flooded cells
    for (Cell c : allCells) {
      if (c.flooded) {
        toChange.add(c);
      }
    }
    ArrayList<Cell> cellsToChange = new ArrayUtils().floodNeighbors(toChange, newCol);
    return cellsToChange;

  }

  // resets game if r is pressed
  public void onKeyEvent(String k) {
    if (k.equalsIgnoreCase("r")) {
      this.board = new ArrayList<Cell>();
      this.rand = new Random();
      this.clickCount = 20;
      this.makeBoard();
    }

    // added functionality -- using g floods green
    else if (k.equalsIgnoreCase("g")) {
      Color newCol = Color.green;
      ArrayList<Cell> cellsToChange = new ArrayList<Cell>();
      cellsToChange = this.cellChangeList(this.board, cellsToChange, newCol);
      for (Cell c : cellsToChange) {
        c.changeColorAndFlood(newCol);
      }
      this.makeScene();
      this.clickCount -= 1;
    }
    // added functionality -- using p floods magenta (pink square but magenta is
    // prettier)
    else if (k.equalsIgnoreCase("p")) {
      Color newCol = Color.magenta;
      ArrayList<Cell> cellsToChange = new ArrayList<Cell>();
      cellsToChange = this.cellChangeList(this.board, cellsToChange, newCol);
      for (Cell c : cellsToChange) {
        c.changeColorAndFlood(newCol);
      }
      this.makeScene();
      this.clickCount -= 1;
    }
    // added functionality -- using o floods orange
    else if (k.equalsIgnoreCase("o")) {
      Color newCol = Color.orange;
      ArrayList<Cell> cellsToChange = new ArrayList<Cell>();
      cellsToChange = this.cellChangeList(this.board, cellsToChange, newCol);
      for (Cell c : cellsToChange) {
        c.changeColorAndFlood(newCol);
      }
      this.makeScene();
      this.clickCount -= 1;
    }
    // added functionality -- using b floods blue
    else if (k.equalsIgnoreCase("b")) {
      Color newCol = Color.blue;
      ArrayList<Cell> cellsToChange = new ArrayList<Cell>();
      cellsToChange = this.cellChangeList(this.board, cellsToChange, newCol);
      for (Cell c : cellsToChange) {
        c.changeColorAndFlood(newCol);
      }
      this.makeScene();
      this.clickCount -= 1;
    }
  }

  // ends the world
  public WorldEnd worldEnds() {
    if (this.clickCount < 0) {
      return new WorldEnd(true, this.lastScene("Out of Clicks, you lose!"));
    }
    else if (this.isAllFlooded()) {
      return new WorldEnd(true, this.lastScene("You win"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // determines if the entire board is flooded
  public boolean isAllFlooded() {
    boolean floodstatus = true;
    int index = 0;
    while (floodstatus && index < this.board.size()) {
      floodstatus = this.board.get(index).flooded;
      index += 1;
    }
    return floodstatus;
  }
}

interface IPred {
  boolean apply(Cell c, int locX, int locY);
}

class SameCellLoc implements IPred {
  public boolean apply(Cell c, int locX, int locY) {
    return ((c.x * 30 - 15) <= locX) && (locX <= (c.x * 30 + 15)) && ((c.y * 30 - 15) <= locY)
        && (locY <= (c.y * 30 + 15));
  }
}

class ArrayUtils {
  // Returns the index of the first item passing the predicate,
  // or -1 if no such item was found
  int find(ArrayList<Cell> arr, IPred whichOne, int locX, int locY) {
    return findHelp(arr, whichOne, 0, locX, locY);
  }

  // Returns the index of the first item passing the predicate at or after the
  // given index, or -1 if no such such item was found
  int findHelp(ArrayList<Cell> arr, IPred whichOne, int index, int locX, int locY) {
    if (index >= arr.size()) {
      return -1;
    }
    else if (whichOne.apply(arr.get(index), locX, locY)) {
      return index;
    }
    else {
      return findHelp(arr, whichOne, index + 1, locX, locY);
    }
  }

  ArrayList<Cell> floodNeighbors(ArrayList<Cell> toChange, Color newCol) {
    ArrayList<Cell> already = new ArrayList<Cell>();
    for (Cell c : toChange) {
      already.add(c);
    }
    for (Cell c : already) {
      if (c.top.color.equals(newCol) && !toChange.contains(c.top)) {
        toChange.add(c.top);
      }
    }
    for (Cell c : already) {
      if (c.bottom.color.equals(newCol) && !toChange.contains(c.bottom)) {
        toChange.add(c.bottom);
      }
    }
    for (Cell c : already) {
      if (c.left.color.equals(newCol) && !toChange.contains(c.left)) {
        toChange.add(c.left);
      }
    }
    for (Cell c : already) {
      if (c.right.color.equals(newCol) && !toChange.contains(c.right)) {
        toChange.add(c.right);
      }
    }
    if (already.equals(toChange)) {
      return toChange;
    }
    else {
      return this.floodNeighbors(toChange, newCol);
    }
  }
}

class ExamplesFloodIt {
  // random to use in examples
  Random r1 = new Random(2);
  Cell c1 = new Cell(1, 1, Color.blue, false, null, null, null, null);
  Cell c2 = new Cell(1, 2, Color.magenta, false, null, null, null, null);
  Cell c3 = new Cell(1, 3, Color.green, false, null, null, null, null);
  Cell c4 = new Cell(2, 1, Color.orange, false, null, null, null, null);
  Cell c5 = new Cell(2, 2, Color.magenta, false, null, null, null, null);
  Cell c6 = new Cell(2, 3, Color.green, false, null, null, null, null);
  Cell c7 = new Cell(3, 1, Color.green, false, null, null, null, null);
  Cell c8 = new Cell(3, 2, Color.orange, false, null, null, null, null);
  Cell c9 = new Cell(3, 3, Color.green, false, null, null, null, null);
  ArrayList<Cell> l1 = new ArrayList<Cell>();
  FloodItWorld f = new FloodItWorld(l1, r1, 3);

  void setBaseline(Tester t) {
    c1 = new Cell(1, 1, Color.blue, false, null, null, null, null);
    c2 = new Cell(1, 2, Color.magenta, false, null, null, null, null);
    c3 = new Cell(1, 3, Color.green, false, null, null, null, null);
    c4 = new Cell(2, 1, Color.orange, false, null, null, null, null);
    c5 = new Cell(2, 2, Color.magenta, false, null, null, null, null);
    c6 = new Cell(2, 3, Color.green, false, null, null, null, null);
    c7 = new Cell(3, 1, Color.green, false, null, null, null, null);
    c8 = new Cell(3, 2, Color.orange, false, null, null, null, null);
    c9 = new Cell(3, 3, Color.green, false, null, null, null, null);
    l1 = new ArrayList<Cell>();
    l1.add(c1);
    l1.add(c2);
    l1.add(c3);
    l1.add(c4);
    l1.add(c5);
    l1.add(c6);
    l1.add(c7);
    l1.add(c8);
    l1.add(c9);
    f = new FloodItWorld(l1, r1, 3);
  }

  World f2 = new FloodItWorld(r1);

  /** tests for cell functions */
  void testDrawCell(Tester t) {
    this.setBaseline(t);
    t.checkExpect(c1.drawCell(),
        new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.blue), Color.black));
    t.checkExpect(c3.drawCell(),
        new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.green), Color.black));
    t.checkExpect(c5.drawCell(),
        new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.magenta), Color.black));
    t.checkExpect(c8.drawCell(),
        new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.orange), Color.black));
  }

  void testChangeColorandFlood(Tester t) {
    this.setBaseline(t);
    t.checkExpect(this.c1.color, Color.blue);
    t.checkExpect(c1.flooded, false);
    c1.changeColorAndFlood(Color.GREEN);
    t.checkExpect(c1.color, Color.green);
    t.checkExpect(c1.flooded, true);
    c1.changeColorAndFlood(Color.green);
    t.checkExpect(c1.color, Color.green);
    t.checkExpect(c1.flooded, true);
  }

  void testSetLeft(Tester t) {
    this.setBaseline(t);
    t.checkExpect(c1.left, null);
    t.checkExpect(c2.left, null);
    c1.setLeft(l1, 3);
    t.checkExpect(c1.left, c1);
    c2.setLeft(l1, 3);
    t.checkExpect(c2.left, c1);
    c3.setLeft(l1, 3);
    t.checkExpect(c3.left.left, c1);
  }

  void testSetRight(Tester t) {
    this.setBaseline(t);
    t.checkExpect(c1.right, null);
    t.checkExpect(c2.right, null);
    c1.setRight(l1, 3);
    t.checkExpect(c1.right, c2);
    c2.setRight(l1, 3);
    t.checkExpect(c2.right, c3);
    c3.setRight(l1, 3);
    t.checkExpect(c3.right, c3);
    t.checkExpect(c1.right.right, c3);
  }

  void testSetTop(Tester t) {
    this.setBaseline(t);
    t.checkExpect(c1.top, null);
    t.checkExpect(c4.top, null);
    c1.setTop(l1, 3);
    t.checkExpect(c1.top, c1);
    c4.setTop(l1, 3);
    t.checkExpect(c4.top, c1);
    c7.setTop(l1, 3);
    t.checkExpect(c7.top, c4);
    t.checkExpect(c7.top.top, c1);
  }

  void testSetBottom(Tester t) {
    this.setBaseline(t);
    t.checkExpect(c1.bottom, null);
    t.checkExpect(c4.bottom, null);
    c1.setBottom(l1, 3);
    t.checkExpect(c1.bottom, c4);
    c4.setBottom(l1, 3);
    t.checkExpect(c4.bottom, c7);
    c7.setBottom(l1, 3);
    t.checkExpect(c7.bottom, c7);
    t.checkExpect(c1.bottom.bottom, c7);
  }

  /** tests for flooditworld */
  void testMakeScene(Tester t) {
    t.checkExpect(new FloodItWorld(r1).makeBoard().makeScene(), false);
  }

  void testMakeBoard(Tester t) {
    t.checkExpect(new FloodItWorld(l1, r1).makeBoard(), f);
    t.checkExpect(c1.flooded, true);
  }

  void testSetNeighbors(Tester t) {
    this.setBaseline(t);
    FloodItWorld f = new FloodItWorld(l1, r1);

  }

  void testOnKey(Tester t) {
    this.setBaseline(t);
    f.makeBoard();
    f.clickCount = 12;
    t.checkExpect(f.clickCount, 12);
    f.onKeyEvent("r");
    t.checkExpect(f.clickCount, 20);
    this.setBaseline(t);
    f.makeBoard();
    f.onKeyEvent("g");
    t.checkExpect(f.clickCount, 19);
    f.onTick();
    t.checkExpect(c1.color, Color.green);
    f.onKeyEvent("b");
    t.checkExpect(f.clickCount, 18);
    t.checkExpect(c1.color, Color.blue);
    f.onKeyEvent("o");
    t.checkExpect(f.clickCount, 17);
    t.checkExpect(c1.color, Color.orange);
    f.onKeyEvent("p");
    t.checkExpect(f.clickCount, 16);
    t.checkExpect(c1.color, Color.magenta);
    t.checkExpect(c1.flooded, true);
  }

  void testWorldEnds(Tester t) {
    this.setBaseline(t);
    f.clickCount = 0;
    t.checkExpect(f.worldEnds(), new WorldEnd(false, f.makeScene()));
    f.clickCount = -1;
    t.checkExpect(f.worldEnds(), new WorldEnd(true, f.lastScene("Out of Clicks, you lose!")));
    this.setBaseline(t);
    t.checkExpect(f.worldEnds(), new WorldEnd(false, f.makeScene()));
    f.clickCount = 6;
    t.checkExpect(f.worldEnds(), new WorldEnd(false, f.makeScene()));
    c2.flooded = true;
    c3.flooded = true;
    c5.flooded = true;
    c6.flooded = true;
    c7.flooded = true;
    t.checkExpect(f.worldEnds(), new WorldEnd(false, f.makeScene()));
    c8.flooded = true;
    c9.flooded = true;
    c1.flooded = true;
    c4.flooded = true;
    t.checkExpect(f.worldEnds(), new WorldEnd(true, f.lastScene("You win")));
  }

  void testIsAllFlooded(Tester t) {
    this.setBaseline(t);
    t.checkExpect(f.isAllFlooded(), false);
    c1.flooded = true;
    t.checkExpect(f.isAllFlooded(), false);
    c2.flooded = true;
    c3.flooded = true;
    c5.flooded = true;
    c6.flooded = true;
    c7.flooded = true;
    c8.flooded = true;
    c9.flooded = true;
    t.checkExpect(f.isAllFlooded(), false);
    c4.flooded = true;
    t.checkExpect(f.isAllFlooded(), true);
  }

  boolean testSameCellLocApply(Tester t) {
    this.setBaseline(t);
    SameCellLoc pred = new SameCellLoc();
    return t.checkExpect(pred.apply(c1, 15, 15), true)
        && t.checkExpect(pred.apply(c1, 15, 40), true)
        && t.checkExpect(pred.apply(c1, 40, 15), true)
        && t.checkExpect(pred.apply(c3, 15, 15), false);
  }

  void testFind(Tester t) {
    this.setBaseline(t);
    SameCellLoc pred = new SameCellLoc();
    ArrayUtils a = new ArrayUtils();
    t.checkExpect(a.find(f.board, pred, 80, 103), 8);
    t.checkExpect(a.find(f.board, pred, 35, 20), 0);
  }

  void testFindHelp(Tester t) {
    this.setBaseline(t);
    SameCellLoc pred = new SameCellLoc();
    ArrayUtils a = new ArrayUtils();
    t.checkExpect(a.findHelp(f.board, pred, 8, 80, 103), 8);
    t.checkExpect(a.findHelp(f.board, pred, 0, 35, 20), 0);
    t.checkExpect(a.findHelp(f.board, pred, 6, 80, 103), a.findHelp(f.board, pred, 7, 80, 103));
    t.checkExpect(a.findHelp(f.board, pred, 10, 80, 103), -1);
  }

  void testFloodNeighbors(Tester t) {
    this.setBaseline(t);
    f.makeBoard();
    ArrayUtils a = new ArrayUtils();
    ArrayList<Cell> al = new ArrayList<Cell>();
    ArrayList<Cell> al2 = new ArrayList<Cell>();
    a.floodNeighbors(al2, Color.blue);
    t.checkExpect(c1.flooded, true);
    al.add(c1);
    a.floodNeighbors(al, Color.magenta);
    t.checkExpect(c1.flooded, true);
    f.onTick();
    t.checkExpect(c1.color, Color.magenta);
  }

  void testGame(Tester t) {
    World f = new FloodItWorld().makeBoard();
    f.bigBang(500, 500, 0.1);
  }
}

