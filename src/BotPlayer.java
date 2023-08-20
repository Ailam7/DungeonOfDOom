import java.util.Random;


//Contains code required for the implementation of a bot player

public class BotPlayer {

    int backwardMove; // used to reduce likelihood of a backward move

    /*
    Get a random command to move the bot
    Used if gold has not been spotted by the bot with the look command
    Modified to make it less likely for the bot to make a "Backward" move
    ie Move N and then Move S back to initial position
     */
    public String getRandomBotCommand(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(4);
        if (randomNumber == backwardMove){
            randomNumber = rand.nextInt(4);
        }
        String command;
        switch (randomNumber){
            case 0:
                command = "MOVE N";
                backwardMove = 1;
                break;
            case 1:
                command = "MOVE S";
                backwardMove = 0;
                break;
            case 2:
                command = "MOVE E";
                backwardMove = 3;
                break;
            case 3:
                command = "MOVE W";
                backwardMove = 4;
                break;
            default:
                command = " ";
        }

        return command;
    }


    /*
    Returns the coordinates of the specified character in a 2d char array
    Used by bot to get coordinate of item in the 5x5 map after looking
     */
    public int[] getCoordinateOfItem(char[][] array, char item) {
        int []coordinates = new int[2];
        for (int i=0; i<array.length; i++) {
            for (int j=0; j<array.length; j++) {
                if (array[i][j] == item) {
                    coordinates[0] = i;
                    coordinates[1] = j;
                }
            }
        }
        return coordinates;
    }

    /*
    Checks whether a character is present in a 2d char array
    Used by bot to check if gold is in the 5x5 map after look command
     */
    public boolean checkIfCharInArray(char[][] array, char item){
        boolean contains = false;
        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array[i].length; j++){
                if (array[i][j] == item){
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }



}

