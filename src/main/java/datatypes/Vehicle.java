package datatypes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class Vehicle {
    private String vehicleType;
    private ID id;
    private Rate rate;
    private Set<Integer> bookedTimeslots;
}
