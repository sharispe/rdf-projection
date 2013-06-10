/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function.custom.number;

import fr.ema.lgi2p.rdfp.sim.PairwiseMeasure;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Sébastien Harispe
 */
public class BasicNumberSimilarity implements PairwiseMeasure {

    public Double compute(Object a, Object b) throws SLIB_Ex_Critic {
        try {
            Double aDouble = Double.parseDouble(a.toString());
            Double bDouble = Double.parseDouble(b.toString());
            return aDouble == bDouble ? 0. : 1.;

        } catch (NumberFormatException e) {
            throw new SLIB_Ex_Critic("Error converting objects " + e.getMessage());
        }

    }
}
