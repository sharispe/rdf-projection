/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.core;

/**
 *
 * @author Sébastien Harispe
 */
public class MeasureInfo {

    final String stringValue;
    final String groupwise;
    final Strategy strategy;
    final ValueType comparedValues;
    final ValueType pairwiseElementType;
    final String pairwise;

    public MeasureInfo(String asString, Strategy strat, ValueType comparedValues, String groupwise, ValueType pairwiseElementType, String pairwise) {
        this.stringValue = asString;
        this.strategy = strat;
        this.comparedValues = comparedValues;
        this.pairwiseElementType = pairwiseElementType;
        this.groupwise = groupwise;
        this.pairwise = pairwise;
    }

    @Override
    public String toString() {
        String o = stringValue + "\n";
        o += "\tCompared values : " + comparedValues + "\n";
        o += "\tElement Type : " + pairwiseElementType + "\n";
        o += "\tStrategy: " + strategy + "\n";
        o += "\tGroupwise measure: " + groupwise + "\n";
        o += "\tPairwise measure: " + pairwise + "\n";
        
        return o;
    }

    public String getStringValue() {
        return stringValue;
    }

    public String getGroupwise() {
        return groupwise;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public ValueType getComparedValues() {
        return comparedValues;
    }

    public ValueType getPairwiseElementType() {
        return pairwiseElementType;
    }

    public String getPairwise() {
        return pairwise;
    }
    
    
}
