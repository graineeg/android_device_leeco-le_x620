 #!/bin/bash
 cd ../../../..
 cd frameworks/av
 patch -p1 < ../../device/leeco/le_x620/patches/frameworks_av.patch
 cd ../..
 cd frameworks/native 
 patch -p1 < ../../device/leeco/le_x620/patches/frameworks_native.patch
 cd ../..
 cd system/sepolicy
 patch -p1 < ../../device/leeco/le_x620/patches/system_sepolicy.patch
 cd ../..
 cd hardware/libhardware
 patch -p1 < ../../device/leeco/le_x620/patches/hardware_libhardware.patch
 cd ../..
 cd system/core
 patch -p1 < ../../device/leeco/le_x620/patches/system_core.patch
 cd ../..
 cd system/netd
 patch -p1 < ../../device/leeco/le_x620/patches/system_netd.patch
 cd ../..
 echo Patches Applied Successfully!


