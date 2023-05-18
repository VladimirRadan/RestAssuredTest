package user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class UserLocation {

    private String street;
    private String city;
    private String state;
    private String country;

}
