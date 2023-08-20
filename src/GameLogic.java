import java.util.Arrays;
import java.util.Random;

public class GameLogic {

    private HumanPlayer humanPlayer;
    private BotPlayer botPlayer;
    private Map map;


    //fields
    private int[] playerCoordinates;
    private int[] botCoordinates;
    private int goldCollected;
    private int goldAvailable;
    private int[] newCoordinates; // used to check if a move is valid

    // variables used by bot to chase gold
    int lookCounter = 0;
    boolean targetSpotted = false;
    char[][] botMapMemory;


    public GameLogic() {
        humanPlayer = new HumanPlayer();
        botPlayer = new BotPlayer();
        loadMapFromFile();
    }

    // method to accept a map from a file
    private void loadMapFromFile(){
        System.out.println("\nIf you'd like a custom map, enter name of the text file here. (eg: mymap.txt)");
        System.out.println("Make sure map format is consistent with sample maps.");
        System.out.println("Leave blank for default map!");

        String filename = humanPlayer.getUserInput();
        if (filename.isEmpty()){
            map = new Map();
            System.out.println("Default " + map.getMapName() + " created!");
        } else if (filename.contains(".txt")){
            try {
                map = new Map(filename);
                System.out.println(map.getMapName() + " created!");
            }
            catch (Exception e){
                System.out.println("Oops. Exception!");
                map = new Map();
            }
        } else {
            map = new Map();
        }
    }

     /*
     Generate random spawn coordinates on any tile on the map that isn't a wall or gold
     Can be used for both human and bot
      */
    private int[] spawnPlayer(){
        while (true) {
            Random random = new Random();

            int xPlayer = random.nextInt(map.getMapWidth());
            int yPlayer = random.nextInt(map.getMapLength());
            int[] spawnCoordinates = {xPlayer, yPlayer};
            // can't spawn in a wall or on gold
            if (map.getItemAtCoordinate(spawnCoordinates) != '#' &&
                    map.getItemAtCoordinate(spawnCoordinates) != 'G') {
                return spawnCoordinates;
            }
        }
    }

    // check if a move is valid (ie not moving into a wall)
    private boolean isMoveValid(int[] coordinates){
        return map.getItemAtCoordinate(coordinates) != '#';
    }

    // update human player coordinates after a move
    private String updatePlayerCoordinates(int[] coordinates){
        if (isMoveValid(coordinates)){
            playerCoordinates = coordinates;
            return "MOVE SUCCESSFUL";
        } else {
            return "MOVE FAILED";
        }
    }

    // update bot coordinates after a move
    private void updateBotCoordinates(int[] coordinates){
        if (isMoveValid(coordinates)){
            botCoordinates = coordinates;
        } else {
            targetSpotted = false; // so that bot stops trying to ram into wall
        }
    }

    /*
    method for human player to pickup gold
    executed with PICKUP command
     */
    private void tryToPickupGold(int[] coordinates){
        if (map.getItemAtCoordinate(coordinates) == 'G'){
            goldCollected ++;
            map.removeItemAtCoordinate(coordinates);
            System.out.println("MOVE SUCCESSFUL");
        } else {
            System.out.println("MOVE FAILED");
        }
    }

    // check if user met win conditions when the QUIT command was entered
    private void showGameResult(){
        if (map.getItemAtCoordinate(playerCoordinates) == 'E' && goldCollected >= map.getGoldRequired()){
            System.out.println("Congratulations! You Won!");
        } else {
            System.out.println("Unfortunate! You Lost!");
        }
        System.exit(0);
    }

    // executes relevant actions relating to commands given by human player
    private void executeHumanCommand(String command){
        switch (command) {
            case "LOOK":
                // show map 5x5
                map.display5x5Map(playerCoordinates);
                break;
            case "PICKUP":
                tryToPickupGold(playerCoordinates);
                break;
            case "HELP":
                humanPlayer.showCommands();
                break;
            case "HELLO":
                System.out.println("Gold required to win: " + map.getGoldRequired());
                break;
            case "QUIT":
                showGameResult();
                break;
            case "GOLD":
                System.out.println("Gold Collected: " + goldCollected);
                break;
            case "MOVE N":
                newCoordinates = new int[] {playerCoordinates[0] - 1, playerCoordinates[1]};
                System.out.println(updatePlayerCoordinates(newCoordinates));
                break;
            case "MOVE S":
                newCoordinates = new int[] {playerCoordinates[0] + 1, playerCoordinates[1]};
                System.out.println(updatePlayerCoordinates(newCoordinates));
                break;
            case "MOVE E":
                newCoordinates = new int[] {playerCoordinates[0], playerCoordinates[1] + 1};
                System.out.println(updatePlayerCoordinates(newCoordinates));
                break;
            case "MOVE W":
                newCoordinates = new int[] {playerCoordinates[0], playerCoordinates[1] - 1};
                System.out.println(updatePlayerCoordinates(newCoordinates));
                break;
            default:
                System.out.println("Invalid Input");
        }
    }


