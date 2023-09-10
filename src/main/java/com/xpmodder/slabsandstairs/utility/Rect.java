package com.xpmodder.slabsandstairs.utility;

import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rect {

    private final Vector3f a, b, c, d;

    public Rect(float[] vertices){

        //Convert float array to points and make a list of them
        Vector3f e = new Vector3f(vertices[0], vertices[1], vertices[2]);
        Vector3f f = new Vector3f(vertices[3], vertices[4], vertices[5]);
        Vector3f g = new Vector3f(vertices[6], vertices[7], vertices[8]);
        Vector3f h = new Vector3f(vertices[9], vertices[10], vertices[11]);

        List<Vector3f> points = Arrays.asList(e, f, g, h);


        //Calculate the center of the points
        float centerX = (e.x() + f.x() + g.x() + h.x())/4.0f;
        float centerY = (e.y() + f.y() + g.y() + h.y())/4.0f;
        float centerZ = (e.z() + f.z() + g.z() + h.z())/4.0f;

        Vector3f center = new Vector3f(centerX, centerY, centerZ);


        //Find the point closest to -1,-1,-1
        int closest = 0;
        double shortest = 100000;
        for(int i = 0; i < 4; i++){
            Vector3f point = points.get(i);

            double length = Math.sqrt(Math.pow(point.x() + 1,2) + Math.pow(point.y() + 1,2) + Math.pow(point.z() + 1,2));

            if(length < shortest){
                shortest = length;
                closest = i;
            }

        }
        //That point will be point a, then remove it from the list
        a = new Vector3f(points.get(closest).x(), points.get(closest).y(), points.get(closest).z());

        List<Vector3f> temp = new ArrayList<>();
        for(int i = 0; i < points.size(); i++){
            if(i != closest){
                temp.add(points.get(i));
            }
        }
        points = temp;

        //Get the vector from the center to a
        Vector3f start = new Vector3f(a.x(), a.y(), a.z());
        start.sub(center);

        //Calculate the magnitude of this vector
        double magStart = Math.sqrt(Math.pow(start.x(), 2) + Math.pow(start.y(), 2) + Math.pow(start.z(), 2));

        double[] angles = new double[3];
        //Calculate the angles between vector center->a and vectors center->point for all remaining points
        for(int i = 0; i < 3; i++){
            Vector3f point = new Vector3f(points.get(i).x(), points.get(i).y(), points.get(i).z());
            point.sub(center);

            double magP = Math.sqrt(Math.pow(point.x(), 2) + Math.pow(point.y(), 2) + Math.pow(point.z(), 2));

            float dotP = point.dot(start);

            double cos = (dotP / (magStart * magP));

            angles[i] = Math.acos(cos);

        }

        //Find the smallest angle and get its index
        shortest = 10000;

        for(int i = 0; i < 3; i++){
            if(angles[i] < shortest){
                shortest = angles[i];
                closest = i;
            }
        }

        //The point with the smallest angle to a will be point b
        b = new Vector3f(points.get(closest).x(), points.get(closest).y(), points.get(closest).z());

        int prev = closest;

        //Find the second-smallest angle by skipping the smallest
        shortest = 10000;

        for(int i = 0; i < 3; i++){
            if(i == prev){
                continue;
            }
            if(angles[i] < shortest){
                shortest = angles[i];
                closest = i;
            }
        }

        //The point with the second-smallest angle will be point c
        d = new Vector3f(points.get(closest).x(), points.get(closest).y(), points.get(closest).z());

        //Remove points b and c from the list, reducing it to only one point, which has to be point d
        points.remove(b);
        points.remove(d);

        c = new Vector3f(points.get(0).x(), points.get(0).y(), points.get(0).z());

        //The points should now be in clockwise order a->b->c->d with a being the one closest to -1,-1,-1


    }


    public static Rect fromQuad(BakedQuad quad){
        return new Rect(Util.getQuadVertices(quad));
    }


    //Return true if this rectangle is either equal to or inside the given rectangle
    public boolean isInside(Rect rect){

        //Check if both are in the same plane first
        if(this.a.x() == this.b.x() && this.a.x() == this.c.x() && this.a.x() == this.d.x()){
            if(!(rect.a.x() == rect.b.x() && rect.a.x() == rect.c.x() && rect.a.x() == rect.d.x() && rect.a.x() == this.a.x())){
                return false;
            }
        } else if(this.a.y() == this.b.y() && this.a.y() == this.c.y() && this.a.y() == this.d.y()){
            if(!(rect.a.y() == rect.b.y() && rect.a.y() == rect.c.y() && rect.a.y() == rect.d.y() && rect.a.y() == this.a.y())){
                return false;
            }
        } else if(this.a.z() == this.b.z() && this.a.z() == this.c.z() && this.a.z() == this.d.z()){
            if(!(rect.a.z() == rect.b.z() && rect.a.z() == rect.c.z() && rect.a.z() == rect.d.z() && rect.a.z() == this.a.z())){
                return false;
            }
        }

        if(this.a.x() >= rect.a.x() && this.a.y() >= rect.a.y() && this.a.z() >= rect.a.z()){
            if(this.b.x() <= rect.b.x() && this.b.y() <= rect.b.y() && this.b.z() <= rect.b.z()){
                if(this.c.x() <= rect.c.x() && this.c.y() <= rect.c.y() && this.c.z() <= rect.c.z()){
                    if(this.d.x() >= rect.d.x() && this.d.y() >= rect.d.y() && this.d.z() >= rect.d.z()){
                        return true;
                    }
                }
            }
        }

        return false;

    }


    public boolean isEqualTo(Rect rect){

        //Check if both rectangles are equal
        if(this.a.x() == rect.a.x() && this.a.y() == rect.a.y() && this.a.z() == rect.a.z()){
            if(this.b.x() == rect.b.x() && this.b.y() == rect.b.y() && this.b.z() == rect.b.z()){
                if(this.c.x() == rect.c.x() && this.c.y() == rect.c.y() && this.c.z() == rect.c.z()){
                    if(this.d.x() == rect.d.x() && this.d.y() == rect.d.y() && this.d.z() == rect.d.z()){
                        return true;
                    }
                }
            }
        }

        return false;

    }


    //Returns 0 if both are equal, 1 if this one is inside the other one and -1 in any other case
    public int compareTo(Rect rect){

        //Check if both are in the same plane first
        if(this.a.x() == this.b.x() && this.a.x() == this.c.x() && this.a.x() == this.d.x()){
            if(!(rect.a.x() == rect.b.x() && rect.a.x() == rect.c.x() && rect.a.x() == rect.d.x() && rect.a.x() == this.a.x())){
                return -1;
            }
        } else if(this.a.y() == this.b.y() && this.a.y() == this.c.y() && this.a.y() == this.d.y()){
            if(!(rect.a.y() == rect.b.y() && rect.a.y() == rect.c.y() && rect.a.y() == rect.d.y() && rect.a.y() == this.a.y())){
                return -1;
            }
        } else if(this.a.z() == this.b.z() && this.a.z() == this.c.z() && this.a.z() == this.d.z()){
            if(!(rect.a.z() == rect.b.z() && rect.a.z() == rect.c.z() && rect.a.z() == rect.d.z() && rect.a.z() == this.a.z())){
                return -1;
            }
        }

        //Then check if they are equal and if not, check if this one is inside the other one
        if(this.isEqualTo(rect)){
            return 0;
        } else if (this.isInside(rect)){
            return 1;
        }

        return -1;

    }


    public String toString(){
        String out = "Rect: [a(";
        out += a.x() + ", " + a.y() + ", " + a.z() + "), b(";
        out += b.x() + ", " + b.y() + ", " + b.z() + "), c(";
        out += c.x() + ", " + c.y() + ", " + c.z() + "), d(";
        out += d.x() + ", " + d.y() + ", " + d.z() + ")]";
        return out;
    }


}
