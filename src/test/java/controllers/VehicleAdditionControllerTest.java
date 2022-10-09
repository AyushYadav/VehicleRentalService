package controllers;

import datatypes.CommandResponse;
import datatypes.RentalBranch;
import datatypes.RentalPrimary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleAdditionControllerTest {

    private RentalPrimary rentalPrimary;
    private final VehicleAdditionController vehicleAdditionController = new VehicleAdditionController();

    @BeforeEach
    public void setup(){
        RentalBranch rentalBranch = RentalBranch.builder()
                .branchName("B1")
                .allowedVehicleTypes(Arrays.asList("VAN","CAR"))
                .vehicles(new ArrayList<>())
                .build();
        this.rentalPrimary = RentalPrimary.builder().branches(Arrays.asList(rentalBranch)).build();
    }

    @Test
    public void testAddingAllowedVehicleToBranchWithSuccess(){
        String[] command = {"ADD_VEHICLE","B1","CAR", "C1", "500"};
        CommandResponse response = vehicleAdditionController.execute(command, rentalPrimary);
        Assertions.assertTrue(response.getIsSuccess());
        Assertions.assertEquals("TRUE", response.getResponse());
        Assertions.assertFalse(response.getRentalPrimary().getBranches().get(0).getVehicles().isEmpty());
        Assertions.assertEquals(1,response.getRentalPrimary().getBranches().get(0).getVehicles().size());
    }

    @Test
    public void testAddingNotAllowedVehicleToBranchWithFailure(){
        String[] command = {"ADD_VEHICLE","B1","BUS", "B1", "500"};
        CommandResponse response = vehicleAdditionController.execute(command, rentalPrimary);
        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("FALSE", response.getResponse());
        Assertions.assertTrue(response.getRentalPrimary().getBranches().get(0).getVehicles().isEmpty());
    }

    @Test
    public void testAddingSameVehicleAgainToBranchWithFailure(){
        String[] commandBase = {"ADD_VEHICLE","B1","CAR", "C1", "500"};
        CommandResponse responseBase = vehicleAdditionController.execute(commandBase, rentalPrimary);
        String[] command = {"ADD_VEHICLE","B1","CAR", "C1", "500"};
        CommandResponse response = vehicleAdditionController.execute(command, responseBase.getRentalPrimary());

        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("FALSE", response.getResponse());
        Assertions.assertEquals(1, response.getRentalPrimary().getBranches().get(0).getVehicles().size());
    }

    @Test
    public void testAddingVehicleToInvalidBranchWithFailure(){
        String[] command = {"ADD_VEHICLE","B2","CAR", "C1", "500"};
        CommandResponse response = vehicleAdditionController.execute(command, rentalPrimary);

        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("FALSE", response.getResponse());
        Assertions.assertTrue(response.getRentalPrimary().getBranches().get(0).getVehicles().isEmpty());
    }

    @Test
    public void testAddingVehicleToBranchWithIncompleteInput(){
        String[] command = {"ADD_VEHICLE","B1","BUS", "B1"};
        CommandResponse response = vehicleAdditionController.execute(command, rentalPrimary);
        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("FALSE", response.getResponse());
        Assertions.assertTrue(response.getRentalPrimary().getBranches().get(0).getVehicles().isEmpty());
    }

    @Test
    public void testAddingVehicleToBranchWithBadInput(){
        String[] command = {"ADD_VEHICLE","B1","BUS", "B1","BAD"};
        CommandResponse response = vehicleAdditionController.execute(command, rentalPrimary);
        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("FALSE", response.getResponse());
        Assertions.assertTrue(response.getRentalPrimary().getBranches().get(0).getVehicles().isEmpty());
    }
}
