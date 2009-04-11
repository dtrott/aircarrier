package net.java.dev.aircarrier.ai.targetting;

import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.TargettingAcobject;

public class TeamTargettingManager {

	/**
	 * Delta required between target values in order to
	 * bother switching. Mainly used to avoid switching 
	 * wildly between targets which each have a value
	 * of 1 or 0 due to clipping to this range
	 */
	private final static float SWITCH_DELTA = 0.001f;
	
	List<? extends TargettingAcobject> players;
	List<? extends TargettingAcobject> friends;
	List<? extends TargettingAcobject> enemies;

	TargetChoiceSensor<Acobject, Acobject> friendsSensor;
	TargetChoiceSensor<Acobject, Acobject> enemiesOnFriendsSensor;
	TargetChoiceSensor<Acobject, Acobject> enemiesOnPlayersSensor;
	
	
	/**
	 * Create a manager
	 * @param players
	 * 		The players (humans, may be treated slightly differently, and obviously can't be targetted)
	 * @param friends
	 * 		The AI friends of players
	 * @param enemies
	 * 		The AI enemies of players
	 * @param friendsSensor
	 * 		Sensor used by friends to assess enemy targets
	 * @param enemiesOnFriendsSensor
	 * 		Sensor used by enemies to assess friendly-AI targets
	 * @param enemiesOnPlayersSensor
	 * 		Sensor used by enemies to asses player targets
	 */
	public TeamTargettingManager(
			List<? extends TargettingAcobject> players, 
			List<? extends TargettingAcobject> friends, 
			List<? extends TargettingAcobject> enemies,
			TargetChoiceSensor<Acobject, Acobject> friendsSensor,
			TargetChoiceSensor<Acobject, Acobject> enemiesOnFriendsSensor,
			TargetChoiceSensor<Acobject, Acobject> enemiesOnPlayersSensor
		) {
		super();
		this.players = players;
		this.friends = friends;
		this.enemies = enemies;
		this.friendsSensor = friendsSensor;
		this.enemiesOnFriendsSensor = enemiesOnFriendsSensor;
		this.enemiesOnPlayersSensor = enemiesOnPlayersSensor;
	}
	
	/**
	 * Update targetting as time elapses
	 * @param time
	 * 		Time passed since last update
	 */
	public void update(float time) {
		//Update sensors
		friendsSensor.update(time);
		enemiesOnFriendsSensor.update(time);
		enemiesOnPlayersSensor.update(time);

		//Update targetting
		updateFriendTargetting();
		updateEnemyTargetting();
	}
	
	void updateFriendTargetting() {
		for (TargettingAcobject friend : friends) {
			//Default to no best target/value
			float bestTargetValue = -1;
			Acobject bestTarget = null;
			
			//Remember current target
			Acobject currentTarget = friend.getTarget();
			
			//Find the best value target among enemies
			for (Acobject possibleTarget : enemies) {
				float targetValue = friendsSensor.getTargettingValue(friend, possibleTarget);

				//Give current target a boost, if we have one, to avoid pointless switching
				if (currentTarget == possibleTarget) targetValue += SWITCH_DELTA;

				if (targetValue > bestTargetValue) {
					bestTarget = possibleTarget;
					bestTargetValue = targetValue;
				}
			}
			
			//If we have found a different best target, set it, and also tell
			//our sensors
			if (bestTarget != currentTarget) {
				friend.setTarget(bestTarget);
				fireTargettingChange(friend, currentTarget, bestTarget);
			}	
		}	
	}

	void updateEnemyTargetting() {
		for (TargettingAcobject enemy : enemies) {
			
			//Default to no best target/value
			float bestTargetValue = -1;
			Acobject bestTarget = null;

			//Remember current target
			Acobject currentTarget = enemy.getTarget();
			
			//Find the best value among friendlies
			for (Acobject possibleTarget : friends) {
				float targetValue = enemiesOnFriendsSensor.getTargettingValue(enemy, possibleTarget);
				//Give current target a boost, if we have one, to avoid pointless switching
				if (currentTarget == possibleTarget) targetValue += SWITCH_DELTA;
				if (targetValue > bestTargetValue) {
					bestTarget = possibleTarget;
					bestTargetValue = targetValue;
				}
			}

			//See if we have a better value among players (exactly as for friendlies, but using
			//a different sensor)
			for (Acobject possibleTarget : players) {
				float targetValue = enemiesOnPlayersSensor.getTargettingValue(enemy, possibleTarget);
				//Give current target a boost, if we have one, to avoid pointless switching
				if (currentTarget == possibleTarget) targetValue += SWITCH_DELTA;
				if (targetValue > bestTargetValue) {
					bestTarget = possibleTarget;
					bestTargetValue = targetValue;
				}
			}

			//If we have found a different best target, set it, and also tell
			//our sensors
			if (bestTarget != currentTarget) {
				enemy.setTarget(bestTarget);
				fireTargettingChange(enemy, currentTarget, bestTarget);
			}	
		}	
	}

	
	/**
	 * Notify our sensors of a targetting change
	 */
	void fireTargettingChange(Acobject hunter, Acobject oldPrey, Acobject newPrey) {
		friendsSensor.targettingChange(hunter, oldPrey, newPrey);
		enemiesOnFriendsSensor.targettingChange(hunter, oldPrey, newPrey);
		enemiesOnPlayersSensor.targettingChange(hunter, oldPrey, newPrey);
	}
	
}
