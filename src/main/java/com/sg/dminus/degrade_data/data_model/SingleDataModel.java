package com.sg.dminus.degrade_data.data_model;

import net.minecraft.resources.Identifier;

public record SingleDataModel(
        Identifier item,
        float value
) {}
