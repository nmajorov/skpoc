
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.skyguide.fuse.prototype.fpservice;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.netty.ClientPipelineFactory;
import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.NettyProducer;
import org.apache.camel.component.netty.ServerPipelineFactory;
import org.apache.camel.component.netty.handlers.ClientChannelHandler;
import org.apache.camel.component.netty.handlers.ServerChannelHandler;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.junit.Test;

public class NettyCustomPipelineFactoryAsynchTest extends CamelTestSupport {
	
    private static final transient Log LOG = LogFactory.getLog(NettyCustomPipelineFactoryAsynchTest.class);

    @Produce(uri = "direct:start")
    protected ProducerTemplate producerTemplate;
    private TestClientChannelPipelineFactory clientPipelineFactory;
    private TestServerChannelPipelineFactory serverPipelineFactory;
    private String response;
    
    @Override
    protected JndiRegistry createRegistry() throws Exception {
    
    	
    	JndiRegistry registry = new JndiRegistry(createJndiContext());
    	
        clientPipelineFactory = new TestClientChannelPipelineFactory(null);
        serverPipelineFactory = new TestServerChannelPipelineFactory(null);
        registry.bind("cpf", clientPipelineFactory);
        registry.bind("spf", serverPipelineFactory);
        return registry;
    }
    
    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    private void sendRequest() throws Exception {
        // Async request
        response = (String) producerTemplate.requestBody(
            "netty:tcp://localhost:5110?clientPipelineFactory=#cpf&textline=true", 
            "Forest Gump describing Vietnam...");        
    }
    
    @Test
    public void testCustomClientPipelineFactory() throws Exception {
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("netty:tcp://localhost:5110?serverPipelineFactory=#spf&textline=true")
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            exchange.getOut().setBody("Forrest Gump: We was always taking long walks, and we was always looking for a guy named 'Charlie'");                           
                        }
                    });                
            }
        });
        context.start();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Beginning Test ---> testCustomClientPipelineFactory()");
        }        
        sendRequest();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Completed Test ---> testCustomClientPipelineFactory()");
        }
        context.stop();
        
        assertEquals("Forrest Gump: We was always taking long walks, and we was always looking for a guy named 'Charlie'", response);
//        assertEquals(true, clientPipelineFactory.isfactoryInvoked());
//        assertEquals(true, serverPipelineFactory.isfactoryInvoked());
    } 
    
    public class TestClientChannelPipelineFactory extends ClientPipelineFactory {
        private int maxLineSize = 1024;
        private boolean invoked;
        private NettyProducer producer;

        public TestClientChannelPipelineFactory(NettyProducer producer) {
        	
        	this.producer = producer;
        }
        
        public ChannelPipeline getPipeline() throws Exception {
            invoked = true;
            
            ChannelPipeline channelPipeline = Channels.pipeline();

            channelPipeline.addLast("decoder-DELIM", new DelimiterBasedFrameDecoder(maxLineSize, true, Delimiters.lineDelimiter()));
            channelPipeline.addLast("decoder-SD", new StringDecoder(CharsetUtil.UTF_8));
            channelPipeline.addLast("encoder-SD", new StringEncoder(CharsetUtil.UTF_8));            
            channelPipeline.addLast("handler", new ClientChannelHandler(producer));

            return channelPipeline;

        }
        
        public boolean isfactoryInvoked() {
            return invoked;
        }

		@Override
		public ClientPipelineFactory createPipelineFactory(
				NettyProducer producer) {
			return new TestClientChannelPipelineFactory(producer);
		}
    }
    
    public class TestServerChannelPipelineFactory extends ServerPipelineFactory {
    	
        private int maxLineSize = 1024;
        private boolean invoked;
        private NettyConsumer consumer;
        
        public TestServerChannelPipelineFactory(NettyConsumer consumer) {
			
        	this.consumer = consumer;
		}
        
        public ChannelPipeline getPipeline() throws Exception {
            invoked = true;
            
            ChannelPipeline channelPipeline = Channels.pipeline();

            channelPipeline.addLast("encoder-SD", new StringEncoder(CharsetUtil.UTF_8));
            channelPipeline.addLast("decoder-DELIM", new DelimiterBasedFrameDecoder(maxLineSize, true, Delimiters.lineDelimiter()));
            channelPipeline.addLast("decoder-SD", new StringDecoder(CharsetUtil.UTF_8));
            channelPipeline.addLast("handler", new ServerChannelHandler(consumer));

            return channelPipeline;
        }
        
        public boolean isfactoryInvoked() {
            return invoked;
        }

		@Override
		public ServerPipelineFactory createPipelineFactory(
				NettyConsumer consumer) {
	        return new TestServerChannelPipelineFactory(consumer);
		}
        
    }
}
