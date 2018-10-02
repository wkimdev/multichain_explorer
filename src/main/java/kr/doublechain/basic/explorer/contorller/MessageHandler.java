package kr.doublechain.basic.explorer.contorller;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import kr.doublechain.basic.explorer.service.websocket.vo.Message;

@RestController
public class MessageHandler {
	
	@MessageMapping("/hello")
	@SendTo("/topic/list") //subscrib
	public Message broadcasting() throws Exception {
		Message message = new Message();
		message.setId(UUID.randomUUID().toString());
		message.setContents("this is broadcast message");

		return message;
	}
}
