package com.sarahk.togglyfiers.registries;

import javax.annotation.Nonnull;

public abstract class TogglyRegistry
{

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    protected static <T> T getNull()
    {
        return null;
    }

}
