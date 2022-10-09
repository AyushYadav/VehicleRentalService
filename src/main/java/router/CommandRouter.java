package router;

import builders.RentalPrimaryBuilder;
import controllers.BookingController;
import controllers.BranchAdditionController;
import controllers.IController;
import controllers.InventoryController;
import controllers.VehicleAdditionController;
import datatypes.CommandResponse;
import datatypes.Commands;
import datatypes.RentalPrimary;

import java.util.Objects;

public class CommandRouter {

    public CommandRouter(){
        this.branchAdditionController = new BranchAdditionController();
        this.vehicleAdditionController = new VehicleAdditionController();
        this.bookingController = new BookingController();
        this.inventoryController = new InventoryController();
        this.rentalPrimary = new RentalPrimaryBuilder().buildRentalPrimary();
    }

    private final RentalPrimary rentalPrimary;
    private final IController<BranchAdditionController> branchAdditionController;
    private final IController<VehicleAdditionController> vehicleAdditionController;
    private final IController<BookingController> bookingController;
    private final IController<InventoryController> inventoryController;

    public void routeCommand(final String[] command){
        Commands commandDirective = getUserCommand(command[0]);
        CommandResponse response = null;
        switch (commandDirective){
            case ADD_BRANCH:
                response = branchAdditionController.execute(command, rentalPrimary);
                break;
            case ADD_VEHICLE:
                response = vehicleAdditionController.execute(command, rentalPrimary);
                break;
            case BOOK:
                response = bookingController.execute(command, rentalPrimary);
                break;
            case DISPLAY_VEHICLES:
                response = inventoryController.execute(command, rentalPrimary);
                break;
            case UNDEFINED:
                break;
        }
        displayResponse(response);
    }

    private Commands getUserCommand(String userInputCommand){
        if(userInputCommand.equalsIgnoreCase(Commands.ADD_VEHICLE.getValue())){
            return Commands.ADD_VEHICLE;
        } else if (userInputCommand.equalsIgnoreCase(Commands.ADD_BRANCH.getValue())) {
            return Commands.ADD_BRANCH;
        } else if (userInputCommand.equalsIgnoreCase(Commands.BOOK.getValue())) {
            return Commands.BOOK;
        } else if (userInputCommand.equalsIgnoreCase(Commands.DISPLAY_VEHICLES.getValue())){
            return Commands.DISPLAY_VEHICLES;
        }
        return Commands.UNDEFINED;
    }

    private void displayResponse(CommandResponse response) {
        if(Objects.nonNull(response))
            System.out.println(response.getResponse());
    }
}
