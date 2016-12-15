# Zoinks, a Starcraft AI bot 
Zoinks is a Starcraft Broodwar AI bot. 

This is the final project for CSC-568 Artificial Intellgience. It is a collective group project by Brian DiZio, Yan Shi, Andreas Elterich, Ethan Poll, Maryeliz Kelleher, Deana Fedaie, and Ryan Plucknett. 

## Project requirements 
### Bot Requirements 
+ Our bot should be able to gather resources for both minerals and vespene gas. 
+ Ability to place buildings for construction. 
+ Executation of at least one build order. 
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
Our bot is set to carry out the 5-Pool build order, so that we can take advantage of the early-game potential and advantages that Zergs have. 

## Class Breakdown 


## Build Orders 
Our build order can also be in this [Google Doc](https://docs.google.com/document/d/1e05FzKy5A5DllCp2OfijMiIUobjpTRM2mPlKQ5jJYNk/edit). 

Generally speaking, the build order we follow in the beginning is what is known as a *5 Pool*. 

START: 4/9 
5/9: Build another drone 
4/9: Build a spawning pool 
5/9: Build a drone while the spawning pool is building 
6/9: Build another drone while the spawning pool is building 
9/9: Use 3 larvae to build 6 zerglings to rush the enemy base and attack workers/buildings to weaken their economy 

IF ENDGAME: 
WIN. 

IF NOT ENDGAME, CONTINUE: 
9/17: Build Overlord 2 to increase control (100 minerals+, have larvae) 
11/17: Make 4 Zerglings for reinforcements for the first wave 
12/17: Build a drone  
11/17: Build extractor 
10/17: Build a drone 

