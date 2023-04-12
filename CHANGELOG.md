# 0.3.0

### Feature
* add json parser: convert from json to model
* add publishing on maven central
* Add ktlint custom configuration to chekc kotlin code format

### Fixes
* Add missing test for all primitive types
* Rename package to io.github.newagewriter
* Fix Converter utils for java 11 and above
* Fix for wrong enum value issue; Remove unnecessary apostrophes

# 0.2.4

### Feature
* Github package publishing

### Fixes
* Fix library configuration to publish it on github packages


# 0.2.0

### Feature
* Add converter pattern for any not mapper type
* Implement simple converter for Date and Color types

### Fixes
* Fix mapping for enum type

# 0.1.1

### Feature
* Add mapper annotation that create mapper for annotated class. Mapper can convert:
  * object to map/json 
  * map to object
* Create annotation processor that generate mapper classes