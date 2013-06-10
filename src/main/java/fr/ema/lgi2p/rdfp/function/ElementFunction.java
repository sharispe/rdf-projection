/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function;

import java.util.Map;
import java.util.Set;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public interface ElementFunction {
    
    boolean isAValidResource(Map<String,Set<Object>> values);
    Set<Object> apply(Map<String,Set<Object>> o) throws SLIB_Exception;
}
