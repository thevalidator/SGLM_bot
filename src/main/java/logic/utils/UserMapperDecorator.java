package logic.utils;

import logic.DTO.StockDTO;
import logic.DTO.UserDTO;
import logic.Stock;
import logic.User;
import java.util.HashMap;
import java.util.Map;

public class UserMapperDecorator implements UserMapper {


    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId( user.getId() );
        userDTO.setOrdinalNumber( user.getOrdinalNumber() );
        userDTO.setJoinStamp( user.getJoinStamp() );
        userDTO.setTelegramId( user.getTelegramId() );
        userDTO.setChatId( user.getChatId() );
        userDTO.setName( user.getName() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        Map<String, Stock> userStocks = user.getStocks();
        Map<String, StockDTO> userDTOStocks = new HashMap<>();
        if ( userStocks != null ) {
            for (Map.Entry<String, Stock> e: userStocks.entrySet()) {
                userDTOStocks.put(e.getKey(), new StockDTO(e.getValue().getId(), userDTO, e.getValue().getName(),
                        e.getValue().getLowTarget(), e.getValue().getHighTarget(), e.getValue().hasNotify()));
            }
            userDTO.setStocks(userDTOStocks);
        }
        return userDTO;
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }
        User user = new User();
        user.setId( userDTO.getId() );
        user.setOrdinalNumber( userDTO.getOrdinalNumber() );
        user.setJoinStamp( userDTO.getJoinStamp() );
        user.setTelegramId( userDTO.getTelegramId() );
        user.setChatId( userDTO.getChatId() );
        user.setName( userDTO.getName() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        Map<String, StockDTO> userDTOStocks = userDTO.getStocks();
        Map<String, Stock> userStocks = new HashMap<>();
        if ( userDTOStocks != null ) {
            for (Map.Entry<String, StockDTO> e: userDTOStocks.entrySet()) {
                userStocks.put(e.getKey(), new Stock(e.getValue().getId(), user, e.getValue().getName(),
                        e.getValue().getLowTarget(), e.getValue().getHighTarget(), e.getValue().hasNotify()));
            }
            user.setStocks(userStocks);
        }
        return user;
    }
}
