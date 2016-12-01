package bot;

import java.util.HashSet;

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

public class MinimalAIClient implements BWAPIEventListener {
	private final JNIBWAPI bwapi;

	/** used for mineral splits*/
	private final HashSet<Unit> claimedMinerals = new HashSet<>();

	/**when should the next overlord be spawned?*/
	private int supplyCap;

	/**the drone that has been assigned to building a pool*/
	private Unit poolDrone;

	/**have drone 5 been morphed*/
	private boolean morphedDrone;

	/** Create a Java AI */
	public static void main(String[] args) {
		new MinimalAIClient();
	}

	public MinimalAIClient() {
		bwapi = new JNIBWAPI(this, false);
		bwapi.start();
	}

	@Override
	public void connected() { System.out.println("Connected!");}

	@Override
	public void matchStart() {
		System.out.println("Game Started");

		bwapi.enableUserInput();
		bwapi.enablePerfectInformation();
		bwapi.setGameSpeed(0);

		//reset agent state
		claimedMinerals.clear();


	}

	@Override
	public void matchFrame() {
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

		//Draw the terrain information
		bwapi.getMap().drawTerrainData((bwapi));

		// Collect minerals
		for(Unit unit : bwapi.getMyUnits()) {
			if(unit.getType() == UnitTypes.Zerg_Drone) {
				for(Unit minerals : bwapi.getNeutralUnits()) {
					if(minerals.getType().isMineralField() &&
							 !claimedMinerals.contains(minerals)) {
						double distance = unit.getDistance(minerals);

						if(distance < 300) {
							unit.rightClick(minerals, false);
							claimedMinerals.add(minerals);
							break;

						}
					}
				}
			}
		}

		//Build a spawning pool (200 minerals, 80 secs)
		if(bwapi.getSelf().getMinerals() >= 200 && poolDrone == null) {
			for(Unit unit : bwapi.getMyUnits()) {
				if (unit.getType() == UnitTypes.Zerg_Drone) {
					poolDrone = unit;
					break;
				}
			}

			/** If I want to figure out how to place spawning pools,
			 * I would put them where? Right next to the minerals? A few feet
			 * away? Look it up later. */
			
		}
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
