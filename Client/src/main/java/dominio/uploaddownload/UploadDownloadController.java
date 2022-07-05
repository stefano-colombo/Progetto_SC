
package dominio.uploaddownload;


import java.io.IOException;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

//import dominio.filestorage.FileStorageImpl;
import dominio.messaggio.Messaggio;
import dominio.messaggio.MessaggioService;
import dominio.rabbitmp.*;

@Controller
public class UploadDownloadController {

	// @Autowired
	// FileStorageImpl fileStorage;
	
	// private String utente;
	
	// @Autowired
	// public UploadDownloadController(@Value("${custom.mittente}") String utente){
	// 	this.utente=utente;
	// }
	@Autowired
	Producer_x producer;
	
	@Autowired
	private MessaggioService messaggioService;
	
   @GetMapping("/")
    public String listaMessaggi(Model model) throws IOException {
        return "redirect:/login";
    }

	@PostMapping("/chat")
    public String listaMessaggi2(Model model,Messaggio e) throws IOException {
	   
	   List<Messaggio> list=messaggioService.getAllMessaggi();
	   
	  	model.addAttribute("nome",e.getMittente());
	   	model.addAttribute("messaggi",list);
        return "interfaccia_chat";
    }
   	
	@PostMapping("/messaggio")
    public void InvioMessaggio(Messaggio e, Model model) {
    	if (e.getId() != null) {
			
			model.addAttribute("Messaggio",null);
		}
		//mando il messaggio al producer e salvo il messaggio nel database
    	producer.run(e.getContenuto(),e.getMittente(), true);
		// List<Messaggio> list=messaggioService.getAllMessaggi();
	   
	  	// model.addAttribute("nome",e.getMittente());
	   	// model.addAttribute("messaggi",list);
		// //messagingTemplate.convertAndSend("/topic/messaggi_chat", e.getContenuto());
        // return "interfaccia_chat";
	
	}

	@GetMapping("/login")
    public String loggin(Model model) throws IOException {
        return "login";
    }
	

}