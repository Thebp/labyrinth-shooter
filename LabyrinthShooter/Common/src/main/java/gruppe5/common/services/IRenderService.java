package gruppe5.common.services;

import gruppe5.common.data.GameData;
import gruppe5.common.data.World;

public interface IRenderService  {
        void create(GameData gameData, World world);
        void render(GameData gameData, World world);
}
