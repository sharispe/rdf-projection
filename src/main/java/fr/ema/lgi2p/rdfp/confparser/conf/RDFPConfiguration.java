/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ema.lgi2p.rdfp.confparser.conf;

import fr.ema.lgi2p.rdfp.core.Context;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import slib.utils.ex.SLIB_Exception;

/**
 *
 * @author Sébastien Harispe
 */
public class RDFPConfiguration {

    Map<String, String> prefixes;
    Map<String, Context> contexts;

    public RDFPConfiguration() {
        prefixes = new HashMap<String, String>();
        contexts = new HashMap<String, Context>();
    }

    public Map<String, String> getPrefixes() {
        return Collections.unmodifiableMap(prefixes);
    }

    public void addPrefix(String name, String value) {
        prefixes.put(name, value);
    }

    public void addContext(Context c) throws SLIB_Exception {
        if (contexts.containsKey(c.getName())) {
            throw new SLIB_Exception("Context " + c.getName() + " already exists");
        }
        contexts.put(c.getName(), c);
    }
    
    public Set<Context> getContexts(){
        return new HashSet<Context>(contexts.values());
    }

    public Context getContext(String key) {
        return contexts.get(key);
    }
}
