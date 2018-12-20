import tester.Tester;

/* An XMLFrag is one of:
 - plainText String)
 - tag [Listof XMLFrag])
 */
interface IXMLFrag {


  // computes the length (number of characters) of the content in an XML document
  int contentLength();

  // helper function which seperates a tag from a PlainText
  boolean hasTag(String t);

  // determines if an XML document contains an attribute with the given name
  boolean hasAttribute(String a);

  //determines if an XML document contains an attribute with the given name within a given tag
  boolean hasAttributeInTag(String a, String t);

  // converts XML to a String without tags and attributes
  String renderAsString();

  // updates all attributes with the given name to the given value.
  IXMLFrag updateAttribute(String a, String v);

}

class Plaintext implements IXMLFrag {

  String txt;

  /* Fields:
   * txt...String
   * 
   * Methods:
   * contentLength() -> int
   * hasTag(String t) -> boolean
   * hasAttribute(String a) -> boolean
   * renderAsString() -> String
   * updateAttribute(String a, String v) -> IXMLFrag
   */

  Plaintext(String txt) {

    this.txt = txt;
  }

  // returns number of characters in PlainText
  public int contentLength() {
    return this.txt.length();

  }


  // returns false if the String is a PlainText
  public boolean hasTag(String t) {
    return false;
  }

  // returns false if the String is a PlainText
  public boolean hasAttribute(String a) {
    return false;
  }

  // returns false if the String is a PlainText
  public boolean hasAttributeInTag(String a, String t) {
    return false;
  }

  // returns the PlainText how it is
  public String renderAsString() {
    return this.txt;
  }

  // returns regular PlainText
  public IXMLFrag updateAttribute(String a, String v) {
    return this;
  }
}

class Tagged implements IXMLFrag {

  Tag tag;
  ILoXMLFrag content;

  /* Fields:
   * tag...Tag
   * content...ILoXMLFrag
   * 
   * Methods:
   * contentLength() -> int
   * hasTag(String t) -> boolean
   * hasAttribute(String a) -> boolean
   * renderAsString() -> String
   * updateAttribute(String a, String v) -> IXMLFrag
   */

  Tagged(Tag tag, ILoXMLFrag content) {
    this.tag = tag;
    this.content = content;
  }

  // Sends content to list
  public int contentLength() {
    return this.content.contentLength();
  }

  //Either sends to Tag class or sends content to ILoXML
  public boolean hasTag(String t) {
    return this.tag.tagHelp(t) || this.content.hasTag(t);
  }

  // Either sends to Tag class or sends content to ILoXML
  public boolean hasAttribute(String a) {
    return this.tag.hasAttribute(a) || this.content.hasAttribute(a);
  }

  // Either sends to Tag class or sends content to ILoXML
  public boolean hasAttributeInTag(String a, String t) {
    return this.tag.hasAttributeInTag(a,t) || this.content.hasAttributeInTag(a,t);
  }

  // sends content to ILoXMLFrag
  public String renderAsString() {
    return this.content.renderAsString();
  }

  // creates new tagged with updated attribute if applicable
  public IXMLFrag updateAttribute(String a, String v) {
    return new Tagged(this.tag.updateAttribute(a,v), this.content.updateAttribute(a, v));
  }

}


interface ILoXMLFrag {

  int contentLength();

  boolean hasTag(String t);

  boolean hasAttribute(String a);

  boolean hasAttributeInTag(String a, String t);

  String renderAsString();

  ILoXMLFrag updateAttribute(String a, String v);

}

class MtLoXMLFrag implements ILoXMLFrag {

  // returns 0 if statement is empty
  public int contentLength() {
    return 0;
  }

  // returns false if statement is empty
  public boolean hasTag(String t) {
    return false;
  }

  // returns false if statement is empty
  public boolean hasAttribute(String a) {
    return false;
  }

  // returns false if statement is empty
  public boolean hasAttributeInTag(String a, String t) {
    return false;
  }

  // returns a blank string if it is empty
  public String renderAsString() {
    return "";
  }

  // returns empty list
  public ILoXMLFrag updateAttribute(String a, String v) {
    return this;
  }
}

class ConsLoXMLFrag implements ILoXMLFrag {

  IXMLFrag first;
  ILoXMLFrag rest;

  /* Fields:
   * this.first...IXMLFrag
   * this.rest...ILoXMLFrag
   * Methods:
   * contentLength() -> int
   * hasTag(String t) -> boolean
   * hasAttribute(String a) -> boolean
   * hasAttributeInTag(String a, String b) -> boolean
   * renderAsString() -> String
   * updateAttribute(String a, String v) -> ILoXMLFrag
   */


  ConsLoXMLFrag(IXMLFrag first, ILoXMLFrag rest) {

    this.first = first;
    this.rest = rest;

  }

  // adds the first length to the rest of the lengths
  public int contentLength() {
    return (this.first.contentLength() + this.rest.contentLength());
  }

  //determines if an XML document contains a Tag with the given name.
  public boolean hasTag(String t) {
    if (this.first.hasTag(t)) {
      return true;
    }
    else {
      return this.rest.hasTag(t);
    }
  }

