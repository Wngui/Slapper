package dk.wngui.slapper.Arena;

import dk.wngui.slapper.ArenaStages.Special.Store;
import dk.wngui.slapper.models.IArenaStage;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ArenaStages {

    public final List<IArenaStage> stageList;

    public ArenaStages() {
        this.stageList = new ArrayList<>();
        //stageList.add(new Leaves());

        Reflections reflections = new Reflections("dk.wngui.slapper.ArenaStages");

        var allClasses = reflections.getSubTypesOf(IArenaStage.class);

        allClasses.forEach(clazz -> {
            try {
                var stage = clazz.getDeclaredConstructor().newInstance();
                if (!(stage instanceof Store)) {
                    stageList.add(stage);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });

        System.out.println(stageList);
    }

    //RANDOM
}