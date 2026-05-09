package com.sg.dminus.degrade_data.data_model;

import net.minecraft.resources.Identifier;

import java.util.List;

public record GroupDataModel(
        List<Identifier> items,
        float value
) {}
