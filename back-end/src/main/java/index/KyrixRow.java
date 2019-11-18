package index;

import java.util.*;


public class KyrixRow {
  double cx, cy, minx, miny, maxx, maxy;  

  protected KyrixRow(ArrayList<Double> bboxData) {
    int minxIndex = -1;
    int minyIndex = -1;

    // bbox data is [cx, cy, minx, miny, maxx, maxy]

    assert(bboxData.size() == 6);

    minxIndex = 2;
    minyIndex = minxIndex + 1;

    assert(minxIndex > 0);
    assert(minyIndex > 0);
    this.minx = bboxData.get(minxIndex);
    this.miny = bboxData.get(minyIndex);
  }

  public double getMinx() {
    return this.minx;
  }

  public double getMiny() {
    return this.miny;
  }

  public long getZOrderValue() {
    int minxA = Math.toIntExact(Math.round(this.getMinx()));
    int minyA = Math.toIntExact(Math.round(this.getMiny()));
    String minxABinary = Integer.toBinaryString(minxA);
    String minyABinary = Integer.toBinaryString(minyA);
    String interleavedBitStringA = interleaveBitStrings(minxABinary, minyABinary); 
    long interleavedBinaryA = Long.parseLong(interleavedBitStringA, 2);
    return interleavedBinaryA;


    // int minxB = Math.toIntExact(Math.round(b.getMinx()));
    // int minyB = Math.toIntExact(Math.round(b.getMiny()));
    // String minxBBinary = Integer.toBinaryString(minxB);
    // String minyBBinary = Integer.toBinaryString(minyB);
    // String interleavedBitStringB = interleaveBitStrings(minxBBinary, minyBBinary);
    // long interleavedBinaryB = Long.parseLong(interleavedBitStringB, 2);
    
    // long difference =  interleavedBinaryA - interleavedBinaryB;
    // if(difference > 0) {
    //   return 1;
    // } else if (difference == 0) {
    //   return 0;
    // } else {
    //   return -1;
    // }
  }

  private String interleaveBitStrings(String a, String b) {
    boolean equalLength = a.length() == b.length();
    boolean aLonger = a.length() > b.length();
    int stopLength = aLonger ? b.length() : a.length();
    StringBuilder interleavedBits = new StringBuilder();
    // if lenA > lenB, then we want to use the lenB because it is the shorter, and vice versa
    for(int i = 0; i < stopLength; i++) {
      interleavedBits.append(a.charAt(i));
      interleavedBits.append(b.charAt(i));
    }

    if (equalLength) {
      if (aLonger) {
        interleavedBits.append(a.substring(stopLength));
      } else {
        interleavedBits.append(b.substring(stopLength));
      }
    }
    
    String interleavedBitString = interleavedBits.toString();
    return interleavedBitString;
  }
  
  @Override
  public String toString() {
    String result = "minx: " + this.minx + " miny: " + this.miny + "\n";
    return result;
  }
}
