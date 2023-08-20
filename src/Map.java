import java.io.*;
import java.util.Scanner;

/*
 * Reads and contains in memory the map of the game.
 */
public class Map {

    /* Representation of the map */
    public char[][] map;

    /* Map name */
    public String mapName;

    /* Gold required for the human player to win */
    public int goldRequired;


    // Default constructor, creates the default map "Very small Labyrinth of doom".
    public Map() {
        mapName = "Very small Labyrinth of Doom";
        goldRequired = 2;
        map = new char[][]{
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'E', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', 'E', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };
    }


    public Map(String fileName) {
        readMap(fileName);
    }

    // method used for reading custom maps
    public void readMap(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line1 = br.readLine();
            String line2 = br.readLine();

            // omit the "name" out of the string and assign map name
            mapName = line1.replace("name", "");

            // get the int gold required
            goldRequired = Integer.parseInt(line2.replace("win ", ""));

            System.out.println("Name:" + mapName);
            System.out.println("Gold to win: " + goldRequired);

            int linesInMap = getLines() - 2;

            map = new char[linesInMap][];
            for (int i = 0; i < linesInMap; i++) {
                map[i] = br.readLine().toCharArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception!");
        }
    }

    /*
    very useful method that returns the total number of lines in a txt file
    taken from: https://www.programiz.com/java-programming/examples/count-lines-in-file
     */
    public int getLines() {
        int count = 0;

        try {
            // create a new file object
            File file = new File("sampleMap.txt");

            // create an object of Scanner
            // associated with the file
            Scanner sc = new Scanner(file);

            // read each line and
            // count number of lines
            while (sc.hasNextLine()) {
                sc.nextLine();
                count++;
            }

            // close scanner
            sc.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return count;
    }


    // accessors
    public String getMapName() {
        return mapName;
    }

    public int getGoldRequired() {
        return goldRequired;
    }

    // returns the item at a given coordinate
    public char getItemAtCoordinate(int[] coordinates) {
        return map[coordinates[0]][coordinates[1]];
    }

    // returns map width
    public int getMapWidth() {
        return map.length;
    }

    //returns map length
    public int getMapLength() {
        return map[0].length;
    }

    /*
    Remove item at the coordinate
    And replace with a '.'
     */
    public void removeItemAtCoordinate(int[] coordinates) {
        map[coordinates[0]][coordinates[1]] = '.';
    }

    /*
    Displays the entire map
    Not accessible to the player during game, but useful for testing/debugging
     */
    public void displayEntireMap(int[] playerCoordinates, int[] botCoordinates) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i == playerCoordinates[0] && j == playerCoordinates[1]) {
                    System.out.print("P ");
                } else if (i == botCoordinates[0] && j == botCoordinates[1]) {
                    System.out.print("B ");
                } else {
                    System.out.print(map[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    // Used to load a 5x5 surrounding map to botMapMemory after bot uses look command
    public char[][] displayMapForBot(int[] coordinates) {
        char[][] map5x5 = new char[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                try {
                    map5x5[i][j] = map[coordinates[0] - 2 + i][coordinates[1] - 2 + j];
                }
                // Area outside map is represented as #'s
                catch (ArrayIndexOutOfBoundsException e) {
                    map5x5[i][j] = '#';
                }
            }
        }
        map5x5[2][2] = 'B';
        return map5x5;
    }

    // Show the surrounding 5x5 area to human player
    public void display5x5Map(int[] coordinates) {
        char[][] map5x5 = new char[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                try {
                    map5x5[i][j] = map[coordinates[0] - 2 + i][coordinates[1] - 2 + j];
                }
                // Area outside map is represented as #'s
                catch (ArrayIndexOutOfBoundsException e) {
                    map5x5[i][j] = '#';
                }
            }
        }
        map5x5[2][2] = 'P';
        for (int i = 0; i < map5x5.length; i++) {
            for (int j = 0; j < map5x5[i].length; j++) {
                System.out.print(map5x5[i][j] + " ");
            }
            System.out.println();
        }
    }

}