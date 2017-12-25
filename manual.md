
#####################################################
#                                                   #
#                Manual for building                #
#                   LineageOS 14.1                  #
#                         for                       #
#                LeEco Le2/Pro (X620)               #
#                                                   #
#####################################################

| Step                | Task                                                                                                                 |
| :-------------------| :--------------------------------------------------------------------------------------------------------------------|
| 1.Init source       | repo init -u git://github.com/LineageOS/android.git -b cm-14.1                                                       |
|                     |                                                                                                                      |
| 2.                  | repo sync                                                                                                            |
| 3.Init device files | git clone https://github.com/graineeg/android_device_leeco-le_x620.git -b cm-14.1 device/leeco/le_x620               |
|                     | git clone https://github.com/graineeg/android_vendor_leeco-le_x620.git -b cm-14.1 vendor/leeco/le_x620               |
|                     | git clone https://github.com/graineeg/android_hardware_leeco-le_x620.git -b cm-14.1 hardware/leeco                   |
|                     |                                                                                                                      |
| 4.Apply patches     | cd device/leeco/le_x620/patches                                                                                      |
|                     | . patch.sh                                                                                                           |
|                     |                                                                                                                      |
| 5.Setup enviroment  | . build/envsetup.sh                                                                                                  |
| 6.Init device       | breakfast le_x620                                                                                                    |
| 7.Start build       | brunch le_x620                                                                                                       |
|                     |                                                                                                                      |
| 8.Coffee            | Just wait when building is finished. For me it took 1.5h                                                             |

| Notes:                                                                                                                                     |
| :------------------------------------------------------------------------------------------------------------------------------------------|
| Make fingerprint working: Put symlinks into updater-script in ROM zip                                                                      |
| symlink("/system/lib/hw/libMcGatekeeper.so", "/system/lib/hw/gatekeeper.mt6797.so");                                                       |
| symlink("/system/lib/hw/libMcGatekeeper.so", "/system/lib/hw/gatekeeper.le_x6.so");                                                        |
| symlink("/system/lib64/hw/libMcGatekeeper.so", "/system/lib64/hw/gatekeeper.mt6797.so");                                                   |
| symlink("/system/lib64/hw/libMcGatekeeper.so", "/system/lib64/hw/gatekeeper.le_x6.so");                                                    |
|                                                                                                                                            |



#####################################################
#                                                   #
#                   Making patches                  #
#                     in source                     #
#                                                   #
#####################################################

| Step                | Task                                                                                                                 |
| :-------------------| :--------------------------------------------------------------------------------------------------------------------|
| 1.Location          | Cd working_dir (For example: cd lineage/frameworks/base)                                                             |
| 2.Show edited       | git status ( edited files will be showned )                                                                          |
| 3.Add edited        | git add    ( edited files )                                                                                          |
| 4.Apply             | git commit -m something-you-wont-some-description                                                                    |
| 5.Make patch        | git show HEAD > ../../device/leeco/le_x620/patches/0001_something_you_want.patch                                     |
|                     |                                                                                                                      |
| 6.Revert            | Step 1.                                                                                                              |
|                     | git reset --hard                                                                                                     |




