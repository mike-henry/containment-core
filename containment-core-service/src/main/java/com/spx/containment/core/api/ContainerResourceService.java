package com.spx.containment.core.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import com.spx.containment.core.actions.ActionExecutor;
import com.spx.containment.core.actions.ContainerActionFactory;
import com.spx.containment.core.actions.CreateContainerAction;
import com.spx.containment.core.actions.CreateContainerTreeAction;
import com.spx.containment.core.actions.GetContainerAction;
import com.spx.containment.core.actions.GetContainerTreeNodeAction;
import com.spx.containment.core.actions.RemoveContainerAction;
import com.spx.containment.core.api.model.Container;
import com.spx.containment.core.security.ApplicationRoles;
import io.swagger.annotations.Api;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Produces({APPLICATION_JSON, TEXT_PLAIN})
@Consumes({APPLICATION_JSON, TEXT_PLAIN})
@Path(ContainerResourceService.RESOURCE)
@Api(ContainerResourceService.RESOURCE)
@Slf4j
public class ContainerResourceService implements ContainerResource {

  private final ContainerActionFactory actionFactory;
  private final ActionExecutor executor;
//  @Autowired
//  Logging logging;


  @Autowired
  public ContainerResourceService(ContainerActionFactory actionFactory, ActionExecutor executor) {
    this.actionFactory = actionFactory;
    this.executor = executor;
  }


  @Override
  @POST
  @Consumes(APPLICATION_JSON)
  @PermitAll /// temp
  @Transactional
  @Deprecated
  public void createTree(ContainerView[] containerViews) {
    final CreateContainerTreeAction action = actionFactory.buildCreateContainerTreeAction(
        containerViews);
    executor.call(action);
    log.debug("test");
  }

  @Override
  @Path(PARENT_RESOURCE_NAME)
  @POST
  @Consumes(APPLICATION_JSON)
  @RolesAllowed({ApplicationRoles.USER})
  @Transactional
  public void createContainer(@PathParam("parent") @NotNull String parentName,
      @NotNull Container container) {
    final CreateContainerAction action = actionFactory.buildCreateContainerAction(parentName,
        container);
    executor.call(action);
  }

  @Override
  @Path(RESOURCE_NAME)
  @GET
  @RolesAllowed({ApplicationRoles.USER})
  public Container getContainerDetails(@PathParam(NAMED_PARAM) @NotNull String rootName) {
    GetContainerAction action = actionFactory.buildGetContainerAction(rootName);
    return executor.call(action);
  }

  @Override
  @Path(RESOURCE_TREE_NAME)
  @GET
  @RolesAllowed({ApplicationRoles.USER})
  public ContainerTreeNode[] getTreeNode(@PathParam(NAMED_PARAM) @NotNull String rootName) {
    GetContainerTreeNodeAction action = actionFactory.buildGetContainerTreeNodeAction(rootName);
    return executor.call(action);
  }

  @Override
  @Path(RESOURCE_NAME)
  @DELETE
  @RolesAllowed({ApplicationRoles.USER})
  public void removeContainer(@PathParam(NAMED_PARAM) @NotNull String name) {
    RemoveContainerAction action = actionFactory.buildRemoveContainerTreeAction(name);
    executor.call(action);
  }

}
