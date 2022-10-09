package datatypes;

public enum Commands {
    ADD_BRANCH("ADD_BRANCH"),
    ADD_VEHICLE("ADD_VEHICLE"),
    BOOK("BOOK"),
    DISPLAY_VEHICLES("DISPLAY_VEHICLES"),
    UNDEFINED("UNDEFINED");
    private final String commandName;

    Commands(String commandName){
        this.commandName = commandName;
    }

    public String getValue(){
        return commandName;
    }
}
