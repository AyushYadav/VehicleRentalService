import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;


class VehicleRentalServiceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpstreams(){
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams(){
        System.setOut(originalOut);
    }

    @Test
    public void testDriverInputAndVerifyExpectedOutput() throws Exception {
        File inputFileDirectory = new File("./src/main/resources/DriverTest.txt");
        File outputFileDirectory = new File("./src/main/resources/DriverTestResponse.txt");
        String expectedOutput = new String(Files.readAllBytes(Paths.get(outputFileDirectory.getCanonicalPath())));
        VehicleRentalService.main(new String[]{inputFileDirectory.getCanonicalPath()});
        Assertions.assertEquals(expectedOutput, outContent.toString());
        System.setOut(originalOut);
        System.out.println(outContent);
    }

    @Test
    public void testMasterInputAndVerifyExpectedOutput() throws Exception {
        File inputFileDirectory = new File("./src/main/resources/MasterTest.txt");
        File outputFileDirectory = new File("./src/main/resources/MasterTestResponse.txt");
        String expectedOutput = new String(Files.readAllBytes(Paths.get(outputFileDirectory.getCanonicalPath())));
        VehicleRentalService.main(new String[]{inputFileDirectory.getCanonicalPath()});
        Assertions.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testWithBadInputCommands() throws Exception {
        File inputFileDirectory = new File("./src/main/resources/BadTestInput.txt");
        VehicleRentalService.main(new String[]{inputFileDirectory.getCanonicalPath()});
        Assertions.assertEquals("", outContent.toString());
    }
}