package builders;

import com.sun.jdi.request.DuplicateRequestException;
import datatypes.RentalBranch;
import datatypes.RentalPrimary;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class BranchBuilder {

    public RentalPrimary buildBranch(RentalPrimary rentalPrimary,
                                     String branchName, List<String> allowedVehicleTypes){
        RentalBranch rentalBranchToBeAdded = RentalBranch.builder()
                .allowedVehicleTypes(allowedVehicleTypes)
                .branchName(branchName)
                .vehicles(new ArrayList<>())
                .build();

        if(!checkIfBranchPreExists(rentalPrimary.getBranches(),rentalBranchToBeAdded)){
            rentalPrimary.getBranches().add(rentalBranchToBeAdded);
        }
        return rentalPrimary;
    }

    private Boolean checkIfBranchPreExists(List<RentalBranch> rentalBranches, RentalBranch branchToBeAdded){
        if(rentalBranches.isEmpty()) return Boolean.FALSE;
        for(RentalBranch branch : rentalBranches){
            if (branch.getBranchName().equalsIgnoreCase(branchToBeAdded.getBranchName())){
                throw new DuplicateRequestException();
            }
        }
        return Boolean.FALSE;
    }
}
