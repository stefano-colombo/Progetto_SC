package dominio.rabbitmp;


import java.io.FileOutputStream;
import java.io.IOException;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Component;


//import dominio.filestorage.FileStorageImpl;
import dominio.messaggio.MessaggioService;
import dominio.messaggio.Messaggio;
import org.json.*;


@Component 
public class Consumer_x{
	//@Autowired
	//FileStorageImpl fileStorage;
	
	@Autowired
	MessaggioService messaggioService;
	
	String percorsoFileRicevuti;
	String destinatario;

	@Autowired
	Producer_x producer;
	
	String estensione="",nomefile="";
	boolean grande=false;
	
	
	@RabbitListener(queues = "${coda.consumatore}" )
	public void run(Message message) throws InterruptedException,IOException {
		
		String body=new String(message.getBody());
		System.out.println(" \t\t\t\t\t\t\t\t[received]: '" +body+ "'");
		JSONObject obj= new JSONObject(body);

		producer.run(obj.getString("body"),obj.getString("mittente"), true);

		//savaltaggio del messaggio sul database
		messaggioService.addMessaggio(new Messaggio(obj.getString("mittente"),obj.getString("body"),"messaggio"));
	}
	
}
