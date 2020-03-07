package com.spx.containment.core.actions;

import com.spx.containment.core.api.ContainerView;
import com.spx.containment.core.services.ContainerEventListener;
import com.spx.containment.core.services.ContainerServices;
import com.spx.containment.core.services.ModelToViewAdaptor;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.stereotype.Component;

@Named
@Component

public class ContainerActionFactory {

    private final ModelToViewAdaptor mapper;
    private final ContainerServices containerServices;
    private final Set<ContainerEventListener> containerEventListeners;

    @Inject
    public ContainerActionFactory(ModelToViewAdaptor modelToViewMapper, ContainerServices containerServices, Set<ContainerEventListener> containerEventListeners) {
        mapper = modelToViewMapper;
        this.containerServices = containerServices;
        this.containerEventListeners = containerEventListeners;
    }

    public CreateContainerTreeAction buildCreateContainerTreeAction(ContainerView[] views) {
        return new CreateContainerTreeAction(mapper, containerServices, views, containerEventListeners);
    }

    public GetContainerTreeAction buildGetContainerTreeAction(String rootName) {
        return new GetContainerTreeAction(mapper, rootName, containerEventListeners);
    }

    public RemoveContainerAction buildRemoveContainerTreeAction(String containerName) {
        return new RemoveContainerAction(containerServices, containerName, containerEventListeners);
    }

}
