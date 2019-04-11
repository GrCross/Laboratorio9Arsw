package edu.eci.arsw.collabpaint.service.impl;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.persistence.CollabPaintPersistence;
import edu.eci.arsw.collabpaint.service.CollabPaintService;

@Service
public class CollabPaintServiceImpl implements CollabPaintService {

    @Autowired
    private CollabPaintPersistence collabPaintPersistence;

    @Override
    public void savePoint(int numdibujo, Point pt) {
        collabPaintPersistence.savePoint(numdibujo, pt);
    }

    @Override
    public ArrayBlockingQueue<Point> getPoints(int numdibujo) {
        return collabPaintPersistence.getPoints(numdibujo);
    }

    @Override
    public void resetPolygon(int numdibujo) {
        collabPaintPersistence.resetPolygon(numdibujo);
    }

}
