# Dungeon of Doom

This is a text based adventure game that I programmed as part of my coursework for the module Principles of Programming 1, of my BSc Computer Science Course at the University of Bath.

<br>

## Players

Each game consists of 2 players; the human controlled player and a bot. The human player must collect gold and escape the map. The bot also tries to loot the gold, so that the human player can't collect enough gold to win the game.

<br>

## The Map

The game takes place on a 2d rectangular map. The walls of the map are represented by a '**#**'. Neither the player nor the bot can move through walls.

The rest of the tiles on the map are represented by the '**.**'. If a tile does not contain a player, a bot, gold, or an exit, it will be represented by a full stop.

Apart from being a wall, or an empty tile there are 4 other entities/items that could be on a tile, they are:

- P: The Player
- B: The Bot
- G: Gold
- E: An Exit

<br>

## Commands

On any given turn, both the player and the bot can use one of the below commands. Turn is lost after any command is used

- **MOVE (N, E, W, S):** Move in any direction by one tile. Valid as long as player is not moving into a wall.

- **LOOK:** Player does not have any vision of the map by default. If he wishes to have a look, he can use the look command to check a 5x5 radius from his current position, to see if there is any gold nearby or if the bot is close

- **GOLD:** Displays the amount of gold the player has collected

- **HELLO:** Displays the amount of gold required to win the game (different for each map)

- **PICKUP:** If the player is standing on the same tile as a gold, he can pick it up using the pickup command.

- **QUIT:** If the player is at an exit tile while having the required number of gold, he can exit the dungeon and win the game. If this command is used while not on an exit tile and not having the required number of gold, the player loses.

- **PICKUP:** If the player is standing on the same tile as a gold, he can pick it up using the pickup command.

<br>

## Rules and Game end states

On every turn, should they decide to move, both the player and the bot can move one tile, in any direction: Up, down, left, or right. A move is invalid if a player tries to move into a wall.

Each map comes loaded with the amount of gold the player will have to collect to win the game.

**Game end states**

- The QUIT command directly triggers a game end state. The player wins if he executes this command while having the required amount of gold and standing on an exit tile on the map. This is the only way for the player to win the game.

- The player loses the game if he executes the EXIT command while not fulfilling the above 2 conditions (not enough gold collected or not on an exit tile)

- If the bot and the player end up on the same tile after a turn, the player has been captured and has lost the game.

- The bot will also try to loot the gold. If the bot collects enough gold such that the player can no longer reach the win target, the player loses

<br>

## Uploading Custom Maps

The program allwos users to upload their own custom maps. All maps are a txt file, and they must be rectangular with the outer boundaries covered by hashtags (walls). Each tile can either be a wall, a normal tile, gold, or an exit. Note that the player and bot positions cannot be predetermined into the map, as their spawn positions is generated at random at the start of a game.

The first line of the txt file must contain the name of the map. The second line contains the number of gold required to win it, and the rest of the lines are the map itself. Please make sure all custom maps follow this format, or else the program will reject it and start with the default map. The map should be uploaded into the src directory of the program. There are 2 maps: samplemap, and bigmap, along with the default map that is included in the program. You can choose any of these maps or a custom map by entering the name of the map with the .txt extension onto the console when printed.

**At this point you know everything you need to play the game. The next two subheadings of the README just describe the thought process behind the algorithms that power the behavior of the bot.**

## Bot Behavior

We were given a lot of freedom in the course specification to modify the behavior of the bot. The only condition was: Tha bot not be omnipresent. ie it shouldn't have knowledge of the map. The only way for the bot to know anything about the map was executing the look command itself and analyze the 5:5 area surrounding its current position.

The approach I took was I made the Bot have 2 states. A gold spotted state (player was in the 5x5 area when bot used the look command) and a default state. On the default state, the bot moves at random, and after everyone 3 moves, it uses the look command.

After using the look command, if the bot spots gold, it triggers the gold spotted case. I used a simple algorithm, which checks which quadrant of the 5x5 area the gold is in, and moves accordingly until it reaches the gold and picks it up. If the 5x5 area does not have any walls, the bot is able to reach the gold guaranteed. The limitation of this algorithm is that it doesn't take walls into account. It'd be stupid if the bot continuosly kept banging against the wall, so I opted to return the bot back to the default state should it bump into a wall in this case, where it would again start to move at random. Due to time constraints I was unable to optimize the algorithm to make the bot bypass the walls in this situation.

<br>

## Bot Testing

I used python's random library to trigger the bots random movements in the default state. The random function in the library uses a system called psuedo random number. I needed to check how the bot's movement was when moving at random over a large number of turns. It covered most tiles in the map in a reasonable number of moves.

The algorithm used for the movement of the bot was very effective in maps with little to no walls except on the boundary, such as the default map. The bot was able to grab the gold quite effectively, in about 15 moves on average by my testing.

The algorithm is less effective in maze like maps with a lot of walls, but the bot was still able to collect gold in a reasonable number of turns to keep the game challenging.
