/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.function.custom.set;

import fr.ema.lgi2p.rdfp.function.SetFunction;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class SetToCardinality implements SetFunction{

    public boolean isAValidResource(Set<Object> values) {
        return true;
    }

    public Set<Object> apply(Map<String, Set<Object>> o) throws SLIB_Exception {
        Set<Object> r = new HashSet<Object>();
        r.add(o.size());
        return r;
    }
    
}
