//package steparrik.manager;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import steparrik.dto.user.EditUserDto;
//import steparrik.model.user.User;
//import steparrik.service.UserDetailService;
//import steparrik.service.UserService;
//import steparrik.utils.mapper.user.ProfileUserMapper;
//
//import java.security.Principal;
//
//@Component
//@RequiredArgsConstructor
//public class EditManager {
//    private final UserService userService;
//    private final UserDetailService userDetailService;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final JwtTokenUtil jwtTokenUtil;
//
//    public ResponseEntity<?> editUser(EditUserDto editUserDto, Principal principal) {
//        User user = userService.findUserByUsername(principal.getName()).get();
//        if(editUserDto.getUsername()!=null && !editUserDto.getUsername().isEmpty()){
//            if(userService.findUserByUsername(editUserDto.getUsername()).isPresent()){
//                return ResponseEntity.status(404).body("Ник занят другим пользователем");
//            }
//            user.setUsername(editUserDto.getUsername());
//        }
//        if(editUserDto.getFullName()!=null && !editUserDto.getFullName().isEmpty()){
//            user.setFullName(editUserDto.getFullName());
//        }
//        if(editUserDto.getPassword()!=null && !editUserDto.getPassword().isEmpty()){
//            user.setPassword(bCryptPasswordEncoder.encode(editUserDto.getPassword()));
//        }
//        userService.save(user);
//        UserDetails userDetails = userDetailService.loadUserByUsername(user.getUsername());
//        String token = jwtTokenUtil.generateToken(userDetails);
//        return ResponseEntity.ok(new JwtResponseDto(token));
//    }
//}
