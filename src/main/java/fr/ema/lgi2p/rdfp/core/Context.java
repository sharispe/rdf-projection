/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class Context {

    final String name;
    final String target;
    String description;
    Map<String, Projection> projections;

    public Context(String name, String target) {
        this.name = name;
        this.target = target;
        projections = new HashMap<String, Projection>();
    }

    public String getTarget() {
        return target;
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
    
    public Set<Projection> getProjections() {
        return new HashSet<Projection>(projections.values());
    }

    public void addProjection(Projection p) throws SLIB_Exception {
        if (projections.containsKey(p.getName())) {
            throw new SLIB_Exception("Projection " + p.getName() + " already exists in context " + name);
        }
        projections.put(p.getName(), p);
    }
    
    @Override
    public String toString() {
        String o = "\n--------------------\nContext: "+name+"\n";
        o += "description: "+description+"\n";
        o += "target: "+target+"\n";
        o += "Projections ("+projections.size()+"): \n";
        for(Projection p : projections.values()){
            o += p.toString();
        }
        
        
        return o;
    }
}