    // execute relevant actions relating to command relayed by bot
    private void executeBotCommand(String botCommand){
        switch (botCommand){
            case "MOVE N":
                newCoordinates = new int[] {botCoordinates[0] - 1, botCoordinates[1]};
                updateBotCoordinates(newCoordinates);
                break;
            case "MOVE S":
                newCoordinates = new int[] {botCoordinates[0] + 1, botCoordinates[1]};
                updateBotCoordinates(newCoordinates);
                break;
            case "MOVE E":
                newCoordinates = new int[] {botCoordinates[0], botCoordinates[1] + 1};
                updateBotCoordinates(newCoordinates);
                break;
            case "MOVE W":
                newCoordinates = new int[] {botCoordinates[0], botCoordinates[1] - 1};
                updateBotCoordinates(newCoordinates);
                break;
            case "PICKUP":
                goldAvailable --;
                map.removeItemAtCoordinate(botCoordinates);
                System.out.println("Bot has taken some of your precious gold!");
                break;
            case "LOOK":
                botMapMemory = map.displayMapForBot(botCoordinates);
                if (botPlayer.checkIfCharInArray(botMapMemory, 'G')){
                    targetSpotted = true;
                }
                break;
            default:
                System.out.println("Invalid Input");
        }
    }

    // check if bot and player are on the same coordinates (player has lost the game in that case)
    private void checkIfBotCaughtPlayer(){
        if (Arrays.equals(playerCoordinates, botCoordinates)){
            System.out.println("Oh no! The bot has caught you! It starts tearing you to pieces!");
            System.out.println("Game Over! You Lose!");
            System.exit(0);
        }
    }

    // check if enough gold remains in map for player to meet win condition
    private void checkIfEnoughGoldRemains(){
        if(goldAvailable < map.getGoldRequired()){
            System.out.println("You lose! The bot looted enough gold such that you can no longer reach win condition!");
            System.exit(0);
        }
    }

    /*
    Bot algorithm to move towards the gold
    Executes if the bot spotted a gold after calling look function
    Algorithm checks which quadrant of the botMapMemory the gold is in and moves accordingly
    If no wall present between gold and bot, bot will capture the gold with this algorithm
    Extremely effective if map is small and has few or no walls apart from border (default map).
    Works fairly well even otherwise
     */
    private String getCalculatedBotCommand(){
        botMapMemory = map.displayMapForBot(botCoordinates);
        String command = "";
        int[] itemCoordinates = botPlayer.getCoordinateOfItem(botMapMemory, 'G');
        int row = itemCoordinates[0];
        int col = itemCoordinates[1];

        if (row >= 0 && row <= 1 && col >= 0 && col <= 2) {
            command = "MOVE N";
        } else if (row >= 0 && row <= 2 && col >= 3 && col <= 4) {
            command = "MOVE E";
        } else if (row >= 3 && row <= 4 && col >= 2 && col <= 4) {
            command = "MOVE S";
        } else if (row >= 0 && row <= 2 && col >= 0 && col <= 1) {
            command = "MOVE W";
        } else {
            command = botPlayer.getRandomBotCommand();
            targetSpotted = false;
        }
        return command;
    }

    // Method to run game
    public void runGame() {

        playerCoordinates = spawnPlayer();
        botCoordinates = spawnPlayer();
        goldAvailable = map.getGoldRequired();
        goldCollected = 0;

        humanPlayer.passOpeningMessage();

        while (true) {
            // Disable below comment to see full map after each move. Useful for testing and debugging.
            // map.displayEntireMap(playerCoordinates, botCoordinates);

            // Human player's turn
            System.out.println("Enter Command: ");
            String command = humanPlayer.getUserInput();
            executeHumanCommand(command.toUpperCase());

            // Bots turn. below method decides which command bot chooses
            String botCommand;
            if (map.getItemAtCoordinate(botCoordinates) == 'G'){
                botCommand = "PICKUP";
                targetSpotted = false;
            } else if (lookCounter == 3){
                botCommand = "LOOK";
                // System.out.println("Bot has looked"); Again useful for debugging. See when bot uses look command
                lookCounter = 0; // reset look counter
            } else if (targetSpotted){
                botCommand = getCalculatedBotCommand(); // from algorithm
            } else {
                botCommand = botPlayer.getRandomBotCommand(); // get a random command
                lookCounter ++;
            }
            executeBotCommand(botCommand);

            // Both are game ending states. human player loses if either is true
            checkIfBotCaughtPlayer();
            checkIfEnoughGoldRemains();
        }
    }

    /*
    Main method
    Program written by Ailam
    For my programming 1 module final coursework
    Enjoy wandering my dungeon of doom :-)
     */
    public static void main(String[] args) {
        GameLogic logic = new GameLogic();
        logic.runGame();
    }

}

