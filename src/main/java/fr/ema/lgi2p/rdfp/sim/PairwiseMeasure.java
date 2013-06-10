/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.sim;

import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Sébastien Harispe
 */
public interface PairwiseMeasure {
    
    Double compute(Object A, Object B) throws SLIB_Ex_Critic;
}
