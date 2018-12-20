import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Comparator;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//class Deque
class Deque<T> {

  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {

    this.header = header;
  }

  /*
   * Fields:
   * 
   * this.header ... Sentinel<T>
   * 
   * this.size() ... int this.addAtHead(T) ... void this.addAtTail(T) ... void
   * this.removeFromHead() ... T this.removeFromTail() ... T
   * this.find(IPred<T>)... ANode<T> this.removeNode(ANode<T>) ... void
   */

  // counts the number of nodes in a list Deque,
  // not including the header node
  int size() {
    return this.header.size();
  }

  // consumes a value of type T and inserts it at the front of the list
  void addAtHead(T t) {
    this.header.addAtHead(t);
  }

  // consumes a value of type T and inserts it at the tail of this list.
  void addAtTail(T t) {
    this.header.addAtTail(t);
  }

  // removes the first node from this Deque
  T removeFromHead() {
    return this.header.removeFromHead();
  }

  // removes the last node from this Deque
  T removeFromTail() {
    return this.header.removeFromTail();
  }

  // removes the given node from this Deque
  void removeNode(ANode<T> n) {
    n.removeNodeHelp();
  }

}

// ANode<T>, either a Sentinel<T> node or an actual Node<T>
abstract class ANode<T> {

  ANode<T> next;
  ANode<T> prev;

  /*
   * Fields:
   * 
   * this.next ... ANode<T> this.prev ... ANode<T>
   * 
   * Methods: this.sizeHelper()...int this.removeHelp()...T
   * this.findHelp(IPred<T>)...ANode<T> this.removeNodeHelp()...void
   */

  abstract int sizeHelper();

  abstract T removeHelp();

  abstract void removeNodeHelp();
}

// Node class has data and inherits ANode<T>
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    this(data);
    this.next = next;
    this.prev = prev;

    if (next == null || prev == null) {
      throw new IllegalArgumentException("null cannot be added");
    }
    else {
      next.prev = this;
      prev.next = this;
    }

    /*
     * Fields: this.data ... T this.next ... ANode<T> this.prev ... ANode<T>
     * 
     * Methods: this.sizeHelper() ... int this.removeHelp() ... T
     * this.findHelp(IPred<T>) ... ANode<T> this.removeNodeHelp() ... void
     */

  }

  int sizeHelper() {
    return 1 + this.next.sizeHelper();
  }

  T removeHelp() {
    prev.next = this.next;
    next.prev = this.prev;
    return this.data;

  }

  void removeNodeHelp() {
    this.removeHelp();

  }
}

// class Sentinel
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.next = this;
    this.prev = this;

  }

  /*
   * Fields: this.next ... Sentinel<T> this.prev ... Sentinel<T>
   * 
   * Methods: this.size() ... int this.sizeHelper()...int this.addAtHead(T) ...
   * void this.addAtTail(T) ... void this.removeFromHead() ... T
   * this.removeFromeTail() ... T this.removeHelp() ... T this.find(IPred<T>) ...
   * ANode<T> this.findHelp(IPred<T>) ... ANode<T> this.removeNodeHelp() ... void
   */

  int size() {
    return this.next.sizeHelper();
  }

  int sizeHelper() {
    return 0;
  }

  public void addAtHead(T t) {
    new Node<T>(t, this.next, this);
  }

  public void addAtTail(T t) {
    new Node<T>(t, this, this.prev);

  }

  public T removeFromHead() {
    return this.next.removeHelp();
  }

  public T removeFromTail() {
    return this.prev.removeHelp();
  }

  T removeHelp() {
    throw new RuntimeException("Cannot remove from an empty list");
  }

  void removeNodeHelp() {
    return;

  }

}

interface ICollection<T> {
  // adds an item to this collection
  void add(T item);

  // removes an item from this collection
  T remove();

  // counts the number of items in this collection
  int size();
}

class Queue<T> implements ICollection<T> {
  Deque<T> items;

  Queue() {
    this.items = new Deque<T>();
  }

