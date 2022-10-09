package controllers;

import datatypes.CommandResponse;
import datatypes.RentalBranch;
import datatypes.RentalPrimary;
import datatypes.Vehicle;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
* BOOK:  Booking Price, if booking succeeds else -1
* book a vehicle - (Branch id, vehicle type, start time, end time)
*
* > BOOK B1 CAR 1 3
* > 1000
* */
public class BookingController implements IController<BookingController>{
    @Override
    public CommandResponse execute(String[] command, RentalPrimary rentalPrimary) {
        try{
            RentalBranch branchToBook = getRentalBranchForBooking(rentalPrimary.getBranches(),getBranchName(command));
            String vehicleTypeToBook = getVehicleType(command);
            if(Objects.nonNull(branchToBook) &&
                    isVehicleTypeAvailableInBranch(branchToBook.getAllowedVehicleTypes(), vehicleTypeToBook)){

                List<Vehicle> vehicles = branchToBook.getVehicles()
                        .stream()
                        .filter(o -> o.getVehicleType().equalsIgnoreCase(vehicleTypeToBook))
                        .collect(Collectors.toList());

                int bookingStartTime = getBookingStartTime(command), bookingEndTime = getBookingEndTime(command);

                List<Vehicle> availableVehicles = vehicles.stream()
                        .filter(o -> isVehicleAvailableAtDesiredTime(o, bookingStartTime, bookingEndTime))
                        .sorted(Comparator.comparingDouble((Vehicle a) -> a.getRate().getCost()))
                        .collect(Collectors.toList());

                if(!availableVehicles.isEmpty()){
                    Vehicle bookedVehicle = bookSlotsForTheVehicle(availableVehicles.get(0), bookingStartTime, bookingEndTime);
                    Double costSurgeFactor = ((double)availableVehicles.size()*100 / branchToBook.getVehicles().size()) <= 20
                            ? 1.1 : 1;
                    Double bookingCost = costSurgeFactor * getBookingCost(bookedVehicle, bookingStartTime, bookingEndTime);
                    return CommandResponse.createResponse(Boolean.TRUE, rentalPrimary, bookingCost.toString());
                }
                throw new InputMismatchException();
            } else {
                throw new IllegalArgumentException();
            }
        } catch (InputMismatchException | IllegalArgumentException ex){
            return CommandResponse.createResponse(Boolean.FALSE, rentalPrimary, "-1") ;
        }
    }

    private Boolean isVehicleAvailableAtDesiredTime(Vehicle vehicle, int startTime, int endTime){
        if(endTime<=startTime) throw new IllegalArgumentException();
        Set<Integer> bookedSlots = vehicle.getBookedTimeslots();
        for(int i =startTime;i<endTime;i++){
            if(bookedSlots.contains(i)) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Double getBookingCost(Vehicle vehicle, int startTime, int endTime){
        return (vehicle.getRate().getCost() * (endTime-startTime));
    }
    private Vehicle bookSlotsForTheVehicle(Vehicle vehicle, int startTime, int endTime){
        Set<Integer> bookedSlots = vehicle.getBookedTimeslots();
        for(int i = startTime; i<endTime;i++) bookedSlots.add(i);

        vehicle.setBookedTimeslots(bookedSlots);
        return vehicle;
    }

    private RentalBranch getRentalBranchForBooking(List<RentalBranch> rentalBranchList, String branchName){
        for(RentalBranch branch : rentalBranchList){
            if(branch.getBranchName().equalsIgnoreCase(branchName)){
                return branch;
            }
        }
        return null;
    }

    private Boolean isVehicleTypeAvailableInBranch(List<String> allowedVehicleTypes, String vehicleType){
        return allowedVehicleTypes.stream().anyMatch(o -> o.equalsIgnoreCase(vehicleType));
    }

    private String getVehicleType(String[] command){return command[2];}

    private Integer getBookingStartTime(String[] command){return Integer.parseInt(command[3]);}

    private Integer getBookingEndTime(String[] command){return Integer.parseInt(command[4]);}
}
