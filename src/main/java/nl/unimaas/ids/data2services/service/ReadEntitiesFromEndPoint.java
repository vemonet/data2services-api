/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import nl.unimaas.ids.rdf2api.io.utils.HttpURLConnect;

/**
 *
 * @author nuno
 */
public class ReadEntitiesFromEndPoint {
	private static final Logger logger = Logger.getLogger(ReadEntitiesFromEndPoint.class.getName());

	HttpURLConnect httpConnect;
	String endpointURL;

	public static void main(String[] args) {
		System.out.println("Starting...");
		ReadEntitiesFromEndPoint entities = new ReadEntitiesFromEndPoint();

		String e = entities.getClassList();
		System.out.println(e);
		// entities.getSubject(new
		// URIImpl("http://identifiers.org/wikipathways/WP1000_r80835"));
	}

	public ReadEntitiesFromEndPoint() {
		this.httpConnect = new HttpURLConnect();
		this.endpointURL = "http://graphdb.dumontierlab.com/repositories/ncats-red-kg";
	}

	// @Override
	public void setSource(URL url) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public String getClassList() {
		String queryString = "select distinct ?Concept where {[] a ?Concept}";

		String result = "";
		try {
			result = post(queryString);
		} catch (Exception ex) {
			Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
		}

		System.out.println(result);

		return result;

	}

	public String getSources() {
		String queryString = "select distinct ?g where {graph ?g {?s ?p ?o}}";
		return post(queryString);
	}

	public String getSubject(IRI subjectURI) throws Exception {

		// Repository repository = repositoryManager.getRepository("RepositoryID");
		// Open a connection to this repository
		// Open a connection to the database

		String queryString = "SELECT ?p ?o \n" + "WHERE {\n" + "  <" + subjectURI.stringValue() + "> ?p ?o.\n" + "}";

		String result = post(queryString);
		return result;
	}

	public String getSubjectListByClass(IRI classURI) throws Exception {

		String queryString = "SELECT ?s ?p ?o \n" + "WHERE {\n" + "  ?s rdf:type <" + classURI.stringValue() + ">.\n" +
		// " ?p ?o.\n" +
				"} ORDER BY ?s";

		System.out.println(queryString);

		String result = post(queryString);

		return result;
	}

	private String myvar; // legacy to be deleted

	public void getEntityTesting(IRI uri) {
		Repository db = new SailRepository(new MemoryStore());
		db.initialize();

		// Open a connection to the database
		try (RepositoryConnection conn = db.getConnection()) {
			try {
				InputStream input = new ByteArrayInputStream(this.myvar.getBytes(StandardCharsets.UTF_8));
				// .class.getResourceAsStream("/" + filename)) {
				// add the RDF data from the inputstream directly to our database
				conn.add(input, "", RDFFormat.TURTLE);

			} catch (IOException ex) {
				logger.log(Level.SEVERE, null, ex);
			}

			// We do a simple SPARQL SELECT-query that retrieves all resources of
			// type `ex:Artist`, and their first names.
			String queryString = "PREFIX ex: <http://example.org/> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?s ?n \n";
			queryString += "WHERE { \n";
			queryString += "    ?s a ex:Artist; \n";
			queryString += "       foaf:firstName ?n .";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			// A QueryResult is also an AutoCloseable resource, so make sure it gets
			// closed when done.
			try (TupleQueryResult result = query.evaluate()) {
				// we just iterate over all solutions in the result...
				while (result.hasNext()) {
					BindingSet solution = result.next();
					// ... and print out the value of the variable bindings
					// for ?s and ?n
					System.out.println("?s = " + solution.getValue("s"));
					System.out.println("?n = " + solution.getValue("n"));
				}
			}
		} finally {
			// Before our program exits, make sure the database is properly shut down.
			db.shutDown();
		}
	}

	private String post(String data) {
		try {
			String r = this.httpConnect.sendPost(this.endpointURL, data);
			return r;
		} catch (Exception ex) {
			Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
			return "error"; // replace with custom error
		}
	}
}