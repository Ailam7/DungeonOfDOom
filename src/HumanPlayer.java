import java.util.Scanner;

public class HumanPlayer {

    // A lovely totally not frightening welcome to the game
    public void passOpeningMessage(){
        System.out.println("\nYou are a brave fortune hunter, delving deep into places no human ever dared to go before!");
        System.out.println("Within it lies nothing but horror and despair, treachery and misery!");
        System.out.println("The bots within it seek your blood! As well as your desired gold!");
        System.out.println("But is all that worth the bountiful treasure? Lying deep in the dungeon of doom?\n");
    }

    // used whenever input is required from the user
    public String getUserInput(){
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    // prints all commands accepted in the game
    public void showCommands(){
        String[] possibleCommands = new String[] {"HELP", "HELLO", "GOLD", "PICKUP", "LOOK", "QUIT", "MOVE N/E/W/S"};
        System.out.println("Commands accepted:");
        for (String possibleCommand : possibleCommands) {
            System.out.println(possibleCommand);
        }
        System.out.println("\n");
    }

}