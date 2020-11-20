package utils;

import models.Account;
import models.Relation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.lang.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

public class TxtFileUtils {
    //获取所有的账号
    public static List<Account> getAllAccount(File file){
        List<Account> accounts = new ArrayList<Account>();
        try{
//            List<String> strs = FileUtils.readLines(file);
            LineIterator strs = FileUtils.lineIterator(file);
         while(strs.hasNext()){
                String str = strs.nextLine();
                String[] pair = str.split(" ");
                Account account = new Account();
                int id = Integer.parseInt(pair[0]);
                String acc = pair[1];
                account.setId(id);
                account.setAccount(acc);
                accounts.add(account);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public static Map<Integer,String> getMapAccount(File file){
        Map<Integer,String> map = new HashMap<>();
        try{
            LineIterator strs = FileUtils.lineIterator(file);
            while(strs.hasNext()){
                String str = strs.nextLine();
                String[] pair = str.split(" ");
                Account account = new Account();
                int id = Integer.parseInt(pair[0]);
                String acc = pair[1];
                map.put(id,acc);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return map;
    }

    public static void getMapAccount(File file,Map<Integer,String> map){
        try{
            LineIterator strs = FileUtils.lineIterator(file);
            while(strs.hasNext()){
                String str = strs.nextLine();
                String[] pair = str.split(" ");
                Account account = new Account();
                int id = Integer.parseInt(pair[0]);
                String acc = pair[1];
                map.put(id,acc);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    //索引切片读取
    public static List<Account> getAccountByLimit(File file, long start, long limit){
        return null;
    }

    //获取所有的关系
    public static List<Relation> getAllRelations(File file){
        List<Relation> relations = new ArrayList<Relation>();
        try{
//            List<String> strs = FileUtils.readLines(file);
            LineIterator strs = FileUtils.lineIterator(file);
            while(strs.hasNext()){
                String str = strs.nextLine();
                String[] pair = str.split(" ");
                Relation relation = new Relation();
                Account account1 = new Account();
                Account account2 = new Account();
                int id1 = Integer.parseInt(pair[0]);
                String acc1 = pair[1];
                account1.setId(id1);
                account1.setAccount(acc1);
                int id2 = Integer.parseInt(pair[2]);
                String acc2 = pair[3];
                account2.setId(id2);
                account2.setAccount(acc2);
                relation.setSrcAccount(account1);
                relation.setDestAccount(account2);
                relations.add(relation);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return relations;
    }

    public static void readFastNio(String path){
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        try{
            aFile = new RandomAccessFile(path,"rw");
            fc = aFile.getChannel();
            long timeBegin = System.currentTimeMillis();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, aFile.length());
            byte[] bb = new byte[mbb.capacity()];
            while (mbb.hasRemaining()){
                byte b = mbb.get();
                bb[mbb.position()] = b;
            }
            System.out.println(new String(bb));
            //强制释放资源
            closeMappedByteBuffer(mbb);

            long timeEnd = System.currentTimeMillis();
            System.out.println("Read time: "+(timeEnd-timeBegin)+"ms");
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(aFile!=null){
                    aFile.close();
                }
                if(fc!=null){
                    fc.close();
                }

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void closeMappedByteBuffer(final MappedByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return;
        }

        if (byteBuffer != null) {
            AccessController.doPrivileged(
                    new PrivilegedAction<Object>() {
                        @Override
                        public Object run() {
                            try {
                                Method cleanerMethod = byteBuffer.getClass().getMethod("cleaner", new Class[0]);
                                cleanerMethod.setAccessible(true);
                                sun.misc.Cleaner cleaner = (sun.misc.Cleaner) cleanerMethod.invoke(byteBuffer, new Object[0]);
                                cleaner.clean();
                            } catch (Exception e) {
                                    e.printStackTrace();
                            }
                            return null;
                        }
                    }
            );
        }
    }
    public static void main(String[] args) {

        long time1 = new Date().getTime();
        File file = new File("D:\\lakers\\src\\main\\java\\accounts.txt");
        getMapAccount(file);
        long time2 = new Date().getTime();
        System.out.println(time2-time1);
        //readFastNio("D:\\lakers\\src\\main\\java\\relations.txt");
    }
}
