/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function.custom.number;

import fr.ema.lgi2p.rdfp.function.ValueFunction;
import org.openrdf.model.Value;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class Multiply implements ValueFunction {
    
    double multiple = 0;
    
    public Multiply(String args) throws SLIB_Exception{
        
        multiple = Double.parseDouble(args);
    }

    public boolean isAValidResource(Value o) {
        try {
            Double.parseDouble(o.stringValue());
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public Object apply(Value o) throws SLIB_Exception {
        
        Double oDoubleCentimeter = null;
        try{
            double odouble = Double.parseDouble(o.stringValue());
            oDoubleCentimeter = odouble*multiple;
        }
        catch(Exception e ){
            e.printStackTrace();
            throw new SLIB_Exception(e.getMessage());
        }
        return oDoubleCentimeter;
    }

}
