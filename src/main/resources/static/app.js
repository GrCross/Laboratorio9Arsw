var app = (function () {

    class Point{
        constructor(x,y){
            this.x=x;
            this.y=y;
        }        
    }
    
    var stompClient = null;

    var idTopic = null;

    var addPointToCanvas = function (point) {        
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(point.x, point.y, 10, 0, 2 * Math.PI);
        ctx.stroke();
    };  
    
    var addPolygonToCanvas = function(polygon){
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        ctx.fillStyle = '#f00';
        ctx.beginPath();

        ctx.moveTo(polygon.points[0].x, polygon.points[0].y);
        for (let i = 0; i < polygon.points.length; i++) {
            
            ctx.lineTo(parseInt(polygon.points[i].x), parseInt(polygon.points[i].y));
        }
        ctx.closePath();
        ctx.fill();
    };
    
    var getMousePosition = function (evt) {
        canvas = document.getElementById("canvas");
        var rect = canvas.getBoundingClientRect();
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    };

    var sendPoint = function (pt) {
        console.log("Voy a enviar el punto "+pt);
        stompClient.send('/app/newpoint.'+idTopic, {}, JSON.stringify(pt));
    };

    var connectAndSubscribe = function () {
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        
        //subscribe to /topic/newpoint when connections succeed
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/newpoint.'+idTopic, function (eventbody) {
                //alert(eventbody);
                var pointReceive = JSON.parse(eventbody.body);
                addPointToCanvas(pointReceive);
            });

            stompClient.subscribe('/topic/newpolygon.'+idTopic,function(eventbody){
                console.log(eventbody);
                var polygonReceive = JSON.parse(eventbody.body);
                addPolygonToCanvas(polygonReceive);
            });
        });
    };

    var doMousedown = function(event){
        var pos = getMousePosition(event);
        var newPoint = new Point(pos.x,pos.y);
        sendPoint(newPoint);
    };   

    return {

        init: function () {
            //websocket connection
            idTopic = $("#topicDibujo").val();
            connectAndSubscribe();
        },

        initListener: function () {
            var can = document.getElementById("canvas");
            can.addEventListener("mousedown",doMousedown,false);
        },

        publishPoint: function(px,py){
            var pt = new Point(px,py);
            console.info("publishing point at "+pt);
            addPointToCanvas(pt);
            sendPoint(pt);
            //publicar el evento
        },

        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }
    };

})();