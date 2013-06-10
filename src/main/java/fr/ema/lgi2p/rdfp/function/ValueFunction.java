/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function;

import org.openrdf.model.Value;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public interface ValueFunction {
    
    boolean isAValidResource(Value o);
    Object apply(Value o) throws SLIB_Exception;
}
