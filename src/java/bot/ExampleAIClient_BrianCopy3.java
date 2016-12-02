package bot;

import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.UpgradeType;
import jnibwapi.types.UpgradeType.UpgradeTypes;
import jnibwapi.util.BWColor;
import java.util.*;
import java.lang.*;
import java.util.Collections;

import java.util.HashSet;

import static java.util.Collections.max;

/**
 * Example Java AI Client using JNI-BWAPI.
 * 
 * Executes a 5-pool rush and cheats using perfect information.
 * 
 * Note: the agent often gets stuck when attempting to build the spawning pool. It works best on
 * maps where the overlord spawns with plenty of free space around it.
 */
public class ExampleAIClient_BrianCopy3 implements BWAPIEventListener {

	/** reference to JNI-BWAPI */
	private final JNIBWAPI bwapi;

	/** used for mineral splits */
	private final HashSet<Unit> claimedMinerals = new HashSet<>();

	/** have drone 5 been morphed */
	private boolean morphedDrone;

	/** the drone that has been assigned to building a pool */
	private Unit poolDrone;

	/** when should the next overlord be spawned? */
	private int supplyCap;

	/** the drone that has been assigned to building an extractor */
	private Unit extractorDrone;
	private Position psExtractor;

	/** the drone that has been assigned to building an extractor */
	private Unit creepDrone1;
	private Unit creepDrone2;

	/** the drone that has been assigned to building an extractor */
	private Unit hydraliskDenDrone;

	/** mainHatchery unit for home location */
	private Unit gasDrone1;
	private Unit gasDrone2;

	/** mainHatchery unit for home location */
	private Unit mainHatchery;

	/** Buildable positions */
	private ArrayList<Position> ps ;

	/** Coordinates of home base */
	int homeX;
	int homeY;

	/**
	 * Create a Java AI.
	 */
	public static void main(String[] args) {
		new ExampleAIClient_BrianCopy3();
	}

	/**
	 * Instantiates the JNI-BWAPI interface and connects to BWAPI.
	 */
	public ExampleAIClient_BrianCopy3() {
		bwapi = new JNIBWAPI(this, true);
		bwapi.start();
	}

	/**
	 * Connection to BWAPI established.
	 */
	@Override
	public void connected() {
		System.out.println("Connected");
	}

	/**
	 * Called at the beginning of a game.
	 */
	@Override
	public void matchStart() {
		System.out.println("Game Started");

		bwapi.enableUserInput();
		bwapi.enablePerfectInformation();
		bwapi.setGameSpeed(0);

		// reset agent state
		claimedMinerals.clear();
		morphedDrone = false;
		poolDrone = null;
		extractorDrone = null;
		creepDrone1 = null;
		creepDrone2 = null;
		hydraliskDenDrone = null;
		supplyCap = 0;
		gasDrone1 = null;
		gasDrone2 = null;
		for (Unit u : bwapi.getMyUnits()) {
			if (u.getType() == UnitTypes.Zerg_Hatchery) {
				mainHatchery = u;
			}
		}

		homeX = mainHatchery.getPosition().getPX();
		homeY = mainHatchery.getPosition().getPY();
		ps = new ArrayList<>();
		//ArrayList<Position> ps2 = new ArrayList<>();
		for (int k = 1; k < 20; k++) {
			for (int i = 1; i <= k * 2 + 1; i++) {
				for (int j = 1; j <= k * 2 + 1; j++) {
					//bwapi.drawCircle(new Position(homeX + (i - (k + 1)) * 32, homeY + (j - (k + 1)) * 32), 5, BWColor.Orange, true, false);
					//bwapi.drawCircle(new Position(homeX2 + (i - (k + 1)) * 32, homeY2 + (j - (k + 1)) * 32), 5, BWColor.Green, true, false);
					ps.add(new Position(homeX + (i - (k + 1)) * 16, homeY + (j - (k + 1)) * 16));

				}
			}
		}


	}

