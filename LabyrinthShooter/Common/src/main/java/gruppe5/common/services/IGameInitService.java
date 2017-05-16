package gruppe5.common.services;

import gruppe5.common.data.GameData;
import gruppe5.common.data.World;

public interface IGameInitService {
    void start(GameData gameData, World world);
    void stop(GameData gameData, World world);
}
