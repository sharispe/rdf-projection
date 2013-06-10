/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class Projection {

    final String name;
    String description;
    private Map<String, String> varsAccess;
    private List<TransformVar> transformVar;
    private List<String> transformElementFunctions;
    private List<String> transformSetFunctions;
    MeasureInfo measure;

    public Projection(String name) {
        this.name = name;
        varsAccess = new HashMap<String, String>();
        transformVar = new ArrayList<TransformVar>();
        transformElementFunctions = new ArrayList<String>();
        transformSetFunctions = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public void addVarAccess(String var, String access) throws SLIB_Exception {
        if (varsAccess.containsKey(var)) {
            throw new SLIB_Exception("Acess for var " + var + " is already specified in projection " + name + "\ncurrent: " + varsAccess.get(var));
        }
        varsAccess.put(var, access);
    }

    public void addTransformVar(TransformVar t) {
        transformVar.add(t);
    }

    public void addTransformElementFunction(String f) {
        transformElementFunctions.add(f);
    }

    public void addTransformSetFunction(String f) {
        transformSetFunctions.add(f);
    }

    public MeasureInfo getMeasure() {
        return measure;
    }

    public void setMeasure(MeasureInfo measure) {
        this.measure = measure;
    }

    public Map<String, String> getVarsAccess() {
        return varsAccess;
    }

    public List<TransformVar> getTransformVar() {
        return transformVar;
    }

    public List<String> getTransformElementFunctions() {
        return transformElementFunctions;
    }

    public List<String> getTransformSetFunctions() {
        return transformSetFunctions;
    }

    @Override
    public String toString() {
        String o = "\n------------\nProjection: "+name+"\n";
        o += "description: "+description+"\n";
        o += "access ("+varsAccess.size()+"): \n";
        for(Map.Entry<String,String> e : varsAccess.entrySet()){
            o += "\tvar: "+e.getKey()+" access: \n"+e.getValue()+"\n";
        }
        o += "transform values functions ("+transformVar.size()+"): \n";
        for(TransformVar t : transformVar){
            o += "\tvar: "+t.var+" function: "+t.function+"\n";
        }
        o += "transform element functions ("+transformElementFunctions.size()+"): \n";
        for(String t : transformElementFunctions){
            o += "\t"+t+"\n";
        }
        o += "transform Set functions ("+transformSetFunctions.size()+"): \n";
        for(String t : transformSetFunctions){
            o += "\t"+t+"\n";
        }
        o += "measure: "+measure.toString()+"\n";
        
        return o;
    }
}
