package gruppe5.collision;

import gruppe5.common.data.Entity;
import com.badlogic.gdx.math.Vector2;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Daniel
 */

@ServiceProvider(service = IEntityProcessingService.class)
public class CollisionControlSystem implements IEntityProcessingService {

    Vector2 mtv = new Vector2();

    @Override
    public void process(GameData gameData, World world) {
        for (Entity shape1 : world.getEntities()) {
            for (Entity shape2 : world.getEntities()) {
                Vector2 velocity = new Vector2(shape1.getDx(), shape1.getDy());
                if (checkConditions(shape1, shape2, mtv, velocity)) {
                    shape1.setPosition(shape1.getX() + (velocity.x + mtv.x), shape1.getY() + (velocity.y + mtv.y));
                    shape1.setLife(shape1.getLife() - shape2.getDamage());
                    shape1.setIsHit(true);
                }

//                if (shape1 != shape2) {
//                    //velocity is the velocity of polygon 1
//                    Vector2 velocity = new Vector2(shape1.getDx(), shape1.getDy());
//
//                    if (polyCollideMTV(shape2, shape1, mtv, velocity)) {
//                        if (shape1.getType() != EntityType.ASTEROIDS) { //Erstattes med Static vs Dynamic
//                            shape1.setPosition(shape1.getX() + (velocity.x + mtv.x), shape1.getY() + (velocity.y + mtv.y));
//                            shape1.setLife(shape1.getLife() - shape2.getDamage());
//                            System.out.println(shape1.getType() + " : health: " + shape1.getLife());
//                            System.out.println("shape2 damage: " + shape2.getDamage());
//                            shape1.setIsHit(true);
//
//                        }
//                    }
//                }
            }
        }
    }

    private boolean checkConditions(Entity shape1, Entity shape2, Vector2 mtv, Vector2 velocity) {
        if (shape1 == shape2) {
            return false;
        }
        if (!polyCollideMTV(shape2, shape1, mtv, velocity)) {
            return false;
        }
        if (!shape1.isDynamic()) {
            return false;
        }
        return true;
    }

    private float min;

    private float max;

    private float getIntervalDistance(Projection a, Projection b) {
        if ((a.min < b.min)) {
            return (float) (b.min - a.max);
        } else {
            return (float) (a.min - b.max);
        }
    }

    public Vector2[] getVertices(Entity shape) {
        Vector2[] axes = new Vector2[shape.getShapeX().length];
        float[] verticesX = shape.getShapeX();
        float[] verticesY = shape.getShapeY();
        for (int i = 0; i < shape.getShapeX().length; i++) {
            axes[i] = new Vector2(verticesX[i], verticesY[i]);
        }
        return axes;
    }

    private Projection project(Vector2 axis, Entity shape) {
        Projection proj = new Projection(min, max);
        float p;
        //get initial projection on first vertex
        //and set min and max values to initial projection value
        p = getVertices(shape)[0].dot(axis);
        min = p;
        max = p;
        /*
        loop through all the verts of the poly 
        and find out if new projections of vertex
        is the new min or max as we need only the 
        min and max points to test for 1d intesection
         */
        for (int i = 0; (i <= (getVertices(shape).length) - 1); i++) {
            p = getVertices(shape)[i].dot(axis);
            if ((p < min)) {
                min = p;
            }
            if ((p > max)) {
                max = p;
            }
        }

        proj.min = min;
        proj.max = max;
        return proj;
    }

    //  returns a perpendicular vector(normal) of a line segment
    private Vector2 get2dnormal(float x1, float y1, float x2, float y2, boolean s) {
        Vector2 normal = new Vector2();
        if (s) {
            normal.x = ((y2 - y1) * -1);
            normal.y = (x2 - x1);
        } else {
            normal.x = (y2 - y1);
            normal.y = ((x2 - x1) * -1);
        }

        //normalizes vertor (skalere den til længden 1)
        return normal.nor();
    }

