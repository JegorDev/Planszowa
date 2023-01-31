package planszowa.planszowa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import planszowa.planszowa.dto.UserDto;
import planszowa.planszowa.models.User;


@Component
public class UserConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
