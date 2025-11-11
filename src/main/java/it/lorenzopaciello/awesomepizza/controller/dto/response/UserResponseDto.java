package it.lorenzopaciello.awesomepizza.controller.dto.response;

import it.lorenzopaciello.awesomepizza.model.Pizza;
import it.lorenzopaciello.awesomepizza.model.Role;
import it.lorenzopaciello.awesomepizza.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private String username;
    private boolean enabled;
    List<RoleResponseDto> roles = new ArrayList<>();

    public UserResponseDto(User user){
        this.username = user.getUsername();
        this.enabled = user.isEnabled();

        if(user.getRoles() != null && !user.getRoles().isEmpty()){
            roles = user.getRoles().stream().map(RoleResponseDto::new).toList();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoleResponseDto{
        private String name;

        public RoleResponseDto(Role role){
            this.name = role.getName();
        }

    }
}