	/**
	 * Called each game cycle.
	 */
	@Override
	public void matchFrame() {
/*		bwapi.drawCircle(mainHatchery.getPosition(), 5, BWColor.Yellow, true, false);
		bwapi.drawCircle(mainHatchery.getTilePosition(), 5, BWColor.Green, true, false);
		int homeX = mainHatchery.getPosition().getPX();
		int homeY = mainHatchery.getPosition().getPY();
		int homeX2 = mainHatchery.getTilePosition().getPX();
		int homeY2 = mainHatchery.getTilePosition().getPY();

		//ArrayList<Position> ps = new ArrayList<>();
		//ArrayList<Position> ps2 = new ArrayList<>();
		for (int k = 1; k < 6; k++) {
			for (int i = 1; i <= k * 2 + 1; i++) {
				for (int j = 1; j <= k * 2 + 1; j++) {
					//bwapi.drawCircle(new Position(homeX + (i - (k + 1)) * 32, homeY + (j - (k + 1)) * 32), 5, BWColor.Orange, true, false);
					//bwapi.drawCircle(new Position(homeX2 + (i - (k + 1)) * 32, homeY2 + (j - (k + 1)) * 32), 5, BWColor.Green, true, false);
					ps.add(new Position(homeX + (i - (k + 1)) * 32, homeY + (j - (k + 1)) * 32));

				}
			}
		}*/

		for (int i = 1 ; i<ps.size(); i++) {
			//bwapi.drawCircle(ps.get(i), 5, BWColor.Orange, true, false);
			if (bwapi.canBuildHere(ps.get(i),UnitTypes.Zerg_Creep_Colony,true)) {
				//bwapi.drawCircle(ps.get(i), 5, BWColor.Green, true, false);
			}
		}

		// print out some info about any upgrades or research happening
		String msg = "=";
		for (TechType t : TechTypes.getAllTechTypes()) {
			if (bwapi.getSelf().isResearching(t)) {
				msg += "Researching " + t.getName() + "=";
			}
			// Exclude tech that is given at the start of the game
			UnitType whatResearches = t.getWhatResearches();
			if (whatResearches == UnitTypes.None) {
				continue;
			}
			if (bwapi.getSelf().isResearched(t)) {
				msg += "Researched " + t.getName() + "=";
			}
		}
		for (UpgradeType t : UpgradeTypes.getAllUpgradeTypes()) {
			if (bwapi.getSelf().isUpgrading(t)) {
				msg += "Upgrading " + t.getName() + "=";
			}
			if (bwapi.getSelf().getUpgradeLevel(t) > 0) {
				int level = bwapi.getSelf().getUpgradeLevel(t);
				msg += "Upgraded " + t.getName() + " to level " + level + "=";
			}
		}
		bwapi.drawText(new Position(0, 20), msg, true);

		// draw the terrain information
		bwapi.getMap().drawTerrainData(bwapi);

		// spawn a drone
		for (Unit unit : bwapi.getMyUnits()) {
			// Note you can use referential equality
			if (unit.getType() == UnitTypes.Zerg_Larva) {
				if (bwapi.getSelf().getMinerals() >= 50 && !morphedDrone) {
					unit.morph(UnitTypes.Zerg_Drone);
					morphedDrone = true;
				}
			}
		}

		// Make all drones collect minerals if we don't have an extractor
		for (Unit unit : bwapi.getMyUnits()) {
			if (unit.getType() == UnitTypes.Zerg_Drone) {
				// You can use referential equality for units, too
				if (unit.isIdle() && unit != poolDrone && unit != creepDrone1 && unit != extractorDrone && unit != gasDrone1 ) {
					for (Unit minerals : bwapi.getNeutralUnits()) {
						if (minerals.getType().isMineralField()
								&& !claimedMinerals.contains(minerals)) {
							double distance = unit.getDistance(minerals);

							if (distance < 300) {
								unit.rightClick(minerals, false);
								claimedMinerals.add(minerals);
								break;
							}
						}
					}
				}
			}
		}

		// If we do have an extractor, then assign gasDrone1 and gasDone2 and assign
		for (Unit unit : bwapi.getMyUnits()) {
			if (unit.getType() == UnitTypes.Zerg_Extractor) {
				for (Unit drones : bwapi.getMyUnits()) {
					if (drones.getType() == UnitTypes.Zerg_Drone) {
						gasDrone1 = drones;
						break;
					}
				}
			}
		}

		for (Unit extractors : bwapi.getMyUnits()) {
			if (extractors.getType() == UnitTypes.Zerg_Extractor) {
				bwapi.drawCircle(extractors.getPosition(), 5, BWColor.Blue, true, false);
				if (gasDrone1.isGatheringMinerals()) {
					gasDrone1.stop(false);
				}
				else if(gasDrone1.isIdle() && gasDrone1 != poolDrone && gasDrone1 != creepDrone1 && gasDrone1 != extractorDrone) {
					gasDrone1.rightClick(extractors, false);

				}
			}
		}

		// build creep colony at the farthest away spot
		if (bwapi.getSelf().getMinerals() >= 75 && creepDrone1 == null) {
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Drone) {
					creepDrone1 = unit;
					break;
				}
			}
			// Create list of buildable locations for creep colonies
			ArrayList<Position> psCreepColony = new ArrayList<>();
			for (int i = 1; i < ps.size(); i++) {
				if (bwapi.canBuildHere(ps.get(i), UnitTypes.Zerg_Creep_Colony, false)) {
					psCreepColony.add(ps.get(i));
				}
			}
			// Create list of distances of these buildable locations
			double [] psCreepColonyDistances = new double[psCreepColony.size()];
			for (int i = 1; i<psCreepColony.size();i++){
				psCreepColonyDistances[i]= psCreepColony.get(i).getApproxPDistance(mainHatchery.getPosition());
			}
			// find the maximum distance and store its index
			double max = psCreepColonyDistances[1];
			int indexOfMax = 1;
			for (int i = 1; i<psCreepColony.size();i++){
				if (psCreepColonyDistances[i]>max){
					max = psCreepColonyDistances[i];
					indexOfMax=i;
				}
			}
			// build cc at the max distance from the base that is buildable
			creepDrone1.build(psCreepColony.get(indexOfMax), UnitTypes.Zerg_Creep_Colony);
			}

		// build a spawning pool
		if (bwapi.getSelf().getMinerals() >= 200 && poolDrone == null) {
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Drone) {
					poolDrone = unit;
					break;
				}
			}

			ArrayList<Position> psSpawningPool = new ArrayList<>();
			for (int i = 1; i < ps.size(); i++) {
				if (bwapi.canBuildHere(ps.get(i), UnitTypes.Zerg_Spawning_Pool, false)) {
					psSpawningPool.add(ps.get(i));
				}
			}
			// Create list of distances of these buildable locations
			double[] psSpawningPoolDistances = new double[psSpawningPool.size()];
			for (int i = 1; i < psSpawningPool.size(); i++) {
				psSpawningPoolDistances[i] = psSpawningPool.get(i).getApproxPDistance(mainHatchery.getPosition());
			}
			// find the maximum distance and store its index
			double min = psSpawningPoolDistances[1];
			int indexOfMin = 1;
			for (int i = 1; i < psSpawningPool.size(); i++) {
				if (psSpawningPoolDistances[i] > min) {
					min = psSpawningPoolDistances[i];
					indexOfMin = i;
				}
			}
			// build spawping pool at the max distance from the base that is buildable
			poolDrone.build(psSpawningPool.get(indexOfMin), UnitTypes.Zerg_Spawning_Pool);
		}


		// build an extractor
		if (bwapi.getSelf().getMinerals() >= 50 && poolDrone == null) {
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Drone) {
					extractorDrone = unit;
					break;
				}
			}

			for (int i = 1; i < ps.size(); i++) {
				if (bwapi.canBuildHere(ps.get(i), UnitTypes.Zerg_Extractor, false)) {
					psExtractor = ps.get(i);
				}
			}

			// build extractor at the one location we can
			extractorDrone.build(psExtractor, UnitTypes.Zerg_Extractor);
		}

		// Send drones to collect gas at the extractor



		// build Hydralisk Den
		if (bwapi.getSelf().getMinerals() >= 100 && bwapi.getSelf().getGas() >= 50 && hydraliskDenDrone == null) {
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Drone) {
					hydraliskDenDrone = unit;
					break;
				}
			}

			// build the hydralisk den on the overlord cuz idk how to build elsewhere
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Overlord) {
					hydraliskDenDrone.build(unit.getPosition(), UnitTypes.Zerg_Hydralisk_Den);
				}
			}
		}







