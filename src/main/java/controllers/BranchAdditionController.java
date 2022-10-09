package controllers;

import builders.BranchBuilder;
import com.sun.jdi.request.DuplicateRequestException;
import datatypes.CommandResponse;
import datatypes.RentalPrimary;

import java.util.Arrays;
import java.util.List;


public class BranchAdditionController implements IController<BranchAdditionController> {
/*
 * ADD_BRANCH
 * Use : TRUE if the operation succeeds else FALSE
 * Desc : onboard branch (Branch Name, Vehicle Type)
 * Eg. command  > ADD_BRANCH B1 CAR,BIKE,VAN
 *              > TRUE
 */
    @Override
    public CommandResponse execute(String[] command, RentalPrimary rentalPrimary) {
        try{
            rentalPrimary = new BranchBuilder().buildBranch(rentalPrimary,
                    getBranchName(command),
                    getAllowedVehicleTypes(command));
            return CommandResponse.createResponse(Boolean.TRUE, rentalPrimary,"TRUE");
        } catch (DuplicateRequestException | ArrayIndexOutOfBoundsException ex){
            return CommandResponse.createResponse(Boolean.FALSE, rentalPrimary, "FALSE") ;
        }
    }

    private List<String> getAllowedVehicleTypes(String[] command){
        return Arrays.asList(command[2].split(","));
    }
}
