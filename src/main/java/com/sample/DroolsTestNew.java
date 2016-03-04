package com.sample;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.sample.model.Concept;
import com.sample.model.Message;
import com.sample.model.Observation;

// this uses the newer Drools Classes for initializing, loading rules
// shows how to dynamically load Drools rules from a String
public class DroolsTestNew {
 
    private static KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    private static Collection<KnowledgePackage> pkgs;
    private static KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    private static StatefulKnowledgeSession ksession;
 
    
    public static void main(final String[] args) {
 
    	
        // this will parse and compile in one step
        // read from file
        kbuilder.add( ResourceFactory.newClassPathResource( "/rules/risks.drl",DroolsTestNew.class),ResourceType.DRL );
 
        // read second rule from String
        String myRule = "import com.sample.model.Message rule \"Hello World 2\" when message:Message (type==\"Test\") then System.out.println(\"Test, Drools!\"); end";
        Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(myRule));
        kbuilder.add(myResource, ResourceType.DRL);
 
        // Check the builder for errors
        if ( kbuilder.hasErrors() ) {
            System.out.println( kbuilder.getErrors().toString() );
            throw new RuntimeException( "Unable to compile drl\"." );
        }
 
        // get the compiled packages (which are serializable)
        pkgs = kbuilder.getKnowledgePackages();
 
        // add the packages to a knowledgebase (deploy the knowledge packages).
        kbase.addKnowledgePackages( pkgs );
 
        ksession = kbase.newStatefulKnowledgeSession();

        Message msg = new Message();
        msg.setType("Test");
        ksession.insert(msg);

        Observation o = new Observation();
        o.setType("fall");
        o.setId(1);
        o.setValueConceptId(123456);
        ksession.insert(o);
                
        List<Integer> list = new ArrayList<Integer>();
        list.add(new Integer("123456"));
        list.add(new Integer("987654"));

        Concept c = new Concept();
        c.setConcepts(list);
        c.setType("obs");
        ksession.insert(c);
        
        
        ksession.fireAllRules();
        
        String x = c.getConcepts().toString();
        x = x.replace('[', ' ');
        x = x.replace(']', ' ');
        System.out.println(x.trim());
    }
    
    
}