  @Override
  public void add(T item) {
    this.items.addAtTail(item);
  }

  @Override
  public T remove() {
    return this.items.removeFromHead();
  }

  @Override
  public int size() {
    return this.items.size();
  }
}

class Stack<T> implements ICollection<T> {
  Deque<T> items;

  Stack() {
    this.items = new Deque<T>();
  }

  @Override
  public void add(T item) {
    this.items.addAtHead(item);
  }

  @Override
  public T remove() {
    return this.items.removeFromHead();
  }

  @Override
  public int size() {
    return this.items.size();
  }

}

// represents an edge of the game
class Edge {
  Cell from;
  Cell to;
  int weight;
  String dir;

  Edge(Cell from, Cell to, int weight, String dir) {
    this.from = from;
    this.to = to;
    this.weight = weight;
    this.dir = dir;

  }

  public Edge(Cell from, Cell to) {
    this.from = from;
    this.to = to;
    this.weight = -1;
    this.dir = "";
    /*
     * Fields: this.from...Cell this.to...Cell this.weight...int
     */
  }

  // draws the Edges of a Cell
  public void drawEdges(WorldScene canvas) {
    int cSize = this.from.size;
    int xFrom = this.from.loc.x;
    int yFrom = this.from.loc.y;
    int xTo = this.to.loc.x;
    int yTo = this.to.loc.y;
    int length = cSize;
    WorldImage connectHoriz = new RectangleImage(length, 2, OutlineMode.SOLID, Color.LIGHT_GRAY);
    WorldImage connectVert = new RectangleImage(2, length, OutlineMode.SOLID, Color.LIGHT_GRAY);
    if (this.dir.equals("top")) {
      canvas.placeImageXY(connectHoriz, xFrom + cSize / 2, yFrom);
    }
    else if (this.dir.equals("bottom")) {
      canvas.placeImageXY(connectHoriz, xTo + cSize / 2, yTo);
    }
    else if (this.dir.equals("left")) {
      canvas.placeImageXY(connectVert, xFrom, yFrom + cSize / 2);
    }
    else {
      canvas.placeImageXY(connectVert, xTo, yTo + cSize / 2);
    }
  }
}

// Represents a Cell on the game board
class Cell {
  int size;
  Posn loc;
  Cell top;
  Cell bottom;
  Cell left;
  Cell right;
  Color col;
  Boolean moveUp;
  Boolean moveDown;
  Boolean moveRight;
  Boolean moveLeft;
  Boolean player;
  Boolean visited;
  Cell previous;

  // Constructor
  Cell(int size, int x, int y) {
    this.size = size;
    this.loc = new Posn(x, y);
    this.top = null;
    this.bottom = null;
    this.left = null;
    this.right = null;
    this.col = Color.LIGHT_GRAY;
    this.moveUp = false;
    this.moveDown = false;
    this.moveRight = false;
    this.moveLeft = false;
    this.player = false;
    this.visited = false;
    this.previous = null;
  }

  Cell(int x, int y) {
    this.size = 1; // fix later
    this.loc = new Posn(x, y);
    this.top = null;
    this.bottom = null;
    this.left = null;
    this.right = null;
    this.col = Color.LIGHT_GRAY;
    this.moveUp = false;
    this.moveDown = false;
    this.moveRight = false;
    this.moveLeft = false;
    this.player = false;
    this.visited = false;
    this.previous = null;
  }

  Cell(int size, int x, int y, Color col) {
    this.size = size;
    this.loc = new Posn(x, y);
    this.top = null;
    this.bottom = null;
    this.left = null;
    this.right = null;
    this.col = col;
    this.moveUp = false;
    this.moveDown = false;
    this.moveRight = false;
    this.moveLeft = false;
    this.player = false;
    this.visited = false;
    this.previous = null;
  }

