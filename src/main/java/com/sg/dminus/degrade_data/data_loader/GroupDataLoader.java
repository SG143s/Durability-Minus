package com.sg.dminus.degrade_data.data_loader;

import com.sg.dminus.degrade_data.DegradeCodec;
import com.sg.dminus.degrade_data.DegradeRegistry;
import com.sg.dminus.degrade_data.data_model.GroupDataModel;
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

public class GroupDataLoader extends JsonDataLoader<GroupDataModel> {
    public static final Logger LOGGER = LoggerFactory.getLogger("durability-minus");
    public GroupDataLoader() {
        super(DegradeCodec.GroupCodec, ResourceFinder.json("degrade_ratio/item_groups"));
    }

    @Override
    protected void apply(Map<Identifier, GroupDataModel> prepared, ResourceManager manager, Profiler profiler) {

        for (Map.Entry<Identifier, GroupDataModel> entry: prepared.entrySet()) {
            GroupDataModel model = entry.getValue();
            float deg_value = model.value();

            for(Identifier itemId: model.items()) {
                Optional<Item> optionalItem = Registries.ITEM.getOptionalValue(itemId);

                if (optionalItem.isEmpty()) {
                    LOGGER.warn("Unknown item id in degrade ratio: {}", itemId);
                    continue;
                }

                Item item = optionalItem.get();

                DegradeRegistry.register(item, deg_value);
            }

        }
        LOGGER.info("Group loader entries: {}", prepared.size());

    }
}
