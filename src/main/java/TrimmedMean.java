import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by wanbin on 11/4/16.
 */
public class TrimmedMean {

    private List<Long> cache;
    private List<Avg<Long>> avgCache;

    private int bottomNum = 0;
    private int topNum = 0;
    private List<Long> bottomCache;
    private List<Long> topCache;

    private int cacheSize = 0;

    private int factor = 1;

    private double coefficient = 0.99;

    private int avgCacheLimit = 100;


    public TrimmedMean(int cacheSize, int bottomNum, int topNum, boolean threadSafe) {
        this.cacheSize = cacheSize;
        this.bottomNum = bottomNum;
        this.topNum = topNum;
        if (threadSafe == false) {
            this.cache = new ArrayList<Long>();
            this.topCache = new ArrayList<Long>();
            this.bottomCache = new ArrayList<Long>();
            this.avgCache = new ArrayList<Avg<Long>>();
        } else {
            this.cache = new CopyOnWriteArrayList<Long>();
            this.topCache = new CopyOnWriteArrayList<Long>();
            this.bottomCache = new CopyOnWriteArrayList<Long>();
            this.avgCache = new CopyOnWriteArrayList<Avg<Long>>();
        }

    }

    void moveList(List<Long> src, int from, int to,List<Long> dist) {

        List<Long> sub = src.subList(from,to);
        for(int i = 0; i < sub.size(); i++) {
            dist.add(sub.get(i));
        }
        sub.clear();
    }


    Avg<Long> compute(List<Long> cache,int from,int toPos) {
        long sum = 0;

        int size =0;
        for(int i = from; i < toPos; i++) {
            sum += cache.get(i);
            size++;
        }
        Avg<Long> avg = new Avg();
        avg.setValue(sum/size);
        avg.setWeight(size);
        return avg;
    }


    void moveMayBeException(List<Long> cache,List<Long> topCache,List<Long> bottomCache) {
        cache.sort(null);

        moveList(cache,(int)(cacheSize*coefficient),cacheSize,topCache);
        moveList(cache,0,(cacheSize - (int)(cacheSize*coefficient)),bottomCache);

        if(topCache.size() > factor*topNum) {
            Collections.sort(topCache);
            moveList(topCache,0,(topCache.size()-factor*topNum),cache);
        }

        if(bottomCache.size() > factor*bottomNum) {
            Collections.sort(bottomCache);
            moveList(bottomCache,factor*bottomNum,bottomCache.size(),cache);
        }
        return;
    }

    public Avg<Long> sum(List<Avg<Long>> avgCache) {

        long size = 0;
        for(int i = 0; i < avgCache.size(); i++) {
            size += avgCache.get(i).getWeight();
        }
        float sum = 0;
        for(int i = 0; i < avgCache.size(); i++) {
            sum += (float)avgCache.get(i).getValue() * ((float)avgCache.get(i).getWeight() / (float)(size));
        }
        Avg<Long> avg = new Avg<Long>();
        avg.setValue((long)sum);
        avg.setWeight((int)size);
        return avg;
    }

    public Avg<Long> getValue() {
        List<Avg<Long>> total = new ArrayList<Avg<Long>>();
        if(cache.size()>0) {
            Avg<Long> avg1 = compute(cache, 0, cache.size());
            total.add(avg1);
        }
        if(avgCache.size() > 0) {
            Avg<Long> avg2 = sum(avgCache);
            avgCache.clear();
            avgCache.add(avg2);
            total.add(avg2);
        }
        if(topCache.size() > topNum) {
            Collections.sort(topCache);

            Avg<Long> avg3 = compute(topCache, topNum, topCache.size());

            total.add(avg3);
        }
        if(bottomCache.size() > bottomNum) {
            Collections.sort(bottomCache);
            Avg<Long> avg4 = compute(bottomCache, 0, (bottomCache.size() - bottomNum));

            total.add(avg4);
        }
        return sum(total);
    }

    public String getExceptionValue() {
        String exception;
        exception = "top:" + topCache.toString() + "\n";
        exception += "bottom:" + bottomCache.toString();
        return exception;

    }

    public List<Long> getTopExceptionValue() {
        return topCache;
    }

    public List<Long> getBottomExceptionValue() {
        return bottomCache;
    }

    public void setValue(Long value) {

        cache.add(value);

        if(this.cacheSize == cache.size()) {

            moveMayBeException(cache,topCache,bottomCache);
            Avg<Long> avg = compute(cache,0,cache.size());
            avgCache.add(avg);


            if(avgCache.size() > avgCacheLimit) {
                Avg<Long> avg1 = sum(avgCache);
                avgCache.clear();
                avgCache.add(avg1);
            }
            cache.clear();
        }
        return;
    }
}
