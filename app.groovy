@Grab('org.eclipse.jetty.aggregate:jetty-server')
@Grab('org.eclipse.jetty.aggregate:jetty-servlet')
@Grab('javax.servlet:javax.servlet-api')

import groovy.servlet.GroovyServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler

def server = new Server(8080)
def context = new ServletContextHandler(server, '/', ServletContextHandler.SESSIONS)

context.with {
	resourceBase = 'webroot'
	addServlet(DefaultServlet, '/')
	addServlet(GroovyServlet, '*.groovy')
	welcomeFiles = ['index.groovy']
}

server.start()
