package com.spx.containment.core.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import com.spx.containment.core.actions.ActionExecutor;
import com.spx.containment.core.actions.ContainerActionFactory;
import com.spx.containment.core.actions.CreateContainerTreeAction;
import com.spx.containment.core.actions.GetContainerTreeAction;
import com.spx.containment.core.actions.RemoveContainerAction;
import io.swagger.annotations.Api;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Produces({APPLICATION_JSON, TEXT_PLAIN})
@Consumes({APPLICATION_JSON, TEXT_PLAIN})
@Path(ContainerResource.RESOURCE)
@Api(ContainerResource.RESOURCE)

public class ContainerResource {

    final static String RESOURCE = "containers";
    private final static String NAMED_PARAM = "name";
    private final static String RESOURCE_NAME = "/{" + NAMED_PARAM + "}";

    private final ContainerActionFactory actionFactory;
    private final ActionExecutor executor;


    @Autowired
    public ContainerResource(ContainerActionFactory actionFactory, ActionExecutor executor) {
        this.actionFactory = actionFactory;
        this.executor = executor;
    }

    // @Path("")
    @POST
    @Consumes(APPLICATION_JSON)
    @PermitAll /// temp
    @Transactional
    public void createTree(ContainerView[] containerViews) {
        final CreateContainerTreeAction action = actionFactory.buildCreateContainerTreeAction(containerViews);
        executor.call(action);
    }

    @Path(RESOURCE_NAME)
    @GET
    @PermitAll /// temp
    public ContainerView[] getTree(@PathParam(NAMED_PARAM) String rootName) {
        GetContainerTreeAction action = actionFactory.buildGetContainerTreeAction(rootName);
        return executor.call(action);
    }

    @Path(RESOURCE_NAME)
    @DELETE
    @PermitAll
    public void removeContainer(@PathParam(NAMED_PARAM) String name) {
        RemoveContainerAction action = actionFactory.buildRemoveContainerTreeAction(name);
        executor.call(action);
    }

}
