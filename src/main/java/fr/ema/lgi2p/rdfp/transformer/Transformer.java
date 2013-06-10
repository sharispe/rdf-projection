/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.transformer;


import fr.ema.lgi2p.rdfp.core.Projection;
import fr.ema.lgi2p.rdfp.core.TransformVar;
import fr.ema.lgi2p.rdfp.function.ElementFunction;
import fr.ema.lgi2p.rdfp.function.FunctionLoader;
import fr.ema.lgi2p.rdfp.function.SetFunction;
import fr.ema.lgi2p.rdfp.function.ValueFunction;
import fr.ema.lgi2p.rdfp.sim.SimRDFP;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Exception;
import slib.utils.impl.UtilDebug;

/**
 *
 * @author Sébastien Harispe
 */
public class Transformer {
    
    static Logger logger = LoggerFactory.getLogger(Transformer.class);

    public static Set<Object> buildValues(Projection p, Map<String, Set<Value>> varsValues) throws SLIB_Exception {
        
        logger.info("ORIGINAL VARS: "+varsValues.toString());
        logger.info("Transform values function nb:"+p.getTransformVar().size());
        
        Map<String, Set<Object>> transformedVars = new HashMap<String, Set<Object>>();
        
        for(String s : varsValues.keySet()){
            transformedVars.put(s, (Set) varsValues.get(s));
        }
            
        for(TransformVar t : p.getTransformVar()){
            
            logger.info("\t"+t.getVar()+"\t"+t.getFunction());
            Set<Value> varValues  = varsValues.get(t.getVar());
            Set<Object> varObjects = new HashSet<Object>();
            
            logger.info("VALUES: "+varValues);
            
            ValueFunction f = FunctionLoader.loadValueFunction(t.getFunction());
            
            if(varValues != null){
                for(Value o : varValues){
                    logger.info("\t-> '"+(o.getClass())+"'");
                    logger.info("\t-> '"+(o.stringValue())+"'");
                    
                    
                    if(!f.isAValidResource(o)){
                        throw new SLIB_Ex_Critic("Function "+t.getFunction()+" cannot be used to process data "+o);
                    }
                    else{
                        logger.debug("type value: ok");
                    }
                    Object r = f.apply(o);
                    logger.info("\t--> '"+r+"'");
                    varObjects.add(r);
                }
                
            }
            transformedVars.put(t.getVar(), varObjects);
            logger.info("VALUES TRANSFORMED: "+varObjects);
//            UtilDebug.exit();
        }
        
        logger.info("TRANSFORMED VARS: "+transformedVars.toString());
        
        logger.info("Transform Element function nb:"+p.getTransformElementFunctions().size());
        Set<Object> instanceAsSetOfObjects = null; 
        if(varsValues.size() == 1){
            instanceAsSetOfObjects = (Set) varsValues.get(varsValues.keySet().iterator().next());
        }
        for(String transformElementFunction : p.getTransformElementFunctions()){
            logger.info("Processing Transform Element "+transformElementFunction);
            ElementFunction f = FunctionLoader.loadElementFunction(transformElementFunction);
            logger.info("\t"+f.toString());
            if(f.isAValidResource(transformedVars)){
                instanceAsSetOfObjects = f.apply(transformedVars);
                logger.info("[result] "+instanceAsSetOfObjects);
            }
            else{
                logger.info("Skipped invalid resource");
            }
        }
        logger.info("Instance as set of objects [tmp]: "+instanceAsSetOfObjects);
        
        logger.info("Transform Set function nb:"+p.getTransformSetFunctions().size());
        for(String functionCall : p.getTransformSetFunctions() ){
            logger.info("Processing Transform Set "+functionCall);
            SetFunction f = FunctionLoader.loadSetFunction(functionCall);
            logger.info("\t"+f.toString());
            if(f.isAValidResource(instanceAsSetOfObjects)){
                instanceAsSetOfObjects = f.apply(transformedVars);
            }
            
        }
        
        logger.info("Instance as set of objects: "+instanceAsSetOfObjects);
        
        
        return instanceAsSetOfObjects;
    }
    
}
