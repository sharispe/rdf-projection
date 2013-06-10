/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function.custom.string;

import fr.ema.lgi2p.rdfp.function.ValueFunction;
import org.openrdf.model.Value;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class StringToLowerCase implements ValueFunction {

    public boolean isAValidResource(Value o) {
        return true;
    }

    public Object apply(Value o) throws SLIB_Exception {
        
        return o.stringValue().toLowerCase();
    }

}
