package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.repository.ParkingSpaceRepository;

public class ParkingSpaceController {

    private ParkingSpaceRepository parkingSpaceRepository;

    public ParkingSpaceController(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }


}
