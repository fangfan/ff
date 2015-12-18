package model;

import inner.ISolution;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by F.Fang on 2015/12/8.
 */
public class Solutions {

    private Map<String, ISolution> solutionMap = new ConcurrentHashMap<>();

    public void addSolution(String key,ISolution solution){
        this.solutionMap.put(key, solution);
    }

    public void removeSolution(String key){
        this.solutionMap.remove(key);
    }

}
