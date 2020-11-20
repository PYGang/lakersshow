package models;

import utils.TxtFileUtils;

import java.io.File;
import java.util.*;

public class Graph {

    int k; //图的顶点数
    VNode[] vNodes;//初始化顶点数组
    boolean[] visited;

    public Graph(int k, VNode[] vNodes){
        this.k = k;
        this.vNodes = vNodes;
        visited = new boolean[k];
    }

    public Graph constructGraph(List<Account> accounts, List<Relation> relations, int nodeNum, int edgeNum){
        VNode[] vNodes = new VNode[nodeNum];
        for(int i =0;i<nodeNum;i++){
            vNodes[i] = new VNode(accounts.get(i));
        }
        Edge e = null;
        Edge e1 = null;
        Account start = null;//起点
        Account end = null;//终点
        for(int i=0;i<edgeNum;i++){
            start = relations.get(i).getSrcAccount();
            end = relations.get(i).getDestAccount();
            e = new Edge(end);
            e.next = vNodes[start.getId()].first;//头插
            vNodes[start.getId()].first = e;

            //无向图对称
            e1 = new Edge(start);
            e1.next = vNodes[end.getId()].first;
            vNodes[end.getId()].first = e1;
        }
        return new Graph(nodeNum,vNodes);
    }

    public Group BFS(Account account){
        Queue<Account> que=new LinkedList<Account>();
        Group group = new Group();
//        group.setId(Integer.MAX_VALUE);
        int minId = Integer.MAX_VALUE;
        List<Account> accounts = new ArrayList<Account>();
        que.add(account);
        while(!que.isEmpty()){
            account=que.poll();
            minId = account.getId()<minId ? account.getId():minId;
            accounts.add(account);
            if(!visited[account.getId()]){
                System.out.print(account.getId()+"  ");
                visited[account.getId()]=true;
            }
            Edge e=vNodes[account.getId()].first;
            while(e!=null){
                if(!visited[e.to.getId()]){
                    que.add(e.to);
                }
                e=e.next;
            }
        }
        // 结果fix 对列表里的进行排序？
        accounts.sort(Comparator.comparingInt(Account::getId));
        group.setId(minId);
        group.setAccounts(accounts);
        return group;
    }

    //获取所有的组合,剪掉已获取的组合
    public List<Group> getAllGroups(List<Account> accounts,List<Relation> relations, int nodeNum, int edgeNum){
        return null;
    }

//    public static void main(String[] args) {
//        File accFile = new File("D:\\lakers\\src\\main\\java\\accounts.txt");
//        File relaFile = new File("D:\\lakers\\src\\main\\java\\relations.txt");
//        List<Account> accounts = TxtFileUtils.getAllAccount(accFile);
//        List<Relation> relations = TxtFileUtils.getAllRelations(relaFile);
//        int nodeNum = accounts.size();
//        int edgeNum = relations.size();
//        Graph graph = Graph.constructGraph(accounts,relations,nodeNum,edgeNum);
////        Account account = new Account();
////        account.setId(0);
////        account.setAccount("73cc29d320aabdad");
////        Group group = graph.BFS(account);
////        System.out.println(group.getId());
//        List<Group> groups = Graph.getAllGroups(accounts,relations,nodeNum,edgeNum);
//        System.out.println(groups.size());
////        List<Account> accounts1 = groups.getAccounts();
////        for(Account account1 :accounts1){
////            System.out.println(account1.getAccount());
////        }
//    }
}
