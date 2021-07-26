package DataModel;

public class Position {
    private String name;
    private int count;

    public Position(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FormatPositionName(name);
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    @Override
    public String toString() {
        return name;
    }

    public String FormatPositionName(String name) {
        return League.FormatName(name);
    }
}
