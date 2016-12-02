# Zoinks, a Starcraft AI bot 
Zoinks is a Starcraft Broodwar AI bot. 

This is the final project for CSC-568 Artificial Intellgience. It is a collective group project by Brian DiZio, Yan Shi, Andreas Elterich, Ethan Poll, Maryeliz Kelleher, Deana Fedaie, and Ryan Plucknett. 

## Project requirements 
### Bot Requirements 
+ Our bot should be able to gather resources for both minerals and vespene gas. 
+ Ability to place buildings for construction. 
+ Execution of at least one build order. 
+ Ability to construct worker and combat units. 
+ Micromanagement of units. 
+ Being able to build and use tier 1 units for each faction: 
 + Zerg: Drones, zerglings, and hydralisks 
+ Being able to build and use some tier 2 units for each faction: 
 + Zerg: Lurkers OR Mutalisks 
 
### Deliverable Requirements 
+ All deliverables should be available on Github. Required: 
 + Clean, documented code with appropriate class decomposition. 
+ High-level overview of approach (~500 words in the README in the base repository) 
 + Technical summary of your argent, including a system diagram, explanation of all major classes, and a time-based guide as to what the agent does over time (also in the README) 

## High Level Overview 
Our goal was to implement a simple decision tree with weights to allow the bot to make decisions based on the current environment as enabled by Perfect Information™.

Our bot is capable of gathering minerals, extracting gas, constructing buildings, and spawning workers and attack units. Our bot is set to carry out the 5-Pool build order in the beginning, so that we can take advantage of the early-game potential and advantages that Zergs have. The build order is explained below in a later section. Our goal with the 5-Pool is weaken the opponent's economy by primarily attacking their workers and their buildings--this is also known as a Zerg Rush. A 5-Pool is not as risky as a 4-Pool, in that we are capable of keeping our own economy balanced and we still have drones gathering minerals and obtaining needed resources for later advancements. 

If the game does not end at that point, we continue with our plan of constructing buildings and advancing through to our tech tree. 

## Class Breakdown 


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

