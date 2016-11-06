/**
 * Created by wanbin on 11/5/16.
 */
public class Avg<T> {
    T value;
    int weight;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean equals(Object obj) {
        if (obj.getClass().getName() == Avg.class.getName()) {
            Avg avg = (Avg) obj;
            if (this.weight == avg.getWeight() && this.value == avg.getValue()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String toString() {
        return " avg:" + value + " number:" + weight + "\n";
    }
}
