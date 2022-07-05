package dominio.rabbitmp;


import java.io.IOException;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


//import dominio.filestorage.FileStorageImpl;
import dominio.messaggio.MessaggioService;


import org.springframework.messaging.simp.SimpMessageSendingOperations;


@Component 
public class Consumer_x{
	//@Autowired
	//FileStorageImpl fileStorage;
	// @Autowired
	// public Consumer_x(@Value("${custom.destinatario}") String destinatario) {
	//  	this.destinatario=destinatario;
	// }	

	@Autowired
	MessaggioService messaggioService;
	
	String percorsoFileRicevuti;
	String destinatario;
	
	
	
	String estensione="",nomefile="";
	boolean grande=false;
	
	@Autowired
    private SimpMessageSendingOperations messagingTemplate;

	@RabbitListener(queues = "${coda.consumatore}" )
	public void run(Message message) throws InterruptedException,IOException {
		
		String body=new String(message.getBody());
		System.out.println(" \t\t\t\t\t\t\t\t[received]: '" +body+ "'");

		messagingTemplate.convertAndSend("/topic/messaggi_chat", body.toString());
		//savaltaggio del messaggio sul database
		//messaggioService.addMessaggio(new Messaggio(obj.getString("mittente"),obj.getString("body"),"messaggio","vuoto","vuoto","vuoto"));
	
	}
	
}
