package controllers;

import datatypes.CommandResponse;
import datatypes.RentalBranch;
import datatypes.RentalPrimary;
import datatypes.Vehicle;

import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/*
* DISPLAY_VEHICLES: Vehicle Ids, comma-separated
* display all available vehicles for a branch, sorted on price ( Branch id, start time, end time )
* DISPLAY_VEHICLES B1 1 5
* > V2
* */
public class InventoryController implements IController<InventoryController>{

    @Override
    public CommandResponse execute(String[] command, RentalPrimary rentalPrimary) {
        try {
            Optional<RentalBranch> branch = rentalPrimary.getBranches()
                    .stream()
                    .filter(o -> o.getBranchName().equalsIgnoreCase(getBranchName(command))).findFirst();
            if(branch.isPresent()){
                List<Vehicle> availableVehiclesInSlot= getAvailableVehiclesInSlot(branch.get(),command);
                List<String> results = new ArrayList<>();
                for(Vehicle vehicle : availableVehiclesInSlot){
                    results.add(vehicle.getId().getId());
                }
                return CommandResponse.createResponse(Boolean.TRUE, rentalPrimary, String.join(",",results));
            } else {
                throw new InvalidAttributesException();
            }
        } catch (InvalidAttributesException | ArrayIndexOutOfBoundsException ex){
            return CommandResponse.createResponse(Boolean.FALSE, rentalPrimary,"");
        }
    }

    private List<Vehicle> getAvailableVehiclesInSlot(RentalBranch rentalBranch, String[] command) throws InvalidAttributesException{
        int startTime = getBookingStartTime(command), endTime = getBookingEndTime(command);
        if(endTime <= startTime || startTime < 0 || endTime >= 24) throw new InvalidAttributesException();

        return rentalBranch.getVehicles().stream()
                .filter(o -> isVehicleAvailableAtDesiredTime(o, startTime, endTime))
                .sorted(Comparator.comparingDouble((Vehicle a) -> a.getRate().getCost()))
                .collect(Collectors.toList());
    }

    private Boolean isVehicleAvailableAtDesiredTime(Vehicle vehicle, int startTime, int endTime){
        Set<Integer> bookedSlots = vehicle.getBookedTimeslots();
        for(int i =startTime;i<endTime;i++){
            if(bookedSlots.contains(i)) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Integer getBookingStartTime(String[] command){return Integer.parseInt(command[2]);}

    private Integer getBookingEndTime(String[] command){return Integer.parseInt(command[3]);}
}
