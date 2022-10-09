package builders;

import com.sun.jdi.request.DuplicateRequestException;
import datatypes.RentalPrimary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BranchBuilderTest {

    private RentalPrimary rentalPrimary;

    @BeforeEach
    public void setup(){
        rentalPrimary = new RentalPrimaryBuilder().buildRentalPrimary();
    }

    @Test
    public void testBranchBuilderWithNoPreExistingBranches(){
        rentalPrimary = new BranchBuilder()
                .buildBranch(rentalPrimary,"B1", Arrays.asList("CAR","VAN"));
        Assertions.assertFalse(rentalPrimary.getBranches().isEmpty());
        Assertions.assertEquals("B1", rentalPrimary.getBranches().get(0).getBranchName());
    }

    @Test
    public void testBranchBuilderWithNewAdditionalBranches(){
        rentalPrimary = new BranchBuilder()
                .buildBranch(rentalPrimary,"B1", Arrays.asList("CAR","VAN"));
        rentalPrimary = new BranchBuilder()
                .buildBranch(rentalPrimary,"B2", Arrays.asList("BIKE","VAN"));
        Assertions.assertFalse(rentalPrimary.getBranches().isEmpty());
        Assertions.assertEquals(2, rentalPrimary.getBranches().size());
    }

    @Test
    public void testBranchBuilderWithPreExistingBranches(){
        rentalPrimary = new BranchBuilder()
                .buildBranch(rentalPrimary,"B1", Arrays.asList("CAR","VAN"));
        Assertions.assertThrows(DuplicateRequestException.class,
                () -> { new BranchBuilder()
                        .buildBranch(rentalPrimary,"B1", Arrays.asList("CAR","VAN"));}
                );

        Assertions.assertFalse(rentalPrimary.getBranches().isEmpty());
        Assertions.assertEquals("B1", rentalPrimary.getBranches().get(0).getBranchName());
    }
}
