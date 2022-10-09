package controllers;

import builders.RentalPrimaryBuilder;
import datatypes.CommandResponse;
import datatypes.RentalPrimary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BranchAdditionControllerTest {

    private RentalPrimary rentalPrimary ;
    private BranchAdditionController branchAdditionController;

    @BeforeAll
    public void setup(){
        this.rentalPrimary = new RentalPrimaryBuilder().buildRentalPrimary();
        this.branchAdditionController = new BranchAdditionController();
    }

    @AfterEach
    public void restoreBranches(){
        this.rentalPrimary = new RentalPrimaryBuilder().buildRentalPrimary();
    }

    @Test
    public void testAddBranchOperationWithSuccessfulResponse(){
        String[] commands = {"ADD_BRANCH","B1","VAN,CAR"};
        CommandResponse actualResponse = branchAdditionController.execute(commands,rentalPrimary);
        Assertions.assertTrue(actualResponse.getIsSuccess());
        Assertions.assertEquals(1, actualResponse.getRentalPrimary().getBranches().size());
        Assertions.assertEquals("B1", actualResponse.getRentalPrimary().getBranches().get(0).getBranchName());
        Assertions.assertEquals(Arrays.asList("VAN","CAR"),
                actualResponse.getRentalPrimary().getBranches().get(0).getAllowedVehicleTypes());
    }

    @Test
    public void testAddBranchOperationWithBranchAlreadyExisting(){
        String[] commands = {"ADD_BRANCH","B1","VAN,CAR"};
        CommandResponse baseResponse = branchAdditionController.execute(commands,rentalPrimary);
        Assertions.assertTrue(baseResponse.getIsSuccess());
        CommandResponse response = branchAdditionController.execute(commands, baseResponse.getRentalPrimary());
        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("FALSE", response.getResponse());
        Assertions.assertEquals(1, response.getRentalPrimary().getBranches().size());
    }

    @Test
    public void testAddBranchOperationWithBadInput(){
        String[] commands = {"ADD_BRANCH","B1"};
        CommandResponse actualResponse = branchAdditionController.execute(commands,rentalPrimary);
        Assertions.assertFalse(actualResponse.getIsSuccess());
        Assertions.assertEquals(0, actualResponse.getRentalPrimary().getBranches().size());
    }
}
