package edu.eci.arsw.collabpaint.persistence;

import java.util.concurrent.ArrayBlockingQueue;

import edu.eci.arsw.collabpaint.model.Point;

public interface CollabPaintPersistence {

    public void savePoint(int numdibujo, Point pt);

    public ArrayBlockingQueue<Point> getPoints(int numdibujo);

    public void resetPolygon(int numdibujo);

}
