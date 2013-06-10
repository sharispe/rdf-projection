/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.core;

/**
 *
 * @author Sébastien Harispe
 */
public class TransformVar {
    
    final String var;
    final String function;
    
    public TransformVar(String var, String function){
        this.var = var;
        this.function = function;
    }

    public String getVar() {
        return var;
    }

    public String getFunction() {
        return function;
    }
    
    
}
