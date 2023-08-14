Mapper generator
=====

## What is it

Mapper-kt is a modern json/map converter library for Kotlin, with Android support (since 0.3.0 version).
This library generate mapper class for all models annotated with @Mapper annotation.

   

## Configuration

To use mapper library add dependency to project:

<details open>
  <summary>Gradle</summary>

```groovy
dependencies {
   implementation("io.github.newagewriter:mapper-kt:[version]")
   kapt("io.github.newagewriter:mapper-kt:[version]")
}
   ```

<details>
  <summary>Maven</summary>

```maven
<dependency>
  <groupId>io.github.newagewriter</groupId>
  <artifactId>mapper-kt</artifactId>
  <version>version</version>
</dependency>
   ```
</details>

## How to use it

<details open>
    <summary>For version 0.4.0 and higher</summary>

1. Add @Mapper annotation to model class:
   ```kotlin
    package example.model
   
    @Mapper
    data class ExampleModel(
        val id: Int,
        val name: String
    )
   ```
2. For model above mapper will be generated with name: model's name + 'Mapper' suffix, eg. **ExampleModelMapper**
3. To convert object to map or json use below example:
   ```kotlin
    val model = ExampleModel(1, "test")
    val mapper = ExampleModelMapper(ExampleModel::class.java)
    
    val objMap: Map<String, Any?> = mapper.toMap(model)
    val json: String = mapper.toJson(model)      
   ```
   
   Another way to create mapper is to call: AbsctractMapper.of(model_name_class).
   This method returns object of mapper for given model class or returns null if model class doesn't have @Mapper annotation.

   ```kotlin
    val model = ExampleModel(1, "test")
    AbstractMapper.of(ExampleModel::class.java)?.let { mapper ->
        val objMap: Map<String, Any?> = mapper.toMap(model)
        val json: String = mapper.toJson(model)   
    }
   ```
4. To convert map/json:
   ```kotlin
    val map = mapOf<String, Any?>(
        "id" to 2,
        "name" to "test2"        
    )
    val jsonString = "{ \"id\" : 2, \"name\" : \"test2\" }"
    AbstractMapper.of(ExampleModel::class.java)?.let { mapper ->
        val modelFromMap = mapper.mapToModel(map)
        val modelFromJson = mapper.jsonToModel(jsonString)
    }
   ```   
### @Field annotation 
   From version 0.4.0 model fields can be annotated with @Field annotation. This annotation adds alias for a field and this alias will be used in serialization/deserialization process
   ```kotlin
    
    @Mapper
    data class ExampleModel(
        @Field("_id")
        val id: Int,
        @Field("model_name")
        val name: String
    )
    
    val map = mapOf<String, Any?>(
        "_id" to 2,
        "model_name" to "test2"        
    )
    val jsonString = "{ \"_id\" : 2, \"model_name\" : \"test2\" }"
    AbstractMapper.of(ExampleModel::class.java)?.let { mapper ->
        val modelFromMap = mapper.mapToModel(map)
        val modelFromJson = mapper.jsonToModel(jsonString)
    }
   ```   

### @Exclude annotation
From version 0.4.0 to exclude a field from serialization process add annotation @Exclude to that field. 
   ```kotlin
    @Mapper
    data class ExampleModel(
        val id: Int,
        val name: String,
        @Exclude
        var otherField: String? = ""
    )

    AbstractMapper.of(ExampleModel::class.java)?.let { mapper ->
        val model = ExampleModel(2, "test2", "Some text that will be excluded")
        // map = [ "id" : 2, "name" : "test2" ]
        val map = mapper.toMap(model)
    }
   ```   
</details>

<details>
   <summary>Before version 0.4.0 (deprecated)</summary>

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
   
</details>

## Add your own converter

In version 0.2.0 and newer of Mapper library, a Converter patter has been added that allows user 
to add some new converter or override existing ones. To do that add annotation @Converter to custom converter class witch
implements GenericConverter interface. GenericConverter contains two method first method is used to convert value to type: toEntity
and second is used to converter type to value. Value must be one of primitive types or String.

For example if user want to change the way to save date to map for example as string (by default converter save date as long):

```kotlin
    import io.github.newagewriter.processor.converter.Converter
    import io.github.processor.converter.GenericConverter
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
