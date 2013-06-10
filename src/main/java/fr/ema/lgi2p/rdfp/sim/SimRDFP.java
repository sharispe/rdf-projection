/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.sim;

import fr.ema.lgi2p.rdfp.core.Context;
import fr.ema.lgi2p.rdfp.core.MeasureInfo;
import fr.ema.lgi2p.rdfp.core.Projection;
import fr.ema.lgi2p.rdfp.queryExecutor.QueryExecutor;
import fr.ema.lgi2p.rdfp.transformer.Transformer;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class SimRDFP {

    static Logger logger = LoggerFactory.getLogger(SimRDFP.class);

    public static void computeProjectionVectors(SimEngine engine, Repository repo, URI instanceURI, Context context, Map<String, String> prefixes) throws SLIB_Ex_Critic, SLIB_Exception {

        logger.info("Computing projection vectors for instance " + instanceURI + " based on context " + context.getName());

        Set<URI> instances = QueryExecutor.getInstancesOfClass(repo, context.getTarget(), prefixes);
        logger.info("Number of comparisons (instances): " + instances.size());

        for (Projection p : context.getProjections()) {

            logger.info("----------------------------");
            logger.info("Processing " + p.getName());
            logger.info("----------------------------");
            Set<Object> projectionResultsInstanceIA = getInstanceAsSetOfObjects(repo, p, instanceURI.stringValue(), prefixes);
            
            logger.info("Values: " + projectionResultsInstanceIA);

            
            for (URI i : instances) {
                logger.debug("\t" + i);
                logger.debug("------------------------------------");
                Set<Object> projectionResultsInstanceIB = getInstanceAsSetOfObjects(repo, p, i.stringValue(), prefixes);
            
                Double score = engine.computeSim(p.getMeasure(),projectionResultsInstanceIA,projectionResultsInstanceIB);
                
            }

        }


    }

    private static Set<Object> getInstanceAsSetOfObjects(Repository repo, Projection p, String stringValue, Map<String, String> prefixes) throws SLIB_Ex_Critic, SLIB_Exception {
        Map<String, Set<Value>> varsValues = QueryExecutor.getBindedVariables(repo, p, stringValue, prefixes);
        return Transformer.buildValues(p, varsValues);
    }

    private static void computeScore(MeasureInfo measure, Set<Object> projectionResultsInstanceIA, Set<Object> projectionResultsInstanceIB) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
