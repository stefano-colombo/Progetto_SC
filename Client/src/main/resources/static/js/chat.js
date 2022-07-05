var stompClient = null;
var name = null;



function connect() {
    var socket = new SockJS('/websocketApp');
    //stompClient = Stomp.over(socket);
    //var socket = new  WebSocket('ws://172.17.0.2:61613');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, connectionSuccess);
    console.log("CONNNESSIONE RIUSCITA");
    
}
function connectionSuccess() {
    stompClient.subscribe('/topic/messaggi_chat', onMessageReceived);
    console.log("messaggio ricevuto1");
    
}

function onMessageReceived(messaggio) {
    console.log("messaggio ricevuto2: ");
    console.log(messaggio.body);
    var body = JSON.parse(messaggio.body);
    console.log(body);
    if(body.mittente!=username){
        // const elementoNuovo ='<td></td><td></td><td></td><td align="right">'+
        //                     '<div >'+body.mittente+'</div>'+'<div >'+body.body+'</div></td><td></td>';
        
        const elementoNuovo= '<div class="messaggio">'+
        '<div class="mittente" >'+body.mittente+'</div>'+
       '<div class="testo" >'+body.body+'</div></div>';
                
        var htmlPrecedente=document.getElementById("lista_messaggi").innerHTML;
        console.log(htmlPrecedente);
        document.getElementById("lista_messaggi").innerHTML=htmlPrecedente+elementoNuovo;
        //window.opener.location.href=window.opener.location;
        //window.location.reload();
    }
    else{
        document.querySelector('#chatMessage').value="";
    }
    document.getElementById("lista_messaggi").scrollTop=document.getElementById("lista_messaggi").scrollHeight;
}
connect();

document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true);
//var nome_utente=document.getElementById('#nome').value;
//console.log(nome_utente);

function sendMessage(event) {
    event.preventDefault();

    var messageContent = document.querySelector('#chatMessage').value.trim();
    console.log(messageContent);
    let formData = new FormData();

    formData.append('mittente', username);
    formData.append('contenuto',messageContent);
    formData.append('tipo', 'messaggio');

    fetch("/messaggio",
    {
        body: formData,
        method: "post"
    });


    const elementoNuovo= '<div class="messaggio-mio">'+
                            '<div class="mittente-mio" >'+username+'</div>'+
                           '<div class="testo" >'+messageContent+'</div></div>'; 

    var htmlPrecedente=document.getElementById("lista_messaggi").innerHTML;
    console.log(htmlPrecedente);
    document.getElementById("lista_messaggi").innerHTML=htmlPrecedente+elementoNuovo;
    document.getElementById("lista_messaggi").scrollTop=document.getElementById("lista_messaggi").scrollHeight;
    
    
}