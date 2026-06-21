package web2.resource;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import web2.config.ApplicationContextProvider;
import web2.service.EmployeeService;

@Path("/hello")
public class EmployeeResource {

    private EmployeeService employeeService() {
        return (EmployeeService) ApplicationContextProvider.getApplicationContext().getBean("employeeService");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayHello() {
        return "<html><title>Hello</title><body><h1>Hello Sachin</h1></body></html>";
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_HTML)
    public Response greet(@PathParam("name") String name) {
        String statement = employeeService().modifyStatement(name);
        return Response.ok().entity("<html><body><h1>" + statement + "</h1></body></html>").build();
    }

    @GET
    @Path("/names")
    @Produces(MediaType.TEXT_HTML)
    public Response getNames() {
        return Response.ok().entity("<html><body><h1>" + employeeService().getEmployeeNames() + "</h1></body></html>").build();
    }

    @GET
    @Path("/rdx/df")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "abc");
        map.put("2", "def");
        map.put("3", "ghi");
        return Response.status(Response.Status.OK).entity(map).build();
    }
}
