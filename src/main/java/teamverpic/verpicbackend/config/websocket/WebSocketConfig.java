package teamverpic.verpicbackend.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import teamverpic.verpicbackend.domain.chat.handler.ChatRoomSubscriptionInterceptor;
import teamverpic.verpicbackend.domain.chat.handler.CustomHandshakeHandler;


@EnableScheduling
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

   // private final StompHandler stompHandler;
    private final ChatRoomSubscriptionInterceptor chatRoomSubscriptionInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("https://www.verpic.net")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatRoomSubscriptionInterceptor);
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }




}
