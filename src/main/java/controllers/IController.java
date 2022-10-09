package controllers;

import datatypes.CommandResponse;
import datatypes.RentalPrimary;

public interface IController<T> {
    CommandResponse execute(String[] command, RentalPrimary rentalPrimary);

    default String getBranchName(String[] command){return command[1];}

}
