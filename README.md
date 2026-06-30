# NC3D

3D model support addon for **NarrativeCraft**. Adds 3D characters with GeckoLib models, Ink actions, and networking.

## Установка

1. Скачайте **nc3d-fabric-1.21.1-1.0.0.jar** (Fabric) или **nc3d-neoforge-1.21.1-1.0.0.jar** (NeoForge) из [Releases](https://github.com/ReggiRi/NC3D/releases)
2. Поместите в папку `mods/` вместе с:
   - Minecraft **1.21.1**
   - Fabric Loader **0.19.3** + Fabric API (Fabric) или NeoForge **21.1.233** (NeoForge)
   - **NarrativeCraft 2.0.11**
   - **GeckoLib 4.8.4**

## Ink-команды

| Команда | Описание |
|---------|----------|
| `spawn_model <story_id> <model_id> <texture> [animation]` | Спавн модели в сцене |
| `set_model <character_id> <model_id> <texture> [animation]` | Смена модели персонажа |
| `play_animation <character_id> <name> [speed]` | Проигрывание анимации |
| `stop_animation <character_id>` | Остановка анимации |

## Сборка

```bash
./gradlew :fabric:remapJar    # Fabric
./gradlew :neoforge:jar       # NeoForge
```

## Зависимости

- NarrativeCraft API — `compileOnly`
- GeckoLib — `compileOnly`/`runtime`
- Fabric API — `compileOnly`/`runtime`

Лицензия: MIT
