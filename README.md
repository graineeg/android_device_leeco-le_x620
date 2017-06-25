# Device Tree for LeEco le 2 Pro (Le_X620)

# Spec Sheet

| Feature                 | Specification                     |
| :---------------------- | :-------------------------------- |
| CPU                     | Deca-core 2.1 GHz                 |
| Chipset                 | Mediatek MT6797 Helio X20         |
| GPU                     | Mali-T880 MP4                     |
| Memory                  | 3/4 GB                            |
| Shipped Android Version | 6.0                               |
| Storage                 | 32 GB                             |
| Battery                 | 3000 mAh (non-removable)          |
| Dimensions              | 151.1 x 74.2 x 7.5 mm             |
| Display                 | 1920x1080 pixels, 5.5 (~401 PPI)  |
| Rear Camera             | 21 MP, LED flash                  |
| Front Camera            | 8 MP                              |
| Release Date            | 2016, April                       |

# Device Picture

![Redmi Note 4](http://cdn2.gsmarena.com/vv/pics/leeco/le-eco-le2.jpg "LeEco Le 2")

   # Build
   * repo init -u git://github.com/LineageOS/android.git -b cm-14.1
   * repo sync
   * git clone https://github.com/SamarV-121/android_device_xiaomi-nikel-lineage-14.1.git -b master device/xiaomi/nikel
   * git clone https://github.com/SamarV-121/android_vendor_xiaomi-nikel-lineage-14.1.git -b cm-14.1 vendor/xiaomi/nikel
   * cd device/xiaomi/nikel/patches
   * . apply.sh
   * source build/envsetup.sh
   * breakfast nikel
   * brunch nikel
   * Done :)
   
   # Known Issue:-
   * FP Scanner
   * Camera
   
   # Credits:-
   * AdrianoMartins
   * divis1969
   * xen0n
   * & Me :)

