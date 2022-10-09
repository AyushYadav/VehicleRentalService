package datatypes;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RentalPrimary implements IRental{
    private List<RentalBranch> branches;
}
