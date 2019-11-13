package index;

import java.util.Comparator;
import java.math.*;

public class SortByZOrder implements Comparator<KyrixRow> {

  public int compare(KyrixRow a, KyrixRow b) {
    int minxA = Math.toIntExact(Math.round(a.getMinx()));
    int minyA = Math.toIntExact(Math.round(a.getMiny()));
    String minxABinary = Integer.toBinaryString(minxA);
    String minyABinary = Integer.toBinaryString(minyA);
    String interleavedBitStringA = interleaveBitStrings(minxABinary, minyABinary); 
    long interleavedBinaryA = Long.parseLong(interleavedBitStringA, 2);

    int minxB = Math.toIntExact(Math.round(b.getMinx()));
    int minyB = Math.toIntExact(Math.round(b.getMiny()));
    String minxBBinary = Integer.toBinaryString(minxB);
    String minyBBinary = Integer.toBinaryString(minyB);
    String interleavedBitStringB = interleaveBitStrings(minxBBinary, minyBBinary);
    long interleavedBinaryB = Long.parseLong(interleavedBitStringB, 2);
    
    long difference =  interleavedBinaryA - interleavedBinaryB;
    if(difference > 0) {
      return 1;
    } else if (difference == 0) {
      return 0;
    } else {
      return -1;
    }
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
}