package logic.utils;

import logic.DTO.UserDTO;
import logic.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "description", ignore = true)
    UserDTO userToUserDTO(User user);


    @InheritInverseConfiguration
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "description", ignore = true)
    User userDTOToUser(UserDTO userDTO);


}
