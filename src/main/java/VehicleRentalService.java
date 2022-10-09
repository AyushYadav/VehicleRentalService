import router.CommandRouter;

import java.io.FileInputStream;
import java.util.Scanner;

public class VehicleRentalService {

    public static void main(String[] args) throws Exception {
        CommandRouter commandRouter = new CommandRouter();
        Scanner scanner = new Scanner(new FileInputStream(args[0]));

        while(scanner.hasNext()){
            String line = scanner.nextLine();
            if(line.equalsIgnoreCase("EXIT"))
                break;
            commandRouter.routeCommand(line.split(" "));
        }
    }
}
