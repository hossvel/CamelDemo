package com.devhoss;


import java.util.concurrent.ExecutorService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.spi.ThreadPoolProfile;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
    	
    	getContext().setTracing(true);
    	getContext().addComponent("activemq",  ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
		
    	//getContext().isAutoStartup();
    	
    	// here is a sample which processes the input files
    			// (leaving them in place - see the 'noop' flag)
    			// then performs content based routing on the message using XPath
    			/* 
    	    	from("file:src/data?noop=true")
    	            .choice()
    	                .when(xpath("/person/city = 'London'"))
    	                    .log("UK message")
    	                    .to("file:target/messages/uk")
    	                .otherwise()
    	                    .log("Other message")
    	                    .to("file:target/messages/others");
    			 */


    		//	from("timer:foo?period=10s")
    		//	.log("${body}")
    		//	.to("log:foo");

    	
    		
    			// envia a activemq
    			from("timer:foo?period=2s")
    			.transform(constant("DummyBodyCamel"))
    			.log("Envia a activemq")
    			.to("activemq:mailbox");



    			//lee de activemq
    		//	from("activemq:orderBar")
    		//	.log("${body}")
    		//	.to("activemq:orderFoo");

    			from("activemq:mailbox")
    			.log("${body}")
    			.choice()
    			.when(bodyAs(String.class).contains("Camel"))
    			.loadBalance().roundRobin()
    			.to("activemq:orderFoo").to("activemq:orderBar").endChoice()
    			.otherwise().to("activemq:orderDef");

    		/// use Bean procesor
    			from("activemq:orderFoo")
    			.log("${body}")
    			.bean(MyBeanProcessor.class,"Modificar(${body})")
    			.to("activemq:orderDef");
    			
    		/// add parallelProcessing	
    			from("activemq:orderDef")
    			.multicast().parallelProcessing()
    			.to("activemq:orderFoo")
    			.to("activemq:orderBar")
    			.to("activemq:orderQueue");
    			
    			
    }

}
