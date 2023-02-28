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

## Add your own converter

In version 0.2.0 and newer of Mapper library, a Converter patter has been added that allows user 
to add some new converter or override existing ones. To do that add annotation @Converter to custom converter class witch
implements GenericConverter interface. GenericConverter contains two method first method is used to convert value to type: toEntity
and second is used to converter type to value. Value must be one of primitive types or String.

For example if user want to change the way to save date to map for example as string (by default converter save date as long):

```kotlin
    import com.newagewriter.processor.converter.Converter
    import com.newagewriter.processor.converter.GenericConverter
    import java.text.DateFormat
    import java.util.Date
    import java.util.Locale

    @Converter(type = Date::class)
    class DateConverter : GenericConverter<Date, String> { 
       private val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale("pl", "PL"))
       override fun toSimpleValue(entity: Date): String { 
          return dateFormat.format(entity) 
       }
       
       override fun toEntity(value: String): Date { 
          return dateFormat.parse(value) 
       }
    }
```

## Supported classes

Mapper support all primitive class plus String and Color. Mapper can be nested in other mapper. 
For any others class mapper will be not generated correctly

For more check example module
