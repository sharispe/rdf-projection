/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.core;

import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class MeasureBuilder {

    public static MeasureInfo buildMeasure(String m) throws SLIB_Exception {

        ValueType comparedValues = null;
        ValueType elementValues = null;
        String groupwiseMeasure = null;
        String pairwiseMeasure = null;
        Strategy strategy = null;

        String[] d = m.split("\\(|\\)");

        groupwiseMeasure = d[0];

        if (d.length == 1) {
            d = groupwiseMeasure.split(":");
            try {
                comparedValues = ValueType.valueOf(d[0]);
            } catch (IllegalArgumentException e) {
                throw new SLIB_Ex_Critic("Cannot map " + d[0] + " to an existing type in measure " + m);
            }
            strategy = Strategy.DIRECT;
            
        } else if (d.length == 2) {
            pairwiseMeasure = d[1];
            d = pairwiseMeasure.split(":");
            try {
                elementValues = ValueType.valueOf(d[0]);
            } catch (IllegalArgumentException e) {
                throw new SLIB_Ex_Critic("Cannot map " + d[0] + " to an existing type in measure " + m);
            }
            comparedValues = ValueType.SET;
            strategy = Strategy.INDIRECT;
        } else {
            throw new SLIB_Exception("Cannot process measure " + m + ", consult the documentation");
        }

        MeasureInfo measure = new MeasureInfo(m, strategy, comparedValues, groupwiseMeasure, elementValues, pairwiseMeasure);
        return measure;
    }
}
