package index;

import java.util.*;


public class KyrixRow {
  double cx, cy, minx, miny, maxx, maxy;
  ArrayList<String> colData;
  

  protected KyrixRow(ArrayList<String> columnNames, ArrayList<String> columnData, boolean isCitus) {
    int minxIndex = -1;
    int minyIndex = -1;

    // will go the columnNames then [cx, cy, minx, miny, maxx, maxy, geom]
    // for example, [id, x, y, cx, cy, minx, miny, maxx, maxy, geom]

    minxIndex = (columnNames.size() - 1) + 3;
    minyIndex = minxIndex + 1;

    assert(minxIndex > 0);
    assert(minyIndex > 0);
    this.minx = Double.parseDouble(columnData.get(minxIndex));
    this.miny = Double.parseDouble(columnData.get(minyIndex));
    this.colData = columnData;
  }

  public ArrayList<String> getColumnData() {
    return this.colData;
  }

  public double getMinx() {
    return this.minx;
  }

  public double getMiny() {
    return this.miny;
  }
  
  @Override
  public String toString() {
    String result = "minx: " + this.minx + " miny: " + this.miny + "\n";
    return result;
  }
}