  //determines if an XML document contains an Attribute w/ given name.
  public boolean hasAttribute(String a) {
    if (this.first.hasAttribute(a)) {
      return true;      
    }
    else {
      return this.rest.hasAttribute(a);
    }
  }

  // checks if first has attribute in tag or sends rest
  public boolean hasAttributeInTag(String a, String t) {
    if (this.first.hasAttributeInTag(a, t)) {
      return true;
    }
    else {
      return this.rest.hasAttributeInTag(a, t);
    }
  }

  // adds on other PlainText to a final string
  public String renderAsString() {
    return this.first.renderAsString() + this.rest.renderAsString();
  }
  
  //returns new updated list if applicable
  public ILoXMLFrag updateAttribute(String a, String v) {
    return new ConsLoXMLFrag(this.first.updateAttribute(a, v), 
        this.rest.updateAttribute(a, v));
  }



}

class Tag {
  String name;
  ILoAtt atts;

  /* Fields:
   * name...String
   * atts...ILoAtt
   * 
   * Methods:
   * contentLength() -> int
   * hasTag(String t) -> boolean
   * hasAttribute(String a) -> boolean
   * hasAttributeInTag(String a, String b) -> boolean
   * updateAttribute(String a, String v) -> Tag
   */

  Tag(String name, ILoAtt atts) {
    
    this.name = name;
    this.atts = atts;
  } 

  int contentLength() {
    return 0;
  }

  // checks if a given tag is the same as one in a XML
  public boolean tagHelp(String t) {
    return (this.name.equals(t));

  }

  //sends list of attributes 
  public boolean hasAttribute(String a) {
    return this.atts.hasAttribute(a); 
  }

  // checks if tag name is the same as given name
  public boolean hasAttributeInTag(String a, String t) {
    if (this.name.equals(t)) {
      return this.atts.hasAttributeInTag(a,t);
    }
    else {
      return false;      
    }
  }

  // creates new tag with new attribute value if applicable
  public Tag updateAttribute(String a, String t) {
    return new Tag(this.name, this.atts.updateAttribute(a,t));
  }


}


interface ILoAtt { 

  //check if it is an attribute/matches string
  boolean hasAttribute(String a);

  //check if an attribute matches the given attribute
  boolean hasAttributeInTag(String a, String t);

  ILoAtt updateAttribute(String a, String v);
}

class MtLoAtt implements ILoAtt {

  // returns false if attribute class is empty
  public boolean hasAttribute(String a) {
    return false;
  }

  // returns false if attribute class is empty
  public boolean hasAttributeInTag(String a, String t) {
    return false;
  }

  // returns empty list if given one
  public ILoAtt updateAttribute(String a, String v) {
    return this;
  }
}


class ConsLoAtt implements ILoAtt {
  Att first;
  ILoAtt rest;

  /*Fields:
   * this.first...Att
   * this.rest...ILoAtt
   * Methods:
   * hasAttribute(String a) -> boolean
   * updateAttribute(String a, String v) -> ILoAtt
   */


  ConsLoAtt(Att first, ILoAtt rest) {
    this.first = first;
    this.rest = rest;
  }


  // checks for first attribute matching string or sends to rest
  public boolean hasAttribute(String a) {
    if (this.first.attHelp(a)) {
      return true;
    }

    else {
      return this.rest.hasAttribute(a);
    }
  }


  // sends to helper to determine if attribute is same
  public boolean hasAttributeInTag(String a, String t) {
    if (this.first.hasAttributeInTag(a,t)) {
      return true;
    }
    else {
      return this.rest.hasAttributeInTag(a, t);

    }

  }

  // checks if this.att is same as given att
  public ILoAtt updateAttribute(String a, String v) {
    if (this.first.attHelp(a)) {
      return new ConsLoAtt(this.first.updateAttribute(a, v), this.rest.updateAttribute(a, v));
    }
    else {
      return new ConsLoAtt(this.first, this.rest.updateAttribute(a, v));
    }
  }
}

class Att {

  String name;
  String value;

  /* Fields:
   * name...String
   * value...String
   * Methods:
   * attHelp(String a) -> boolean
   * hasAttributeInTag(String a, String t) -> boolean
   * updateAttribute(String a, String v) -> Att
   */

  Att(String name, String value) {
    this.name = name;
    this.value = value;
  }

  // checks if given att name is same as this.name
  public boolean attHelp(String a) {
    return this.name.equals(a);
  }
  
  // checks if attribute name is same as given name in tag
  public boolean hasAttributeInTag(String a, String t) {
    return this.name.equals(a);

  }
  
  // changes attribute with given name to a new value
  public Att updateAttribute(String a, String v) {
    return new Att(this.name, v);
  }


}

// Examples of an XML
class ExamplesXML {

  ExamplesXML() {}