/*
			// spawn overlords
			if (bwapi.getSelf().getSupplyUsed() + 2 >= bwapi.getSelf().getSupplyTotal()
					&& bwapi.getSelf().getSupplyTotal() > supplyCap) {
				if (bwapi.getSelf().getMinerals() >= 100) {
					for (Unit larva : bwapi.getMyUnits()) {
						if (larva.getType() == UnitTypes.Zerg_Larva) {
							larva.morph(UnitTypes.Zerg_Overlord);
							supplyCap = bwapi.getSelf().getSupplyTotal();
						}
					}
				}
			}

			// spawn zerglings
			else if (bwapi.getSelf().getMinerals() >= 50) {
				for (Unit unit : bwapi.getMyUnits()) {
					if (unit.getType() == UnitTypes.Zerg_Spawning_Pool && unit.isCompleted()) {
						for (Unit larva : bwapi.getMyUnits()) {
							if (larva.getType() == UnitTypes.Zerg_Larva) {
								larva.morph(UnitTypes.Zerg_Zergling);
							}
						}
					}
				}
			}


/*
		// attack move toward an enemy
		for (Unit unit : bwapi.getMyUnits()) {
			if (unit.getType() == UnitTypes.Zerg_Zergling && unit.isIdle()) {
				for (Unit enemy : bwapi.getEnemyUnits()) {
					unit.attack(enemy.getPosition(), false);
					break;
				}
			}
		}


			// build the extractor on the overlord cuz idk how to build elsewhere
			for (Unit unit : bwapi.getNeutralUnits()) {
				if (unit.getType().isVespeneGeyser()){
					// Ask Alex about how to build this on the vespine geyser
					extractorDrone.build(unit.getPosition(), UnitTypes.Zerg_Extractor);
				}
			}
		}

		// build creep colony
		if (bwapi.getSelf().getMinerals() >= 75 && creepDrone == null) {
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Drone) {
					creepDrone = unit;
					break;
				}
			}

			// build the creep colony on the overlord cuz idk how to build elsewhere.
			for (Unit unit : bwapi.getMyUnits()) {
				// Ask Ethan.  Build it somewhere around where our drones are.
				//  If we build another build it near the choke point
				if (unit.getType() == UnitTypes.Zerg_Overlord) {
					creepDrone.build(unit.getPosition(), UnitTypes.Zerg_Creep_Colony);
				}
			}
		}


		// build Hydralisk Den
		if (bwapi.getSelf().getMinerals() >= 100 && bwapi.getSelf().getGas() >= 50 && hydraliskDenDrone == null) {
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Drone) {
					hydraliskDenDrone = unit;
					break;
				}
			}

			// build the hydralisk den on the overlord cuz idk how to build elsewhere
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Overlord) {
					hydraliskDenDrone.build(unit.getPosition(), UnitTypes.Zerg_Hydralisk_Den);
				}
			}
		}

		// spawn hydralisks
		else if (bwapi.getSelf().getMinerals() >= 75 && bwapi.getSelf().getGas() >= 25) {
			for (Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Hydralisk_Den && unit.isCompleted()) {
					for (Unit larva : bwapi.getMyUnits()) {
						if (larva.getType() == UnitTypes.Zerg_Larva) {
							larva.morph(UnitTypes.Zerg_Hydralisk);
						}
					}
				}
			}
		}

		// Turn the creep colony into a sunken colony



		//

		//



*/
	}
	
	@Override
	public void keyPressed(int keyCode) {}
	@Override
	public void matchEnd(boolean winner) {}
	@Override
	public void sendText(String text) {}
	@Override
	public void receiveText(String text) {}
	@Override
	public void nukeDetect(Position p) {}
	@Override
	public void nukeDetect() {}
	@Override
	public void playerLeft(int playerID) {}
	@Override
	public void unitCreate(int unitID) {}
	@Override
	public void unitDestroy(int unitID) {}
	@Override
	public void unitDiscover(int unitID) {}
	@Override
	public void unitEvade(int unitID) {}
	@Override
	public void unitHide(int unitID) {}
	@Override
	public void unitMorph(int unitID) {}
	@Override
	public void unitShow(int unitID) {}
	@Override
	public void unitRenegade(int unitID) {}
	@Override
	public void saveGame(String gameName) {}
	@Override
	public void unitComplete(int unitID) {}
	@Override
	public void playerDropped(int playerID) {}
}
