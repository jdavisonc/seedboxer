#!/bin/bash
###############################################################################
# seedboxer-update.sh
# 
# Copyright (c) 2012 SeedBoxer Team.
# 
# This file is part of SeedBoxer.
# 
# SeedBoxer is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# SeedBoxer is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
#
#####
#
# seedboxer-update.sh: a simple script that install the last snapshot version of SeedBoxer.
# author: Jorge Davison
#
##############################################################################


REPO_URL=https://oss.sonatype.org/content/repositories/snapshots
VERSION=2.1-SNAPSHOT
ARTIFACT=net.seedboxer:seedboxer-standalone:$VERSION
TMP=/tmp
DEST_NAME=seedboxer-standalone-$VERSION.jar
INSTALL_PATH=/usr/local/share/seedboxer
INSTALL_JAR=seedboxer.jar
INIT_SCRIPT=/etc/init.d/seedboxer

mvn org.apache.maven.plugins:maven-dependency-plugin:2.6:get \
    -DrepoUrl=$REPO_URL \
    -Dartifact=$ARTIFACT \
    -Ddest=$TMP/$DEST_NAME


$INIT_SCRIPT stop

cp $TMP/$DEST_NAME $INSTALL_PATH/.
rm $INSTALL_PATH/$INSTALL_JAR
ln -s $INSTALL_PATH/$DEST_NAME $INSTALL_PATH/$INSTALL_JAR

$INIT_SCRIPT start