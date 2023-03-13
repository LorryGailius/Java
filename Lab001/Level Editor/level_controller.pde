public class LevelController {
  ArrayList<Object> os;

  public LevelController() {
  }

  public void loadMap(String filePath, PImage[] tiles, ArrayList<Object> objects) {
    objects.clear();
    String[] lines = loadStrings(filePath);

    for (int row = 0; row < lines.length; row++) {
      String[] values = split(lines[row], ",");
      for (int col = 0; col < values.length; col++) {
        switch(values[col]) {
        case "1":
          {
            Object o = new Object(tiles[1], 1);
            o.centerX = 32 + 64*col;
            o.centerY = 32 + 64*row;
            objects.add(o);
            break;
          }
        case "2":
          {
            Object o = new Object(tiles[2], 2);
            o.centerX = 32 + 64*col;
            o.centerY = 32 + 64*row;
            objects.add(o);
            break;
          }
        case "3":
          {
            Object o = new Object(tiles[3], 3);
            o.centerX = 32 + 64*col;
            o.centerY = 32 + 64*row;
            objects.add(o);
            break;
          }
        case "4":
          {
            Object o = new Object(tiles[4], 4);
            o.centerX = 32 + 64*col;
            o.centerY = 32 + 64*row;
            objects.add(o);
            break;
          }
        case "5":
          {
            Object o = new Object(tiles[5], 5);
            o.centerX = 32 + 64*col;
            o.centerY = 32 + 64*row;
            objects.add(o);
            break;
          }
        }
      }
    }
  }

  public void saveMap(String filePath, ArrayList<Object> objects) {
    this.saveMap(filePath, objects, "Save");
  }


  public void saveMap(String filePath, ArrayList<Object> objects, String message) {
    ArrayList<ArrayList<Integer>> map = new ArrayList<>();
    os = objects;

    if (filePath != null) {
      int maxRow = 0;
      int maxCol = 0;
      for (Object o : objects) {
        int row = o.centerY / 64;
        int col = o.centerX / 64;
        maxRow = Math.max(maxRow, row);
        maxCol = Math.max(maxCol, col);
      }

      for (int i = 0; i <= maxRow; i++) {
        ArrayList<Integer> rowList = new ArrayList<>();
        for (int j = 0; j <= maxCol; j++) {
          rowList.add(0);
        }
        map.add(rowList);
      }

      for (Object o : objects) {
        int row = o.centerY / 64;
        int col = o.centerX / 64;
        map.get(row).set(col, o.id);
      }


      PrintWriter output = createWriter(filePath);
      for (int i = 0; i < map.size(); i++) {
        ArrayList<Integer> row = map.get(i);
        for (int j = 0; j < row.size(); j++) {
          output.print(row.get(j) + ",");
        }
        output.println();
      }
      output.flush();
      output.close();
      return;
    }
    selectInput(message, "selected", null, this);
    return;
  }


  public void selected(File selection) {
    if (selection == null) {
      println("Window was closed or the user hit cancel.");
    } else {
      if (selection.getAbsolutePath().endsWith(".csv")) {
        this.saveMap(selection.getAbsolutePath(), this.os);
      }
    }
  }

  public void display(ArrayList<Object> objects) {
    for (Object o : objects) {
      o.display();
    }
  }
}
