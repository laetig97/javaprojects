import tester.Tester;

// An IEntertainment is a:
// - Magazine
// - TVSeries
// - Podcast

interface IEntertainment {

  // computes price of yearly subscription
  double totalPrice();

  // computes minutes of entertainment
  int duration();

  // produces a String showing the name and price of this entertainment
  String format();

  // determines whether the this entertainment is the same as the given one
  boolean sameEntertainment(IEntertainment that);

  // are the Podcasts the same?
  boolean samePodcast(Podcast that);

  // are the TV series the same?
  boolean sameTVSeries(TVSeries that);

  // are the mMagazines the same?
  boolean sameMagazine(Magazine that);

}

abstract class AEntertainment implements IEntertainment {

  String name;
  double price;
  int installments;

  AEntertainment(String name, double price, int installments) {

    this.name = name;
    this.price = price;
    this.installments = installments;

    /*-
     * Fields:
     * 
     * this.name...String
     * this.price...double 
     * this.installments...int
     * 
     * Methods: 
     * totalPrice() -> double 
     * duration() -> int format() -> String
     * sameEntertainment(AEntertainment that) -> boolean
     */
  }

  // computes the price of a yearly subscription
  public double totalPrice() {
    return this.price * this.installments;
  }

  // computes the minutes of entertainment (includes all installments)
  public int duration() {
    return 50 * this.installments;
  }

  // produces a String showing the name and price of this entertainment
  public String format() {
    return this.name + ", " + Double.toString(this.price) + ".";
  }
}

class Magazine extends AEntertainment {

  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {

    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  /*-
   * Fields:
   * 
   * this.name...String 
   * this.price...double 
   * this.genre...String 
   * this.pages...int
   * this.installments...int
   * 
   * Methods:
   * totalPrice() -> double 
   * duration() -> int 
   * format() -> String
   * sameEntertainment(AEntertainment that) -> boolean 
   * sameMagazine(Magazine that)-> boolean 
   * samePodcast(Podcast that)-> boolean 
   * sameTVSeries(TVSeries that)-> boolean
   */

  // computes minutes of entertainment in a magazine
  public int duration() {
    return (5 * this.pages) * this.installments;
  }

  // checks if given Magazine is the same as this Magazine
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMagazine(this);
  }

  // checks if given Magazine is same Magazine
  public boolean sameMagazine(Magazine that) {
    return this.name == that.name && this.price == that.price && this.genre == that.genre
        && this.pages == that.pages && this.installments == that.installments;
  }

  public boolean samePodcast(Podcast that) {
    return false;
  }

  public boolean sameTVSeries(TVSeries that) {
    return false;
  }
}

class TVSeries extends AEntertainment {

  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {

    super(name, price, installments);
    this.corporation = corporation;

  }

  /*-
   * Fields:
   * 
   * this.name...String this.price...double 
   * this.installments...int
   * this.corporation...String
   * 
   * Methods:
   * 
   * totalPrice() -> double duration() -> int 
   * format() -> String
   * sameEntertainment(AEntertainment that) -> boolean 
   * sameTVSeries(TVSeries that)-> boolean samePodcast(Podcast that)-> boolean 
   * sameMagazine(Magazine that)-> boolean
   * 
   */

  // checks if given TVSeries is the same as this TVSeries
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVSeries(this);
  }

  // checks if given TVSeries is same as TVSeries
  public boolean sameTVSeries(TVSeries that) {
    return this.name == that.name && this.price == that.price
        && this.installments == that.installments && this.corporation == that.corporation;

  }

  public boolean samePodcast(Podcast that) {
    return false;
  }

  public boolean sameMagazine(Magazine that) {
    return false;
  }
}

class Podcast extends AEntertainment {

  Podcast(String name, double price, int installments) {
    super(name, price, installments);

  }

  /*-
   * Fields:
   * 
   * this.name...String 
   * this.price...double 
   * this.installments...int
   * 
   * Methods: 
   * totalPrice() -> double 
   * duration() -> int 
   * format() -> String
   * sameEntertainment(AEntertainment that) -> boolean 
   * samePodcast(Podcast that)-> boolean 
   * sameTVSeries(TVSeries that)-> boolean 
   * sameMagazine(Magazine that)-> boolean
   */

  // checks if given Podcast is the same as this Podcast
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }

  // checks if given podcast is the same as podcast
  public boolean samePodcast(Podcast that) {
    return this.name == that.name && this.price == that.price
        && this.installments == that.installments;
  }

  public boolean sameTVSeries(TVSeries that) {
    return false;
  }

  public boolean sameMagazine(Magazine that) {
    return false;
  }
}

class ExamplesEntertainment {
  Magazine rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  TVSeries houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  Podcast serial = new Podcast("Serial", 0.0, 8);
  Magazine vogue = new Magazine("Vogue", 4.0, "Fashion", 35, 10);
  TVSeries gameOfThrones = new TVSeries("Game of Thrones", 2.99, 63, "HBO");
  Podcast americanScandal = new Podcast("American Scandal", 1.0, 4);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.vogue.totalPrice(), 4.00 * 10, .0001)
        && t.checkInexact(this.gameOfThrones.totalPrice(), 2.99 * 63, .0001)
        && t.checkInexact(this.americanScandal.totalPrice(), 1.00 * 4, .0001);
  }

  // testing duration method
  boolean testduration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 3600)
        && t.checkExpect(this.houseOfCards.duration(), 650)
        && t.checkExpect(this.serial.duration(), 400);
  }

  // testing format method
  boolean testformat(Tester t) {
    return t.checkExpect(this.vogue.format(), "Vogue, 4.0.")
        && t.checkExpect(this.gameOfThrones.format(), "Game of Thrones, 2.99.")
        && t.checkExpect(this.americanScandal.format(), "American Scandal, 1.0.");
  }

  // testing sameEntertainment method
  boolean testsameEntertainment(Tester t) {
    return t.checkExpect(this.rollingStone.sameEntertainment(this.vogue), false)
        && t.checkExpect(this.rollingStone.sameEntertainment(this.serial), false)
        && t.checkExpect(this.gameOfThrones.sameEntertainment(this.houseOfCards), false)
        && t.checkExpect(this.americanScandal.sameEntertainment(serial), false)
        && t.checkExpect(this.serial.sameEntertainment(this.vogue), false)
        && t.checkExpect(this.serial.sameEntertainment(this.serial), true)
        && t.checkExpect(this.vogue.sameEntertainment(this.vogue), true)
        && t.checkExpect(this.gameOfThrones.sameEntertainment(this.gameOfThrones), true)
        && t.checkExpect(this.serial.sameEntertainment(this.serial), true);
  }

  // testing helper method samePodcast
  boolean testsamePodcast(Tester t) {
    return t.checkExpect(this.serial.samePodcast(this.serial), true)
        && t.checkExpect(this.serial.samePodcast(this.americanScandal), false);
  }

  // testing helper method sameTVSeries
  boolean testsameTVSeries(Tester t) {
    return t.checkExpect(this.gameOfThrones.sameTVSeries(this.gameOfThrones), true)
        && t.checkExpect(this.gameOfThrones.sameTVSeries(this.houseOfCards), false);
  }

  // testing helper method sameMagazine
  boolean testsameMagazine(Tester t) {
    return t.checkExpect(this.vogue.sameMagazine(this.vogue), true)
        && t.checkExpect(this.vogue.sameMagazine(this.rollingStone), false);
  }
}