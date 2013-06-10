/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.sim;

import java.util.Set;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Sébastien Harispe
 */
public interface DirectGroupwiseMeasure {
    
    Double compute(Set<Object> setA, Set<Object> setB) throws SLIB_Ex_Critic;
}
