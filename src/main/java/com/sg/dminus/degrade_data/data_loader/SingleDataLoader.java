package com.sg.dminus.degrade_data.data_loader;

import com.sg.dminus.degrade_data.DegradeCodec;
import com.sg.dminus.degrade_data.DegradeRegistry;
import com.sg.dminus.degrade_data.data_model.SingleDataModel;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class SingleDataLoader extends JsonDataLoader<SingleDataModel> {
    public static final Logger LOGGER = LoggerFactory.getLogger("durability-minus");
    public SingleDataLoader() {
        super(DegradeCodec.SingleCodec, ResourceFinder.json("degrade_ratio/items"));
    }

    @Override
    protected void apply(Map<Identifier, SingleDataModel> prepared, ResourceManager manager, Profiler profiler) {

        for (Map.Entry<Identifier, SingleDataModel> entry: prepared.entrySet()) {
            SingleDataModel model = entry.getValue();

            Identifier itemId = model.item();

            Optional<Item> optionalItem = Registries.ITEM.getOptionalValue(itemId);

            if (optionalItem.isEmpty()) {
                LOGGER.warn("Unknown item id in degrade ratio: {}", itemId);
                continue;
            }

            Item item = optionalItem.get();

            DegradeRegistry.register(item, model.value());
        }
    }
    
}
