/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.confparser;

import fr.ema.lgi2p.rdfp.confparser.conf.RDFPConfiguration;
import fr.ema.lgi2p.rdfp.core.Context;
import fr.ema.lgi2p.rdfp.core.MeasureBuilder;
import fr.ema.lgi2p.rdfp.core.Projection;
import fr.ema.lgi2p.rdfp.core.RDFPConstants;
import fr.ema.lgi2p.rdfp.core.TransformVar;
import fr.ema.lgi2p.rdfp.queryBuilder.SparqlQueryBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class ConfParserJSON {

    Logger logger = LoggerFactory.getLogger(ConfParserJSON.class);
    RDFPConfiguration conf;

    public ConfParserJSON() {
        this.conf = new RDFPConfiguration();
    }

    public ConfParserJSON(RDFPConfiguration conf) {
        this.conf = conf;
    }

    public RDFPConfiguration load(String filepath) throws SLIB_Exception {

        logger.info("Loading configuration");
        logger.info("file: " + filepath);

        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(filepath));

            JSONObject jsonObject = (JSONObject) obj;

            logger.info("Loading prefixes");
            JSONArray prefixes = (JSONArray) jsonObject.get("Prefixes");
            if (prefixes != null) {
                Iterator<JSONObject> iterator = prefixes.iterator();


                while (iterator.hasNext()) {
                    JSONObject prefixJSON = iterator.next();
                    loadPrefix(prefixJSON);
                }
            }

            logger.info("Loading contexts");

            JSONArray contexts = (JSONArray) jsonObject.get("Contexts");
            Iterator<JSONObject> iterator = contexts.iterator();


            while (iterator.hasNext()) {
                JSONObject contextJSON = iterator.next();
                Context c = loadContext(contextJSON);
                conf.addContext(c);
                logger.info(c.toString());
            }

        } catch (FileNotFoundException e) {
            throw new SLIB_Exception(e.getMessage());
        } catch (IOException e) {
            throw new SLIB_Exception(e.getMessage());
        } catch (ParseException e) {
            throw new SLIB_Exception(e.getMessage());
        }
        return conf;
    }

    private Context loadContext(JSONObject contextJSON) throws SLIB_Exception {

        logger.info("------------------------------------------");
        String name = (String) contextJSON.get("name");
        String target = (String) contextJSON.get("target");
        String description = (String) contextJSON.get("description");

        logger.info("name: " + name);
        logger.info("target: " + target);
        logger.info("description: " + description);

        if (name == null) {
            throw new SLIB_Exception("All contexts must have a name");
        }
        if (target == null) {
            throw new SLIB_Exception("All contexts must have a target");
        }

        Context c = new Context(name, target);
        c.setDescription(description);



        logger.info("Loading Projections");

        JSONArray projections = (JSONArray) contextJSON.get("Projections");

        Iterator<JSONObject> iterator = projections.iterator();


        while (iterator.hasNext()) {

            JSONObject projJSON = iterator.next();
            Projection p = loadProjection(projJSON);
            c.addProjection(p);
        }
        return c;
    }

    private Projection loadProjection(JSONObject projJSON) throws SLIB_Exception {

        logger.info("--------------------");

        String name = (String) projJSON.get("name");
        if (name == null) {
            throw new SLIB_Exception("All projections must have a name");
        }
        String description = (String) projJSON.get("description");

        logger.info("name: " + name);
        logger.info("description: " + description);

        Projection p = new Projection(name);
        p.setDescription(description);

        /**
         * Access to the variables used by the projection
         */
        String access = (String) projJSON.get("access");


        if (access != null) {
            logger.info("access: " + access);
            p.addVarAccess(RDFPConstants.DEFAULT_SPARQLVAR, SparqlQueryBuilder.buildTemplate(RDFPConstants.DEFAULT_SPARQLVAR, access,conf.getPrefixes()));

        } else { // process multiple access values
            logger.info("Lookink for multiple access values");

            JSONArray vars = (JSONArray) projJSON.get("vars");
            Iterator<JSONObject> iterator = vars.iterator();

            while (iterator.hasNext()) {

                JSONObject varJSON = iterator.next();
                String bind = (String) varJSON.get("bind");
                String accessVar = (String) varJSON.get("access");

                logger.info("var: " + bind + "\taccess: " + accessVar);

                if (bind.equals(RDFPConstants.DEFAULT_SPARQLVAR)) {
                    throw new SLIB_Exception(RDFPConstants.DEFAULT_SPARQLVAR + " cannot be used has binding name in projection " + name);
                }
                p.addVarAccess(bind, SparqlQueryBuilder.buildTemplate(bind, accessVar,conf.getPrefixes()));
            }
        }

        /**
         * Functions defined to transform the values
         */
        JSONArray transformValuesJson = (JSONArray) projJSON.get("transformValues");

        if (transformValuesJson != null) {

            logger.info("Loading transform Values functions");

            if (access != null) {
                Iterator<String> iterator = transformValuesJson.iterator();

                while (iterator.hasNext()) {
                    String function = iterator.next();
                    logger.info("'x' -> " + function);
                    TransformVar t = new TransformVar(RDFPConstants.DEFAULT_SPARQLVAR, function);
                    p.addTransformVar(t);
                }
            } else {

                Iterator<JSONObject> iterator = transformValuesJson.iterator();

                while (iterator.hasNext()) {
                    JSONObject i = iterator.next();
                    String var = (String) i.get("var");
                    String function = (String) i.get("function");
                    logger.info("'" + var + "' -> " + function);
                    p.addTransformVar(new TransformVar(var, function));
                }
            }
        }

        /**
         * Functions defined to transform the elements
         */
        JSONArray transformElementsJson = (JSONArray) projJSON.get("transformElements");

        if (transformElementsJson != null) {

            logger.info("Loading transform Elements functions");

            Iterator<String> iterator = transformElementsJson.iterator();

            while (iterator.hasNext()) {
                String function = iterator.next();
                logger.info(function);
                p.addTransformElementFunction(function);
            }
        }

        String transformSet = (String) projJSON.get("transformSet");
        if (transformSet != null) {
            logger.info("Loading transform Set function: " + transformSet);
            p.addTransformSetFunction(transformSet);
        }

        String measure = (String) projJSON.get("measure");
        logger.info("measure: " + measure);
        p.setMeasure(MeasureBuilder.buildMeasure(measure));
        
        logger.info(p.toString());
        return p;

    }

    private void loadPrefix(JSONObject prefixJSON) {
        String name = (String) prefixJSON.get("name");
        String value = (String) prefixJSON.get("value");

        logger.info(name + ": " + value);
        conf.addPrefix(name, value);
    }

    public RDFPConfiguration getConf() {
        return conf;
    }
}
