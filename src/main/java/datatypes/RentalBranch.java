package datatypes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RentalBranch {
    private String branchName;
    private List<Vehicle> vehicles;
    private List<String> allowedVehicleTypes;
}
