package edu.eci.arsw.collabpaint;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.service.CollabPaintService;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class STOMPMessagesHandler {

    @Autowired
    private CollabPaintService collabPaintService;

    @Autowired
    SimpMessagingTemplate msgt;

    @MessageMapping("/newpoint.{numdibujo}")
    public void handlePointEvent(Point pt, @DestinationVariable int numdibujo) throws Exception {
        System.out.println("¡Nuevo punto recibido en el servidor!: " + pt);
        collabPaintService.savePoint(numdibujo, pt);
        ArrayBlockingQueue<Point> points = collabPaintService.getPoints(numdibujo);
        if (points.size() < 3) {
            msgt.convertAndSend("/topic/newpoint." + numdibujo, pt);
        } else {
            System.out.println("Ya hay 3 o más puntos. Se debe dibujar un polígono.");
            msgt.convertAndSend("/topic/newpoint." + numdibujo, pt);
            msgt.convertAndSend("/topic/newpolygon." + numdibujo, points);
            collabPaintService.resetPolygon(numdibujo);
        }
    }

}
