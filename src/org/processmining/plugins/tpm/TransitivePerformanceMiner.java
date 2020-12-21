package org.processmining.plugins.tpm;

import org.processmining.framework.plugin.annotations.*;
import org.processmining.framework.plugin.*;
import org.processmining.contexts.uitopia.*;
import org.processmining.contexts.uitopia.annotations.*;


@Plugin(name = "TransitivePerformanceMiner",
    parameterLabels = { "First", "Second", "Third" },
    returnLabels = { "Child" },
    returnTypes = { Person.class })
public class TransitivePerformanceMiner {
    @UITopiaVariant(affiliation = "ISPRAS",
        author = "Default",
        email = "default@example.com",
        uiLabel = UITopiaVariant.USEPLUGIN)
    @PluginVariant(requiredParameterLabels = { 0, 1, 2 })
    public static Person procreate(final PluginContext context,
        final Person father, final Person mother,
        final ProcreationConfiguration config) {

        Person child = new Person();
        child.setAge(0);
        child.setName(new Name(config.getName(), father.getName().getLast()));
        return child;
    }

    @UITopiaVariant(affiliation = "ISPRAS",
        author = "Default",
        email = "default@example.com",
        uiLabel = UITopiaVariant.USEPLUGIN)
    @PluginVariant(requiredParameterLabels = { 0, 1 })
    public static Person procreate(final UIPluginContext context,
        final Person father, final Person mother) {

        ProcreationConfiguration config = new ProcreationConfiguration("Some configuration");
        return procreate(context, father, mother, config);
    }
}
