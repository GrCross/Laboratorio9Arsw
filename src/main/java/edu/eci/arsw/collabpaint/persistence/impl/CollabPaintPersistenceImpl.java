package edu.eci.arsw.collabpaint.persistence.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.persistence.CollabPaintPersistence;

@Service
public class CollabPaintPersistenceImpl implements CollabPaintPersistence {

    private static ConcurrentHashMap<Integer, ArrayBlockingQueue<Point>> polygons = new ConcurrentHashMap<>();

    @Override
    public void savePoint(int numdibujo, Point pt) {
        if (!polygons.containsKey(numdibujo)) {
            ArrayBlockingQueue<Point> points = new ArrayBlockingQueue<>(100);
            points.add(pt);
            polygons.put(numdibujo, points);
        } else {
            ArrayBlockingQueue<Point> points = polygons.get(numdibujo);
            points.add(pt);
            polygons.replace(numdibujo, points);
        }
    }

    @Override
    public ArrayBlockingQueue<Point> getPoints(int numdibujo) {
        return polygons.get(numdibujo);
    }

    @Override
    public void resetPolygon(int numdibujo) {
        ArrayBlockingQueue<Point> points = polygons.get(numdibujo);
        points.clear();
        polygons.replace(numdibujo, points);
    }

}
