package builders;

import datatypes.RentalBranch;
import datatypes.RentalPrimary;

import java.util.ArrayList;

public class RentalPrimaryBuilder {
    public RentalPrimary buildRentalPrimary(){
        return RentalPrimary.builder()
                .branches(new ArrayList<RentalBranch>())
                .build();
    }
}
