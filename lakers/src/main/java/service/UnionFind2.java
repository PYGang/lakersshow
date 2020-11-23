package service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import utils.FileThread;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnionFind2 {
    private int[] parent;
    int size;

    public UnionFind2(int size) {
        this.size = size;
        this.parent = new int[size];

        for (int i = 0; i < size; i++) {
            parent[i] = i;

        }
    }

    public int find(int element) {
        while (element != parent[element]) {
            element = parent[element];
        }
        return element;
    }

    public boolean isConnected(int firstElement, int secondElement) {
        return find(firstElement) == find(secondElement);
    }

    public void unionElements(int firstElement, int secondElement) {
        int firstRoot = find(firstElement);
        int secondRoot = find(secondElement);

        if(firstRoot<secondRoot){
            parent[secondRoot] = firstRoot;
        }else{
            parent[firstRoot] = secondRoot;
        }

    }

                /*
              如果要合并的两个集合高度一样，那么随意选一个作为根
              我这里选的是让secondRoot作为新集合的根。
              然后secondRoot高度高了一层，所以+1
            */

    private void printArr(int[] arr){
        for(int p : arr){
            System.out.print(p+"\t");
        }
        System.out.println();
    }

    private void clear(){
        parent = null;
//        height = null;
    }

    public static void main(String[] args) {
        int n = 5000001;
        UnionFind2 union = new UnionFind2(n);
        Map<Integer, List<Integer>> map = new LinkedHashMap<>();
        long start = new Date().getTime();

        //此处耗时约1.4秒，读与计算耗时2.2秒，此时在计算的同时就把账户表给加载好了
        File file1 = new File("D:\\Project\\Java\\Competition\\lakersshow\\lakers\\src\\main\\java\\accounts.txt");
        ExecutorService executorService = Executors.newCachedThreadPool();
        Map<Integer, String> nameMap = new HashMap<>();
        executorService.submit(new FileThread(file1,nameMap));
        executorService.shutdown();

        //耗时最多2.7s，能不能考虑利用多线程吃掉一些时间和空间
        long start0 = new Date().getTime();
        File file = new File("D:\\Project\\Java\\Competition\\lakersshow\\lakers\\src\\main\\java\\relations.txt");
        try {
            LineIterator strs = FileUtils.lineIterator(file);
            while (strs.hasNext()) {
                String line = strs.nextLine();
                String[] words = line.split(" ");
                String id1 = words[0];
                String id2 = words[2];
                int ida = Integer.parseInt(id1);
                int idb = Integer.parseInt(id2);
                union.unionElements(ida, idb);
            }
            strs = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end0 = new Date().getTime();
        System.out.println("读加计算耗时" + (end0 - start0));


        long start1 = new Date().getTime();
        int[] parents = union.parent;
        for (int i = 0; i < n; i++) {
            int key = parents[i];
            while(key!=parents[key]){
                key=parents[key];
            }
            List<Integer> list = map.getOrDefault(key, new ArrayList<>());
            list.add(i);
            map.put(key, list);
        }
        //排序耗时1s，其实在并查集算法中可以直接融入排序规则，我没考虑，估计顶多相差个100毫秒左右，排序跟拼接能不能考虑同时执行
        List<List<Integer>> allList = new ArrayList<>();
        for (List<Integer> value : map.values()) {
            allList.add(value);
        }
        List<String> lines = new ArrayList<>();

        //不再引用
        map.clear();
        union.clear();
        union = null;
        map = null;

        StringBuffer sb = new StringBuffer();

        long start3 = new Date().getTime();
        for (int i=0;i<allList.size();i++) {
            sb.setLength(0);
            sb.append(allList.get(i).get(0));
            sb.append(" ");
            for (int j = 0; j < allList.get(i).size() - 1; j++) {
                sb.append(nameMap.get(allList.get(i).get(j)));
                sb.append(",");
            }
            sb.append(nameMap.get(allList.get(i).get(allList.get(i).size() - 1)));
            lines.add(sb.toString());
        }

        //不再引用
        nameMap.clear();
        nameMap=null;
        allList.clear();
        allList = null;

        long end1 = new Date().getTime();
        System.out.println("排序+拼接总共耗时" + (end1 - start1));

        //写文件很快了，300毫秒左右，极限优化的话，还得将计算结果加到消息队列中，在计算的同时将写操作完成
        long start2 = new Date().getTime();
        File wfile = new File("D:\\Project\\Java\\Competition\\lakersshow\\lakers\\src\\main\\java\\result.txt");
        try {
            FileUtils.writeLines(wfile, lines);
            lines.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end2 = new Date().getTime();
        System.out.println("写文件耗时" + (end2 - start2));
        long end = new Date().getTime();
        System.out.println("总耗时"+(end - start));
    }
}
