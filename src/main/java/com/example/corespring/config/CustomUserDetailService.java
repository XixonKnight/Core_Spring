package com.example.corespring.config;

import com.example.corespring.domain.CustomUserDetails;
import com.example.corespring.domain.make.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {
//    private final UserService  userService;
//    private final RoleService roleService;
//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userService.findUserByUsername(username);
//        user.setRoleSet(roleService.getListRoleForUser(user.getId()))
//     ;
        return new CustomUserDetails(new User());
    }

}
