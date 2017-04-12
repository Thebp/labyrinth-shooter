package gruppe5.collision;

/**
 *
 * @author Daniel
 */
public class Projection {

    double min;
    double max;

    public Projection(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    boolean overlap(Projection b) {
        if (contains(this.getMin(), b)) {
            return true;
        } else if (contains(this.getMax(), b)) {
            return true;
        } else if (contains(b.getMin(), this)) {
            return true;
        } else if (contains(b.getMax(), this)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean contains(double n, Projection range) {
        double a = range.getMin();
        double b = range.getMax();
        if (b < a) {
            a = b;
            b = this.getMin();
        }
        return (n >= a && n <= b);
    }
    
    public double getOverlap(Projection b)
	{
            return (this.max < b.max) ? this.max - b.min : b.max - this.min;
	}
}
