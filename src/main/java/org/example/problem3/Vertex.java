package org.example.problem3;

public class Vertex {
    public String first;
    public String second;

    public Vertex(String pattern1, String pattern2)
    {
        this.first = pattern1;
        this.second = pattern2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex otherVertex))
            return false;

        return otherVertex.second.equals(this.second) &&
                otherVertex.first.equals(this.first);
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }
}
