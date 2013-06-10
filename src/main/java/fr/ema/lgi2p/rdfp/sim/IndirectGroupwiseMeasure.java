/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.sim;

import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.impl.MatrixDouble;

/**
 *
 * @author Sébastien Harispe
 */
public interface IndirectGroupwiseMeasure {
    Double compute(MatrixDouble<Object,Object> matrix)  throws SLIB_Ex_Critic;
            
}
