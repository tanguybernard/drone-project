package projet.istic.fr.firedrone.listener;

import java.util.List;

import projet.istic.fr.firedrone.map.EnumTrajectoryMode;
import projet.istic.fr.firedrone.model.PointMissionDrone;

/**
 * Group A
 */
public interface DroneMissionFragmentInterface {
    EnumTrajectoryMode getMode();
    List<PointMissionDrone> getListPointsMissionDrone();
}
