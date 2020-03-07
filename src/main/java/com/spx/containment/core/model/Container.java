package com.spx.containment.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;


@Getter
@Setter
@Slf4j
public class Container extends AbstractReferencable {


    @Relationship(type = "PARENTING", direction = Relationship.INCOMING)
    private Set<Container> children = new HashSet<Container>();

    @Relationship(type = "PARENTING", direction = Relationship.OUTGOING)
    private Container parent;

    public Set<Container> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Optional<Container> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(Container newParent) {
        newParent.addChild(this);
    }

    public void addChild(Container child) {

        if (isAncestor(child)) {
            throw new IllegalArgumentException("Container in Ancestor");
        }

        if (child.getParent().isPresent()) {
            child.getParent().get().children.remove(child);
        }
        child.parent = this;
        children.add(child);
    }

    private boolean isAncestor(Container child) {
        return getAncestors().contains(child);
    }

    private List<Container> getAncestors() {
        Container presentAncestor = this;
        List<Container> result = new ArrayList<Container>();
        // result.add(presentAncestor);
        while (presentAncestor.getParent().isPresent()) {
            log.error(presentAncestor.getName());
            presentAncestor = presentAncestor.getParent().get();
            result.add(presentAncestor);
            if (presentAncestor instanceof Global) {
                break;
            }
        }
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Container other = (Container) obj;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (parent == null) {
            return other.parent == null;
        } else return parent.equals(other.parent);
    }


}
