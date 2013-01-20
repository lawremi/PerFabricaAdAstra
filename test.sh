#!/bin/sh
cp -R src/org ../src/minecraft
cd ..
./recompile.sh
./reobfuscate.sh
cp -R src/minecraft/org/pfaa/geologica/resources \
    reobf/minecraft/org/pfaa/geologica
jar cf PFAAGeologica-0.0.1.zip -C reobf/minecraft org/pfaa
rm -r src/minecraft/org/pfaa
cp PFAAGeologica-0.0.1.zip ~/.minecraft/mods
java -jar ~/personal/fun/minecraft/MagicLauncher.jar
cd pfaa
