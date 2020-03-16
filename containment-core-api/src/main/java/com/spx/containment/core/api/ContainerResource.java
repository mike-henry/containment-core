package com.spx.containment.core.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.spx.containment.core.api.model.Container;
import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface ContainerResource {


  public final static String RESOURCE = "containers";
  public final static String NAMED_PARAM = "name";
  public final static String RESOURCE_NAME = "/{" + NAMED_PARAM + "}";

  public final static String RESOURCE_TREE_NAME = "/tree/{" + NAMED_PARAM + "}";
  public static final String PARENT_RESOURCE_NAME = "/{parent}";


  @POST
  @Consumes(APPLICATION_JSON)
  @PermitAll
    /// temp

  void createTree(ContainerView[] containerViews);

  @Path(PARENT_RESOURCE_NAME)
  @POST
  @Consumes(APPLICATION_JSON)
  @PermitAll
    /// temp

  void createContainer(@PathParam("parent") @NotNull String parentName,
      @NotNull Container container);

  @Path(RESOURCE_NAME)
  @GET
  Container getContainerDetails(@PathParam(NAMED_PARAM) @NotNull String rootName);

  @Path(RESOURCE_TREE_NAME)
  @GET
  ContainerTreeNode[] getTreeNode(@PathParam(NAMED_PARAM) @NotNull String rootName);

  @Path(RESOURCE_NAME)
  @DELETE
  @PermitAll
  public void removeContainer(@PathParam(NAMED_PARAM) @NotNull String name);
}
