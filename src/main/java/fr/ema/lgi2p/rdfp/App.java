package fr.ema.lgi2p.rdfp;

import fr.ema.lgi2p.rdfp.confparser.ConfParserJSON;
import fr.ema.lgi2p.rdfp.confparser.conf.RDFPConfiguration;
import fr.ema.lgi2p.rdfp.core.Context;
import fr.ema.lgi2p.rdfp.sim.SimEngine;
import fr.ema.lgi2p.rdfp.sim.SimRDFP;
import fr.ema.lgi2p.rdfp.stat.StatComputer;
import java.io.File;
import org.openrdf.OpenRDFException;
import org.openrdf.model.URI;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.memory.MemoryStore;
import slib.utils.ex.SLIB_Ex_Critic;

public class App {

    public static void main(String[] args) throws Exception {
        String configuration = "/data/projections/persons/configuration.json";
        String data;
        data = "/data/projections/persons/data.nt";
//        data = "/tmp/rdfdata.nt";

        ConfParserJSON reader = new ConfParserJSON();
        RDFPConfiguration conf = reader.load(configuration);

        Repository repo = new SailRepository(new MemoryStore());
        repo.initialize();

        File file = new File(data);
        String baseURI = null;

        try {
            RepositoryConnection con = repo.getConnection();
            try {
                con.add(file, baseURI, RDFFormat.NTRIPLES);
            } finally {
                con.close();
            }
        } catch (OpenRDFException e) {
            throw new SLIB_Ex_Critic(e.getMessage());
        }

        for (Context c : conf.getContexts()) {
            StatComputer.computeStats(repo, c, conf.getPrefixes());
        }
        
        Context c = conf.getContext("Person");
        
        URI instance_A = repo.getValueFactory().createURI("http://ex/instances/p_1");
        SimEngine simEngine = new SimEngine();
        
        SimRDFP.computeProjectionVectors(simEngine, repo, instance_A, c, conf.getPrefixes());
        
        
        

//        PrintWriter writer = new PrintWriter("/tmp/rdfdata.nt", "UTF-8");
//        for (int i = 0; i < 10000; i++) {
//            String uriPerson = "<http://ex/instances/p_" + i + ">";
//            writer.println(uriPerson + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://ex/person> .");
//            writer.println(uriPerson + "<http://xmlns.com/foaf/0.1/name> \"person_" + i + "\".");
//            writer.println(uriPerson + " <http://ex/height> \"180\" .");
//            writer.println(uriPerson + " <http://ex/weight> \"80\" .");
//        }
//        writer.close();
//


    }
}
