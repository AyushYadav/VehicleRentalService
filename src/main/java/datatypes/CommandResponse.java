package datatypes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommandResponse {
    Boolean isSuccess;
    String response;
    RentalPrimary rentalPrimary;

    public static CommandResponse createResponse(Boolean isSuccess, RentalPrimary rentalPrimary, String response){
        return CommandResponse.builder()
                .isSuccess(isSuccess)
                .rentalPrimary(rentalPrimary)
                .response(response)
                .build();
    }
}
