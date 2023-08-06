# 0.4.0-beta.1

### Feature
* Add @field annotation to provide set different name for serialization/deserialization: [PR](https://github.com/newagewriter/mapper/pull/77)
* Add @Exclude annotation, this annotation can be used for field to exclude from serialization: [PR](https://github.com/newagewriter/mapper/pull/78)

### Changes
* Refactor AbstractMapper to simplify conversions from json/map to model and vice versa: [PR](https://github.com/newagewriter/mapper/pull/79)

# 0.3.1

### Feature
* Support android project for @Mapper and @Converter annotation.

### Fixes
* Fix build problem when library is used on android project
* Exclude org.hamrest group, it caused conflict with test library like mockito and android espresso

# 0.3.1-beta.1

### Feature
* Basic support android project. ( Not all features may work )

### Fixes
* Fix build problem when library is used on android project
* Exclude org.hamrest group, it caused conflict with test library like mockito and android espresso

# 0.3.0

### Fixes
* Fix issue when mapper model contains list of model

# 0.3.0-rc2

### Fixes
* Remove json-simple library as submodule and use maven dependency instead to fix maven error

# 0.3.0-rc

### Feature
* Add required check for every class that extend GenericConverter
* Add documentation for the most important classes
* Move all method that convert primitive from AbstractMapper to PrimitiveConverter

# 0.3.0-beta

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