package controllers;

import builders.RentalPrimaryBuilder;
import builders.VehicleBuilder;
import datatypes.CommandResponse;
import datatypes.RentalBranch;
import datatypes.RentalPrimary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BookingControllerTest {

    private final BookingController bookingController = new BookingController();

    private RentalPrimary rentalPrimary = new RentalPrimaryBuilder().buildRentalPrimary();

    @BeforeEach
    public void setup(){
        rentalPrimary = RentalPrimary.builder()
                .branches(Arrays.asList(createBranchAndAddVehicles()))
                .build();
    }

    @Test
    public void testBookingWithAvailableVehicleWithSuccess(){
        String[] command = {"BOOK","B1","CAR","5","7"};
        CommandResponse response = bookingController.execute(command, rentalPrimary);
        Assertions.assertTrue(response.getIsSuccess());
        Assertions.assertEquals("800",response.getResponse());
    }

    @Test
    public void testBookingWithWrongBranchWithFailure(){
        String[] command = {"BOOK","B3","CAR","5","7"};
        CommandResponse response = bookingController.execute(command, rentalPrimary);
        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("-1",response.getResponse());
    }

    @Test
    public void testBookingMultipleVehiclesForSameTimeWithSuccess(){
        String[] command = {"BOOK","B1","CAR","5","7"};
        bookingController.execute(command, rentalPrimary);
        CommandResponse response = bookingController.execute(command, rentalPrimary);
        Assertions.assertTrue(response.getIsSuccess());
        Assertions.assertEquals("1000", response.getResponse());
    }

    @Test
    public void testBookingMultipleVehiclesForSameTimeWithSurgePricingSuccess(){
        String[] command = {"BOOK","B1","CAR","5","7"};
        bookingController.execute(command, rentalPrimary);
        bookingController.execute(command, rentalPrimary);
        bookingController.execute(command, rentalPrimary);
        bookingController.execute(command, rentalPrimary);
        CommandResponse response = bookingController.execute(command, rentalPrimary);
        Assertions.assertTrue(response.getIsSuccess());
        Assertions.assertEquals("2200", response.getResponse());
    }

    @Test
    public void testBookingMultipleVehiclesForSameTimeWithFailure(){
        String[] command = {"BOOK","B1","CAR","5","7"};
        bookingController.execute(command, rentalPrimary);
        bookingController.execute(command, rentalPrimary);
        bookingController.execute(command, rentalPrimary);
        bookingController.execute(command, rentalPrimary);
        bookingController.execute(command, rentalPrimary);
        CommandResponse response = bookingController.execute(command, rentalPrimary);
        Assertions.assertFalse(response.getIsSuccess());
        Assertions.assertEquals("-1", response.getResponse());
    }


    private RentalBranch createBranchAndAddVehicles(){
        return RentalBranch.builder()
                .branchName("B1")
                .allowedVehicleTypes(Arrays.asList("VAN", "CAR"))
                .vehicles(Arrays.asList(
                        new VehicleBuilder().createVehicle("CAR","C1",500),
                        new VehicleBuilder().createVehicle("CAR","C2",400),
                        new VehicleBuilder().createVehicle("CAR","C3",600),
                        new VehicleBuilder().createVehicle("CAR","C4",700),
                        new VehicleBuilder().createVehicle("CAR","C5",1000)
                ))
                .build();
    }
}