    //  checks whether poly1 and poly2 collides using SAT + MTV
    private boolean polyCollideMTV(Entity p1, Entity p2, Vector2 mtv, Vector2 velocity) {

        Vector2[] p1normals = new Vector2[getVertices(p1).length];
        Vector2[] p2normals = new Vector2[getVertices(p2).length];
        for (int i = 0; i < getVertices(p1).length; i++) {
            int j = (i + 1) % getVertices(p1).length;
            p1normals[i] = get2dnormal(getVertices(p1)[i].x, getVertices(p1)[i].y, getVertices(p1)[j].x, getVertices(p1)[j].y, true);
        }
        for (int i = 0; i < getVertices(p2).length; i++) {
            int j = (i + 1) % getVertices(p2).length;
            p2normals[i] = get2dnormal(getVertices(p2)[i].x, getVertices(p2)[i].y, getVertices(p2)[j].x, getVertices(p2)[j].y, true);
        }
        Projection proj1;
        Projection proj2;
        Vector2 axis;
        float interval_distance;
        float overlap;
        // ' project all the verts of the poly to each axis (normal)
        // ' of the poly we are testing and find out if the projections
        // ' overlap (ie: length if proj1 and proj2 are intersecting).
        // ' if they are intersecting, there is an axis (line perpendicular 
        // ' to the axis tested or the "edge" of the poly where the normal connects)
        // ' that separates the two polygons so we do an early out from the function.
        // ' polygon1
        float magnitude = 9999999;
        // ' uber large numbah

        //1. Polygon
        for (int i = 0; (i <= (getVertices(p1).length) - 1); i++) {
            axis = p1normals[i];
            // ' get axis to test
            proj1 = project(axis, p1);
            // ' project vertices
            proj2 = project(axis, p2);
            // ' get 1-d interval distance
            interval_distance = getIntervalDistance(proj1, proj2);

            if (interval_distance > 0) { // Since there's no overlap, there's a separating axis so get out early
                return false;
            }

            // project velocity of polygon 1 to axis
            float v_proj = axis.dot(velocity);

            // get the projection of polygon a during movement
            if (v_proj < 0) { // left
                proj1.min += v_proj;
            } else {	//right
                proj1.max += v_proj;
            }

            // get new interval distance
            // ie. (p1 + velocity) vs (non-moving p2)
            interval_distance = getIntervalDistance(proj1, proj2);

            // get the absolute value of the overlap
            overlap = Math.abs(interval_distance);

//          overlap is less than last overlap
//	    so get the MTV
//	    MTV = axis
//	    magnitude = minimum overlap
            if (overlap < magnitude) {
                magnitude = overlap;
                mtv.x = axis.x;
                mtv.y = axis.y;

//	    	vertex to edge and edge to edge check
//	    	project distance of p1 to p2 onto axis
//	    	if it's on the left, do nothing as
//	    	we are using the left-hand normals
//	    	negate translation vector if projection is on the right side
                Vector2 vd = new Vector2(p1.getX() - p2.getX(), p1.getY() - p2.getY());
                if (vd.dot(axis) > 0) {
                    mtv.x = -axis.x;
                    mtv.y = -axis.y;
                }
            }
        }

        for (int i = 0; (i <= (getVertices(p2).length) - 1); i++) {
            axis = p2normals[i];
            // ' get axis to test
            proj1 = project(axis, p1);
            // ' project vertices
            proj2 = project(axis, p2);
            // ' get 1-d interval distance
            interval_distance = getIntervalDistance(proj1, proj2);

            if (interval_distance > 0) { // Since there's no overlap, there's a separating axis so get out early
                return false;
            }

            // project velocity of polygon 1 to axis
            float v_proj = axis.dot(velocity);

            // get the projection of polygon a during movement
            if (v_proj < 0) { // left
                proj1.min += v_proj;
            } else {	//right
                proj1.max += v_proj;
            }

            // get new interval distance
            // ie. (p1 + velocity) vs (non-moving p2)
            interval_distance = getIntervalDistance(proj1, proj2);

            // get the absolute value of the overlap
            overlap = Math.abs(interval_distance);

//          overlap is less than last overlap
//	    so get the MTV
//	    MTV = axis
//	    magnitude = minimum overlap
            if (overlap < magnitude) {
                magnitude = overlap;
                mtv.x = axis.x;
                mtv.y = axis.y;

//	    	vertex to edge and edge to edge check
//	    	project distance of p1 to p2 onto axis
//	    	if it's on the left, do nothing as
//	    	we are using the left-hand normals
//	    	negate translation vector if projection is on the right side
                Vector2 vd = new Vector2(p1.getX() - p2.getX(), p1.getY() - p2.getY());
                if (vd.dot(axis) > 0) {
                    mtv.x = -axis.x;
                    mtv.y = -axis.y;
                }
            }
        }

//      if we get to this point, the polygons are intersecting so
//	scale the normalized MTV with the minimum magnitude
        mtv.x = mtv.x * magnitude;
        mtv.y = mtv.y * magnitude;

        //no separating axis found so p1 and p2 are colliding
        return true;
    }

}
