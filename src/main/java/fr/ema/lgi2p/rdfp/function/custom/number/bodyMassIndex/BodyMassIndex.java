/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function.custom.number.bodyMassIndex;

import fr.ema.lgi2p.rdfp.function.ElementFunction;
import fr.ema.lgi2p.rdfp.function.ValueFunction;
import fr.ema.lgi2p.rdfp.transformer.Transformer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class BodyMassIndex implements ElementFunction {
    
    static Logger logger = LoggerFactory.getLogger(BodyMassIndex.class);
    

    String bindWeight, bindHeight;
    
    public BodyMassIndex(String argsBind) throws SLIB_Exception {

        logger.info("BodyMassIndex Bind variables");
        String[] args = argsBind.split(",");
        if (args.length != 2) {
            throw new SLIB_Exception("Cannot extract parameters from " + argsBind);
        }

        for (String s : args) {
            
            String[] params = s.split("=");
            
            
            if (params[0].equals("height")) {
                bindHeight = params[1];
            }
            if (params[0].equals("weight")) {
                bindWeight = params[1];
            }
        }



        if (bindHeight == null) {
            throw new SLIB_Ex_Critic("Error cannot bind parameter height");
        }
        if (bindWeight == null) {
            throw new SLIB_Ex_Critic("Error cannot bind parameter weight");
        }
        
        if(bindHeight.startsWith("?")){
            bindHeight = bindHeight.substring(1);
        }
        if(bindWeight.startsWith("?")){
            bindWeight = bindWeight.substring(1);
        }
        
        logger.info("height: "+bindHeight);
        logger.info("weight: "+bindWeight);
    }

    public boolean isAValidResource(Map<String, Set<Object>> values) {
        
        Set<Object> heights = values.get(bindHeight);
        if(heights ==  null || heights.size() != 1){
            logger.info("Cannot bind height ("+bindHeight+")"+heights);
            logger.info("values: "+values);
            return false;
        }
        Set<Object> weights = values.get(bindWeight);
        if(weights ==  null || weights.size() != 1){
            logger.info("Cannot bind weight ("+bindWeight+") "+weights);
            logger.info("values: "+values);
            return false;
        }
        return true;
        
    }

    public Set<Object> apply(Map<String, Set<Object>> o) throws SLIB_Exception {
        
        Set<Object> weightValues = o.get(bindWeight);
        Set<Object> heightValues = o.get(bindHeight);
        
        if(weightValues.isEmpty() || heightValues.isEmpty()){
         return new HashSet<Object>();   
        }
        Object w = weightValues.iterator().next();
        Object h = heightValues.iterator().next();
        String wstring = w.toString();
        String hstring = h.toString();
        
        if(h instanceof Literal){
            hstring = ((Literal) h).stringValue();
        }
        if(w instanceof Literal){
            wstring = ((Literal) w).stringValue();
        }
        double wdouble = Double.parseDouble(wstring);
        double hdouble = Double.parseDouble(hstring);
        
        double bma = wdouble/hdouble;
        
        logger.info("BMA: "+bma);
         
        Set<Object> r = new HashSet<Object>();
        r.add(bma);
        return r;
    }
}
