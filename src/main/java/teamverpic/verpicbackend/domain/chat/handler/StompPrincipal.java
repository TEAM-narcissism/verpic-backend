package teamverpic.verpicbackend.domain.chat.handler;

import lombok.Getter;

import java.security.Principal;

class StompPrincipal implements Principal {
    private String name;

    StompPrincipal(String name) { this.name = name; }

    @Override
    public String getName() { return name;}
}