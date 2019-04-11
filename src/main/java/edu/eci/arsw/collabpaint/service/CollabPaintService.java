package edu.eci.arsw.collabpaint.service;

import java.util.concurrent.ArrayBlockingQueue;

import edu.eci.arsw.collabpaint.model.Point;

public interface CollabPaintService {

    public void savePoint(int numdibujo, Point pt);

    public ArrayBlockingQueue<Point> getPoints(int numdibujo);

    public void resetPolygon(int numdibujo);

}
