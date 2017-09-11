# Device Tree for LeEco Le 2/Le 2 Pro (Le_X620)

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
| Rear Camera             | 16/21 MP, LED flash               |
| Front Camera            | 8 MP                              |
| Release Date            | 2016, April                       |

# Device Picture

![Le 2 Pro](http://cdn2.gsmarena.com/vv/pics/leeco/le-eco-le2.jpg "LeEco Le 2")

   # Build
   * repo init -u git://github.com/LineageOS/android.git -b cm-14.1
   * repo sync
   * git clone https://github.com/graineeg/android_device_leeco-le_x620-lineage-14.1.git -b cm-14.1 device/leeco/le_x620
   * git clone https://github.com/graineeg/android_vendor_leeco-le_x620-lineage-14.1.git -b cm-14.1 vendor/leeco/le_x620
   * cd device/leeco/le_x620/patches
   * . apply.sh
   * source build/envsetup.sh
   * breakfast le_x620
   * brunch le_x620
   * Done :)
   
   # Known Issue:-
   * Camera
   * Torch
   * Wifi tethering
   * Battery draining
   
   # Credits:-
   * Verevka
   * DeckerSU
   * AdrianoMartins
   * divis1969
   * xen0n
   * SamarV-121

