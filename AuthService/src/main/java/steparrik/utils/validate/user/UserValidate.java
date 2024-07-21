package steparrik.utils.validate.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.service.UserService;
import steparrik.utils.exceptions.ExceptionEntity;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidate {
    private final UserService userService;

    public ExceptionEntity validateRegistrationDate(RegistrationUserDto registrationUserDto){

        if (registrationUserDto.getUsername() == null || registrationUserDto.getUsername().isEmpty()) {
            return new ExceptionEntity("Ник - обязательное поле", LocalDateTime.now());
        }
        if(!checkUsername(registrationUserDto.getUsername())){
            return new ExceptionEntity("Ник содержит запрещенные символы, используйте только анлглийский алфавит", LocalDateTime.now());
        }
        if (registrationUserDto.getPassword() == null || registrationUserDto.getPassword().isEmpty()) {
            return new ExceptionEntity("Пароль - обязательное поле", LocalDateTime.now());
        }
        if (userService.findUserByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ExceptionEntity("Пользователь с таким ником уже существует", LocalDateTime.now());
        }
        if (userService.findUserByPhoneNumber(registrationUserDto.getPhoneNumber()).isPresent()) {
            return new ExceptionEntity("Данный номер телефона уже используется другим пользователем", LocalDateTime.now());
        }
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ExceptionEntity("Пароли не совпадают", LocalDateTime.now());
        }

        return null;
    }

    public boolean checkUsername(String username){
        boolean response;
        List<String> forbiddenSymbols = List.of(
                "А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я",
                "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я",
                "\u0000", "\u0001", "\u0002", "\u0003", "\u0004", "\u0005", "\u0006", "\u0007", "\u0008", "\u0009", "\n", "\u000B", "\u000C", "\r", "\u000E", "\u000F",
                "\u0010", "\u0011", "\u0012", "\u0013", "\u0014", "\u0015", "\u0016", "\u0017", "\u0018", "\u0019", "\u001A", "\u001B", "\u001C", "\u001D", "\u001E", "\u001F",
                "\u007F",
                " ", "\t",
                "(", ")", "<", ">", "@", ",", ";", ":", "\\", "\"", "/", "[", "]", "?", "=", "{", "}"
        );

        String[] symbolsFromUsername = username.split("");
        for(String letter : symbolsFromUsername){
            if(forbiddenSymbols.contains(letter)){
                return false;
            }
        }
        return true;
    }
}
