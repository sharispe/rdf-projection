/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function;

import fr.ema.lgi2p.rdfp.function.custom.number.Multiply;
import fr.ema.lgi2p.rdfp.function.custom.number.bodyMassIndex.BodyMassIndex;
import fr.ema.lgi2p.rdfp.function.custom.set.SetToCardinality;
import fr.ema.lgi2p.rdfp.function.custom.string.StringToLowerCase;
import fr.ema.lgi2p.rdfp.function.custom.string.StringTrim;
import fr.ema.lgi2p.rdfp.sim.SimRDFP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class FunctionLoader {
    
    static Logger logger = LoggerFactory.getLogger(FunctionLoader.class);

    public static ValueFunction loadValueFunction(String function) throws SLIB_Exception {
        logger.info("Loading VAR function "+function);
        String[] arr = function.split("\\(|\\)");
        
        String fname = arr[0];
        String argsAsString = null;
        if(arr.length > 1){ argsAsString = arr[1]; }
        
        logger.info("name: "+fname);
        logger.info("args: "+argsAsString);
        
        ValueFunction f = null;
        
        if(fname.equals("Number:multiply")){
            f = new Multiply(argsAsString);
        }
        else if(fname.equals("String:trim")){
            f = new StringTrim();   
        }
        else if(fname.equals("String:toLowerCase")){
            f = new StringToLowerCase();
        }
        if(f == null){
            throw new SLIB_Exception("Error loading function "+fname+" (complete call:"+function+") Cannot be found");
        }
        return f;
    }
    
    public static ElementFunction loadElementFunction(String function) throws SLIB_Exception {
        logger.info("Loading Element function "+function);
        String[] arr = function.split("\\(|\\)");
        
        String fname = arr[0];
        String argsAsString = null;
        if(arr.length > 1){ argsAsString = arr[1]; }
        
        logger.info("name: "+fname);
        logger.info("args: "+argsAsString);
        
        ElementFunction f = null;
        
        if(fname.equals("Custom:bodyMassIndex")){
            f = new BodyMassIndex(argsAsString);
        }
        
        if(f == null){
            throw new SLIB_Exception("Error loading function "+fname+" (complete call:"+function+") Cannot be found");
        }
        return f;
    }
    
    public static SetFunction loadSetFunction(String function) throws SLIB_Exception {
        logger.info("Loading Set function "+function);
        String[] arr = function.split("\\(|\\)");
        
        String fname = arr[0];
        String argsAsString = null;
        if(arr.length > 1){ argsAsString = arr[1]; }
        
        logger.info("name: "+fname);
        logger.info("args: "+argsAsString);
        
        SetFunction f = null;
        
        if(fname.equals("Set:toCardinality")){
            f = new SetToCardinality();
        }
        
        if(f == null){
            throw new SLIB_Exception("Error loading function "+fname+" (complete call:"+function+") Cannot be found");
        }
        return f;
    }
    
}
