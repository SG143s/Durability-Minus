package com.sg.dminus.degrade_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sg.dminus.degrade_data.data_model.GroupDataModel;
import com.sg.dminus.degrade_data.data_model.SingleDataModel;
import net.minecraft.resources.Identifier;

public class DegradeCodec {
    public static final Codec<SingleDataModel> SingleCodec =
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            Identifier.CODEC
                                    .fieldOf("item")
                                    .forGetter(SingleDataModel::item),
                            Codec.floatRange(0, Float.MAX_VALUE)
                                    .optionalFieldOf("degrade_value", 0.0f)
                                    .forGetter(SingleDataModel::value)
                    )
                            .apply(instance, SingleDataModel::new)
            );
    public static final Codec<GroupDataModel> GroupCodec =
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            Identifier.CODEC
                                    .listOf()
                                    .fieldOf("items")
                                    .forGetter(GroupDataModel::items),
                            Codec.floatRange(0, Float.MAX_VALUE)
                                    .optionalFieldOf("degrade_value",0.0f)
                                    .forGetter(GroupDataModel::value)
                    )
                            .apply(instance, GroupDataModel::new)
            );
}
