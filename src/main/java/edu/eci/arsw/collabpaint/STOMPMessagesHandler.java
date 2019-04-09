package edu.eci.arsw.collabpaint;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.model.Polygon;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class STOMPMessagesHandler {
    AtomicReferenceArray <Polygon> polygons = new AtomicReferenceArray<>(50);

    @Autowired
    SimpMessagingTemplate msgt;

    @MessageMapping("/newpoint.{numdibujo}")
    public void handlePointEvent(Point pt, @DestinationVariable String numdibujo) throws Exception {
        
        
        int idDibujo = Integer.parseInt(numdibujo);
        System.out.println(idDibujo);
        System.out.println(polygons.get(idDibujo));
        if(polygons.get(idDibujo) == null){
            
            polygons.lazySet(idDibujo, new Polygon());
        }
        
        if(polygons.get(idDibujo).getNumberOfPoints() < 3){
            
            msgt.convertAndSend("/topic/newpoint." + numdibujo, pt);
            polygons.get(idDibujo).addPoint(pt);
            
        }else{
            msgt.convertAndSend("/topic/newpoint." + numdibujo, pt);
            polygons.get(idDibujo).addPoint(pt);
            msgt.convertAndSend("/topic/newpolygon."+numdibujo,polygons.get(idDibujo));
            polygons.get(idDibujo).resetPoints();
        }
        
       
    }

}