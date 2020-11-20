package models;

public class Edge{
    Account to;//边的终点
    Edge next;//具有同一起点的下一条边
    public Edge(Account to){
        this.to=to;
        next=null;
    }
    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public Edge getNext() {
        return next;
    }

    public void setNext(Edge next) {
        this.next = next;
    }
}