  /*
   * Fields: this.size...int this.loc...Posn this.top...Cell this.bottom...Cell
   * this.left...Cell this.right...Cell this.up...Boolean this.down...Boolean
   * this.l...Boolean this.r...Boolean this.col...Color
   * 
   * Methods: this.setLeft(Cell)->void this.setTop(Cell)->void
   * this.setRight(Cell)->void this.setBottom(Cell)->void
   * this.drawCell()->WorldImage this.drawEdges()->WorldImage
   */

  // sets a neighbor to the left of Cell
  public void setLeft(Cell neighbor) {
    this.left = neighbor;
  }

  // sets a neighbor to the Top of cell
  public void setTop(Cell neighbor) {
    this.top = neighbor;

  }

  // sets a neighbor to right of Cell
  public void setRight(Cell neighbor) {
    this.right = neighbor;
  }

  // sets a neighbor to bottom of Cell
  public void setBottom(Cell neighbor) {
    this.bottom = neighbor;
  }

  // draws a Cell
  public WorldImage drawCell() {
    return new FrameImage(new RectangleImage(this.size, this.size, OutlineMode.SOLID, this.col));
  }

  // colors the correct path through the maze
  public void colorCorrectPath() {
    this.col = Color.red;
    if (this.previous != null) {
      this.previous.colorCorrectPath();
    }
  }

}

// function that shows hashmap
class UnionFind {
  HashMap<Posn, Posn> reps;

  UnionFind(HashMap<Posn, Posn> reps) {
    this.reps = reps;

    /*
     * Fields: this.reps...HashMap<Posn, Posn>
     * 
     * Methods: this.combine(Posn,Posn)->void this.find(Posn)->Posn
     */

  }

  UnionFind() {
    this.reps = null;
  }

  void combine(Posn one, Posn two) {
    this.reps.put(this.find(one), this.find(two));
  }

  Posn find(Posn given) {
    if (given.equals(this.reps.get(given))) {
      return given;
    }
    else {
      return find(this.reps.get(given));
    }
  }

}

// Comparator class for Edge
class EdgeComparator implements Comparator<Edge> {

  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}

// Represents the Player class
class Player {
  Cell curr;

  Player(Cell curr) {
    this.curr = curr;

    /*
     * fields: this.curr...Cell
     */
  }
}

// represents the World for the Maze Game
class MazeGame extends World {

  int cellsAcross;
  int cellsDown;
  int cellSize;
  Random rand;
  UnionFind union;
  ArrayList<Edge> mazeEdges;
  ArrayList<Edge> edgeWorklist;
  ArrayList<ArrayList<Cell>> board;
  Cell start;
  Cell end;
  Player p;

  // Constructor
  MazeGame(int across, int down, int size, int rand) {
    this.cellsAcross = across;
    this.cellsDown = down;
    this.cellSize = size;
    this.rand = new Random(rand);
    this.union = new UnionFind();
    this.mazeEdges = new ArrayList<Edge>();
    this.edgeWorklist = new ArrayList<Edge>();
    this.board = new ArrayList<ArrayList<Cell>>();
    this.start = null;
    this.end = null;
    this.p = null;
  }

  MazeGame(int across, int down, int size, Random rand) {
    this.cellsAcross = across;
    this.cellsDown = down;
    this.cellSize = size;
    this.rand = rand;
    this.union = new UnionFind();
    this.mazeEdges = new ArrayList<Edge>();
    this.edgeWorklist = new ArrayList<Edge>();
    this.board = new ArrayList<ArrayList<Cell>>();
    this.start = null;
    this.end = null;
    this.p = null;
  }
  /*
   * Fields: this.cellsAcross...int this.cellsDown...int this.cellSIze...int
   * this.rand...Random this.union...UnionFind this.mazeEdges...ArrayList<Edge>
   * this.edgeWorkList...ArrayList<Edge> this.board...ArrayList<ArrayList<Cells>>
   * 
   * Methods: this.createBoard()->void
   * this.giveNeighbors(ArrayList<ArrayList<Cell>>)->void
   * this.mapCells()->HashMaps<Posn, Posn> this.createEdges()->void
   * this.setEdges()->void this.cellEdges()->void this.makeScene()->WorldScene
   * this.findPath(Cell,Cell,ICollection<Cell>) -> void this.onKeyEvent(String)->
   * void this.OnTick()->void this.lastScene(string) -> WorldScene
   * this.worldEnds() -> WorldEnd
   */

