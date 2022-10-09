package builders;

import datatypes.ID;
import datatypes.Rate;
import datatypes.Vehicle;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@NoArgsConstructor
public class VehicleBuilder {

    public Vehicle createVehicle(String vehicleType, String vehicleId, int rentCost){
        return Vehicle.builder()
                .id(ID.builder().id(vehicleId).build())
                .rate(Rate.builder().cost(rentCost).build())
                .vehicleType(vehicleType)
                .bookedTimeslots(new HashSet<>())
                .build();
    }
}
