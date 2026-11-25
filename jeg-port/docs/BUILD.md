# Building the JEG 1.21.1 NeoForge port

This port has NEVER been compiled (it was written without a compiler). The first build will almost certainly show errors;
the workflow below gets you all of them in one go so they can be fixed in bulk.

## Requirements
- Java 21 JDK (`java -version` must say 21). Gradle itself is downloaded by the wrapper (9.2.0); first run needs internet
  (NeoForge/Minecraft/Parchment/GeckoLib downloads, a few GB, several minutes).
- MrCrayfish's Framework jar for NeoForge 1.21.1: `framework-neoforge-1.21.1-0.13.11.jar`.

## Steps
1. Create the folder `libs/` in the project root (it is git-ignored) and copy `framework-neoforge-1.21.1-0.13.11.jar` into it.
   (The file name must match `framework_jar` in `gradle.properties`; if you use another 0.13.x jar, change that line.)
2. Get ALL compile errors at once (Linux/macOS; on Windows use `gradlew.bat`):
   ```
   ./gradlew compileJava --continue 2>&1 | tee errors.txt
   ```
   Send `errors.txt` back. (Errors are capped at 2000 by `-Xmaxerrs` in build.gradle.) If Gradle itself fails before compiling
   (dependency/AT errors), `errors.txt` contains that too - send it anyway.
3. When it compiles: `./gradlew build` -> the mod jar is `build/libs/JESM-0.14.0-1.21.1.jar`.
4. Optional test run from the dev environment: `./gradlew runClient` (or `runServer`).

## Putting it in your modpack (NeoForge 1.21.1)
- The mod id of this port is `jeg`. JEGN (also modid `jeg`) uses the same id, so you MUST remove the JEGN jar when you add this one
  (two mods with one id will not load). Anything that depends on JEGN's own classes/items/vehicles will break.
- Required next to it: MrCrayfish's Framework 0.13.x (NeoForge 1.21.1) and GeckoLib 4.8.x (NeoForge 1.21.1).
- Dropped compat (no effect if those mods are present): Controllable, Shoulder Surfing, Valkyrien Skies, Guard Villagers, Recruits,
  Villager Gunners, Death Angels, Migamigos, Superpower Allstars. JEI integration and datagen were removed.
- Existing 1.20.1 worlds/items are not compatible (item NBT became data components); use a new world.

## If startup (not compile) fails
- "Access transformer ... member not found": a Bee/Beehive name in `src/main/resources/META-INF/accesstransformer.cfg` is wrong; the log names it.
- "Mixin ... failed": see `docs/notes/integration.md` (all risky mixins use `require = 0`, so they skip instead of crashing).
- Send `logs/latest.log` (and `crash-reports/*`) together with what you were doing.
