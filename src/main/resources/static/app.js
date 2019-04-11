var app = (function () {

    class Point {
        constructor(x, y) {
            this.x = x;
            this.y = y;
        }
    }

    var stompClient = null;

    var idTopic = null;

    var addPointToCanvas = function (point) {
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(point.x, point.y, 5, 0, 2 * Math.PI);
        ctx.stroke();
    };

    var addPolygonToCanvas = function (points) {
        var c2 = canvas.getContext('2d');
        c2.fillStyle = '#1AA5D1';
        c2.beginPath();
        c2.moveTo(points[0].x, points[0].y);
        for (var i = 1; i < points.length; i++) {
            c2.lineTo(points[i].x, points[i].y);
        }
        c2.closePath();
        c2.fill();
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
        console.log("Voy a enviar el punto " + pt);
        stompClient.send('/app/newpoint.' + idTopic, {}, JSON.stringify(pt));
    };

    var connectAndSubscribe = function () {
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);

        // Subscribe to /topic/newpoint when connections succeed
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            // El cliente se suscribe a una sala para pintar puntos.
            stompClient.subscribe('/topic/newpoint.' + idTopic, function (eventbody) {
                var pointReceive = JSON.parse(eventbody.body);
                //realizarAccion(pointReceive, generarAlerta);
                realizarAccion(pointReceive, addPointToCanvas);
            });
            // El cliente se suscribe a una sala para pintar polígonos.
            stompClient.subscribe('/topic/newpolygon.' + idTopic, function (eventbody) {
                var pointsReceive = JSON.parse(eventbody.body);
                addPolygonToCanvas(pointsReceive);
            });
        });
    };

    var realizarAccion = function (pointReceive, callback) {
        callback(pointReceive);
    };

    var generarAlerta = function (pointReceive) {
        alert(pointReceive);
    };

    var doMousedown = function (event) {
        var pos = getMousePosition(event);
        var newPoint = new Point(pos.x, pos.y);
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
            can.addEventListener("mousedown", doMousedown, false);
        },
        publishPoint: function (px, py) {
            var pt = new Point(px, py);
            console.info("publishing point at " + pt);
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