  // creates the Board
  void createBoard() {
    for (int h = 0; h < this.cellsDown; h += 1) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      this.board.add(row);
      for (int w = 0; w < this.cellsAcross; w += 1) {
        this.board.get(h).add(new Cell(this.cellSize, w * this.cellSize, h * this.cellSize));
      }
    }
    this.start = this.board.get(0).get(0);
    this.start.col = Color.GREEN;
    this.end = this.board.get(cellsDown - 1).get(cellsAcross - 1);
    this.end.col = Color.MAGENTA;
    this.giveNeighbors(this.board);
    this.setEdges();
  }

  // Arranges neighbors of each cell
  void giveNeighbors(ArrayList<ArrayList<Cell>> brd) {
    for (int h = 0; h < brd.size(); h += 1) {
      for (int w = 0; w < brd.get(h).size(); w += 1) {
        Cell current = brd.get(h).get(w);
        if (w == 0) {
          current.setLeft(current);
        }
        else {
          current.setLeft(brd.get(h).get(w - 1));
        }
        if (h == 0) {
          current.setTop(current);
        }
        else {
          current.setTop(brd.get(h - 1).get(w));
        }
        if (w == this.cellsAcross - 1) {
          current.setRight(current);
        }
        else {
          current.setRight(brd.get(h).get(w + 1));
        }
        if (h == this.cellsDown - 1) {
          current.setBottom(current);
        }
        else {
          current.setBottom(brd.get(h + 1).get(w));
        }
      }
    }
  }

  // creates the hashmap of game
  HashMap<Posn, Posn> mapCells() {
    HashMap<Posn, Posn> represents = new HashMap<Posn, Posn>();
    for (int i = 0; i < this.board.size(); i += 1) {
      for (int j = 0; j < this.board.get(i).size(); j += 1) {
        represents.put(this.board.get(i).get(j).loc, this.board.get(i).get(j).loc);
      }
    }
    return represents;
  }

  // creates and sorts the Edges of the hashmap
  void createEdges() {
    this.edgeWorklist = new ArrayList<Edge>();
    Cell current;
    for (int h = 0; h < this.board.size(); h += 1) {
      for (int w = 0; w < this.board.get(h).size(); w += 1) {
        current = this.board.get(h).get(w);
        if (h < this.board.size() && !current.bottom.equals(current)) {
          this.edgeWorklist.add(
              new Edge(current, current.bottom, this.rand.nextInt(this.cellsDown) + 1, "bottom"));
        }
        if (h < this.board.size() && !current.top.equals(current)) {
          this.edgeWorklist
              .add(new Edge(current, current.top, this.rand.nextInt(this.cellsDown) + 1, "top"));
        }
        if (w < this.board.get(h).size() && !current.right.equals(current)) {
          this.edgeWorklist.add(
              new Edge(current, current.right, this.rand.nextInt(this.cellsAcross) + 1, "right"));
        }
        if (w < this.board.get(h).size() && !current.left.equals(current)) {
          this.edgeWorklist.add(
              new Edge(current, current.left, this.rand.nextInt(this.cellsAcross) + 1, "left"));
        }
      }
    }

    Collections.sort(this.edgeWorklist, new EdgeComparator());
  }

  // sets the Edges of the game
  void setEdges() {
    this.createEdges();

    this.mazeEdges = new ArrayList<Edge>();
    this.union = new UnionFind(this.mapCells());
    int i = 0;
    int total = this.cellsAcross * this.cellsDown;
    Edge currentE;
    // i < total - 1 &&
    while (i < total - 1 && i < this.edgeWorklist.size()) {
      currentE = this.edgeWorklist.get(i);
      Posn k1 = this.union.find(currentE.from.loc);
      Posn k2 = this.union.find(currentE.to.loc);
      if (this.union.find(k1).equals(this.union.find(k2))) {
        i += 1;
      }

      else {
        this.mazeEdges.add(currentE);
        union.combine(k1, k2);
        i += 1;
        if (currentE.dir.equals("top")) {
          currentE.from.moveUp = true;
          currentE.to.moveDown = true;
        }
        else if (currentE.dir.equals("bottom")) {
          currentE.from.moveDown = true;
          currentE.to.moveUp = true;
        }
        else if (currentE.dir.equals("right")) {
          currentE.from.moveRight = true;
          currentE.to.moveLeft = true;
        }
        else {
          currentE.from.moveLeft = true;
          currentE.to.moveRight = true;
        }
      }
    }
  }

  // //colors the correct path through the maze
  // public void colorCorrectPath(Cell current) {
  // current.col = Color.red;
  // if(current.previous != null) {
  // this.colorCorrectPath(current.previous);
  // }
  // }

  // search maze
  public void findPath(Cell s, Cell e, ICollection<Cell> worklist) {
    ArrayList<Cell> seen = new ArrayList<Cell>();
    worklist.add(s);
    Cell prev = null;
    Cell current = s;
    while (worklist.size() > 0 && !current.equals(e)) {
      current = worklist.remove();
      if (seen.contains(current)) {
        // do nothing
      }
      else {
        if (current.moveLeft) {
          worklist.add(current.left);
        }
        if (current.moveDown) {
          worklist.add(current.bottom);
        }
        if (current.moveRight) {
          worklist.add(current.right);
        }
        if (current.moveUp) {
          worklist.add(current.top);
        }
        seen.add(current);
        current.col = Color.yellow;
        current.visited = true;
        current.previous = prev;
        prev = current;
      }
    }
    e.previous = prev;
    // e.colorCorrectPath();
  }

  // on key event functionality
  public void onKeyEvent(String k) {
    // left arrow press
    if (k.equals("left")) {
      if (this.p != null) {
        if (this.p.curr.moveLeft) {
          this.p.curr.visited = true;
          this.p.curr.col = Color.yellow;
          this.p.curr = this.p.curr.left;
          this.p.curr.col = Color.blue;
        }
      }
    }
    // right arrow press
    if (k.equals("right")) {
      if (this.p != null) {
        if (this.p.curr.moveRight) {
          this.p.curr.visited = true;
          this.p.curr.col = Color.yellow;
          this.p.curr = this.p.curr.right;
          this.p.curr.col = Color.blue;
        }
      }
    }
    // up arrow press
    if (k.equals("up")) {
      if (this.p != null) {
        if (this.p.curr.moveUp) {
          this.p.curr.visited = true;
          this.p.curr.col = Color.yellow;
          this.p.curr = this.p.curr.top;
          this.p.curr.col = Color.blue;
        }
      }
    }
    // down arrow press
    if (k.equals("down")) {
      if (this.p != null) {
        if (this.p.curr.moveDown) {
          this.p.curr.visited = true;
          this.p.curr.col = Color.yellow;
          this.p.curr = this.p.curr.bottom;
          this.p.curr.col = Color.blue;
        }
      }
    }
    // pressing p will start a player run maze
    if (k.equalsIgnoreCase("p")) {
      this.p = new Player(this.start);
      this.p.curr.col = Color.blue;
    }
    // pressing d will start a depth first solve
    if (k.equalsIgnoreCase("d")) {
      this.findPath(this.start, this.end, new Stack<Cell>());
    }
    // pressing b will start a breadth first solve
    if (k.equalsIgnoreCase("b")) {
      this.findPath(this.start, this.end, new Queue<Cell>());
    }
    // new random maze
    if (k.equalsIgnoreCase("n")) {
      this.rand = new Random();
      this.union = new UnionFind();
      this.mazeEdges = new ArrayList<Edge>();
      this.edgeWorklist = new ArrayList<Edge>();
      this.board = new ArrayList<ArrayList<Cell>>();
      this.createBoard();
      this.setEdges();
    }
  }
  

  // draws the Scene
  public WorldScene makeScene() {
    WorldScene canvas = getEmptyScene();
    Cell current;
    for (int h = 0; h < this.board.size(); h += 1) {
      for (int w = 0; w < this.board.get(h).size(); w += 1) {
        current = this.board.get(h).get(w);
        canvas.placeImageXY(current.drawCell(), current.loc.x + this.cellSize / 2,
            current.loc.y + this.cellSize / 2);
      }
    }
    for (Edge e : this.mazeEdges) {
      e.drawEdges(canvas);
    }
    return canvas;
  }

  // creates the end scene
  public WorldScene lastScene(String s) {
    WorldScene endScene = this.makeScene();
    // endScene.colorCorrectPath
    endScene.placeImageXY(new TextImage(s, 20, Color.red), 250, 250);
    return endScene;
  }

  // ends the world
  public WorldEnd worldEnds() {
    if (this.p != null) {
      if (this.p.curr.equals(end)) {
        return new WorldEnd(true, this.lastScene("Solved"));
      }
      return new WorldEnd(false, this.makeScene());
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

}

// Examples && Tests
class Examples {

  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;
  Cell cell5;

  /** Deque tests */
  Sentinel<String> s1;
  Sentinel<String> s2;
  Node<String> n1;
  Node<String> n2;
  Node<String> n3;
  Node<String> n4;
  Deque<String> deque1;
  Deque<String> deque2;

  void initDeque() {

    s1 = new Sentinel<String>();
    s2 = new Sentinel<String>();
    n1 = new Node<String>("abc", s1, s2);
    n2 = new Node<String>("bcd", s2, n1);
    n3 = new Node<String>("cde", s2, n2);
    n4 = new Node<String>("def", s2, n3);

    deque1 = new Deque<String>();
    deque2 = new Deque<String>(this.s2);

  }

  void testSize(Tester t) {

    this.initDeque();
    t.checkExpect(this.s1.size(), 0);
    t.checkExpect(this.s2.size(), 4);
    t.checkExpect(this.deque1.size(), 0);
    t.checkExpect(this.deque2.size(), 4);

  }

  void testAddAtHead(Tester t) {
    initDeque();
    t.checkExpect(this.s2.next, this.n1);
    t.checkExpect(this.s1.next, this.s1);

    initDeque();
    this.deque1.addAtHead("def");
    this.deque1.addAtHead("cde");
    this.deque1.addAtHead("bcd");
    this.deque1.addAtHead("abc");
    t.checkExpect(deque1, deque2);
  }

  void testAddAtTail(Tester t) {
    initDeque();
    this.deque2.addAtTail("abc");
    this.deque2.addAtTail("bcd");
    this.deque2.addAtTail("cde");
    this.deque2.addAtTail("def");
    t.checkExpect(this.deque2, this.deque2);

  }

  void testRemoveNode(Tester t) {
    initDeque();
    this.deque2.removeNode(n2);
    t.checkExpect(this.deque2.header.next.next, this.n3);
  }

  /** game tests */
  void setCells() {

    cell1 = new Cell(12, 13);
    cell2 = new Cell(12, 12);
    cell3 = new Cell(12, 11);
    cell4 = new Cell(13, 12);
    cell5 = new Cell(11, 12);

  }

  // test setLeft
  void testsetLeft(Tester t) {
    this.setCells();
    t.checkExpect(cell2.left == null, true);
    cell2.setLeft(cell5);
    t.checkExpect(cell2.left == cell5, true);
  }

  // test setRight
  void testsetRight(Tester t) {
    this.setCells();
    t.checkExpect(cell2.right == null, true);
    cell2.setRight(cell5);
    t.checkExpect(cell2.right == cell5, true);
  }

  // test setBottom
  void testsetBottom(Tester t) {
    this.setCells();
    t.checkExpect(cell2.bottom == null, true);
    cell2.setBottom(cell5);
    t.checkExpect(cell2.bottom == cell5, true);
  }

  // test setTop
  void testsetTop(Tester t) {
    this.setCells();
    t.checkExpect(cell2.top == null, true);
    cell2.setTop(cell5);
    t.checkExpect(cell2.top == cell5, true);
  }

  // test Draw Cell method with cell
  void testDrawCellandEdges(Tester t) {
    WorldImage box = new FrameImage(new RectangleImage(1, 1, OutlineMode.SOLID, Color.LIGHT_GRAY));
    Cell c1 = new Cell(1, 2, 2);
    t.checkExpect(c1.drawCell(), box);
  }

  // tests that a board has been created and that the neighbors have been
  // assigned
  void testCreateBoard(Tester t) {
    MazeGame m = new MazeGame(4, 4, 1, 1);
    t.checkExpect(m.board, new ArrayList<ArrayList<Cell>>());
    m.createBoard();
    t.checkExpect(m.board.size(), 4);
    t.checkExpect(m.board.get(3).size(), 4);
    t.checkExpect(m.board.get(1).get(2).left, m.board.get(1).get(1));
    t.checkExpect(m.board.get(0).get(3).bottom, m.board.get(1).get(3));
    t.checkExpect(m.board.get(0).get(3).top, m.board.get(0).get(3));
    t.checkExpect(m.board.get(1).get(1).right, m.board.get(1).get(2));
    t.checkExpect(m.board.get(0).get(1).right, m.board.get(0).get(2));
  }

  // test the hashmapping
  void testMapCellsandCombine(Tester t) {
    MazeGame m = new MazeGame(4, 4, 1, 1);
    m.createBoard();
    HashMap<Posn, Posn> hm = m.mapCells();
    t.checkExpect(hm.get(new Posn(1, 1)), new Posn(1, 1));
    t.checkExpect(hm.get(new Posn(5, 1)), null);
    t.checkExpect(hm.get(new Posn(1, 0)), new Posn(1, 0));
    MazeGame n = new MazeGame(4, 4, 1, 1);
    n.createBoard();
    UnionFind union = new UnionFind(n.mapCells());
    union.combine(new Posn(1, 1), new Posn(1, 0));
    t.checkExpect(union.reps.get(new Posn(1, 1)), new Posn(1, 0));
  }

  // test createEdges
  void testCreateEdges(Tester t) {
    MazeGame m = new MazeGame(3, 3, 1, 1);
    m.createBoard();
    m.createEdges();
    t.checkExpect(m.edgeWorklist.size(), 24);
  }

  // test setEdges
  void testSetEdges(Tester t) {
    MazeGame m = new MazeGame(4, 4, 1, 1);
    m.createBoard();
    t.checkExpect(m.equals(m), true);
  }

  // Tests on tick method
  void testOnTick(Tester t) {
    this.setCells();
    MazeGame m = new MazeGame(3, 3, 1, 1);
    m.createBoard();
    m.onTick();
  }

  // Tests on key event method
  void testOnKeyEvent(Tester t) {
    this.setCells();
    MazeGame m = new MazeGame(3, 3, 1, 1);
    m.createBoard();
    m.p = new Player(new Cell(0, 0));
    m.onKeyEvent("n");
    t.checkExpect(m.p.curr.loc.equals(new Posn(0, 0)), true);
    m.onKeyEvent("left");
    t.checkExpect(m.p.curr.loc.equals(new Posn(0, 0)), true);
    m.onKeyEvent("up");
    t.checkExpect(m.p.curr.loc.equals(new Posn(0, 0)), true);

  }

  void testWorldEnds(Tester t) {
    this.setCells();
    MazeGame m = new MazeGame(3, 3, 1, 1);
    m.createBoard();
    // t.checkExpect(m.worldEnds(), new WorldEnd(false, this.makeScene()));
  }

  // tests Game

  void testGame(Tester t) {
    MazeGame f = new MazeGame(03, 3, 20, 1);
    f.createBoard();
    f.bigBang(1000, 1000, 0.1);
  }
}
