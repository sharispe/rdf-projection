/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.core;

import java.util.Set;

/**
 *
 * @author Sébastien Harispe
 */
public interface Measure {
    
    public double compute(Set<Object> setA, Set<Object> setB);
}
