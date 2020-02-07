import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import virtuoso.jena.driver.VirtModel;

@SuppressWarnings("unchecked")
public class Main {

    public static void main(String[] args) {

        String URL = "jdbc:virtuoso://localhost:1111";
        final String uid = "dba";
        final String pwd = "dba";

        Model ontology = ModelFactory.createDefaultModel();
        ontology.read("./src/main/resources/dataset.n3");

        VirtModel vdata = VirtModel.openDatabaseModel("virtuoso-example2", URL, uid, pwd);
        vdata.read("./src/main/resources/dataset.n3");

        Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL("./src/main/resources/rules.txt"));
        reasoner = reasoner.bindSchema(vdata);

        InfModel inf = ModelFactory.createInfModel(reasoner, vdata);

        StmtIterator it = inf.listStatements();

        while (it.hasNext()) {

            Statement stmt = it.nextStatement();

            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            System.out.println(subject.toString() + " " + predicate.toString() + " " + object.toString());

        }

        vdata.close();
    }
}
