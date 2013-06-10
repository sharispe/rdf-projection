/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.stat;

import fr.ema.lgi2p.rdfp.core.Context;
import fr.ema.lgi2p.rdfp.core.Projection;
import fr.ema.lgi2p.rdfp.queryExecutor.QueryExecutor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Sébastien Harispe
 */
public class StatComputer {

    static Logger logger = LoggerFactory.getLogger(StatComputer.class);

    public static void computeStats(Repository repo, Context c, Map<String,String> prefixes) throws SLIB_Ex_Critic {
        logger.info("Compute stat");
        logger.info("Context " + c.getName() + "\t(" + c.getProjections().size() + " projections)");
        logger.info("Target Class: " + c.getTarget());
        
        Set<URI> instances = QueryExecutor.getInstancesOfClass(repo, c.getTarget(),prefixes);
        
        logger.info("instances: " + instances.size());
        Map<Projection, Integer> countInstanceWithData = new HashMap<Projection, Integer>();

        logger.info("Processing projections");
        for (Projection p : c.getProjections()) {

            logger.info("- " + p.getName());
            countInstanceWithData.put(p, 0);

            for (URI i : instances) {

                boolean valid = true;
                Map<String, Set<Value>> varsValues = QueryExecutor.getBindedVariables(repo, p, i.stringValue(),prefixes);

                for (String var : p.getVarsAccess().keySet()) {
                    if (varsValues.get(var).isEmpty()) {
                        valid = false;
                    }
                }
                if (valid) {
                    countInstanceWithData.put(p, countInstanceWithData.get(p) + 1);
                }
            }
            int per = 0;
            if(!instances.isEmpty()){
                per = countInstanceWithData.get(p) * 100 / instances.size();
            }
            logger.info("\t\tstats: " + countInstanceWithData.get(p) + "/" + instances.size() + "\t" + per + "%");
        }
    }
}
