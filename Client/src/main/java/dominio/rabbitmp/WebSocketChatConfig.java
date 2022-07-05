package dominio.rabbitmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.*;

@Component 
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketChatConfig implements WebSocketMessageBrokerConfigurer {


	private String indirizzo;

	@Autowired
	public WebSocketChatConfig (@Value("${rabbit.indirizzo}") String rabbit) {
		this.indirizzo=rabbit;
	}


	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocketApp").withSockJS();
	}


	@Override
	public void configureMessageBroker (MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableStompBrokerRelay("/topic")
                .setRelayHost(indirizzo)
                .setRelayPort(61613)
                .setClientLogin("guest")
				.setClientPasscode("guest");
	}
}