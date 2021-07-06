package ru.mephi3.dto;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ru.mephi3.domain.Sample;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalDTO {
    private String username;
    private List<String> authorities;

    public static PrincipalDTO fromPrincipal(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            return PrincipalDTO.builder()//
                    .username(token.getName())//
                    .authorities(token.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))//
                    .build();
        } else
            throw new IllegalArgumentException("");
    }
}
