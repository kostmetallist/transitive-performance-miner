package org.processmining.plugins.transitiveminer;

import org.processmining.framework.plugin.annotations.*;
import org.processmining.framework.plugin.*;
import org.processmining.contexts.uitopia.*;
import org.processmining.contexts.uitopia.annotations.*;


@Plugin(name = "TransitiveMiner",
    parameterLabels = { "First", "Second", "Third" },
    returnLabels = { "Child" },
    returnTypes = { Person.class })
public class TransitiveMiner {
    @UITopiaVariant(affiliation = "CMC MSU",
        author = "Default",
        email = "default@example.com",
        uiLabel = UITopiaVariant.USEPLUGIN)
    @PluginVariant(requiredParameterLabels = { 0, 1, 2 })
    public static Person procreate(final PluginContext context,
        final Person father, final Person mother,
        final ProcreationConfiguration config) {

        Person child = new Person();
        child.setAge(0);
        child.setName(new Name(config.getName(), father.getLast()));
        return child;
    }

    @UITopiaVariant(affiliation = "CMC MSU",
        author = "Default",
        email = "default@example.com",
        uiLabel = UITopiaVariant.USEPLUGIN)
    @PluginVariant(requiredParameterLabels = { 0, 1 })
    public static Person procreate(final UIPluginContext context,
        final Person father, final Person mother) {

        ProcreationConfiguration config = new ProcreationConfiguration();
        populate(context, config);
        return procreate(context, father, mother, config);
    }
}

public class ProcreationConfiguration {
    private String name;

    public ProcreationConfiguration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
