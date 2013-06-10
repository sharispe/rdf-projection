/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.queryExecutor;

import fr.ema.lgi2p.rdfp.core.Projection;
import fr.ema.lgi2p.rdfp.queryBuilder.SparqlQueryBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openrdf.OpenRDFException;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Sébastien Harispe
 */
public class QueryExecutor {
    
    static Logger logger = LoggerFactory.getLogger(QueryExecutor.class);

    public static Set<Value> exec(Repository repo, String bindedVar, String q) throws SLIB_Ex_Critic {
        Set<Value> values = new HashSet<Value>();
        try {
            RepositoryConnection con = repo.getConnection();
            try {
                String queryString = q;
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

                TupleQueryResult result = tupleQuery.evaluate();
                try {

                    while (result.hasNext()) {
                        BindingSet bindingSet = result.next();
                        Value valueOfX = bindingSet.getValue(bindedVar);
                        
                        values.add(valueOfX);
                    }
                } finally {
                    result.close();
                }
            } finally {
                con.close();
            }
        } catch (OpenRDFException e) {
            throw new SLIB_Ex_Critic(e.getMessage());
        }
        return values;
    }

    public static Set<URI> getInstancesOfClass(Repository repo, String classURI,Map<String,String> prefixes) throws SLIB_Ex_Critic {
        
        logger.info("GET instances of class "+classURI);
        
        Set<URI> instancesURI = new HashSet<URI>();
        try {

            RepositoryConnection con = repo.getConnection();
            try {
                String queryString = SparqlQueryBuilder.buildGetInstanceQuery(classURI, prefixes);
                
                logger.info(queryString);
                
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

                TupleQueryResult result = tupleQuery.evaluate();
                try {

                    while (result.hasNext()) {
                        BindingSet bindingSet = result.next();
                        Value valueOfX = bindingSet.getValue("x");
                        instancesURI.add((URI) valueOfX);
                    }
                } finally {
                    result.close();
                }
            } finally {
                con.close();
            }
        } catch (OpenRDFException e) {
            e.printStackTrace();
            throw new SLIB_Ex_Critic(e.getMessage());
        }
        return instancesURI;
    }

    public static Map<String, Set<Value>> getBindedVariables(Repository repo, Projection p, String uriAsString, Map<String,String> prefixes) throws SLIB_Ex_Critic {

        logger.debug("Binded variables "+p.getName());
        logger.debug("Repo: " + repo.toString());
        logger.debug("Projection: " + p.getName());
        logger.debug("URI '"+uriAsString+"'");
        Map<String, Set<Value>> results = new HashMap<String, Set<Value>>();

        for (Map.Entry<String, String> e : p.getVarsAccess().entrySet()) {

            logger.debug("VAR: "+e.getKey());

            String template = e.getValue();
            logger.debug("TEMPLATE: "+template);

            String query = template.replace("<THIS>", "<" + uriAsString + ">");
            
            logger.debug("Query:\n"+query);
            Set<Value> values = QueryExecutor.exec(repo, e.getKey(), query);
            logger.debug("Result size: "+values.size());
            results.put(e.getKey(), values);
        }
        return results;
    }
}