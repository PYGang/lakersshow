package models;

public class VNode{
    Account from;//边的起点
    Edge first;//以from为起点的第一条边
    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Edge getFirst() {
        return first;
    }

    public void setFirst(Edge first) {
        this.first = first;
    }

    public VNode(Account from){
        this.from=from;
        first=null;
    }
}
