#!/bin/bash

echo ""
echo "Patching bionic files"
echo ""
cd ../../../..
cd bionic
git apply -v ../device/leeco/le_x620/patches/bionic.patch

echo ""
echo "Patching frameworks/av files"
echo ""
cd ../
cd frameworks/av
git apply -v ../../device/leeco/le_x620/patches/frameworks_av.patch

echo ""
echo "Patching frameworks/base files"
echo ""
cd ../base
git apply -v ../../device/leeco/le_x620/patches/frameworks_base_fix_fingerprint.patch

echo ""
echo "Patching system/core files"
echo ""
cd ../..
cd system/core
git apply -v ../../device/leeco/le_x620/patches/system_core.patch

echo ""
echo "Patching system/sepolicy files"
echo ""
cd ../sepolicy
git apply -v ../../device/leeco/le_x620/patches/system_sepolicy.patch

echo "Patching system/netd files"
echo ""
cd ../netd
git apply -v ../../device/leeco/le_x620/patches/hotspot_fix.patch
cd ../..

echo ""
echo "Patching done"
