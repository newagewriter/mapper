# Mapper generator

## What is it

Mapper generator is a library that create Mapper class for model class with @Mapper annotation.
Mapper class convert object to map/json or create object from given map (json is not supported yet)

## How to use it

1. Add @Mapper annotation to model class:
   ```kotlin
    package example.model
   
    @Mapper
    data class ExampleModel(
        val id: Int,
        val name: String
    )
   ```

2. Mapper for this model will be generated with template model name + Mapper suffix
3. To convert object to map or json. Use below example:
   ```kotlin
    val model = ExampleModel(1, "test")
    val mapper = ExampleModelMapper(model)
    
    val objMap: Map<String, Any?> = mapper.toMap()
    val json: String = mapper.toJson()      
   ```
4. To convert map to object (json is not supported yet) create mapper with map argument instead of model object
   ```kotlin
    val map = mapOf<String, Any?>(
        "id" to 2,
        "name" to "test2"        
    )
    val mapper = ExampleModelMapper(null, map)
    val model = mapper.createMappedObj()
   ```   
5. There is another way to use mapper without using exact name. Instead of import concrete mapper use AbstractMapper
6. To convert object to map/json using AbstractMapper:
   ```kotlin
    val model = ExampleModel(3, "test3")
    val mapper = AbstractMapper.of(model)
   
    // check if mapper for given object exist
    mapper?.let { m -> 
        val objMap: Map<String, Any?> = m.toMap()
        val json: String = m.toJson()
    }
   ```
7. To convert map to object using AbstractMapper:
   ```kotlin
    val map = mapOf<String, Any?>(
        "id" to 4,
        "name" to "test4"
    )
    val mappedObj: ExampleModel? = AbstractMapper.toObject(ExampleModel::class.java, map)
   
    // check if object is not null to use it
    mappedObj?.let { m -> 
        println("id: ${m.id}")
        println("name: ${m.name}")
    }
   ```

## Supported classes

Mapper support all primitive class plus String and Color. Mapper can be nested in other mapper. 
For any others class mapper will be not generated correctly

For more check example module
