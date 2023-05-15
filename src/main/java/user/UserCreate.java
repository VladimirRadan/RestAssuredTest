package user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.*;

import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class UserCreate {

    @JsonProperty("firstName")
    private String first_name;

    private String lastName;
    private String email;
    private String gender;
    private String phone;
    @JsonProperty("location")
    private UserLocation userLocation;

    public static UserCreate createUser(){
        Faker faker = new Faker(new Locale("en-US"));
        UserLocation location = UserLocation.builder()
                .city(faker.address().city())
                .state(faker.address().state())
                .street(faker.address().streetAddress())
                .country(faker.address().country())
                .build();

        UserCreate user = UserCreate.builder()
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .first_name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .userLocation(location)
                .build();
        return user;
    }

}
