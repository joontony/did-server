var stompClient = null;
var sender = "";
var receiver = "";
var routingKey = "";
var clubSeq = 0;
//var url = "http://localhost:30001";
//var url = "https://rhymecard.avchain.io:31001";
var url = "https://rhymecard-dev.avchain.io:31001";

var subscribeAddress 	= "/queue/rabbit-message/";
var sendAddress 		= "/app/rabbit/send-message/";

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {	
	clubSeq = Number($("#clubSeq").val());
	sender = $("#sender").val();
	receiver = $("#receiver").val();	
	routingKey = $("#routingKey").val(); // seq issuerId holderId	
	
    var socket = new SockJS(url + '/ws');    
    console.log('***** connect url : ' + url + '/ws');    
    
    stompClient = Stomp.over(socket); 
    var headers = { 'sender' : sender };    
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('***** Connected: ' + frame + 'subscribe routingKey : ' + routingKey);
        stompClient.subscribe('/queue/subscribe-message/'+ routingKey, function (message) {	
			var body = JSON.parse(message.body);
            showGreeting(body.message + body.originalFileName + body.fileUrl);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("***** Disconnected");
}

function sendMessage() {
	var message = $("#message").val();	
	console.log('***** SendMsg : /app/rabbit/send-message ' + 	JSON.stringify({'clubSeq':clubSeq,'routingKey':routingKey, 'sender':  sender , 'receiver':  receiver , 'message': $("#message").val()}));   
    stompClient.send("/app/rabbit/send-message", {}, 			JSON.stringify({'clubSeq':clubSeq,'routingKey':routingKey, 'sender':  sender , 'receiver':  receiver , 'message': $("#message").val()}));
   
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
});