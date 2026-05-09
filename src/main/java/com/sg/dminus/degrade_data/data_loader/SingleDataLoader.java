package com.sg.dminus.degrade_data.data_loader;

import com.sg.dminus.degrade_data.DegradeCodec;
import com.sg.dminus.degrade_data.DegradeRegistry;
import com.sg.dminus.degrade_data.data_model.SingleDataModel;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class SingleDataLoader extends SimpleJsonResourceReloadListener<SingleDataModel> {
    public static final Logger LOGGER = LoggerFactory.getLogger("durability-minus");
    public SingleDataLoader() {
        super(DegradeCodec.SingleCodec, FileToIdConverter.json("degrade_ratio/items"));
    }

    @Override
    protected void apply(Map<Identifier, SingleDataModel> prepared, ResourceManager manager, ProfilerFiller profiler) {

        for (Map.Entry<Identifier, SingleDataModel> entry: prepared.entrySet()) {
            SingleDataModel model = entry.getValue();

            Identifier itemId = model.item();

            Optional<Item> optionalItem = BuiltInRegistries.ITEM.getOptional(itemId);

            if (optionalItem.isEmpty()) {
                LOGGER.warn("Unknown item id in degrade ratio: {}", itemId);
                continue;
            }

            Item item = optionalItem.get();

            DegradeRegistry.register(item, model.value());
        }
    }
    
}
