package controllers;

import builders.VehicleBuilder;
import datatypes.CommandResponse;
import datatypes.RentalBranch;
import datatypes.RentalPrimary;
import datatypes.Vehicle;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;
import java.util.Objects;

/*
* ADD_VEHICLE  TRUE if the operation succeeds else FALSE
* onboard vehicle - (Branch Name, Vehicle Type, vehicle id, price)
* I>ADD_VEHICLE B1 CAR V1 500
* O>TRUE
*
* I>ADD_VEHICLE B1 BUS V5 2500 // Since this vehicleType is not allowed in this Branch
* O>FALSE
* */
public class VehicleAdditionController implements IController<VehicleAdditionController>{

    @Override
    public CommandResponse execute(String[] command, RentalPrimary rentalPrimary) {
        try {
            RentalBranch rentalBranch = getRentalBranch(rentalPrimary, getBranchName(command));
            if(Objects.nonNull(rentalBranch) && isVehicleTypeAllowedInBranch(rentalBranch,getVehicleType(command))){
                List<Vehicle> inventoryVehicles = rentalBranch.getVehicles();
                if(!doesVehicleAlreadyExists(inventoryVehicles, getVehicleId(command))){
                    inventoryVehicles.add(createVehicleInventory(command));
                    rentalBranch.setVehicles(inventoryVehicles);
                    return CommandResponse.createResponse(Boolean.TRUE, rentalPrimary,"TRUE");
                }

            } else {
                throw new InvalidAttributesException();
            }
        } catch (InvalidAttributesException  | ArrayIndexOutOfBoundsException | NumberFormatException ex){
            return CommandResponse.createResponse(Boolean.FALSE, rentalPrimary, "FALSE");
        }
        return CommandResponse.createResponse(Boolean.FALSE, rentalPrimary, "FALSE");
    }

    private boolean doesVehicleAlreadyExists(List<Vehicle> inventoryVehicles, String vehicleId){
        for(Vehicle vehicle : inventoryVehicles){
            if(vehicle.getId().getId().equalsIgnoreCase(vehicleId)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private Boolean isVehicleTypeAllowedInBranch(RentalBranch rentalBranch, String vehicleType){
        for(String allowedType: rentalBranch.getAllowedVehicleTypes()){
            if(allowedType.equalsIgnoreCase(vehicleType)) return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private RentalBranch getRentalBranch(RentalPrimary rentalPrimary, String branchName) {
        for(RentalBranch rentalBranch : rentalPrimary.getBranches()){
            if(rentalBranch.getBranchName().equalsIgnoreCase(branchName)){
                return rentalBranch;
            }
        }
        return null;
    }

    private Vehicle createVehicleInventory(String[] command){
        return new VehicleBuilder()
                .createVehicle(getVehicleType(command),getVehicleId(command),getRentCost(command));
    }

    private String getVehicleType(String[] command){
        return command[2];
    }

    private String getVehicleId(String[] command){
        return command[3];
    }

    private Double getRentCost(String[] command){
        return Double.parseDouble(command[4]);
    }
}
