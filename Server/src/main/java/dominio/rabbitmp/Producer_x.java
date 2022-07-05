package dominio.rabbitmp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import org.json.*;




@Component
public class Producer_x{
	
	@Autowired
	private Queue queue;
	
	@Autowired
	private RabbitTemplate rabbit;
	
	Scanner tastiera=new Scanner(System.in);
	
	//@Scheduled(fixedDelay = 1000, initialDelay = 500)
	public void run(String mess,String mittente, boolean messaggio) {	
		String jstring="{\"mittente\":\""+mittente+"\",\"body\":\""+mess+"\" }";
		rabbit.convertAndSend(queue.getName(), jstring);
	}
}
