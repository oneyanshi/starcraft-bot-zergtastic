# Zoinks, a Starcraft AI bot 
Zoinks is a Starcraft Broodwar AI bot. 

This is the final project for CSC-568 Artificial Intellgience. It is a collective group project by Brian DiZio, Yan Shi, Andreas Elterich, Ethan Poll, Maryeliz Kelleher, Deana Fedaie, and Ryan Plucknett. 

The full GitHub Repo is available here https://github.com/oneyanshi/starcraft-bot-zergtastic

## Point Justification
We gather both minerals and gas
We are able to place buildings using a concentric square to find locations
We are able to build a Spawning Pool, Extractor, and a Hydralisk Den
Our Drones are constructed at multiple times during the game and Zerglings are constructed 
Finally there are attack orders that specify the correct targets 
Our full order is below but it progresses from Zerglings to Hydralisks to mutalisks
We also have fully commented code and believe our README is up to snuff


## High Level Overview 
Our goal was to implement a simple decision tree with weights to allow the bot to make decisions based on the current environment as enabled by having perfect information. 

Our bot is capable of gathering minerals, extracting gas, constructing buildings, and spawning workers and attack units. Our bot is set to carry out a strategy known as a 5-Pool build order in the beginning, so that we can take advantage of the early-game potential and advantages that Zergs have. The build order is explained below in a later section. A 5-pool is a build where you build a spawning pool nearly immediately but not as soon as possible. The decision tree will have an alternate path that chooses a more defensive strategy if the enemy looks like they will have a faster rush or quicker defenses than our rush can manage. We anticipate most of the opponents to choose a slower build. Our goal with this fast strategy is weaken the opponent's economy by primarily attacking their workers and their buildings--this is also known as a Zerg Rush.

The Zerg specific resource we need to account for is control. Zerglings, Drones, and buildings consume control and overlords produce it. Initially we have 4 control, the fastest version of a rush strategy, a 4-pool goes right into a spawning pool as soon as enough minerals are generated. A 5-Pool is not as risky as a 4-Pool, in that we can keep our own economy balanced by maintaining a drone gathering minerals and obtaining needed resources as we’re spawning in Zerglings. As soon as we have six they attack. We use the extra resources to spawn another Overlord and four additional Zerglings to reinforce our initial attack. During this early game period we also work towards an extractor. 

If the game does not end at that point, we do a tech switch and begin moving to an economy strategy. If we went with the 4-pool this shift would be much more challenging but because we went with the slightly more conservative strategy and will be carefully managing our control this will work. After the extractor is done we're able to upgrade the spawning pool to a Hydralisk den. We hope to outpace our opponent in reaching our 2nd tier unit because of the damage we inflict early game, even if we're not able to effectively eliminate them. Hydralisks have the advantage of being more versatile and less micro focused than Zerg's other 2nd tier units. By the mid game we expect to attack with Zerglings and Hydralisks. The Hydralisks will allow us to break through the intensive defenses we would expect to be built to counter our earlier rush. Then we hope to finish the game before reaching late game when Zergs have less of an advantage.

 
## Major Classes
We work mainly within the mainFrame(), creating methods if needed and when appropriate. The main class is ZoinksZergBot.java, and similar files withiin the src > bot > java folder were test files in which we were figuring out the best build order or the best way to initialize a build order. 

## Build Orders 
Our build order can also be found in this [Google Doc](https://docs.google.com/document/d/1e05FzKy5A5DllCp2OfijMiIUobjpTRM2mPlKQ5jJYNk/edit). 

Generally speaking, the build order we follow in the beginning is what is known as a *5 Pool*. This name comes from when the spawing pool is built, when there is both 5 control (unit population) and the requisite resources are available.

There are 4 drones provided at the start of the game, starting the control value at 4/9. 
Immediately build a 5th drone, while gathering minerals toward the 200 necessary to build a spawning pool. 
When 200 minerals is available, use one drone to build a spawning pool, the necessary building to create zerglings. This brings control to 4/9.
While this building is being constructed, have the 4 other drones continue gathering minerals. This should allow for two additional drones to be spawned in the time it takes for the spawning pool to finish. The reason for these two drones and the drone at the beginning is so that if the rush fails, there are enough drones available to continue effectively building economy. After these drones, control should be 6/9.
Immediately when the spawining pool is finished, use the three available larvae at the hatchery to create 6 zerglings. Control should be 9/9, puting it at its current max. 
When there are enough minerals and an available larva, spawn another overlord while the zerglings are being made. This increases the control limit, to create more units. 
After the zerglings hatch, immediately send them to the enemy base. This will take some time.
When the overlord spawns, and there is 100 minerals and 2 larvae available, start on 4 additional zerglings. These will be the reinforcements for the initial 6, in an attempt to solidify the initial rush into a victory. 
When the first 6 zerglings arrive, focus on the enemy starting units if they have any, like other zerglings, marines, or zealots, depending on the race. Following that, attack the initial offensive buildings, like spawning pools, barracks', or gateways. Afterwards, focus on their worker units and kill all of them to make sure they dont produce anything else. At this point, victory has been achieved, and there is only their remaining buildings to destroy. 

In the case that the rush fails, there is a back up plan. This is the reason we used a 5 pool, as opposed to a 4 pool. We can transition into another build from here, hopefully either with an advantage or equal footing because of the rush. 


+ 5/9: Build another drone 
+ 4/9: Build a spawning pool 
+ 5/9: Build a drone while the spawning pool is building 
+ 6/9: Build another drone while the spawning pool is building 
+ 9/9: Use 3 larvae to build 6 zerglings to rush the enemy base and attack workers/buildings to weaken their economy 

+ **IF ENDGAME:**
+ WIN. 

+ **IF NOT ENDGAME, CONTINUE:** 
+ 9/17: Build Overlord 2 to increase control (100 minerals+, have larvae) 
+ 11/17: Make 4 Zerglings for reinforcements for the first wave 
+ 12/17: Build a drone  
+ 11/17: Build extractor 
+ 10/17: Build a drone 
