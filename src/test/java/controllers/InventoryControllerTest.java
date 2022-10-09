package controllers;

import builders.VehicleBuilder;
import datatypes.CommandResponse;
import datatypes.RentalBranch;
import datatypes.RentalPrimary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class InventoryControllerTest {
    private final InventoryController inventoryController = new InventoryController();

    private final BookingController bookingController = new BookingController();
    private RentalPrimary rentalPrimary;

    @BeforeEach
    public void setup(){
        rentalPrimary = RentalPrimary.builder()
                .branches(Arrays.asList(createBranchAndAddVehicles()))
                .build();
    }

    @Test
    public void testInventoryControllerWithAllAvailableVehicleWithSuccess(){
        String[] command = {"DISPLAY_VEHICLES","B1","5","8"};
        CommandResponse response = inventoryController.execute(command, rentalPrimary);
        Assertions.assertTrue(response.getIsSuccess());
        Assertions.assertEquals("C2,C1,V1,C3,V2",response.getResponse());
    }

    @Test
    public void testInventoryControllerWithBookedVehiclesWithSuccess(){
        String[] bookingCommand = {"BOOK","B1","CAR","5","7"};
        String[] command = {"DISPLAY_VEHICLES","B1", "4","7"};
        bookingController.execute(bookingCommand, rentalPrimary);
        CommandResponse response = inventoryController.execute(command,rentalPrimary);
        Assertions.assertTrue(response.getIsSuccess());
        Assertions.assertEquals("C1,V1,C3,V2", response.getResponse());
    }

    @Test
    public void testInventoryControllerWithNoAvailableVehicleWithFailure(){
        String[] bookingCarCommand = {"BOOK","B1","CAR","5","7"};
        String[] bookingVanCommand = {"BOOK","B1","VAN","5","7"};
        bookingController.execute(bookingCarCommand, rentalPrimary);
        bookingController.execute(bookingCarCommand, rentalPrimary);
        bookingController.execute(bookingCarCommand, rentalPrimary);
        bookingController.execute(bookingVanCommand, rentalPrimary);
        bookingController.execute(bookingVanCommand, rentalPrimary);
        String[] command = {"DISPLAY_VEHICLES","B1", "5","7"};
        CommandResponse response = inventoryController.execute(command,rentalPrimary);
        Assertions.assertTrue(response.getIsSuccess());
        Assertions.assertEquals("", response.getResponse());
    }

    @Test
    public void testInventoryControllerWithInvalidBranchWithFailure(){
        String[] command = {"DISPLAY_VEHICLES","B5","5","8"};
        CommandResponse response = inventoryController.execute(command, rentalPrimary);
        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("",response.getResponse());
    }

    private RentalBranch createBranchAndAddVehicles(){
        return RentalBranch.builder()
                .branchName("B1")
                .allowedVehicleTypes(Arrays.asList("VAN", "CAR"))
                .vehicles(Arrays.asList(
                        new VehicleBuilder().createVehicle("CAR","C1",500D),
                        new VehicleBuilder().createVehicle("CAR","C2",400D),
                        new VehicleBuilder().createVehicle("CAR","C3",600D),
                        new VehicleBuilder().createVehicle("VAN","V1",500D),
                        new VehicleBuilder().createVehicle("VAN","V2",1000D)
                ))
                .build();
    }
}
