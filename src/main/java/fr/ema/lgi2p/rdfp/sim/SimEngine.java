/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.sim;

import fr.ema.lgi2p.rdfp.core.MeasureInfo;
import fr.ema.lgi2p.rdfp.core.Strategy;
import fr.ema.lgi2p.rdfp.function.custom.number.BasicNumberSimilarity;
import fr.ema.lgi2p.rdfp.transformer.Transformer;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.impl.MatrixDouble;

/**
 *
 * @author Sébastien Harispe
 */
public class SimEngine {

    static Logger logger = LoggerFactory.getLogger(SimEngine.class);

    public Double computeSim(MeasureInfo measure, Set<Object> projectionResultsInstanceIA, Set<Object> projectionResultsInstanceIB) throws SLIB_Ex_Critic {

        if (measure.getStrategy() == Strategy.DIRECT) {
            DirectGroupwiseMeasure gm = loadDirectGroupwiseMeasure(measure.getGroupwise());
            
            if(gm != null){
                
                return gm.compute(projectionResultsInstanceIA, projectionResultsInstanceIB);
            }
            else{
                logger.info("Cannot state Groupwise measure "+measure.getGroupwise());
                logger.info("Try auto cast");
                if(projectionResultsInstanceIA == null || projectionResultsInstanceIA.size() != 1){
                    throw new SLIB_Ex_Critic("Cannot state set "+projectionResultsInstanceIA+" to a single object...");
                }
                if(projectionResultsInstanceIB == null || projectionResultsInstanceIB.size() != 1){
                    throw new SLIB_Ex_Critic("Cannot state set "+projectionResultsInstanceIB+" to a single object...");
                }
                logger.info("Pairwise comparison of objects");
                PairwiseMeasure m = loadDirectPairwiseMeasure(measure.getGroupwise());
                if(m == null){
                    throw new SLIB_Ex_Critic("Cannot retrieve pairwise measure for String "+measure.getGroupwise()+" (trying to perform auto cast)");
                }
                return m.compute(projectionResultsInstanceIA.iterator().next(), projectionResultsInstanceIB.iterator().next());
            }
        } else {
            MatrixDouble<Object, Object> pairwiseMatrix = new MatrixDouble<Object, Object>(projectionResultsInstanceIA, projectionResultsInstanceIB);
            PairwiseMeasure pm = loadDirectPairwiseMeasure(measure.getPairwise());
            
            if(pm == null){
                throw new SLIB_Ex_Critic("Cannot state pairwise measure "+measure.getPairwise());
            }
            
            for (Object oA : projectionResultsInstanceIA) {
                for (Object oB : projectionResultsInstanceIB) {
                    Double simAB = pm.compute(oA, oB);
                    pairwiseMatrix.setValue(oA, oB, simAB);
                }
            }
            IndirectGroupwiseMeasure indirectGroupwiseMeasure = loadIndirectGroupiseMeasure(measure.getGroupwise());
            
            if(indirectGroupwiseMeasure == null){
                throw new SLIB_Ex_Critic("Cannot state Indirect Groupwise Measure "+measure.getGroupwise());
            }
            
            return indirectGroupwiseMeasure.compute(pairwiseMatrix);
        }
    }

    private DirectGroupwiseMeasure loadDirectGroupwiseMeasure(String groupwise)  {
        logger.info("Loading Direct Groupwise Measure from: " + groupwise);
        DirectGroupwiseMeasure m = null;

        String minfo = m == null ? "null" : m.getClass().toString();
        logger.info("Loaded "+minfo);
        return m;
    }

    private PairwiseMeasure loadDirectPairwiseMeasure(String pairwise)  {
        logger.info("Loading Pairwise Measure from: " + pairwise);
        PairwiseMeasure m = null;
        
        if(pairwise.equalsIgnoreCase("Number:BasicNumberSimilarity")){
            m = new BasicNumberSimilarity();
        }

        String minfo = m == null ? "null" : m.getClass().toString();
        logger.info("Loaded "+minfo);
        return m;
    }

    private IndirectGroupwiseMeasure loadIndirectGroupiseMeasure(String groupwise) throws SLIB_Ex_Critic{
        logger.info("Loading Indirect Groupwise measure from: " + groupwise);
        IndirectGroupwiseMeasure m = null;
        
        String minfo = m == null ? "null" : m.getClass().toString();
        logger.info("Loaded "+minfo);
        return m;
    }
}
