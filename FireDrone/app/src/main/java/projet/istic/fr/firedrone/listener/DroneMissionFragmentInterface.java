package projet.istic.fr.firedrone.listener;

import java.util.List;

import projet.istic.fr.firedrone.map.ModeMissionDrone;
import projet.istic.fr.firedrone.model.PointMissionDrone;

/**
 * Group A
 */
public interface DroneMissionFragmentInterface {
    ModeMissionDrone getMode();
    List<PointMissionDrone> getListPointsMissionDrone();
}
