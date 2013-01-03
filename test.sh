#!/bin/sh
./recompile.sh
./reobfuscate.sh
cp -R src/minecraft/org/pfaa/geologica/resources \
    reobf/minecraft/org/pfaa/geologica
cp -R src/common/org/pfaa/geologica/resources \
    reobf/minecraft/org/pfaa/geologica
jar cf PFAAGeologica-0.0.1.zip -C reobf/minecraft org/pfaa
cp PFAAGeologica-0.0.1.zip ~/.minecraft/mods
java -jar ~/personal/fun/minecraft/MagicLauncher.jar
