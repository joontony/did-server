var stompClient = null;
var userId = "";
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
	//userId = "ID202309131247262204"; //$("#name").val();	
	userId = "test123222"; //$("#name").val();
	//var url = "http://localhost:30001";
	//var url = "https://rhymecard.avchain.io:31001";
	var url = "https://rhymecard-dev.avchain.io:31001";
    var socket = new SockJS(url + '/ws');    
    console.log('connect url : ' + url + '/ws');    
    
    stompClient = Stomp.over(socket); 
    var headers = { 'sender' : userId };    
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/waiting-message/'+userId, function (message) {
            //showGreeting(JSON.parse(greeting.body).content);
            showGreeting(message.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

// 사용안함 
function sendName() {
    //stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));    
    //stompClient.send("/app/send-message/"+ userId, {}, JSON.stringify({'userId':  userId ,'name': $("#name").val()}));
   
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
    $( "#send" ).click(function() { sendName(); });
});