  Tag yell = new Tag("yell", new MtLoAtt()); 
  Tag italic = new Tag("italic", new MtLoAtt());
  Att volume = new Att("volume", "30db");
  Att newVolume = new Att("volume", "70db");
  Tag yellatt = new Tag("yell", new ConsLoAtt(this.volume, new MtLoAtt()));
  Tag newYellAtt = new Tag("yell", new ConsLoAtt(this.newVolume, new MtLoAtt()));
  Att duration = new Att("duration", "5sec");
  Tag yvd = new Tag("yell", new ConsLoAtt(this.volume, 
      new ConsLoAtt(this.duration, new MtLoAtt())));
  Plaintext iAm = new Plaintext("I am ");
  Plaintext XML = new Plaintext("XML");
  Plaintext exclamation = new Plaintext("!");
  Tagged one = new Tagged(this.yell, new ConsLoXMLFrag(this.XML, new MtLoXMLFrag()));
  Tagged two = new Tagged(this.italic, new ConsLoXMLFrag(new Plaintext("X"), new MtLoXMLFrag()));
  Tagged both = new Tagged(this.yell,
      new ConsLoXMLFrag(this.two, new ConsLoXMLFrag(new Plaintext("ML"), new MtLoXMLFrag())));
  Tagged third = new Tagged(this.yellatt, 
      new ConsLoXMLFrag(this.two, new ConsLoXMLFrag(new Plaintext("ML"), new MtLoXMLFrag())));
  Tagged updateThird = new Tagged(this.newYellAtt, 
      new ConsLoXMLFrag(this.two, new ConsLoXMLFrag(new Plaintext("ML"), new MtLoXMLFrag())));
  Tagged fourth = new Tagged(this.yvd, 
      new ConsLoXMLFrag(this.two, new ConsLoXMLFrag(new Plaintext("ML"), new MtLoXMLFrag())));
  ConsLoXMLFrag xml1 = new ConsLoXMLFrag(new Plaintext("I am XML!"), new MtLoXMLFrag());
  ConsLoXMLFrag xml2 = new ConsLoXMLFrag(this.iAm, 
      new ConsLoXMLFrag(this.one, new ConsLoXMLFrag(this.exclamation, new MtLoXMLFrag())));
  ConsLoXMLFrag xml3 = new ConsLoXMLFrag(this.iAm, 
      new ConsLoXMLFrag(this.both, new ConsLoXMLFrag(this.exclamation, new MtLoXMLFrag())));
  ConsLoXMLFrag xml4 = new ConsLoXMLFrag(this.iAm, 
      new ConsLoXMLFrag(this.third, new ConsLoXMLFrag(this.exclamation, new MtLoXMLFrag())));
  ConsLoXMLFrag xml5 = new ConsLoXMLFrag(this.iAm,
      new ConsLoXMLFrag(this.fourth, new ConsLoXMLFrag(this.exclamation, new MtLoXMLFrag())));



  // tests contentLength() Method  
  boolean testcontentLength(Tester t) {
    return t.checkExpect(this.XML.contentLength(), 3) 
        && t.checkExpect(this.exclamation.contentLength(), 1)
        && t.checkExpect(this.one.contentLength(), 3) 
        && t.checkExpect(this.yell.contentLength(), 0) 
        && t.checkExpect(this.xml1.contentLength(), 9) 
        && t.checkExpect(this.xml5.contentLength(), 9);
  }

  // tests hasTag method 
  boolean testhasTag(Tester t) {
    return  t.checkExpect(this.xml2.hasTag("yell"), true) 
        && t.checkExpect(this.xml2.hasTag("italic"), false) 
        && t.checkExpect(this.xml1.hasTag("yell"), false)
        && t.checkExpect(this.xml3.hasTag("italic"), true)
        && t.checkExpect(this.xml4.hasTag("hi"), false)
        && t.checkExpect(this.xml4.hasTag("italic"), true);
  }

  // tests hasAttribute method
  boolean testhasAttribute(Tester t) {
    return t.checkExpect(this.xml1.hasAttribute("volume"), false)
        && t.checkExpect(this.xml4.hasAttribute("volume"), true) 
        && t.checkExpect(this.xml4.hasAttribute("duration"), false) 
        && t.checkExpect(this.xml5.hasAttribute("attitude"), false);
  }

  // tests hasAttributeInTag method
  boolean testhasAttributeInTag(Tester t) {
    return t.checkExpect(this.xml1.hasAttributeInTag("volume", "yell"), false) 
        && t.checkExpect(this.xml2.hasAttributeInTag("volume", "yell"), false) 
        && t.checkExpect(this.xml4.hasAttributeInTag("volume", "yell"), true)
        && t.checkExpect(this.xml5.hasAttributeInTag("duration", "yell"), true);

  }

  //tests renderAsString method
  boolean testrenderAsString(Tester t) {
    return t.checkExpect(this.xml1.renderAsString(), ("I am XML!"))
        && t.checkExpect(this.xml2.renderAsString(), ("I am XML!"))
        && t.checkExpect(this.xml3.renderAsString(), ("I am XML!"));
  }

  //tests updateAttribute method
  boolean testupdateAttribute(Tester t) {
    return t.checkExpect(this.xml4.updateAttribute("volume", "70db"),
        new ConsLoXMLFrag(this.iAm, new ConsLoXMLFrag(this.updateThird, 
            new ConsLoXMLFrag(this.exclamation, new MtLoXMLFrag()))));

  }
}







