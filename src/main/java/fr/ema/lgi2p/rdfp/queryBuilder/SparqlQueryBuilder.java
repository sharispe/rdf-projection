/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.queryBuilder;

import java.util.Arrays;
import java.util.Map;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Sébastien Harispe
 */
public class SparqlQueryBuilder {

    public static String buildTemplate(String bindedValue, String q, Map<String, String> prefixes) throws SLIB_Ex_Critic {

        
        String sparqlQueryTemplate = buildPrefixString(prefixes) + "\n";

        sparqlQueryTemplate += "SELECT DISTINCT ?" + bindedValue + " WHERE {\n";
        String[] d = q.split("\\s+\\.");
        if (d.length == 1) {
            String[] spo = q.trim().split("\\s+");

            if (spo.length == 1) {
                sparqlQueryTemplate += "\t <THIS> " + spo[0] + " ?" + bindedValue + " \n";
            } else if (spo.length == 3) {
                sparqlQueryTemplate += "\t " + spo[0].replace("?this", "<THIS>") + " " + spo[1] + " " + spo[2].replace("?this", "<THIS>") + " \n";
            } else {
                throw new SLIB_Ex_Critic("Cannot process query " + q);
            }

        } else {

            int c = 0;
            for (String l : d) {
                c++;


                String[] spo = l.trim().split("\\s+");

                if (spo.length == 3) {
                    sparqlQueryTemplate += "\t " + spo[0].replace("?this", "<THIS>") + " <" + spo[1] + "> " + spo[2].replace("?this", "<THIS>");
                    if (c != d.length) {
                        sparqlQueryTemplate += " . ";
                    }
                    sparqlQueryTemplate += "\n";
                } else {
                    throw new SLIB_Ex_Critic("Cannot process query part " + l + " of query " + q + ", expect size split 3, obtain " + d.length + "\t" + Arrays.toString(spo));
                }
            }
        }

        sparqlQueryTemplate += "}";
        return sparqlQueryTemplate;
    }

    public static String buildGetInstanceQuery(String classURI, Map<String, String> prefixes) {

        return buildPrefixString(prefixes)+"\nSELECT DISTINCT ?x WHERE { ?x rdf:type " + classURI + " } ";

    }

    private static String buildPrefixString(Map<String, String> prefixes) {
        String p = "";
        if (prefixes != null) {
            for (Map.Entry<String, String> e : prefixes.entrySet()) {
                p += "PREFIX "+e.getKey() + ": <" + e.getValue() + ">\n";
            }
        }
        return p;
    }
}
